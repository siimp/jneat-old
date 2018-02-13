package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import main.java.config.Config;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import main.java.genetics.DendriteGene;
import main.java.genetics.GenePool;
import main.java.genetics.Genome;
import main.java.neuralnetwork.NeuralNetwork;

public class NeuralNetworkTests {

    @Before
    public void setup() {
        Config.reset();
    }

    @Test
    public void testNoActivationFunctionBetweenInputAndOutput() {
        GenePool genePool = new GenePool();

        Genome genome = new Genome();
        genome.addNeuronGene(genePool.getNewNeuronOutputGene());
        genome.addNeuronGene(genePool.getNewNeuronInputGene());
        genome.addNeuronGene(genePool.getNewNeuronInputGene());

        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(0), genome.getNeuronGenes().get(1)));
        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(0), genome.getNeuronGenes().get(2)));

        NeuralNetwork neuralNetwork = NeuralNetwork.of(genome);
        neuralNetwork.getInputs().get(0).setValue(-1);
        neuralNetwork.getInputs().get(1).setValue(-1);
        neuralNetwork.calculate();

        assertEquals(neuralNetwork.getOutputs().get(0).getValue(), -2, 0.1);

        neuralNetwork.getInputs().get(0).setValue(1);
        neuralNetwork.getInputs().get(1).setValue(1);
        neuralNetwork.calculate();

        assertEquals(neuralNetwork.getOutputs().get(0).getValue(), 2, 0.1);
    }

    @Test
    public void testNotExpressed() {
        GenePool genePool = new GenePool();

        Genome genome = new Genome();
        genome.addNeuronGene(genePool.getNewNeuronOutputGene());
        genome.addNeuronGene(genePool.getNewNeuronInputGene());
        genome.addNeuronGene(genePool.getNewNeuronInputGene());
        genome.addNeuronGene(genePool.getNewNeuronGene());

        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(0), genome.getNeuronGenes().get(1)));
        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(0), genome.getNeuronGenes().get(2)));

        DendriteGene dendriteGene = genePool.getNewDendtriteGene(genome.getNeuronGenes().get(3), genome.getNeuronGenes().get(1));
        dendriteGene.setWeight(100);
        dendriteGene.setExpressed(false);
        genome.addDendriteGene(dendriteGene);
        dendriteGene = genePool.getNewDendtriteGene(genome.getNeuronGenes().get(0), genome.getNeuronGenes().get(2));
        dendriteGene.setWeight(100);
        dendriteGene.setExpressed(false);
        genome.addDendriteGene(dendriteGene);

        NeuralNetwork neuralNetwork = NeuralNetwork.of(genome);
        neuralNetwork.getInputs().get(0).setValue(-1);
        neuralNetwork.getInputs().get(1).setValue(-1);
        neuralNetwork.calculate();

        assertEquals(neuralNetwork.getOutputs().get(0).getValue(), -2, 0.1);
    }

    @Ignore("TODO")
    @Test
    public void testBiasNode() {
        GenePool genePool = new GenePool();

        Genome genome = new Genome();
        genome.addNeuronGene(genePool.getNewNeuronOutputGene());
        genome.addNeuronGene(genePool.getNewNeuronBiasGene(1.0));
        genome.addNeuronGene(genePool.getNewNeuronInputGene());
        
        DendriteGene dendriteGene = genePool.getNewDendtriteGene(genome.getNeuronGenes().get(2), genome.getNeuronGenes().get(1));
        dendriteGene.setWeight(1);
        dendriteGene.setExpressed(true);
        genome.addDendriteGene(dendriteGene);
        
        dendriteGene = genePool.getNewDendtriteGene(genome.getNeuronGenes().get(2), genome.getNeuronGenes().get(0));
        dendriteGene.setWeight(1);
        dendriteGene.setExpressed(true);
        genome.addDendriteGene(dendriteGene);
        
        NeuralNetwork neuralNetwork = NeuralNetwork.of(genome);
        neuralNetwork.getInputs().get(0).setValue(-1);
        neuralNetwork.calculate();
        
        assertTrue(neuralNetwork.getOutputs().get(0).getValue() == 0.0);

    }

}
