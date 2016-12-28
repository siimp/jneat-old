package ee.siimp.jneat;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Population {
	private static final Logger log = LogManager.getLogger(Population.class);
	
	private List<Individual> individuals;
	
	private double fitestIndividualFitnessValue = Double.MAX_VALUE;
	private double fitestIndividualFitnessValueDeviation = Double.MAX_VALUE;
	private Individual fitestIndividual;
	private FitnessFunction fitnessFunction;
	//TODO: species list

	public Population(int populationCount, FitnessFunction fitnessFunction, int inputNodeCount,
			int outputNodeCount, double minWeightValue, double maxWeightValue) {
		this.fitnessFunction = fitnessFunction;
		
		individuals = new ArrayList<>();
		
		log.info(String.format("creating populatuion with %d individuals", populationCount));
		for (int individualIndex = 0; individualIndex < populationCount; individualIndex++) {
			individuals.add(new Individual(inputNodeCount, outputNodeCount, minWeightValue, maxWeightValue));
		}
	}

	public double getCurrentFitnessValue() {
		log.info(String.format("current fitness value is %f", fitestIndividualFitnessValue));
		return fitestIndividualFitnessValue;
	}

	public void evaluate(double goalFitnessValue) {
		for (Individual individual : individuals) {
			double individualFitness = fitnessFunction.calculateFitness(individual);
			
			double currentIndividualFitnessValueDeviation = Math.abs(Math.abs(goalFitnessValue) - Math.abs(individualFitness));
			if(currentIndividualFitnessValueDeviation < fitestIndividualFitnessValueDeviation) {
				fitestIndividualFitnessValueDeviation = currentIndividualFitnessValueDeviation;
				fitestIndividualFitnessValue = individualFitness;
				fitestIndividual = individual;
			}
		}
		
	}

}
