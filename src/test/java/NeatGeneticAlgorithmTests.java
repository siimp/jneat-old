package test.java;

import org.junit.Test;

import main.java.ga.Population;

public class NeatGeneticAlgorithmTests {

    @Test
    public void testPopulationCrossover() {
        Population population = Population.initialize(2, 1, 1, new Double(1.0), (a,b) -> {return 0;});
    }

}
