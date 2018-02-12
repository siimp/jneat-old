package main.java.genetics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

import main.java.config.Config;
import main.java.config.ConfigValue;

public class Genome {

    private static final Random random = new Random();
    private static final double PROBABILITY_MAX_VALUE = 1.0;

    private List<NeuronGene> neuronGenes = new ArrayList<>();
    private List<DendriteGene> dendriteGenes = new ArrayList<>();
    private Map<NeuronGene, List<DendriteGene>> neuronConnections = new HashMap<>();

    // private Double biasNeuronGeneValue;

    public static double getWeightRange() {
        return Config.getDouble(ConfigValue.WEIGHTS_ABSOLUTE_RANGE);
    }

    public static double getPerturbMaxAmount() {
        return Config.getDouble(ConfigValue.WEIGHTS_PERTURB_MAX_AMOUNT);
    }

    public List<NeuronGene> getNeuronGenes() {
        return neuronGenes;
    }

    public void setNeuronGenes(List<NeuronGene> neuronGenes) {
        this.neuronGenes = neuronGenes;
    }

    public List<DendriteGene> getDendriteGenes() {
        return dendriteGenes;
    }

    public void setDendriteGenes(List<DendriteGene> dendriteGenes) {
        this.dendriteGenes = dendriteGenes;
    }

    public void addNeuronGene(NeuronGene neuronGene) {
        this.neuronGenes.add(neuronGene);
    }

    /**
     * neuronGeneDendrites keeps track of neuron connections
     */
    public void addDendriteGene(DendriteGene dendriteGene) {
        this.dendriteGenes.add(dendriteGene);
        this.neuronConnections.computeIfAbsent(dendriteGene.getFrom(), (gene) -> new ArrayList<>());
        this.neuronConnections.get(dendriteGene.getFrom()).add(dendriteGene);
    }

    public void addDendriteGeneWithValue(DendriteGene dendriteGene, double value) {
        dendriteGene.setWeight(value);
        addDendriteGene(dendriteGene);
    }

    public void addDendriteGeneWithRandomValue(DendriteGene dendriteGene) {
        addDendriteGeneWithValue(dendriteGene, getRandomWeight());
    }

    public void mutateWeights(double probability) {
        if (isMutationHappening(probability)) {
            if (random.nextDouble() < Config.getDouble(ConfigValue.RANDOM_WEIGHT_PROBABILITY)) {
                //System.out.println("mutating weights randomly");
                mutateWeightsRandomWithProbability();
            } else {
                //System.out.println("perturbing weights");
                mutateWeightsPerturbWithProbability();
            }
        }
    }

    private void mutateWeightsRandomWithProbability() {
        // System.out.println("random weight");
        DendriteGene dendriteGene = getRandomActiveDendriteGene();
        if (dendriteGene == null) {
            return;
        }
        dendriteGene.setWeight(getRandomWeight());
    }

    private void mutateWeightsPerturbWithProbability() {
        // System.out.println("perturb weight weight");
        DendriteGene dendriteGene = getRandomActiveDendriteGene();
        if (dendriteGene == null) {
            return;
        }
        dendriteGene.setWeight(getPerturbedWeight(dendriteGene.getWeight()));
    }

    private static boolean isMutationHappening(double probability) {
        return Double.compare(probability, PROBABILITY_MAX_VALUE) == 0 || random.nextDouble() < probability;
    }

    private static double getRandomWeight() {
        double value = random.nextDouble() * getWeightRange();
        boolean isNegative = random.nextBoolean();
        if (isNegative) {
            return -value;
        }
        return value;
    }

    private static double getPerturbedWeight(double weight) {
        double value = random.nextDouble() * getPerturbMaxAmount();
        // System.out.println("perturbing by "+value);
        boolean isNegative = random.nextBoolean();
        if (isNegative) {
            return weight - value;
        }
        return weight + value;
    }

    /**
     * In the add node mutation, an existing connection is split and the new
     * node placed where the old connection used to be. The old connection is
     * disabled and two new connections are added to the genome. The new
     * connection leading into the new node receives a weight of 1, and the new
     * connection leading out receives the same weight as the old connection.
     * 
     */
    public void mutateNeuronCountWithProbability(double probability, GenePool genePool) {
        if (isMutationHappening(probability)) {
            //System.out.println("mutating neuron count");
            DendriteGene old = getRandomActiveDendriteGene();
            if (old == null) {
                return;
            }
            old.setExpressed(false);

            NeuronGene middleNeuron = genePool.getNewNeuronGene();

            DendriteGene oldFromToMiddle = genePool.getNewDendtriteGene(old.getFrom(), middleNeuron);
            DendriteGene middleToOldTo = genePool.getNewDendtriteGene(middleNeuron, old.getTo());
            middleToOldTo.setWeight(old.getWeight());
            this.addNeuronGene(middleNeuron);
            this.addDendriteGene(oldFromToMiddle);
            this.addDendriteGene(middleToOldTo);
        }
    }

    private DendriteGene getRandomActiveDendriteGene() {
        List<DendriteGene> activeDentriteGenes = getActiveDendriteGenes();
        if (activeDentriteGenes.size() == 0) {
            return null;
        }
        return activeDentriteGenes.get(random.nextInt(activeDentriteGenes.size()));
    }

    private List<DendriteGene> getActiveDendriteGenes() {
        return dendriteGenes.stream().filter(DendriteGene::isExpressed).collect(Collectors.toList());
    }

