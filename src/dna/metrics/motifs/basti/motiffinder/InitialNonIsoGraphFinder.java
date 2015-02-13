package dna.metrics.motifs.basti.motiffinder;

import java.util.Collection;
import java.util.HashMap;

import dna.graph.Graph;
import dna.graph.edges.DirectedEdge;
import dna.graph.edges.IEdge;
import dna.graph.nodes.DirectedNode;
import dna.metrics.motifs.acc.AccCaller;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motifcounter.DirectedMotifCounter;
import dna.metrics.motifs.basti.motiffinder.hub.HubManager;
import dna.metrics.motifs.basti.utils.GraphTransformer;

public class InitialNonIsoGraphFinder {
	private static HashMap<Integer, DirectedNode> addedNodes;
	
	public static void initializeDirectedMotifCounter(Graph g, DirectedMotifCounter dmc,
			HubManager hubManager, int isoGraphSize) {
		
		SmallGraph transformedGraph = GraphTransformer.transformToSmallGraph(g);
		HashMap<Long, Integer> counts = new AccCaller().getMotifs(transformedGraph, isoGraphSize);
	}
	
	public static void initializeDirectedMotifCounter_old(Graph g, DirectedMotifCounter dmc,
			HubManager hubManager, int isoGraphSize) {
		addedNodes = new HashMap<>();
		
		SmallGraph growingGraph = new SmallGraph();
		Iterable<IEdge> edges = (Iterable<IEdge>)(Object)g.getEdges();
		for(IEdge edge : edges) {
			int n1Index = edge.getN1().getIndex();
			DirectedNode newNode1 = addedNodes.get(n1Index);
			if(newNode1 == null) {
				newNode1 = new DirectedNode(n1Index, g.getGraphDatastructures());
				addedNodes.put(n1Index, newNode1);
				
				growingGraph.getNodes().add(newNode1);
			}
			
			int n2Index = edge.getN2().getIndex();
			DirectedNode newNode2 = addedNodes.get(n2Index);
			if(newNode2 == null) {
				newNode2 = new DirectedNode(n2Index, g.getGraphDatastructures());
				addedNodes.put(n2Index, newNode2);
				
				growingGraph.getNodes().add(newNode2);
			}
			
			DirectedEdge newEdge = new DirectedEdge(newNode1, newNode2);
			newNode1.addEdge(newEdge);
			newNode2.addEdge(newEdge);
			growingGraph.getEdges().add(newEdge);
			
			Collection<Path> foundSubgraphs = MotifTraverser3.getSubgraphsForEdge(newEdge, isoGraphSize,
					hubManager, MotifTraverser3.EdgeAction.added);
			
			for(Path fsg : foundSubgraphs) {
				if(fsg.hasChanged() && fsg.getPrevGraph() != null) {
					dmc.decrementCounterFor(fsg.getPrevGraph());
				}
				dmc.incrementCounterFor(fsg.getGraph());
			}
		}
	}
}
