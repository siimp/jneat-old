package main.java.ga;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import main.java.genetics.GenePool;
import main.java.genetics.NeuronGene;

public class Population {
    private static AtomicInteger generator = new AtomicInteger(0);

    private int id;

    private PriorityQueue<Individual> individuals;
    private GenePool genePool = new GenePool();
    private long generation;

    private Population() {
        setId(generator.getAndIncrement());
    }

    public void evaluate(FitnessFunction fitnessFunction) {
        for (Individual individual : individuals) {
            double fitnessValue = fitnessFunction.calculateFitness(individual);
            individual.setFitnessValue(fitnessValue);
        }
        generation++;
    }

    public GenePool getGenePool() {
        return genePool;
    }

    public long getGeneration() {
        return generation;
    }

    public void setGeneration(long generation) {
        this.generation = generation;
    }

    public static Population initialize(int populationSize, int inputNeuronCount, int outputNeuronCount, Double biasNodeInputValue,
            Comparator<Individual> comparator) {
        Population population = new Population();
        population.setIndividuals(new PriorityQueue<>(comparator));

        IntStream.range(0, populationSize).forEach(value -> {
            Individual individual = population.generateRandomIndividual(inputNeuronCount, outputNeuronCount, biasNodeInputValue);
            population.individuals.add(individual);
        });
        return population;

    }

    /**
     * NEAT biases the search towards minimal-dimensional spaces by starting out
     * with a uniform population of networks with zero hidden nodes (i.e., all
     * inputs connect directly to outputs).
     */
    private Individual generateRandomIndividual(int inputNeuronCount, int outputNeuronCount, Double biasNodeInputValue) {
        Individual individual = new Individual();

        List<NeuronGene> inputs = new ArrayList<>();
        IntStream.range(0, inputNeuronCount).forEach(value -> {
            NeuronGene input = this.getGenePool().getNewNeuronInputGene();
            individual.getGenome().addNeuronGene(input);
            inputs.add(input);
        });
        
        // single bias node to all none input nodes
        if (biasNodeInputValue != null) {
            NeuronGene bias = this.getGenePool().getNewNeuronBiasGene(biasNodeInputValue);
            individual.getGenome().addNeuronGene(bias);
            inputs.add(bias);
        }
        
        List<NeuronGene> outputs = new ArrayList<>();
        IntStream.range(0, outputNeuronCount).forEach(value -> {
            NeuronGene output = this.getGenePool().getNewNeuronOutputGene();
            individual.getGenome().addNeuronGene(output);
            outputs.add(output);
        });
        
        for (NeuronGene input : inputs) {
            for (NeuronGene output : outputs) {
                if (input.isBias()) {
                    individual.getGenome().addDendriteGeneWithValue(genePool.getNewDendtriteGene(output, input), 1.0);
                } else {
                    individual.getGenome().addDendriteGeneWithRandomValue(genePool.getNewDendtriteGene(output, input));                    
                }
            }
        }

        return individual;
    }

    public Individual getFitestIndividual() {
        return individuals.peek();
    }

    public PriorityQueue<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(PriorityQueue<Individual> individuals) {
        this.individuals = individuals;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
