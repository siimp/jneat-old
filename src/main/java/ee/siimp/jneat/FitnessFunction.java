package ee.siimp.jneat;

@FunctionalInterface
public interface FitnessFunction {

	public double calculateFitness(Individual individual);
}
