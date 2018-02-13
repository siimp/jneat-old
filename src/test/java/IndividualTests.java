package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import main.java.config.Config;
import main.java.config.ConfigValue;
import main.java.ga.Individual;
import main.java.genetics.GenePool;

public class IndividualTests {

    @Before
    public void setup() {
        Config.reset();
    }

    @Test
    public void testEvaluation() {
        GenePool genePool = new GenePool();
        Individual individual = getNewIndividual(genePool);

        double[] result = individual.evaluate(0.0);
        assertEquals(0.5, result[0], 0.0001);
    }

    @Test
    public void testCrossover() {
        GenePool genePool = new GenePool();
        Individual parent1 = getNewIndividual(genePool);
        Individual parent2 = getNewIndividual(genePool);
        Individual offspring = parent1.crossoverWithLessFit(parent2);
        assertNotNull(offspring);
    }
    
    @Test
    public void testNodeMutation() {
        GenePool genePool = new GenePool();
        Individual individual = getNewIndividual(genePool);
        Config.getInstance().setDouble(ConfigValue.NEW_NODE_PROBABILITY, 1.0);
        individual.mutate(genePool);
    }
    
    @Test
    public void testConnectionMutation() {
        GenePool genePool = new GenePool();
        Individual individual = new Individual();
        individual.getGenome().addNeuronGene(genePool.getNewNeuronOutputGene());
        individual.getGenome().addNeuronGene(genePool.getNewNeuronGene());
        individual.getGenome().addNeuronGene(genePool.getNewNeuronGene());
        individual.getGenome().addNeuronGene(genePool.getNewNeuronInputGene());
        individual.getGenome().addDendriteGene(genePool
                .getNewDendtriteGene(individual.getGenome().getNeuronGenes().get(0), individual.getGenome().getNeuronGenes().get(1)));
        individual.getGenome().addDendriteGene(genePool
                .getNewDendtriteGene(individual.getGenome().getNeuronGenes().get(1), individual.getGenome().getNeuronGenes().get(2)));
        individual.getGenome().addDendriteGene(genePool
                .getNewDendtriteGene(individual.getGenome().getNeuronGenes().get(2), individual.getGenome().getNeuronGenes().get(3)));
        
        
        int connectionCount = individual.getGenome().getDendriteGenes().size();
        Config.getInstance().setDouble(ConfigValue.NEW_NODE_PROBABILITY, 0.0);
        Config.getInstance().setDouble(ConfigValue.NEW_CONNECTION_PROBABILITY, 1.0);
        individual.mutate(genePool);
        assertEquals(connectionCount + 1, individual.getGenome().getDendriteGenes().size());
        
        individual.mutate(genePool);
        assertEquals(connectionCount + 2, individual.getGenome().getDendriteGenes().size());
        
        individual.mutate(genePool);
        assertEquals(connectionCount + 3, individual.getGenome().getDendriteGenes().size());
        
        //second time stays same
        individual.mutate(genePool);
        assertEquals(connectionCount + 3, individual.getGenome().getDendriteGenes().size());
    }
    
    @Test
    public void testWeightMutation() {
        GenePool genePool = new GenePool();
        Individual individual = getNewIndividual(genePool);
        Config.getInstance().setDouble(ConfigValue.WEIGHTS_MUTATION_PROBABILITY, 1.0);
        individual.mutate(genePool);
    }
    
    private static Individual getNewIndividual(GenePool genePool) {
        Individual individual = new Individual();
        

        individual.getGenome().addNeuronGene(genePool.getNewNeuronOutputGene());
        individual.getGenome().addNeuronGene(genePool.getNewNeuronGene());
        individual.getGenome().addNeuronGene(genePool.getNewNeuronInputGene());

        individual.getGenome().addDendriteGene(genePool
                .getNewDendtriteGene(individual.getGenome().getNeuronGenes().get(0), individual.getGenome().getNeuronGenes().get(1)));
        individual.getGenome().addDendriteGene(genePool
                .getNewDendtriteGene(individual.getGenome().getNeuronGenes().get(1), individual.getGenome().getNeuronGenes().get(2)));
        return individual;
    }

}
