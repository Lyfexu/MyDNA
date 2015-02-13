package dna.metrics.motifs.basti.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Iterables;

import dna.graph.IElement;
import dna.graph.edges.Edge;
import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.datastructures.SmallGraph;

public class SmallGraphUtils {
	public static Collection<IEdge> getAllConnectingEdges(INode node, SmallGraph graph){
		Collection<IEdge> returnList = new ArrayList<>(node.getDegree());
		
		Iterable<IEdge> edges = (Iterable<IEdge>)(Object)node.getEdges();
		for(IEdge edge : edges){
			Node diffNode = edge.getDifferingNode((Node)node);
			if(graph.getNodes().contains(diffNode)) {
				returnList.add(edge);
			}
		}
		
		return returnList;
	}
	
	public static List<IEdge> getAllConnectingEdges(SmallGraph graph1, SmallGraph graph2){
		Collection<INode> graph1Nodes = graph1.getNodes();
		List<IEdge> returnList = new ArrayList<>();
		
		for(INode graph1Node : graph1Nodes){
			
			for(IElement elem : graph1Node.getEdges()){
				if(!(elem instanceof IEdge))
					continue;
				IEdge edge = (IEdge)elem;
				
				Node diffNode = edge.getDifferingNode((Node)graph1Node);
				INode foundNeighbour = graph2.getNodeByIndex(diffNode.getIndex());
				if(foundNeighbour != null){
					returnList.add(edge);
				}
			}
		} 
		
		return returnList;
	}
}
