package ee.siimp.jneat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GeneticAlgorithm {
	private static final Logger log = LogManager.getLogger(GeneticAlgorithm.class);
	
	private Population population;
	private int initialPopulationCount = 10;
	private double minWeightValue = -1.0;
	private double maxWeightValue = 1.0;
	private double goalFitnessValue = 1.0;
	private double allowedDeltaFromGoalFitnessValue = 0.0;
	private int inputNodeCount = 1;
	private int outputNodeCount = 1;
	private FitnessFunction fitnessFunction;
	
	public boolean isTerminationConditionMet() {
		double currentFitnessValue = Math.abs(population.getCurrentFitnessValue());
		boolean isTerminated = Math.abs(Math.abs(goalFitnessValue) - currentFitnessValue) <= allowedDeltaFromGoalFitnessValue;
		log.info(String.format("goal value is %f allowed delta is %f %b", 
				goalFitnessValue, allowedDeltaFromGoalFitnessValue, isTerminated));
		return isTerminated;
	}
	
	public void applyCrossover() {
		// TODO Auto-generated method stub
		
	}

	public void applyMutation() {
		// TODO Auto-generated method stub
		
	}

	public void evaluatePopulation() {
		this.population.evaluate(goalFitnessValue);
	}

	public void setInitialPopulationCount(int initialPopulationCount) {
		this.initialPopulationCount = initialPopulationCount;
	}

	public Individual getFittestIndividual() {
		// TODO Auto-generated method stub
		return null;
	}

	public FitnessFunction getFitnessFunction() {
		return fitnessFunction;
	}

	public void setFitnessFunction(FitnessFunction fitnessFunction) {
		this.fitnessFunction = fitnessFunction;
	}

	public double getMaxWeightValue() {
		return maxWeightValue;
	}

	public void setMaxWeightValue(double maxWeightValue) {
		this.maxWeightValue = maxWeightValue;
	}

	public double getMinWeightValue() {
		return minWeightValue;
	}

	public void setMinWeightValue(double minWeightValue) {
		this.minWeightValue = minWeightValue;
	}

	public void initialize() {
		if(fitnessFunction == null) {
			throw new UnsupportedOperationException("Fitness function is not initialized");
		}
		
		log.info("initializing");
		
		this.population = new Population(initialPopulationCount, fitnessFunction, 
				inputNodeCount, outputNodeCount, minWeightValue, maxWeightValue);
		
	}

	public int getInputNodeCount() {
		return inputNodeCount;
	}

	public void setInputNodeCount(int inputNodeCount) {
		this.inputNodeCount = inputNodeCount;
	}

	public int getOutputNodeCount() {
		return outputNodeCount;
	}

	public void setOutputNodeCount(int outputNodeCount) {
		this.outputNodeCount = outputNodeCount;
	}

	public double getGoalFitnessValue() {
		return goalFitnessValue;
	}

	public void setGoalFitnessValue(double goalFitnessValue) {
		this.goalFitnessValue = goalFitnessValue;
	}

	public double getAllowedDeltaFromGoalFitnessValue() {
		return allowedDeltaFromGoalFitnessValue;
	}

	public void setAllowedDeltaFromGoalFitnessValue(double allowedDeltaFromGoalFitnessValue) {
		this.allowedDeltaFromGoalFitnessValue = allowedDeltaFromGoalFitnessValue;
	}

}
