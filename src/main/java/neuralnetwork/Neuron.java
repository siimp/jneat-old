package main.java.neuralnetwork;

import java.util.ArrayList;
import java.util.List;

import main.java.genetics.NeuronGene;

public class Neuron {

    private double value = 0.0;
    private List<Dendrite> inputs;
    private Neuron output;
    private boolean bias = false;

    public static Neuron of(NeuronGene neuronGene) {
        Neuron neuron = new Neuron();
        if (!neuronGene.isInput()) {
            neuron.setInputs(new ArrayList<>());
        } 
        neuron.setBias(neuronGene.isBias());
        
        return neuron;
    }
    
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isInput() {
        return inputs == null;
    }

    public boolean isOutput() {
        return output == null;
    }

    public Neuron getOutput() {
        return output;
    }

    public void setOutput(Neuron output) {
        this.output = output;
    }

    public List<Dendrite> getInputs() {
        return inputs;
    }

    public void setInputs(List<Dendrite> inputs) {
        this.inputs = inputs;
    }

    public boolean isBias() {
        return bias;
    }

    public void setBias(boolean bias) {
        this.bias = bias;
    }
    
    

}
