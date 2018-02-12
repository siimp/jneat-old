package main.java.ga;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import main.java.config.Config;
import main.java.config.ConfigValue;
import main.java.genetics.GenePool;
import main.java.genetics.Genome;
import main.java.neuralnetwork.NeuralNetwork;
import main.java.neuralnetwork.Neuron;

public class Individual {
    private static AtomicInteger generator = new AtomicInteger(0);

    private int id;
    private Genome genome = new Genome();
    private double fitnessValue;
    
    public Individual() {
        this.setId(generator.getAndIncrement());
    }

    public double[] evaluate(double... inputValues) {
        NeuralNetwork neuralNetwork = NeuralNetwork.of(genome);
        for (int i = 0; i < inputValues.length; i++) {
            List<Neuron> inputNeurons = neuralNetwork.getInputs();
            inputNeurons.get(i).setValue(inputValues[i]);
        }
        neuralNetwork.calculate();

        double[] result = new double[neuralNetwork.getOutputs().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = neuralNetwork.getOutputs().get(i).getValue();
        }
        return result;
    }

    public Genome getGenome() {
        return genome;
    }

    public double getFitnessValue() {
        return fitnessValue;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }
    
    public void setGenome(Genome genome) {
        this.genome = genome;
    }
    
    public Individual crossoverWithLessFit(Individual partner) {
        Individual offspring = new Individual();
        offspring.setGenome(this.getGenome().crossoverWithLessFit(partner.getGenome()));
        return offspring;
    }

    public void mutate(GenePool genePool) {
        this.getGenome().mutateNeuronCountWithProbability(Config
                .getDouble(ConfigValue.NEW_NODE_PROBABILITY), genePool);
        this.getGenome().mutateDendriteCountWithProbability(Config
                .getDouble(ConfigValue.NEW_CONNECTION_PROBABILITY), genePool);
        this.getGenome().mutateWeights(Config
                .getDouble(ConfigValue.WEIGHTS_MUTATION_PROBABILITY));
        
    }
    
    
    @Override
    public String toString() {
        return genome.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




}
