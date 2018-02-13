package main.java.ga;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import main.java.config.Config;
import main.java.config.ConfigValue;

public class NeatGeneticAlgorithm {

    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final Random random = new Random();
    
    private FitnessFunction fitnessFunction;
    private FitnessCompareFunction fitnessCompareFunction;
    private TerminationFunction terminationFunction;
    private Comparator<Individual> comparator;
    private int inputNeuronCount;
    private int outputNeuronCount;
    private Double biasNodeInputValue;

    List<Population> species = new ArrayList<>();

    public void initialize() {
        int populationSize = Config.getInstance().getInteger(ConfigValue.POPULATION_SIZE);
        Population population = Population.initialize(populationSize, inputNeuronCount, outputNeuronCount, biasNodeInputValue, 
                (a,b) -> {return fitnessCompareFunction.compare(a.getFitnessValue(), b.getFitnessValue());});
        this.species.add(population);
    }

    public Individual solve() {
        long start = System.currentTimeMillis();
        Individual solution = null;
        while(solution == null && secondsLeftToSolve(start) > 0) {
            //System.out.println("evaluate");
            solution = evaluate(start);
            if (solution == null) {
                //System.out.println("crossover");
                crossover(start);
                //System.out.println("mutate");
                mutate(start);
            }
            
        }
        executor.shutdown();
        
        return solution;
    }

    private void mutate(long start) {
        //TODO: do in parallel
//        List<Callable<Void>> tasks = new ArrayList<>();
        for (Population population : species) {
//            tasks.add(() -> {
                populationMutation(population);
//                return null;
//            });
        }
//        completeAllTasks(tasks, start); 
        
    }

    private static void populationMutation(Population population) {
        for (Individual individual : population.getIndividuals()) {
            if (random.nextDouble() < Config.getInstance().getDouble(ConfigValue.MUTATION_PROBABILITY)) {
                //System.out.println("Mutating individual " + individual.getId());
                individual.mutate(population.getGenePool());
            }
        }
        
    }

    private void crossover(long start) {
        //TODO do in parallel
        //List<Callable<Void>> tasks = new ArrayList<>();
        for (Population population : species) {
            //tasks.add(() -> {
                populationCrossover(population);
                //return null;
            //});
        }
        //completeAllTasks(tasks, start);       
    }



    private static void populationCrossover(Population population) {
        //XXX: should more fit crossover with higher probability???
        List<Individual> individuals = new ArrayList<>(population.getIndividuals());
        Set<Individual> individualsToRemove = new HashSet<>();
        Set<Individual> offsprings = new HashSet<>();
        for (int i = 0; i < individuals.size(); i++) {
            if (random.nextDouble() < Config.getInstance().getDouble(ConfigValue.CROSSOVER_PROBABILITY)) {
                Individual parent1 = individuals.get(i);
                Individual parent2 = choosePartner(individuals, i, individualsToRemove);
                if (parent2 != null) {
                    Individual offspring = null;
                    if (parent1.getFitnessValue() > parent2.getFitnessValue()) {
                        offspring = parent1.crossoverWithLessFit(parent2);
                        //System.out.println("crossing over " + parent1.getId() + " with " + parent2.getId() + " offspring is " + offspring.getId() + " killing " + parent2.getId());
                        addToRemovedListIfNotRemoved(individualsToRemove, parent2, parent1);
                    } else {
                        offspring = parent2.crossoverWithLessFit(parent1);
                        addToRemovedListIfNotRemoved(individualsToRemove, parent1, parent2);
                        //System.out.println("crossing over " + parent2.getId() + " with " + parent1.getId() + " offspring is " + offspring.getId() + " killing " + parent1.getId());
                    }
                    offsprings.add(offspring);
                }
            }
        }
        population.getIndividuals().removeAll(individualsToRemove);
        for(int i = 0; i < offsprings.size() - individualsToRemove.size(); i++) {
            population.getIndividuals().poll();
        }
        population.getIndividuals().addAll(offsprings);
    }

    private static void addToRemovedListIfNotRemoved(Set<Individual> individualsToRemove, Individual primary, Individual secondary) {
        if (!individualsToRemove.contains(primary)) {
            individualsToRemove.add(primary);
        } else {
            individualsToRemove.add(secondary);
        }
    }

    private static Individual choosePartner(List<Individual> individuals, int firstParentIndex, Set<Individual> individualsToRemove) {
        if (individuals.size() < 2) {
            return null;
        }
        
        int secondParentIndex = 0;
        do {
            secondParentIndex = random.nextInt(individuals.size());
        } while(firstParentIndex == secondParentIndex && !individualsToRemove.contains(individuals.get(secondParentIndex)));
        
        return individuals.get(secondParentIndex);
    }

    private Individual evaluate(long start) {
        Individual solution = null;
        //TODO do in parallel
        //List<Callable<Void>> tasks = new ArrayList<>();
        for (Population population : species) {
            //tasks.add(() -> {
                population.evaluate(fitnessFunction);
                
                //return null;
            //});
        }
        //completeAllTasks(tasks, start);
        for (Population population : species) {
            Individual fitestIndividual = population.getFitestIndividual();
            System.out.println(String.format("population %s (%s), generation %s, fitest value %s", population.getId(), population.getIndividuals().size(), population.getGeneration(), fitestIndividual.getFitnessValue()));
            //System.out.println("population "+population.getId()+" generation "+ population.getGeneration() + "; fitest "+fitestIndividual.getId() + " value "+fitestIndividual.getFitnessValue());
            if (terminationFunction.terminate(fitestIndividual.getFitnessValue())) {
                solution = fitestIndividual;
                break;
            }
        } 
        return solution;
    }
    
    private static List<Future<Void>> completeAllTasks(List<Callable<Void>> tasks, long start) {
        try {
            return executor.invokeAll(tasks, secondsLeftToSolve(start), TimeUnit.SECONDS);
        } catch (@SuppressWarnings("unused") InterruptedException e) {
            System.out.println("could not find solution within time");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static long secondsLeftToSolve(long startTimeInMillis) {
        long elapsedTimeInSeconds = (System.currentTimeMillis() - startTimeInMillis) / 1000;
        return Config.getInstance().getLong(ConfigValue.TIME_LIMIT_IN_SECONDS) - elapsedTimeInSeconds;
    }

    public FitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }

    public void setFitnessFunction(FitnessFunction fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    public int getOutputNeuronCount() {
        return outputNeuronCount;
    }

    public void setOutputNeuronCount(int outputNeuronCount) {
        this.outputNeuronCount = outputNeuronCount;
    }

    public int getInputNeuronCount() {
        return inputNeuronCount;
    }

    public void setInputNeuronCount(int inputNeuronCount) {
        this.inputNeuronCount = inputNeuronCount;
    }

    public TerminationFunction getTerminationFunction() {
        return terminationFunction;
    }

    public void setTerminationFunction(TerminationFunction terminationFunction) {
        this.terminationFunction = terminationFunction;
    }

    public Comparator<Individual> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<Individual> comparator) {
        this.comparator = comparator;
    }

    public FitnessCompareFunction getFitnessCompareFunction() {
        return fitnessCompareFunction;
    }

    public void setFitnessCompareFunction(FitnessCompareFunction fitnessCompareFunction) {
        this.fitnessCompareFunction = fitnessCompareFunction;
    }

    public void setBiasNodeInputValue(double biasNodeInputValue) {
        this.biasNodeInputValue = Double.valueOf(biasNodeInputValue);
    }
    
}
