package main.java.genetics;

public class NeuronGene {

    private boolean input;
    private boolean output;
    private long innovation;
    private Double biasValue;
    

    protected NeuronGene() {
    }

    public boolean isInput() {
        return input;
    }

    protected void setInput(boolean input) {
        this.input = input;
    }

    public boolean isOutput() {
        return output;
    }

    protected void setOutput(boolean output) {
        this.output = output;
    }

    public long getInnovation() {
        return innovation;
    }

    protected void setInnovation(long innovation) {
        this.innovation = innovation;
    }

    public Double getBiasValue() {
        return biasValue;
    }

    public void setBiasValue(Double biasValue) {
        this.biasValue = biasValue;
    }
    
    public boolean isBias() {
        return getBiasValue() != null;
    }

    @Override
    public String toString() {
        String type = null;
        if (biasValue != null) {
            type = "bias";
        } else if (input == false && output == false) {
            type = "hidden";
        } else if (input) {
            type = "input";
        } else if (output) {
            type = "output";
        } else {
            type = "unknown";
        }
        return type+ "[" + innovation + "]";
    }    

}
