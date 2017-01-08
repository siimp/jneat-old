package ee.siimp.jneat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Genome implements Serializable {
	private static final long serialVersionUID = 3926741534412604660L;
	
	private List<Gene> nodeGenes = new ArrayList<>();
	private List<Gene> inputNodeGenes = new ArrayList<>();
	private List<Gene> outputNodeGenes = new ArrayList<>();
	private List<Gene> connectionGenes = new ArrayList<>();
	private Map<Gene, List<Gene>> nodeGeneConnectionGenes = new HashMap<>();
	private Ribosome ribosome = new Ribosome();
	
	public Genome(int inputNodeCount, int outputNodeCount, double minWeightValue, double maxWeightValue) {
		
		for (int inputNodeIndex = 0; inputNodeIndex < inputNodeCount; inputNodeIndex++) {
			Gene gene = new Gene(true);
			nodeGenes.add(gene);
			inputNodeGenes.add(gene);
		}
		
		for (int outputNodeIndex = 0; outputNodeIndex < outputNodeCount; outputNodeIndex++) {
			Gene gene = new Gene(false);
			nodeGenes.add(gene);
			outputNodeGenes.add(gene);
		}
		
		//in initial state all output nodes are connected to all input nodes with connection random weight
		long connectionGeneInnovation = 1;
		for (Gene outputNodeGene : outputNodeGenes) {
			for (Gene inputNodeGene : inputNodeGenes) {
				Gene connectionGene = new Gene(inputNodeGene, outputNodeGene, 
						NeuralNetwork.randomWeight(minWeightValue, maxWeightValue), connectionGeneInnovation++);
				connectionGene.setWeight(NeuralNetwork.randomWeight(minWeightValue, maxWeightValue));
				connectionGenes.add(connectionGene);
				addConnectionGeneToNodeGeneList(connectionGene);
			}
		}
		GeneticAlgorithm.setGlobalInnovationNumber(connectionGeneInnovation);
	}

	private void addConnectionGeneToNodeGeneList(Gene connectionGene) {
		nodeGeneConnectionGenes.putIfAbsent(connectionGene.getInputNodeGene(), new ArrayList<>());
		nodeGeneConnectionGenes.get(connectionGene.getInputNodeGene()).add(connectionGene);
		nodeGeneConnectionGenes.putIfAbsent(connectionGene.getOutputNodeGene(), new ArrayList<>());
		nodeGeneConnectionGenes.get(connectionGene.getOutputNodeGene()).add(connectionGene);
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
		sb.append("connectionGenes: ");
		for (Gene gene : connectionGenes) {
			sb.append(String.format("(w: %.2f; i: %d) ", gene.getWeight(), gene.getInnovation()));
		}
		return sb.toString();
	}

	public Gene getConnectionGene(Gene nodeGene, Gene otherNodeGene) {
		List<Gene> connectionGenes = nodeGeneConnectionGenes.get(nodeGene);
		if(connectionGenes != null) {
			for (Gene connectionGene : connectionGenes) {
				if(connectionGene.getInputNodeGene() == otherNodeGene || connectionGene.getOutputNodeGene() == otherNodeGene) {
					return connectionGene;
				} 
			}
		}
		return null;
	}

	public void addConnectionGene(Gene connectionGene) {
		this.connectionGenes.add(connectionGene);
		addConnectionGeneToNodeGeneList(connectionGene);		
	}

	public void addNodeGene(Gene nodeGene) {
		getNodeGenes().add(nodeGene);
		
	}

}
