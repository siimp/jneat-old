package main.java.genetics;

public class DendriteGene {

    private NeuronGene from;
    private NeuronGene to;
    private double weight = 1.0;
    private boolean expressed = true;
    private long innovation;

    public NeuronGene getFrom() {
        return from;
    }

    protected void setFrom(NeuronGene from) {
        this.from = from;
    }

    public NeuronGene getTo() {
        return to;
    }

    protected void setTo(NeuronGene to) {
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean isExpressed() {
        return expressed;
    }

    public void setExpressed(boolean expressed) {
        this.expressed = expressed;
    }

    public long getInnovation() {
        return innovation;
    }

    protected void setInnovation(long innovation) {
        this.innovation = innovation;
    }

    @Override
    public String toString() {
        return "w=" + weight + ", inno=" + innovation + ", expr=" + expressed + ", " + from + " -> " + to;
    }

}