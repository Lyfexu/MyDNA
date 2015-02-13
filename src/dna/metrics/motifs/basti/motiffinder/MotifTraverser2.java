package dna.metrics.motifs.basti.motiffinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motiffinder.hub.HubManager;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathInfo;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverter;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverterOptions;

public class MotifTraverser2 extends Traverser2 {
	
	private static int maxSize;
	private static HubManager hubManager;
	private static INode otherNode;
	private static INode startNode;
	private static Collection<Path> foundSubgraphs;
	private static IEdge actEdge;
	private static ArrayList<Path> allFoundSubgraphs;
	private static boolean firstRun;
	private static boolean useHubs = true;

	public  static Collection<Path> getSubgraphsForEdge(IEdge actEdge_, int maxSize_, HubManager hubManager_){
		if(maxSize_ <= 0)
			return new ArrayList<Path>();
		
		maxSize = maxSize_;
		hubManager = hubManager_;
		actEdge = actEdge_;
		startNode = actEdge_.getN1();
		otherNode = actEdge_.getN2();
		allFoundSubgraphs = new ArrayList<>();
		foundSubgraphs = new ArrayList<>();
		firstRun = true;
		
		Path startPath = createStartPath();
		Collection<INode> n1Neighbours = null;
		Collection<INode> n2Neighbours = null;
		
		if(useHubs) {
			if(hubManager.getStoredPathInfoForNode(startNode) != null) {
				n1Neighbours = new ArrayList<INode>();
				n1Neighbours.add(startNode);
			} else {
				n1Neighbours = getNeighboursOfNodeWithout(startNode, Arrays.asList(otherNode), new SmallGraph(), actEdge);
			}
			if(hubManager.getStoredPathInfoForNode(otherNode) != null) {
				n2Neighbours = new ArrayList<INode>();
				n2Neighbours.add(otherNode);
			} else {
				n2Neighbours = getNeighboursOfNodeWithout(otherNode, Arrays.asList(startNode), new SmallGraph(), actEdge);
			}
		} else {
			n1Neighbours = getNeighboursOfNodeWithout(startNode, Arrays.asList(otherNode), new SmallGraph(), actEdge);
			n2Neighbours = getNeighboursOfNodeWithout(otherNode, Arrays.asList(startNode), new SmallGraph(), actEdge);
		}
		
		traverseWithHubs(startNode, startPath, n1Neighbours, n2Neighbours, true);
		
		
		ArrayList<Path> returnList = new ArrayList<Path>();
		for(Path p : foundSubgraphs) {
			// todo remove
			HashSet<IEdge> tmp = new HashSet<IEdge>(p.getGraph().getEdges());
			if(tmp.size() != p.getGraph().getEdges().size()) {
				System.out.println("lol");
			}
			// todo remove
			if(p.getGraph().getEdges().size() == 1 &&
					p.getGraph().getSize() == 3) {
				System.out.println("lol");
			}
			
			if(p.getGraph().getSize() == maxSize) {
				returnList.add(p);
			}
		}
		return returnList;
	}
	
	private static Path createStartPath() {
		SmallGraph graph = new SmallGraph();
		graph.getNodes().add(otherNode);
		graph.getEdges().add(actEdge);
		
		Path path = new Path(graph);
		
		return path;
	}

	private static void traverseWithHubs(INode actNode, Path actPath,
			Collection<INode> n1Neighbours, Collection<INode> n2Neighbours, boolean n1PathActive){
		
		Path clonedActPath = actPath.shallowClone();
		
		Collection<INode> activeNeighbours = getActiveNeighbours(n1Neighbours, n2Neighbours, n1PathActive);
		activeNeighbours.remove(actNode);
		
		StoredPathInfo spi = hubManager.getStoredPathInfoForNode(actNode);
		if(spi != null && useHubs){
			if(actNode.equals(otherNode)) {
				clonedActPath.getGraph().getNodes().remove(otherNode);
				clonedActPath.getGraph().getEdges().remove(actEdge);
			}
			
			addHubPaths(actNode, clonedActPath, spi,n1PathActive);
			StoredPathConverter.incorporatePathIntoHub(actEdge, clonedActPath.getGraph(), spi.getStoredPathRoot());
			
			if(actNode.equals(startNode) && firstRun) {
				firstRun = false;
				clonedActPath.getGraph().getNodes().add(startNode);
				clonedActPath.getGraph().getEdges().add(actEdge);
				clonedActPath.getGraph().getNodes().remove(otherNode);
				n1Neighbours.add(startNode);
				traverseWithHubs(otherNode, clonedActPath, n1Neighbours, n2Neighbours, false);
			}
		} else {
			traverse(actNode, clonedActPath, n1Neighbours, n2Neighbours, n1PathActive);
		}
	}

