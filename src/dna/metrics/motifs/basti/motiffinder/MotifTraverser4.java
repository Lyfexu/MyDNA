package dna.metrics.motifs.basti.motiffinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.RedundantHubExplorer;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverter;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverterOptions;
import dna.metrics.motifs.basti.utils.GraphUtils;

/**
 * @author Bastian Laur
 *
 */
public class MotifTraverser4 extends Traverser2 {
	
	private static enum Side{
		N1,
		N2,
		None
	}
	
	public static enum EdgeAction {
		added,
		removed
	}
	
	private static int maxSize;
	private static HubManager hubManager;
	private static INode otherNode;
	private static INode startNode;
	private static List<HashSet<Path>> foundSubgraphs;
	private static IEdge actEdge;
	private static boolean useHubs = true;
	private static Path startPath;
	private static Side sideThatUsesHubs;
	private static HashSet<StoredPathInfo> hubsToUpdate;

	public  static Collection<Path> getSubgraphsForEdge(IEdge actEdge_, int maxSize_,
			HubManager hubManager_, EdgeAction edgeAction){
		if(maxSize_ <= 0) {
			return new ArrayList<Path>();
		}
		
		init(actEdge_, maxSize_, hubManager_);
		
		startTraversing();
		
		updateHubs(edgeAction);
		
		return getFoundSubgraphsWithMaxSize();
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Update Hubs

	private static void updateHubs(EdgeAction edgeAction) {
		if (edgeAction.equals(EdgeAction.added) ) {
			updateHubsWithAddedEdge();
		} else if (edgeAction.equals(EdgeAction.removed)) {
			updateHubsWithRemovedEdge();
		}
	}

	private static void updateHubsWithRemovedEdge() {
		for(StoredPathInfo hubToUpdate : hubsToUpdate) {
			RedundantHubExplorer.removePath(actEdge, hubToUpdate.getStoredPathRoot());
		}
	}

	private static void updateHubsWithAddedEdge() {
		for (int i=2; i<=maxSize; i++) {
			for(StoredPathInfo hubToUpdate : hubsToUpdate) {
				for(Path foundSubgraph : foundSubgraphs.get(i)) {
					if(foundSubgraph.getGraph().getNodes().contains(hubToUpdate.getAssociatedNode())) {
						RedundantHubExplorer.addPath(actEdge, foundSubgraph, hubToUpdate.getStoredPathRoot());
					}
				}
			}
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Init

	private static void init(IEdge actEdge_, int maxSize_,
			HubManager hubManager_) {
		maxSize = maxSize_;
		hubManager = hubManager_;
		actEdge = actEdge_;
		startNode = actEdge_.getN1();
		otherNode = actEdge_.getN2();
		foundSubgraphs = new ArrayList<>(maxSize);
		startPath = createStartPath();
		sideThatUsesHubs = Side.None;
		hubsToUpdate = new HashSet<>();
		
		// The +1 allows to receive graph sets directly by size without the annoying -1
		// e.g. you can use foundSubgraphs.get(graph.size()) instead of
		// foundSubgraphs.get(graph.size()-1)
		for (int i=0; i<maxSize+1; i++) {
			foundSubgraphs.add(new HashSet<Path>());
		}
	}
	
	private static void startTraversing() {
		StoredPathInfo otherNodeHub = hubManager.getStoredPathInfoForNode(otherNode);
		boolean otherNodeIsHub = otherNodeHub != null;
		boolean startNodeIsHub = hubManager.getStoredPathInfoForNode(startNode) != null;
		
		Collection<INode> n1Neighbours = new ArrayList<INode>();
		Collection<INode> n2Neighbours = new ArrayList<INode>();
		
		if(startNodeIsHub && otherNodeIsHub) {
			hubsToUpdate.add(otherNodeHub);
			n1Neighbours.add(startNode);
			traverse(otherNode, startPath, n1Neighbours, n2Neighbours, false);
		}
		else if(startNodeIsHub) {
			n1Neighbours.add(startNode);
			traverseWithHubs(otherNode, startPath, n1Neighbours, n2Neighbours, false);
		} else {
			n2Neighbours.add(otherNode);
			traverseWithHubs(startNode, startPath, n1Neighbours, n2Neighbours, true);
		}
	}

	private static ArrayList<Path> getFoundSubgraphsWithMaxSize() {
		return new ArrayList<Path>(foundSubgraphs.get(maxSize));
	}
	
	private static Path createStartPath() {
		SmallGraph graph = new SmallGraph();
		graph.getNodes().add(startNode);
		graph.getNodes().add(otherNode);
		graph.getEdges().add(actEdge);
		
		Path path = new Path(graph);
		
		Traverser2.addConnectingEdgesToPath(startNode, path, actEdge, true);
		
		return path;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Traverse Decision

	private static void traverseWithHubs(INode actNode, Path actPath,
			Collection<INode> n1Neighbours, Collection<INode> n2Neighbours, boolean n1PathActive){
		
		Path clonedActPath = actPath.shallowClone();
		
		Collection<INode> activeNeighbours = getActiveNeighbours(n1Neighbours, n2Neighbours,
				n1PathActive);
		activeNeighbours.remove(actNode);
		
		StoredPathInfo spi = hubManager.getStoredPathInfoForNode(actNode);
		if(hubCanBeUsed(spi, n1PathActive)){
			hubsToUpdate.add(spi);
			setSideThatUsesHubs(n1PathActive);
			
			Collection<Path> newPaths = addHubPaths(actNode, clonedActPath, spi, n1PathActive);
			
			traverseFoundHubPaths(n1Neighbours, n2Neighbours, n1PathActive, newPaths);
		} else {
			if(spi != null) {
				hubsToUpdate.add(spi);
			}
			traverse(actNode, clonedActPath, n1Neighbours, n2Neighbours, n1PathActive);
		}
	}
	
	
///////////////////////////////////////////////////////////////////////////////////////////////////
// Normal Traverse
	
	private static void traverse(INode actNode, Path clonedActPath,
			Collection<INode> n1Neighbours, Collection<INode> n2Neighbours,
			boolean n1PathActive){
		
		Collection<INode> activeNeighbours = getActiveNeighbours(n1Neighbours, n2Neighbours,
				n1PathActive);
		
		if(!actNode.equals(startNode) && !actNode.equals(otherNode)) {
			addNodeToPath(actNode, clonedActPath);
		}
		if(!(actNode.equals(startNode) || actNode.equals(otherNode))) {
			addConnectingEdgesToPath(actNode, clonedActPath, actEdge, n1PathActive);
		}

		if(pathAlreadyFound(clonedActPath)) {
			if (!actNode.equals(startNode) && !actNode.equals(otherNode)) {
				return;
			}
		} else {
			foundSubgraphs.get(clonedActPath.getGraph().getSize()).add(clonedActPath);
		}
		
		if(clonedActPath.getGraph().getSize() >= maxSize) {
			return;
		}
		
		Collection<INode> newNeighbours = getNeighboursOfNodeWithout(actNode, n1Neighbours,
				n2Neighbours, clonedActPath.getGraph(), actEdge);
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
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Incorporate Hub Paths
	
	private static Collection<Path> addHubPaths(INode actNode, Path actPath, StoredPathInfo spi,
			boolean n1PathActive) {
		
		StoredPathConverterOptions options = createPathConverterOptions(actNode, actPath, n1PathActive);
		
		List<Path> newPaths = StoredPathConverter.toGraphs(spi.getStoredPathRoot(), options);
		
		scanPathsForHubs(newPaths);
		
		return mergeActPathWithHubPaths(actPath, newPaths);
	}

	private static boolean hubCanBeUsed(StoredPathInfo spi, boolean n1PathActive) {
		//return spi != null;
		
		if(!useHubs || spi == null ||
				n1PathActive && sideThatUsesHubs.equals(Side.N2) ||
				!n1PathActive && sideThatUsesHubs.equals(Side.N1)) {
			return false;
		}
		
		return true;
	}
	
	private static void setSideThatUsesHubs(boolean n1PathActive) {
		if(n1PathActive) {
			sideThatUsesHubs = Side.N1;
		} else {
			sideThatUsesHubs = Side.N2;
		}
	}

	/**
	 * Scans all paths for hub and add found hubs to the {@code hubsToUpdate} set. 
	 * 
	 * @param paths
	 */
	private static void scanPathsForHubs(List<Path> paths) {
		for (Path path : paths) {
			for (INode node : path.getGraph().getNodes()) {
				StoredPathInfo spi = hubManager.getStoredPathInfoForNode(node);
				if (spi != null) {
					hubsToUpdate.add(spi);
				}
			}
		}
	}

	private static Collection<Path> mergeActPathWithHubPaths(Path actPath, List<Path> newPaths) {
		Collection<Path> newCombinedPaths = new ArrayList<>();
		
		for(Path newPath : newPaths){
			Path combinedPath = combineStoredPath(actPath, newPath);
			
			if(pathAlreadyFound(combinedPath)) {
				continue;
			}
			
			if(combinedPath.hasChanged()) {
				SmallGraph prevGraph = combinedPath.getGraph().shallowClone();
				prevGraph.getEdges().remove(actEdge);
				combinedPath.setPrevGraph(prevGraph);
			}
			
			foundSubgraphs.get(combinedPath.getGraph().getSize()).add(combinedPath);
			newCombinedPaths.add(combinedPath);
		}
		
		return newCombinedPaths;
	}

	private static StoredPathConverterOptions createPathConverterOptions(INode actNode,
			Path actPath, boolean n1PathActive) {
		StoredPathConverterOptions options = new StoredPathConverterOptions();
		options.setActPath(actPath.getGraph().getNodes());
		options.setMaxSize(maxSize - actPath.getGraph().getSize());
		if(n1PathActive) {
			options.setOtherNode(otherNode);
			options.setStartNode(startNode);
		} else {
			options.setOtherNode(startNode);
			options.setStartNode(otherNode);
		}
		options.setOnlyExactSize(false);
		if(actNode.equals(startNode) || actNode.equals(otherNode)) {
			options.setExcludeStartHub(true);
		}
		
		return options;
	}
	
	private static Path combineStoredPath(Path path1, Path path2) {
		Path clonedPath = path1.shallowClone();
		
		clonedPath.getGraph().getNodes().addAll(path2.getGraph().getNodes());
		clonedPath.getGraph().getEdges().addAll(path2.getGraph().getEdges());
		
		boolean changed = path1.hasChanged() || path2.hasChanged();
		clonedPath.setChanged(changed);
		
		return clonedPath;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Traverse after Hub visit
	
	private static void traverseFoundHubPaths(Collection<INode> n1Neighbours,
			Collection<INode> n2Neighbours, boolean n1PathActive, Collection<Path> newPaths) {
		
		for (Path newPath : newPaths) {
			Collection<INode> cleanedN1Neighbours = cleanNeighbours(n1Neighbours, newPath);
			Collection<INode> cleanedN2Neighbours = cleanNeighbours(n2Neighbours, newPath);
			
			traverseAfterHub(newPath.shallowClone(), cleanedN1Neighbours, cleanedN2Neighbours,
					n1PathActive);
		}
	}

	/**
	 * Removes all nodes in {@code path} from {@code neighbours} and returns a new cleaned list.
	 * @param neighbours Neighbours to clean.
	 * @param path {@link Path} with the nodes that shall be removed. 
	 * @return
	 */
	private static Collection<INode> cleanNeighbours(Collection<INode> neighbours, Path path) {
		Collection<INode> cleanedNeighbours = new ArrayList<>(neighbours);
		cleanedNeighbours.removeAll(path.getGraph().getNodes());
		
		return cleanedNeighbours;
	}
	
	private static void traverseAfterHub(Path clonedActPath,
			Collection<INode> n1Neighbours, Collection<INode> n2Neighbours,
			boolean n1PathActive) {
		
		if(clonedActPath.getGraph().getSize() >= maxSize) {
			return;
		}
		
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

	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Helper
	
	private static Collection<INode> getActiveNeighbours(
			Collection<INode> n1Neighbours, Collection<INode> n2Neighbours,
			boolean n1PathActive) {
		
		if(n1PathActive) {
			return n1Neighbours;
		}
		return n2Neighbours;
	}
	
	private static boolean pathAlreadyFound(Path actPath) {
		return foundSubgraphs.get(actPath.getGraph().getSize()).contains(actPath);
	}
}
