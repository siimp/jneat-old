package ee.siimp.jneat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GeneticAlgorithm {
	private static final Logger log = LogManager.getLogger(GeneticAlgorithm.class);
	
	//General config
	public static double MUTATION_RATE = 0.3;
	private static long globalInnovationNumber = 1L;
	
	private Population population;
	private int initialPopulationCount = 10;
	private double minWeightValue = -1.0;
	private double maxWeightValue = 1.0;
	private double goalFitnessValue = 1.0;
	private double allowedDeltaFromGoalFitnessValue = 0.0;
	private List<String> inputs = new ArrayList<>();
	private List<String> outputs = new ArrayList<>();
	private FitnessFunction fitnessFunction;
	private Random random = new Random();
	
	public boolean isTerminationConditionMet() {
		double currentFitnessValue = Math.abs(population.getCurrentFitnessValue());
		boolean isTerminated = Math.abs(Math.abs(goalFitnessValue) - currentFitnessValue) <= allowedDeltaFromGoalFitnessValue;
		log.info(String.format("termination check: goal value is %.4f allowed delta is %.4f best value is %.4f", 
				goalFitnessValue, allowedDeltaFromGoalFitnessValue, currentFitnessValue));
		return isTerminated;
	}
	
	public void applyCrossover() {
		// TODO Auto-generated method stub
		
	}

	public void applyMutation() {
		for (Individual individual : population.getIndividuals()) {
			if(random.nextDouble() <= MUTATION_RATE) {
				switch (random.nextInt(3)) {
				case 0:
					addConnection(individual);
					break;
				case 1:
					addNode(individual);
					break;
				case 2:
					changeConnectionWeights(individual);
					break;
				}
			}
		}
	}

	private void addNode(Individual individual) {
		//add node, old connection node is disabled and both new connections get weight of 1.0
		//new node is created between connected nodes only, this minimizes the initial impact of the mutation
		
		List<Gene> expressedConnectionGenes = individual.getGenome().getConnectionGenes().stream()
				.filter(Gene::isExpressed).collect(Collectors.toList());
		
		if(expressedConnectionGenes.size() == 0) {
			log.warn("No expressed connection node where to add new node");
			return;
		}
		//randomNode
		Gene connectionGene = expressedConnectionGenes.get(random.nextInt(expressedConnectionGenes.size()));
		connectionGene.setExpressed(false);
		Gene nodeGene = new Gene();
		Gene firstNewConnectionGene = new Gene(connectionGene.getInputNodeGene(), nodeGene, 1.0, GeneticAlgorithm.nextInnovationNumber());
		Gene secondNewConnectionGene = new Gene(nodeGene, connectionGene.getOutputNodeGene(), 1.0, GeneticAlgorithm.nextInnovationNumber());
		individual.getGenome().addNodeGene(nodeGene);
		individual.getGenome().addConnectionGene(firstNewConnectionGene);
		individual.getGenome().addConnectionGene(secondNewConnectionGene);
		
	}

	private void addConnection(Individual individual) {
		boolean done = false;
		for (Gene node : individual.getGenome().getNodeGenes()) {
			for (Gene otherNode : individual.getGenome().getNodeGenes()) {
				if(node != otherNode && !(node.isInput() && otherNode.isInput()) && !(node.isOutput() && otherNode.isOutput())) {
					Gene connectionGene = individual.getGenome().getConnectionGene(node, otherNode);
					if(connectionGene == null) {
						connectionGene = new Gene(node, otherNode, randomWeight(), nextInnovationNumber());
						individual.getGenome().addConnectionGene(connectionGene);
						done = true;
						break;
					}
				}
			}
			if(done) {break;}
		}
		
	}

	private double randomWeight() {
		return NeuralNetwork.randomWeight(minWeightValue, maxWeightValue);
	}

	private void changeConnectionWeights(Individual individual) {
		for (Gene gene : individual.getGenome().getConnectionGenes()) {
			gene.setWeight(randomWeight());
		}
	}

	public void evaluatePopulation() {
		this.population.evaluate(goalFitnessValue);
	}

	public void setInitialPopulationCount(int initialPopulationCount) {
		this.initialPopulationCount = initialPopulationCount;
	}

	public Individual getFittestIndividual() {
		return population.getFittestIndividual();
	}

	public FitnessFunction getFitnessFunction() {
		return fitnessFunction;
	}

	public void setFitnessFunction(FitnessFunction fitnessFunction) {
		this.fitnessFunction = fitnessFunction;
	}

	public double getMaxWeightValue() {
		return maxWeightValue;
	}

	public void setMaxWeightValue(double maxWeightValue) {
		this.maxWeightValue = maxWeightValue;
	}

	public double getMinWeightValue() {
		return minWeightValue;
	}

	public void setMinWeightValue(double minWeightValue) {
		this.minWeightValue = minWeightValue;
	}

	public void initialize() {
		if(fitnessFunction == null) {
			throw new UnsupportedOperationException("Fitness function is not initialized");
		}
		
		log.info("initializing");
		
		this.population = new Population(initialPopulationCount, fitnessFunction, 
				getInputNodeCount(), getOutputNodeCount(), minWeightValue, maxWeightValue);
				
	}

	public int getInputNodeCount() {
		return inputs.size();
	}

	public int getOutputNodeCount() {
		return outputs.size();
	}

	public double getGoalFitnessValue() {
		return goalFitnessValue;
	}

	public void setGoalFitnessValue(double goalFitnessValue) {
		this.goalFitnessValue = goalFitnessValue;
	}

	public double getAllowedDeltaFromGoalFitnessValue() {
		return allowedDeltaFromGoalFitnessValue;
	}

	public void setAllowedDeltaFromGoalFitnessValue(double allowedDeltaFromGoalFitnessValue) {
		this.allowedDeltaFromGoalFitnessValue = allowedDeltaFromGoalFitnessValue;
	}

	public Population getPopulation() {
		return population;
	}

	public void addInput(String inputLabel) {
		inputs.add(inputLabel);
	}

	public void addOutput(String outputLabel) {
		outputs.add(outputLabel);
	}
	
	public List<String> getInputs() {
		return inputs;
	}

	public List<String> getOutputs() {
		return outputs;
	}
	
	public static long nextInnovationNumber() {
		return globalInnovationNumber++;
	}

	public static void setGlobalInnovationNumber(long globalInnovationNumber) {
		GeneticAlgorithm.globalInnovationNumber = globalInnovationNumber;
	}

	
}
