package dna.metrics.motifs.basti.motifcounter;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import edu.ucla.sspace.graph.SimpleDirectedEdge;
import edu.ucla.sspace.graph.SparseDirectedGraph;
import edu.ucla.sspace.graph.isomorphism.VF2IsomorphismTester;

public class IsoTester {
	
	public static boolean testIso(DirectedGraph<Integer, DefaultEdge> graph1, DirectedGraph<Integer, DefaultEdge> graph2) {
		SparseDirectedGraph g1 = new SparseDirectedGraph(graph1.vertexSet());
		for(DefaultEdge edge : graph1.edgeSet()) {
			g1.add(new SimpleDirectedEdge(
					graph1.getEdgeSource(edge),
					graph1.getEdgeTarget(edge)));
		}
		
		SparseDirectedGraph g2 = new SparseDirectedGraph(graph2.vertexSet());
		for(DefaultEdge edge : graph2.edgeSet()) {
			g2.add(new SimpleDirectedEdge(
					graph2.getEdgeSource(edge),
					graph2.getEdgeTarget(edge)));
		}
		
		boolean res = new VF2IsomorphismTester().areIsomorphic(g1, g2);
		return res;
	}
}
