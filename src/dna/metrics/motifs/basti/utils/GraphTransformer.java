package dna.metrics.motifs.basti.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import dna.graph.Graph;
import dna.graph.IElement;
import dna.graph.datastructures.GDS;
import dna.graph.edges.DirectedEdge;
import dna.graph.edges.IEdge;
import dna.graph.nodes.DirectedNode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.datastructures.SmallGraph;

public class GraphTransformer {
	public static String transformToTxt(DirectedGraph<Integer, DefaultEdge> input) {
		StringBuilder sb = new StringBuilder();
		
		for(DefaultEdge edge : input.edgeSet()) {
			sb.append(input.getEdgeSource(edge).intValue());
			sb.append(" ");
			sb.append(input.getEdgeTarget(edge).intValue());
			sb.append(" 1\n");
		}
		
		return sb.toString();
	}
	
	public String transformToTxt(SmallGraph input) {
		StringBuilder sb = new StringBuilder();
		
		for(IEdge edge : input.getEdges()) {
			sb.append(edge.getN1().getIndex());
			sb.append(" ");
			sb.append(edge.getN2().getIndex());
			sb.append(" 1\n");
		}
		
		return sb.toString();
	}
	
	public static String transformToTxt(Graph input) {
		StringBuilder sb = new StringBuilder();
		
		Iterable<IEdge> edges = (Iterable<IEdge>)(Object)input.getEdges();
		for(IEdge edge : edges) {
			sb.append(edge.getN1().getIndex());
			sb.append(" ");
			sb.append(edge.getN2().getIndex());
			sb.append(" 1\n");
		}
		
		return sb.toString();
	}
	
	public static String transformToAccFile(SmallGraph graph) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(graph.getNodes().size());
		sb.append(" ");
		sb.append(graph.getEdges().size());
		sb.append("\n");
		
		for(IEdge edge : graph.getEdges()) {
			sb.append(edge.getN1().getIndex());
			sb.append(" ");
			sb.append(edge.getN2().getIndex());
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public static String transformToAccFile(dna.graph.Graph graph) {
		SmallGraph smallGraph = transformToSmallGraph(graph);
		return transformToAccFile(smallGraph);
	}
	
	public static SmallGraph transformToSmallGraph(DirectedGraph<Integer, DefaultEdge> input) {
		SmallGraph newSmallGraph = new SmallGraph();
		
		for(Integer vertex : input.vertexSet()) {
			newSmallGraph.getNodes().add(new DirectedNode(vertex, GDS.directed()));
		}
		
		for(DefaultEdge edge : input.edgeSet()) {
			DirectedNode src = (DirectedNode) newSmallGraph.getNodeByIndex(input.getEdgeSource(edge).intValue());
			DirectedNode dst = (DirectedNode) newSmallGraph.getNodeByIndex(input.getEdgeTarget(edge).intValue());
			
			DirectedEdge directedEdge = new DirectedEdge(src, dst);
			newSmallGraph.getEdges().add(directedEdge);
			src.addEdge(directedEdge);
			dst.addEdge(directedEdge);
		}
		
		return newSmallGraph;
	}
	
	public static SmallGraph transformToSmallGraph(edu.ucla.sspace.graph.Graph<edu.ucla.sspace.graph.DirectedEdge> input) {
		SmallGraph newSmallGraph = new SmallGraph();
		
		for(Integer vertex : input.vertices()) {
			newSmallGraph.getNodes().add(new DirectedNode(vertex, GDS.directed()));
		}
		
		for(edu.ucla.sspace.graph.DirectedEdge edge : input.edges()) {
			DirectedNode src = (DirectedNode) newSmallGraph.getNodeByIndex(edge.from());
			DirectedNode dst = (DirectedNode) newSmallGraph.getNodeByIndex(edge.to());
			
			DirectedEdge directedEdge = new DirectedEdge(src, dst);
			newSmallGraph.getEdges().add(directedEdge);
			src.addEdge(directedEdge);
			dst.addEdge(directedEdge);
		}
		
		return newSmallGraph;
	}
	
	public static SmallGraph transformToSmallGraph(dna.graph.Graph input) {
		SmallGraph newSmallGraph = new SmallGraph();
		
		Iterable<Node> nodes = (Iterable<Node>)(Object)input.nodes;
		for(Node vertex : nodes) {
			newSmallGraph.getNodes().add(vertex);
		}
		
		Iterable<dna.graph.edges.Edge> edges = (Iterable<dna.graph.edges.Edge>)(Object)input.edges;
		for(dna.graph.edges.Edge edge : edges) {
			DirectedNode src = (DirectedNode) newSmallGraph.getNodeByIndex(edge.getN1Index());
			DirectedNode dst = (DirectedNode) newSmallGraph.getNodeByIndex(edge.getN2Index());
			
			DirectedEdge directedEdge = new DirectedEdge(src, dst);
			newSmallGraph.getEdges().add(directedEdge);
			src.addEdge(directedEdge);
			dst.addEdge(directedEdge);
		}
		
		return newSmallGraph;
	}
}
