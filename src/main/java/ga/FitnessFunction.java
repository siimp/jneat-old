package main.java.ga;

@FunctionalInterface
public interface FitnessFunction {

	double calculateFitness(Individual individual);
}
