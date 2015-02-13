package dna.metrics.motifs.basti.motiffinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.google.common.collect.Iterables;

import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.utils.GraphUtils;

public abstract class Traverser2 {
	
	protected static void addNodeWithConnectingEdgesToPath(INode node, Path path, IEdge actEdge, boolean n1PathActive){
		addNodeToPath(node, path);
		addConnectingEdgesToPath(node, path, actEdge, n1PathActive);
	}
	
	protected static void addNodeToPath(INode node, Path path){
		path.getGraph().getNodes().add(node);
	}
	
	protected static void addConnectingEdgesToPath(INode node, Path path, IEdge actEdge,
			boolean n1PathActive){
		Collection<IEdge> edges = getAllConnectingEdges(node, path, actEdge, n1PathActive);
		addConnectingEdgesToPath(node, path, actEdge, n1PathActive, edges);
	}
	
	protected static void addConnectingEdgesToPath(INode node, Path path, IEdge actEdge,
			boolean n1PathActive, Collection<IEdge> edges){
		
		if(!n1PathActive && node.equals(actEdge.getN2())) {
			for(IEdge edge : edges) {
				if(edge.getN1().equals(actEdge.getN2()) && edge.getN2().equals(actEdge.getN1())) {
					edges.remove(edge);
					break;
				}
			}
		}
		
		path.getGraph().getEdges().addAll(edges);
		
		if(path.hasChanged()) {
			setPrevGraph(path, actEdge);
		}
	}
	
	protected static Collection<IEdge> getAllConnectingEdges(INode node, Path path, IEdge actEdge,
			boolean n1PathActive){
		Collection<IEdge> returnList = new ArrayList<IEdge>(node.getDegree());
		
		Iterable<IEdge> edges = GraphUtils.getEdgesForNode(node);
		for(IEdge edge : edges){
			if(edge.equals(actEdge)) {
				continue;
			}
			
			Node diffNode = edge.getDifferingNode((Node)node);
			if(path.getGraph().getNodes().contains(diffNode)) {
				returnList.add(edge);
				
				if(diffNode.equals(actEdge.getN2()) && n1PathActive) {
					path.setChanged(true);
				}
			}
		}
		
		return returnList;
	}

	private static void setPrevGraph(Path path, IEdge actEdge) {
		SmallGraph prevGraph = path.getGraph().shallowClone();
		prevGraph.getEdges().remove(actEdge);
		path.setPrevGraph(prevGraph);
	}
	
	protected static boolean pathAlreadyFound(Path actPath, Collection<Path> foundPaths){
		
		for(Path foundPath : foundPaths) {
			if(foundPath.getGraph().equals(actPath.getGraph())) {
				return true;
			}
		}
		
		return false;
	}
	
	protected static Collection<INode> getNeighboursOfNodeWithout(INode node,
			Collection<INode> n1Neighbours, Collection<INode> n2Neighbours,
			SmallGraph actPath){
		return getNeighboursOfNodeWithout(node, n1Neighbours, n2Neighbours, actPath, null);
	}
	
	protected static Collection<INode> getNeighboursOfNodeWithout(INode node,
			Collection<INode> n1Neighbours, Collection<INode> n2Neighbours,
			SmallGraph actPath, IEdge actEdge){
		
		Iterable<IEdge> edges = (Iterable<IEdge>)(Object)node.getEdges();
		Collection<INode> neighbourNodes = new ArrayList<INode>(Iterables.size(edges));
		
		for(IEdge edge : edges){
			if(edge.equals(actEdge)) {
				continue;
			}
			
			INode diffNode = edge.getDifferingNode((Node)node);
			
			if(!neighbourNodes.contains(diffNode) &&
					!n1Neighbours.contains(diffNode) &&
					!n2Neighbours.contains(diffNode) &&
					!actPath.getNodes().contains(diffNode)) {
				neighbourNodes.add(diffNode);
			}
		}
		
		return neighbourNodes;
	}
}
