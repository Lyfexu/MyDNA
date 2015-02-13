package dna.metrics.motifs.basti.motiffinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motiffinder.hub.HubManager;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathInfo;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverter;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverterOptions;

public class MotifTraverser extends Traverser {
	
	private static int maxSize;
	private static HubManager hubManager;
	private static INode otherNode;
	private static INode startNode;
	private static Collection<Path> foundSubgraphs;
	private static boolean includeOtherNode;

	public  static Collection<Path> getSubgraphsForEdge(IEdge actEdge, INode startNode_, INode otherNode_,
			int maxSize_, HubManager hubManager_, boolean includeOtherNode_){
		if(maxSize_ <= 0)
			return new ArrayList<Path>();
		
		maxSize = maxSize_;
		hubManager = hubManager_;
		otherNode = otherNode_;
		startNode = startNode_;
		foundSubgraphs = new ArrayList<>();
		includeOtherNode = includeOtherNode_;

		SmallGraph startGraph = createStartGraph();
		
		traverseWithHubs(startNode, startGraph, new ArrayList<INode>());
		
		return foundSubgraphs;
	}
	
	private static SmallGraph createStartGraph() {
		SmallGraph graph = new SmallGraph();
		
		if(includeOtherNode) {
			graph.getNodes().add(otherNode);
		}
		
		return graph;
	}

	private static void traverseWithHubs(INode actNode, SmallGraph actPath, ArrayList<INode> openNeighbours){
		openNeighbours.remove(actNode);
		
		StoredPathInfo spi = hubManager.getStoredPathInfoForNode(actNode);
		if(spi != null && false){
			addHubPaths(actPath, spi);
		} else {
			traverse(actNode, actPath, openNeighbours);
		}
	}

	private static void addHubPaths(SmallGraph actPath, StoredPathInfo spi) {
		StoredPathConverterOptions options = new StoredPathConverterOptions();
		options.setActPath(actPath.getNodes());
		options.setMaxSize(maxSize - actPath.getSize());
		options.setOtherNode(otherNode);
		
		List<Path> newPaths = StoredPathConverter.toGraphs(spi.getStoredPathRoot(), options);
		
		for(Path newPath : newPaths){
			SmallGraph combinedPath = combineStoredPath(actPath, newPath.getGraph());
			addPathToFoundSubgraphs(combinedPath, newPath.hasChanged());
		}
	}

	private static void traverse(INode actNode, SmallGraph actPath, List<INode> openNeighbours){
		boolean graphChanged = actNode.equals(otherNode);
		
		SmallGraph clonedActPath = actPath.shallowClone();
		
		clonedActPath = addNodeWithConnectingEdgesToPath(actNode, clonedActPath);
		
		if(pathAlreadyFound(clonedActPath, foundSubgraphs))
			return;
		
		addPathToFoundSubgraphs(clonedActPath, graphChanged);
		
		if(clonedActPath.getSize() >= maxSize)
			return;
		
		Collection<INode> newNeighbours = getNeighboursOfNodeWithout(actNode, openNeighbours, clonedActPath);
		if(!includeOtherNode && actNode.equals(startNode)) {
			newNeighbours.remove(otherNode);
		}
		openNeighbours.addAll(newNeighbours);
		
		for(INode openNeighbour : openNeighbours){
			traverseWithHubs(openNeighbour, clonedActPath, new ArrayList<INode>(openNeighbours));
		}
	}
	
	private static SmallGraph combineStoredPath(SmallGraph actPath, SmallGraph graph) {
		SmallGraph clonedPath = actPath.shallowClone();
		clonedPath.getNodes().addAll(graph.getNodes());
		clonedPath.getEdges().addAll(graph.getEdges());
		
		return clonedPath;
	}
	
	private static void addPathToFoundSubgraphs(SmallGraph clonedActPath, boolean changed) {
		Path p = new Path(clonedActPath);
		p.setChanged(changed);
		
		foundSubgraphs.add(p);
	}
}
