package test.java;

import main.java.config.Config;
import main.java.ga.Individual;
import main.java.ga.NeatGeneticAlgorithm;
import main.java.ga.NeatGeneticAlgorithmBuilder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

public class OrTests {

    @Before
    public void setup() {
        Config.reset();
    }

    @Test
    public void testOrFunctionCalculation() {
        NeatGeneticAlgorithm geneticAlgorithm = new NeatGeneticAlgorithmBuilder()
                .withFitnessFunction(OrTests::or)
                .withTerminationFunction(fitnessValue -> Math.abs(fitnessValue) < 0.001)
                .withFitnessCompareFunction(Double::compare)
                .withInputs(2)
                .withOutputs(1)
                .withBiasInputValue(1.0)
                .build();

        assertNotNull(geneticAlgorithm.solve());
    }

    private static double or(Individual individual) {
        return -(Math.pow(individual.evaluate(0.0, 0.0)[0], 2) +
                Math.pow((1.0 - Math.abs(individual.evaluate(0.0, 1.0)[0])), 2) +
                Math.pow((1.0 - Math.abs(individual.evaluate(1.0, 0.0)[0])), 2) +
                Math.pow((1.0 - Math.abs(individual.evaluate(1.0, 0.0)[0])), 2));
    }
}
