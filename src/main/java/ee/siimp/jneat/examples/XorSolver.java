package ee.siimp.jneat.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ee.siimp.jneat.GeneticAlgorithm;
import ee.siimp.jneat.GeneticAlgorithmBuilder;

public class XorSolver {
	
	private static final Logger log = LogManager.getLogger(XorSolver.class);
	
	public static void main(String[] args) {
		GeneticAlgorithm ga = new GeneticAlgorithmBuilder()
			.withInitialPopulation(3)
			/*
			 * XOR fitness subtracts individual estimation from the
			 * real value and adds the deviations together.
			 * 
			 * More closer to 0.0 individual gets the more fit it is.
			 * */
			.withFitnessFunction(individual -> {
				return Math.abs((0.0 - individual.evaluate(0.0, 0.0)[0])) +
				Math.abs((1.0 - individual.evaluate(0.0, 1.0)[0])) +
				Math.abs((1.0 - individual.evaluate(1.0, 0.0)[0])) +
				Math.abs((0.0 - individual.evaluate(1.0, 1.0)[0]));
			})
			.withTerminationFitnessValueAndDelta(0.0, 0.0001)
			.addInput("x1").addInput("x2")
			.addOutput("xor")
			.withWeightValueRange(-10.0, 10.0)
			.build();
		
		log.info("Starting to solve XOR");
		
		while(!ga.isTerminationConditionMet()) {
			ga.applyCrossover();
			ga.applyMutation();
			ga.evaluatePopulation();
			
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		log.info("XOR problem completed");
		log.info("Fittest individual: "+ga.getFittestIndividual());
	}

}

