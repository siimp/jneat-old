package ee.siimp.jneat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Genome implements Serializable {
	private static final long serialVersionUID = 3926741534412604660L;
	
	private List<Gene> inputs = new ArrayList<>();
	private List<Gene> outputs = new ArrayList<>();
	//intermediate
	
	public Genome(int inputNodeCount, int outputNodeCount, double minWeightValue, double maxWeightValue) {
		long indexValue = 1;
		for (int inputNodeIndex = 0; inputNodeIndex < inputNodeCount; inputNodeIndex++) {
			Gene gene = new Gene(indexValue++);
			inputs.add(gene);
		}
		
		for (int outputNodeIndex = 0; outputNodeIndex < outputNodeCount; outputNodeIndex++) {
			Gene gene = new Gene(indexValue++);
			outputs.add(gene);
		}
	}

	public double calculate(double... inputValues) {
		// TODO Auto-generated method stub
		return 0;
	}

	


}
