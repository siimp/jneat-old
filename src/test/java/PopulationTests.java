package test.java;

import static org.junit.Assert.assertEquals;

import main.java.config.Config;
import org.junit.Before;
import org.junit.Test;

import main.java.ga.Individual;
import main.java.ga.Population;

public class PopulationTests {

    @Before
    public void setup() {
        Config.reset();
    }
    
    @Test
    public void testRandomGeneration() {
        Population population = Population.initialize(1, 5, 1, null, (a,b) -> {return 1;});
        Individual individual = population.getIndividuals().peek();
        assertEquals(5, individual.getGenome().getDendriteGenes().size());
        individual.evaluate(1.0, 1.0, 1.0, 1.0, 1.0);
        
        population = Population.initialize(1, 5, 2, null, (a,b) -> {return 1;});
        individual = population.getIndividuals().peek();
        assertEquals(10, individual.getGenome().getDendriteGenes().size());
        individual.evaluate(1.0, 1.0, 1.0, 1.0, 1.0);
    }
    
    @Test
    public void testInitialize() {
        Population population = Population.initialize(1, 1, 1, null, (a,b) -> {return 1;});
        assertEquals(1, population.getIndividuals().size());
        
        population = Population.initialize(100, 1, 1, null, (a,b) -> {return 1;});
        assertEquals(100, population.getIndividuals().size());
        
        
        
        population = Population.initialize(1, 1, 1, Double.valueOf(1.0), (a,b) -> {return 1;});
        assertEquals(1, population.getIndividuals().size());
    }
    

}
