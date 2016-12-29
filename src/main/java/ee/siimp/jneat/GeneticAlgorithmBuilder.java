package ee.siimp.jneat;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithmBuilder {
	
	private GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
	private List<String> inputs = new ArrayList<>();
	private List<String> outputs = new ArrayList<>();

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
		geneticAlgorithm.setInputNodeCount(inputs.size());
		geneticAlgorithm.setOutputNodeCount(outputs.size());
		geneticAlgorithm.initialize();
		return this.geneticAlgorithm;
	}


	public GeneticAlgorithmBuilder withTerminationFitnessValueAndDelta(double goalFitnessValue, 
			double allowedDeltaFromGoalFitnessValue) {
		geneticAlgorithm.setGoalFitnessValue(goalFitnessValue);
		geneticAlgorithm.setAllowedDeltaFromGoalFitnessValue(allowedDeltaFromGoalFitnessValue);
		return this;
	}

	public GeneticAlgorithmBuilder addInput(String inputLabel) {
		inputs.add(inputLabel);
		return this;
	}

	public GeneticAlgorithmBuilder addOutput(String outputLabel) {
		outputs.add(outputLabel);
		return this;
	}

}
