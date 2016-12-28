package ee.siimp.jneat;

public class Individual {
	
	private Genome genome;
	
	public Individual(int inputNodeCount, int outputNodeCount, double minWeightValue, double maxWeightValue) {
		this.genome = new Genome(inputNodeCount, outputNodeCount, minWeightValue, maxWeightValue);
	}

	public double evaluate(double... inputValues) {
		return genome.calculate(inputValues);
	}
	
	public Genome getGenome() {
		return genome;
	}

	public void setGenome(Genome genome) {
		this.genome = genome;
	}


}
