package ee.siimp.jneat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NeuralNetwork {
	
	private Genome genome;
	private Map<Gene, Neuron> neuronGeneMap = new HashMap<>();
	private final static Random random = new Random();

	public NeuralNetwork(Genome genome) {
		this.genome = genome;
	}

	public double[] calculate(double... inputValues) {
		if(inputValues.length != genome.getInputNodeGenes().size()) {
			throw new RuntimeException("not enough input nodes");			
		}
		
		double[] output = new double[genome.getOutputNodeGenes().size()];
		
		List<Neuron> neuronList = new ArrayList<>();
		int inputValueIndex = 0;
		for (Gene nodeGene : genome.getNodeGenes()) {
			Neuron neuron = new Neuron();
			neuron.setGene(nodeGene);
			
			if(nodeGene.isInput()) {
				neuron.setValue(inputValues[inputValueIndex++]);
			} 
			
			for(Gene connectionGene: genome.getConnectionGenes()) {				
				if(connectionGene.getOutputNodeGene() == nodeGene) {
					neuron.getConnectionGenes().add(connectionGene);
				}
			}
			
			neuronList.add(neuron);
			neuronGeneMap.put(nodeGene, neuron);
		}
		
		boolean solved = false;
		while(!solved) {
			solved = true;
			for (Neuron neuron : neuronList) {
				if(!neuron.isSolved()) {
					solved = false;
					double value = 0.0;
					boolean connectionsSolvedForNeuron = true;
					for (Gene connectionGene : neuron.getConnectionGenes()) {
						Neuron inputNeuron = neuronGeneMap.get(connectionGene.getInputNodeGene());
						if(inputNeuron.isSolved()) {
							value += inputNeuron.getValue() * connectionGene.getWeight();
						} else {
							connectionsSolvedForNeuron = false;
							break;
						}
					}
					
					if(connectionsSolvedForNeuron) {
						neuron.setValue(value);
					}
				}
			}
		}

		
		for (int i = 0; i < genome.getOutputNodeGenes().size(); i++) {
			output[i] = neuronGeneMap.get(genome.getOutputNodeGenes().get(i)).getValue();
		}

		return output;	
	}
	
	public static double randomWeight(double minWeightValue, double maxWeightValue) {
		double absRange = Math.abs(minWeightValue) + maxWeightValue;
		return maxWeightValue - random.nextDouble() * absRange;	
	}

}

/**
 * Height index defines order in tree set, lower nodes have lower value
 *
 */
class Neuron {
	private Gene gene;
	private double value = 0;
	private boolean solved = false;
	private List<Gene> connectionGenes = new ArrayList<>();
	
	public Gene getGene() {
		return gene;
	}
	public void setGene(Gene gene) {
		this.gene = gene;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
		this.setSolved(true);
	}
	public boolean isSolved() {
		return solved;
	}
	public void setSolved(boolean solved) {
		this.solved = solved;
	}
	public List<Gene> getConnectionGenes() {
		return connectionGenes;
	}
	public void setConnectionGenes(List<Gene> connectionGenes) {
		this.connectionGenes = connectionGenes;
	}
	
	

}
