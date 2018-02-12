package main.java.neuralnetwork;
import main.java.genetics.DendriteGene;

public class Dendrite {

    private double weight;
    private Neuron input;

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Neuron getInput() {
        return input;
    }

    public void setInput(Neuron input) {
        this.input = input;
    }

    public double getValue() {
        return input.getValue() * this.getWeight();
    }

    public static Dendrite of(DendriteGene dendriteGene, Neuron input) {
        Dendrite dendrite = new Dendrite();
        dendrite.setWeight(dendriteGene.getWeight());
        dendrite.setInput(input);
        return dendrite;
    }

}
