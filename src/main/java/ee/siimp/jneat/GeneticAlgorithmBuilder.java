package ee.siimp.jneat;

public class GeneticAlgorithmBuilder {
	
	private GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();

	public GeneticAlgorithmBuilder withInitialPopulation(int initialPopulation) {
		geneticAlgorithm.setInitialPopulationCount(initialPopulation);
		return this;
	}

	public GeneticAlgorithmBuilder withWeightValueRange(double from, double to) {
		geneticAlgorithm.setMinWeightValue(from);
		geneticAlgorithm.setMaxWeightValue(to);
		return this;
	}

	public GeneticAlgorithmBuilder withFitnessFunction(FitnessFunction fitnessFunction) {
		geneticAlgorithm.setFitnessFunction(fitnessFunction);
		return this;
	}

	public GeneticAlgorithm build() {
		this.geneticAlgorithm.initialize();
		return this.geneticAlgorithm;
	}

	public GeneticAlgorithmBuilder withNeuralNetworkInputOutputNodes(int inputNodeCount, int outputNodeCount) {
		geneticAlgorithm.setInputNodeCount(inputNodeCount);
		geneticAlgorithm.setOutputNodeCount(outputNodeCount);
		return this;
		
	}

	public GeneticAlgorithmBuilder withTerminationFitnessValueAndDelta(double goalFitnessValue, 
			double allowedDeltaFromGoalFitnessValue) {
		geneticAlgorithm.setGoalFitnessValue(goalFitnessValue);
		geneticAlgorithm.setAllowedDeltaFromGoalFitnessValue(allowedDeltaFromGoalFitnessValue);
		return this;
	}

}
