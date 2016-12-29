package ee.siimp.jneat;

import java.io.Serializable;

/**
 * 
 * Represents both gene and also neural network node.
 * 
 * TODO: implement different activation functions
 *
 */
public class Gene implements Serializable {
	private static final long serialVersionUID = 7418375206274125873L;
	
	//for connection gene
	private Gene inputNodeGene;
	private Gene outputNodeGene;
	private double weight = 1.0;
	private boolean expressed = true;
	private long innovation = 1;
	private boolean input = false;
	private boolean output = false;
	
	public Gene() {
		
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
	
	public boolean isOutput() {
		return output;
	}
	
	public void setOutput(boolean output) {
		this.output = output;
	}
	
	public boolean isInput() {
		return input;
	}
	
	public void setInput(boolean input) {
		this.input = input;
	}

	public Gene getInputNodeGene() {
		return inputNodeGene;
	}

	public void setInputNodeGene(Gene inputNodeGene) {
		this.inputNodeGene = inputNodeGene;
	}

	public Gene getOutputNodeGene() {
		return outputNodeGene;
	}

	public void setOutputNodeGene(Gene outputNodeGene) {
		this.outputNodeGene = outputNodeGene;
	}

	public long getInnovation() {
		return innovation;
	}

	public void setInnovation(long innovation) {
		this.innovation = innovation;
	}

}
