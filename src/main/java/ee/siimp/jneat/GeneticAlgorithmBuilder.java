package ee.siimp.jneat;

public class GeneticAlgorithmBuilder {
	
	private GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

	public GeneticAlgorithmBuilder withInitialPopulation(int initialPopulation) {
		geneticAlgorithm.setInitialPopulation(initialPopulation);
		return this;
	}

	public GeneticAlgorithmBuilder withWeightValueRange(double from, double to) {
		return this;
	}

	public GeneticAlgorithmBuilder withFitnessFunction(FitnessFunction fitnessFunction) {
		return this;
	}

	public GeneticAlgorithm build() {
		return this.geneticAlgorithm;
	}

	public GeneticAlgorithmBuilder withNeuralNetwork(int inputNodes, int outputNodes) {
		return this;
	}

}
