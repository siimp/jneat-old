package ee.siimp.jneat.gui;

import java.util.HashMap;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import ee.siimp.jneat.Gene;
import ee.siimp.jneat.GeneticAlgorithm;
import ee.siimp.jneat.Genome;
import ee.siimp.jneat.Individual;

public class GeneticAlgorithmViwer {
	
	private GeneticAlgorithm geneticAlgorithm;
	private Graph graph;
	private Map<Gene, Node> geneToNode = new HashMap<>();

	public GeneticAlgorithmViwer(GeneticAlgorithm geneticAlgorithm) {
		this.graph = new MultiGraph("population");
		this.geneticAlgorithm = geneticAlgorithm;
		graph.display();
		update();
	}

	public void update() {
		for (int individualIndex = 0; individualIndex < geneticAlgorithm.getPopulation().getIndividuals().size(); individualIndex++) {
			Individual individual = geneticAlgorithm.getPopulation().getIndividuals().get(individualIndex);
			Genome genome = individual.getGenome();
			
			for (int i = 0; i < genome.getInputNodeGenes().size(); i++) {
				Gene gene = genome.getInputNodeGenes().get(i);
				String name = geneticAlgorithm.getInputs().get(i)+String.format("(%d)", individualIndex);
				if(graph.getNode(name) != null) { continue; }
				
				Node inputNode = graph.addNode(name);
				inputNode.addAttribute("ui.label", name);
				geneToNode.put(gene, inputNode);
			}
			
			for (int i = 0; i < genome.getOutputNodeGenes().size(); i++) {
				Gene gene = genome.getOutputNodeGenes().get(i);
				String name = geneticAlgorithm.getOutputs().get(i)+String.format("(%d)", individualIndex);
				if(graph.getNode(name) != null) { continue; }
				
				Node outputNode = graph.addNode(name);
				outputNode.addAttribute("ui.label", name);
				geneToNode.put(gene, outputNode);
			}
			
			for (int i = 0; i < genome.getNodeGenes().size(); i++) {
				Gene gene = genome.getNodeGenes().get(i);
				String name = "" + gene.hashCode();
				if(graph.getNode(name) != null) { continue; }
				
				Node node = graph.addNode(name);
				
				geneToNode.put(gene, node);
			}
			
			for (Gene connectionGene : genome.getConnectionGenes()) {
				Node n1 = geneToNode.get(connectionGene.getInputNodeGene());
				Node n2 = geneToNode.get(connectionGene.getOutputNodeGene());
				String name = n1.getId()+n2.getId();
				if(graph.getEdge(name) != null) { 
					if(!connectionGene.isExpressed()) {
						graph.removeEdge(name);
					}
					continue; 
				}
				
				Edge e = graph.addEdge(name, n1.getId(), n2.getId());
				e.addAttribute("ui.label", String.format("w:(%.2f)", connectionGene.getWeight()));
				
			}
			
		}		
		
	}

}
