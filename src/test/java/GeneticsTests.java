package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import main.java.genetics.DendriteGene;
import main.java.genetics.GenePool;
import main.java.genetics.Genome;
import main.java.genetics.NeuronGene;
import main.java.neuralnetwork.NeuralNetwork;

public class GeneticsTests {

    @Test
    public void testSameStructureInnovationNumber() {
        GenePool genePool = new GenePool();

        List<NeuronGene> neuronGenes = new ArrayList<>();
        neuronGenes.add(genePool.getNewNeuronOutputGene());
        neuronGenes.add(genePool.getNewNeuronGene());
        neuronGenes.add(genePool.getNewNeuronInputGene());

        List<DendriteGene> dendriteGenes = new ArrayList<>();
        DendriteGene dendriteGene = genePool.getNewDendtriteGene(neuronGenes.get(0), neuronGenes.get(1));
        dendriteGenes.add(dendriteGene);
        dendriteGene = genePool.getNewDendtriteGene(neuronGenes.get(1), neuronGenes.get(2));
        dendriteGenes.add(dendriteGene);

        long structureInnovation = dendriteGene.getInnovation();
        DendriteGene sameInnovation = genePool.getNewDendtriteGene(neuronGenes.get(1), neuronGenes.get(2));
        assertEquals(structureInnovation, sameInnovation.getInnovation());

    }

    @Test
    public void testMutation() {
        GenePool genePool = new GenePool();

        Genome genome = new Genome();
        genome.addNeuronGene(genePool.getNewNeuronOutputGene());
        genome.addNeuronGene(genePool.getNewNeuronGene());
        genome.addNeuronGene(genePool.getNewNeuronInputGene());

        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(0), genome.getNeuronGenes().get(1)));
        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(1), genome.getNeuronGenes().get(2)));

        NeuralNetwork neuralNetwork = NeuralNetwork.of(genome);
        neuralNetwork.getInputs().get(0).setValue(0);
        neuralNetwork.calculate();
        assertEquals(0.5, neuralNetwork.getOutputs().get(0).getValue(), 0.0001);

        double lastValue = neuralNetwork.getOutputs().get(0).getValue();
        
        //TODO test perturb and random
        genome.mutateWeights(1.0);
        assertEquals(lastValue, neuralNetwork.getOutputs().get(0).getValue(), 0.0001);
    }

    @Test
    public void testNewNodeMutation() {
        GenePool genePool = new GenePool();

        Genome genome = new Genome();
        genome.addNeuronGene(genePool.getNewNeuronOutputGene());
        genome.addNeuronGene(genePool.getNewNeuronGene());
        genome.addNeuronGene(genePool.getNewNeuronInputGene());

        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(0), genome.getNeuronGenes().get(1)));
        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(1), genome.getNeuronGenes().get(2)));

        int oldNeuronCount = genome.getNeuronGenes().size();
        int oldDendriteCount = genome.getDendriteGenes().size();

        genome.mutateNeuronCountWithProbability(1.0, genePool);

        assertEquals(oldNeuronCount + 1, genome.getNeuronGenes().size());
        assertEquals(oldDendriteCount + 2, genome.getDendriteGenes().size());

    }

    @Test
    public void testNewConnectionMutation() {
        GenePool genePool = new GenePool();

        Genome genome = new Genome();
        genome.addNeuronGene(genePool.getNewNeuronOutputGene());
        genome.addNeuronGene(genePool.getNewNeuronGene());
        genome.addNeuronGene(genePool.getNewNeuronGene());
        genome.addNeuronGene(genePool.getNewNeuronGene());
        genome.addNeuronGene(genePool.getNewNeuronInputGene());

        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(0), genome.getNeuronGenes().get(1)));
        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(1), genome.getNeuronGenes().get(2)));
        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(2), genome.getNeuronGenes().get(3)));
        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(3), genome.getNeuronGenes().get(4)));

        int oldNeuronCount = genome.getNeuronGenes().size();
        int oldDendriteCount = genome.getDendriteGenes().size();

        genome.mutateDendriteCountWithProbability(1.0, genePool);

        assertEquals(oldNeuronCount, genome.getNeuronGenes().size());
        assertEquals(oldDendriteCount + 1, genome.getDendriteGenes().size());
        assertNotEquals(1.0, genome.getDendriteGenes().get(genome.getDendriteGenes().size() - 1).getWeight(), 0.0001);
    }

    @Test
    public void testCrossover() {
        GenePool genePool = new GenePool();

        Genome genome = new Genome();
        genome.addNeuronGene(genePool.getNewNeuronOutputGene());
        genome.addNeuronGene(genePool.getNewNeuronGene());
        genome.addNeuronGene(genePool.getNewNeuronGene());
        genome.addNeuronGene(genePool.getNewNeuronInputGene());

        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(0), genome.getNeuronGenes().get(1)));
        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(1), genome.getNeuronGenes().get(2)));
        genome.addDendriteGene(genePool.getNewDendtriteGene(genome.getNeuronGenes().get(2), genome.getNeuronGenes().get(3)));

        Genome moreFitGenome = Genome.dublicateOf(genome);
        moreFitGenome.mutateDendriteCountWithProbability(1.0, genePool);

        Genome offspring = moreFitGenome.crossoverWithLessFit(genome);
        assertEquals(moreFitGenome.getNeuronGenes().size(), offspring.getNeuronGenes().size());
        assertEquals(moreFitGenome.getDendriteGenes().size(), offspring.getDendriteGenes().size());

        for (DendriteGene offspringDendriteGene : offspring.getDendriteGenes()) {
            boolean weightCameFromParent = false;
            for (DendriteGene parentDendriteGene : genome.getDendriteGenes()) {
                if (parentDendriteGene.getInnovation() == offspringDendriteGene.getInnovation()) {
                    weightCameFromParent = (parentDendriteGene.getWeight() == offspringDendriteGene.getWeight());
                    break;
                }
            }

            if (!weightCameFromParent) {
                for (DendriteGene parentDendriteGene : moreFitGenome.getDendriteGenes()) {
                    if (parentDendriteGene.getInnovation() == offspringDendriteGene.getInnovation()) {
                        weightCameFromParent = (parentDendriteGene.getWeight() == offspringDendriteGene.getWeight());
                        break;
                    }
                }
            }

            assertTrue(
                    String.format("Weight did not from neighter of the parents for gene with innovation &d", offspringDendriteGene.getInnovation()),
                    weightCameFromParent);
        }

    }

}
