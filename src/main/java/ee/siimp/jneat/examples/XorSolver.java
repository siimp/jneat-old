package ee.siimp.jneat.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ee.siimp.jneat.GeneticAlgorithm;
import ee.siimp.jneat.GeneticAlgorithmBuilder;

public class XorSolver {
	
	private static final Logger log = LogManager.getLogger(XorSolver.class);
	
	public static void main(String[] args) {
		GeneticAlgorithm ga = new GeneticAlgorithmBuilder()
			.withInitialPopulation(20)
			.withFitnessFunction(individual -> {
				return Math.abs((0.0 - individual.evaluate(0.0, 0.0))) +
				Math.abs((1.0 - individual.evaluate(0.0, 1.0))) +
				Math.abs((1.0 - individual.evaluate(1.0, 0.0))) +
				Math.abs((0.0 - individual.evaluate(1.0, 1.0)));
			})
			.withNeuralNetwork(2, 1)
			.withWeightValueRange(-10.0, 10.0)
			.build();
		
		log.info("Starting to solve XOR");
		while(!ga.isTerminationConditionMet()) {
			ga.applyCrossover();
			ga.applyMutation();
			ga.evaluatePopulation();
		}
		log.info("XOR problem completed");
		log.info("Fittest individual: "+ga.getFittestIndividual());
	}

}