	private static Collection<INode> getActiveNeighbours(
			Collection<INode> n1Neighbours, Collection<INode> n2Neighbours,
			boolean n1PathActive) {
		
		if(n1PathActive) {
			return n1Neighbours;
		}
		return n2Neighbours;
	}

	private static void addHubPaths(INode actNode, Path actPath, StoredPathInfo spi, boolean n1PathActive) {
		if(actNode == startNode || actNode == otherNode) {
			actPath.getGraph().getEdges().remove(actEdge);
		}
		
		StoredPathConverterOptions options = new StoredPathConverterOptions();
		options.setActPath(actPath.getGraph().getNodes());
		options.setMaxSize(maxSize - actPath.getGraph().getSize());
		if(n1PathActive) {
			options.setOtherNode(otherNode);
		} else {
			options.setOtherNode(startNode);
		}
		options.setOnlyExactSize(true);
		
		List<Path> newPaths = StoredPathConverter.toGraphs(spi.getStoredPathRoot(), options);
		
		for(Path newPath : newPaths){
			Path combinedPath = combineStoredPath(actPath, newPath);
			
			if(combinedPath.hasChanged() && combinedPath.getPrevGraph() == null) {
				SmallGraph prevGraph = combinedPath.getGraph().shallowClone();
				prevGraph.getEdges().remove(actEdge);
				combinedPath.setPrevGraph(prevGraph);
			}
			
			if(pathAlreadyFound(combinedPath, foundSubgraphs))
				return;
			
			// todo remove
			if(combinedPath.getGraph().getNodes().size() == 3 &&
					combinedPath.getGraph().getEdges().size() == 1) {
				System.out.println("lol");
			}
			
			// todo remove
			HashSet<IEdge> edgeHashSet = new HashSet<IEdge>(combinedPath.getGraph().getEdges());
			if(combinedPath.getGraph().getEdges().size() != edgeHashSet.size()) {
				System.out.println("loL");
			}
			
			foundSubgraphs.add(combinedPath);
		}
	}

	private static void traverse(INode actNode, Path actPath,
			Collection<INode> n1Neighbours, Collection<INode> n2Neighbours,
			boolean n1PathActive){
		
		firstRun = false;
		Collection<INode> activeNeighbours = getActiveNeighbours(n1Neighbours, n2Neighbours, n1PathActive);
		
		Path clonedActPath = actPath.shallowClone();
		
		addNodeWithConnectingEdgesToPath(actNode, clonedActPath, actEdge, n1PathActive);
		
		if(pathAlreadyFound(clonedActPath, foundSubgraphs))
			return;
		
		// todo remove
		if(clonedActPath.getGraph().getEdges().size() == 1 &&
				clonedActPath.getGraph().getSize() == 3) {
			System.out.println("lol");
		}
		foundSubgraphs.add(clonedActPath);
		
		if(clonedActPath.getGraph().getSize() >= maxSize)
			return;
		
		Collection<INode> newNeighbours = getNeighboursOfNodeWithout(actNode, activeNeighbours, clonedActPath.getGraph(), actEdge);
		activeNeighbours.addAll(newNeighbours);
		
		for(INode n1Neighbour : n1Neighbours){
			traverseWithHubs(n1Neighbour, clonedActPath,
					new ArrayList<INode>(n1Neighbours), new ArrayList<INode>(n2Neighbours),
					true);
		}
		
		for(INode n2Neighbour : n2Neighbours){
			traverseWithHubs(n2Neighbour, clonedActPath,
					new ArrayList<INode>(n1Neighbours), new ArrayList<INode>(n2Neighbours),
					false);
		}
	}
	
	private static Path combineStoredPath(Path path1, Path path2) {
		Path clonedPath = path1.shallowClone();
		
		clonedPath.getGraph().getNodes().addAll(path2.getGraph().getNodes());
		clonedPath.getGraph().getEdges().addAll(path2.getGraph().getEdges());
		
		boolean changed = path1.hasChanged() || path2.hasChanged();
		clonedPath.setChanged(changed);
		
		return clonedPath;
	}
}
