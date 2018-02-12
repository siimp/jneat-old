package main.java.neuralnetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.config.Config;
import main.java.config.ConfigValue;
import main.java.genetics.DendriteGene;
import main.java.genetics.Genome;
import main.java.genetics.NeuronGene;

public class NeuralNetwork {

    // private static final String ACTIVATION_FUNCTION_SIGMOID = "sigmoid";
    private static final String ACTIVATION_FUNCTION_TANH = "tanh";
    private static final String ACTIVATION_FUNCTION_RELU = "relu";

    private List<Neuron> inputs = new ArrayList<>();
    private List<Neuron> outputs = new ArrayList<>();

    public static NeuralNetwork of(Genome genome) {
        Map<NeuronGene, Neuron> cache = new HashMap<>();
        NeuralNetwork neuralNetwork = new NeuralNetwork();
        for (NeuronGene neuronGene : genome.getNeuronGenes()) {
            Neuron neuron = Neuron.of(neuronGene);
            if (neuronGene.isInput()) {
                neuralNetwork.inputs.add(neuron);
            } else if (neuronGene.isOutput()) {
                neuralNetwork.getOutputs().add(neuron);
            }
            cache.put(neuronGene, neuron);
        }

        //neural network is built from top down. output -> input
        for (DendriteGene dendriteGene : genome.getDendriteGenes()) {
            if (dendriteGene.isExpressed()) {
                Neuron neuron = cache.get(dendriteGene.getFrom());
                Neuron input = cache.get(dendriteGene.getTo());
                Dendrite dendrite = Dendrite.of(dendriteGene, input);
                input.setOutput(neuron);
                neuron.getInputs().add(dendrite);
            }
        }

        return neuralNetwork;
    }

    public List<Neuron> getInputs() {
        return inputs;
    }

    public void setInputs(List<Neuron> inputs) {
        this.inputs = inputs;
    }

    public List<Neuron> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<Neuron> outputs) {
        this.outputs = outputs;
    }

    public void calculate() {
        Map<Neuron, Double> calculatedValues = new HashMap<>();
        for (Neuron output : outputs) {
            output.setValue(calculate(output, calculatedValues));
        }
    }

    private double calculate(Neuron neuron, Map<Neuron, Double> calculatedValues) {
        if (calculatedValues.containsKey(neuron)) {
            return calculatedValues.get(neuron).doubleValue();
        }

        double summedInputs = 0.0;
        for (Dendrite dendrite : neuron.getInputs()) {
            if (dendrite.getInput().isInput()) {
                summedInputs += dendrite.getWeight() * dendrite.getInput().getValue();
            } else {
                //System.out.println(neuron + " --> " +dendrite.getInput());
                summedInputs += dendrite.getWeight() * calculate(dendrite.getInput(), calculatedValues);
            }
        }

        double value = summedInputs;
        if (!neuron.isInput() && !neuron.isOutput()) {
            value = applyActivationFunction(summedInputs);
        }
        calculatedValues.put(neuron, value);
        return value;
    }

    private static double applyActivationFunction(double sum) {
        String function = Config.getString(ConfigValue.ACTIVATION_FUNCTION);
        if (ACTIVATION_FUNCTION_TANH.equals(function)) {
            return 2 * applySigmoid(2 * sum) - 1;
        } else if (ACTIVATION_FUNCTION_RELU.equals(function)) {
            return applyRelu(sum);
        }
        return applySigmoid(sum);
    }

    private static double applyRelu(double sum) {
        return Math.max(0, sum);
    }

    private static double applySigmoid(double sum) {
        return 1 / (1 + Math.pow(Math.E, Config.getDouble(ConfigValue.SIGMOID_MULTIPLIER) * sum));
    }

}
