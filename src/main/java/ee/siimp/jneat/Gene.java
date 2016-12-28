package ee.siimp.jneat;

import java.io.Serializable;

/**
 * 
 * Represents both gene and also neural network node.
 *
 */
public class Gene implements Serializable {
	
	//for connection gene
	private long inIndex;
	private long outIndex;
	private double weight = 1.0;
	private boolean enabled = true;
	//innovation
	
	//for node gene
	private long index;
	
	
	public Gene(long index) {
		this.index = index;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	//type: sensor/output/hidden

}
