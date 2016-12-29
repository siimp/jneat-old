package ee.siimp.jneat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Genome implements Serializable {
	private static final long serialVersionUID = 3926741534412604660L;
	
	private List<Gene> nodeGenes = new ArrayList<>();
	private List<Gene> inputNodeGenes = new ArrayList<>();
	private List<Gene> outputNodeGenes = new ArrayList<>();
	
	private List<Gene> connectionGenes = new ArrayList<>();
	private Random random = new Random();
	private Ribosome ribosome = new Ribosome();
	
	public Genome(int inputNodeCount, int outputNodeCount, double minWeightValue, double maxWeightValue) {
		
		for (int inputNodeIndex = 0; inputNodeIndex < inputNodeCount; inputNodeIndex++) {
			Gene gene = new Gene();
			gene.setInput(true);
			nodeGenes.add(gene);
			inputNodeGenes.add(gene);
		}
		
		for (int outputNodeIndex = 0; outputNodeIndex < outputNodeCount; outputNodeIndex++) {
			Gene gene = new Gene();
			gene.setOutput(true);
			nodeGenes.add(gene);
			outputNodeGenes.add(gene);
		}
		
		//in initial state all output nodes are connected to all input nodes with connection random weight
		for (Gene outputNodeGene : outputNodeGenes) {
			for (Gene inputNodeGene : inputNodeGenes) {
				Gene connectionGene = new Gene();
				double absRange = Math.abs(minWeightValue) + maxWeightValue;
				connectionGene.setWeight(maxWeightValue - random.nextDouble()*absRange);
				connectionGene.setOutputNodeGene(outputNodeGene);
				connectionGene.setInputNodeGene(inputNodeGene);
				connectionGenes.add(connectionGene);
			}
		}
	}

	public double[] calculate(double... inputValues) {
		NeuralNetwork neuralNetwork = ribosome.translate(this);
		return neuralNetwork.calculate(inputValues);
	}
	
	public List<Gene> getNodeGenes() {
		return nodeGenes;
	}
	public List<Gene> getConnectionGenes() {
		return connectionGenes;
	}

	public List<Gene> getInputNodeGenes() {
		return inputNodeGenes;
	}

	public List<Gene> getOutputNodeGenes() {
		return outputNodeGenes;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("");
		sb.append("connectionGenes:[");
		for (Gene gene : connectionGenes) {
			sb.append("w:"+gene.getWeight()+" ");
		}
		sb.append("]");
		return sb.toString();
	}

}