    /**
     * In the add connection mutation, a single new connection gene with a
     * random weight is added connecting two previously unconnected nodes.
     * 
     */
    public void mutateDendriteCountWithProbability(double probability, GenePool genePool) {
        if (isMutationHappening(probability)) {
            //System.out.println("mutating connection count");
            List<NeuronGene> nonInputNeurons = neuronGenes.stream().filter(it -> !it.isInput()).collect(Collectors.toList());

            Collections.shuffle(nonInputNeurons, random);

            for (NeuronGene neuronGeneFrom : nonInputNeurons) {
                NeuronGene neuronGeneTo = findNotConnectedNeuronGene(neuronGeneFrom);
                if (neuronGeneTo != null) {
                    DendriteGene dendriteGene = genePool.getNewDendtriteGene(neuronGeneFrom, neuronGeneTo);
                    dendriteGene.setWeight(getRandomWeight());
                    this.addDendriteGene(dendriteGene);
                    break;
                }
            }
        }
    }

    /**
     * 1. neuronGeneFrom can not be directly connected to neuron from non input
     * 2. found neuronGene should not form cyclic connection
     */
    private NeuronGene findNotConnectedNeuronGene(NeuronGene neuronGeneFrom) {
        List<NeuronGene> notConnectedNeurons = neuronGenes.stream().filter(it -> !it.isOutput() && !it.equals(neuronGeneFrom))
                .collect(Collectors.toList());

        for (NeuronGene neuronGene : notConnectedNeurons) {
            if (!directlyConnected(neuronGeneFrom, neuronGene) && !undirectlyConnected(neuronGene, neuronGeneFrom)) {
                return neuronGene;
            }
        }
        return null;
    }

    private boolean directlyConnected(NeuronGene from, NeuronGene to) {
        for (DendriteGene dendriteGene : neuronConnections.get(from)) {
            if (dendriteGene.getTo().equals(to)) {
                return true;
            }
        }
        return false;
    }

    private boolean undirectlyConnected(NeuronGene from, NeuronGene to) {
        if (from == null || from.isInput()) {
            return false;
        }

        for (DendriteGene dendriteGene : neuronConnections.get(from)) {
            if (dendriteGene.getTo().equals(to)) {
                return true;
            }

            boolean connected = undirectlyConnected(dendriteGene.getTo(), to);
            if (connected) {
                return connected;
            }
        }
        return false;
    }

    /**
     * When crossing over, the genes in both genomes with the same innovation
     * numbers are lined up. These genes are called matching genes. Genes that
     * do not match are either disjoint or excess, depending on whether they
     * occur within or outside the range of the other parent’s innovation
     * numbers. They represent structure that is not present in the other
     * genome. In composing the offspring, genes are randomly chosen from either
     * parent at matching genes, whereas all excess or disjoint genes are always
     * included from the more fit parent.
     * 
     * @param genome
     * @return
     */
    public Genome crossoverWithLessFit(Genome other) {
        // System.out.println("crossover");
        Genome offspring = Genome.dublicateOf(this);
        Map<Long, DendriteGene> otherInnovationGenes = other.getDendriteGenes().stream()
                .collect(Collectors.toMap(DendriteGene::getInnovation, Function.identity()));

        for (DendriteGene offspringDendriteGene : offspring.getDendriteGenes()) {
            DendriteGene sameInnovationGene = otherInnovationGenes.get(Long.valueOf(offspringDendriteGene.getInnovation()));
            if (sameInnovationGene != null) {
                boolean fromLessFitParent = random.nextBoolean();
                if (fromLessFitParent) {
                    // XXX: set all properties
                    offspringDendriteGene.setWeight(sameInnovationGene.getWeight());
                    offspringDendriteGene.setExpressed(sameInnovationGene.isExpressed());
                }
            }
        }
        return offspring;
    }

    public static Genome dublicateOf(Genome genome) {
        Genome dublicate = new Genome();
        Map<NeuronGene, NeuronGene> originalToDublicateNeuronGeneMapping = new HashMap<>();
        for (NeuronGene neuronGene : genome.getNeuronGenes()) {
            NeuronGene dublicateGene = new NeuronGene();
            dublicateGene.setInput(neuronGene.isInput());
            dublicateGene.setOutput(neuronGene.isOutput());
            dublicateGene.setInnovation(neuronGene.getInnovation());
            dublicate.addNeuronGene(dublicateGene);
            originalToDublicateNeuronGeneMapping.put(neuronGene, dublicateGene);
        }

        for (DendriteGene dendriteGene : genome.getDendriteGenes()) {
            DendriteGene dublicateGene = new DendriteGene();
            dublicateGene.setExpressed(dendriteGene.isExpressed());
            dublicateGene.setWeight(dendriteGene.getWeight());
            dublicateGene.setFrom(originalToDublicateNeuronGeneMapping.get(dendriteGene.getFrom()));
            dublicateGene.setTo(originalToDublicateNeuronGeneMapping.get(dendriteGene.getTo()));
            dublicateGene.setInnovation(dendriteGene.getInnovation());
            dublicate.addDendriteGene(dublicateGene);
        }
        return dublicate;
    }

    /**
     * The number of excess and disjoint genes between a pair of genomes is a
     * natural measure of their compatibility distance. The more disjoint two
     * genomes are, the less evolutionary history they share, and thus the less
     * compatible they are. Therefore, we can measure the compatibility distance
     * δ of different structures in NEAT as a simple linear combination of the
     * number of excess E and disjoint D genes, as well as the average weight
     * differences of matching genes W
     */
    public static double evolutionaryDistance(Genome first, Genome second) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        String result = "neurons: " + neuronGenes.toString();
        for (DendriteGene dendriteGene : dendriteGenes) {
            result += "\n\t" + dendriteGene.toString();
        }
        return result;
    }

    // public Double getBiasNeuronGeneValue() {
    // return biasNeuronGeneValue;
    // }
    //
    // public void setBiasNeuronGeneValue(Double biasNeuronGeneValue) {
    // this.biasNeuronGeneValue = biasNeuronGeneValue;
    // }

}
