package dna.metrics.motifs.basti.motiffinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.mockito.cglib.core.CollectionUtils;

import com.google.common.collect.Iterables;

import dna.graph.Graph;
import dna.graph.edges.DirectedEdge;
import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motiffinder.hub.HubManager;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathInfo;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverter;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverterOptions;
import dna.metrics.motifs.basti.utils.GraphUtils;
import dna.metrics.motifs.basti.utils.SmallGraphUtils;
import dna.util.ArrayUtils;

public class HubImprovedSubgraphFinder {
	
	private static HashMap<Integer, List<Path>> foundSubgraphsTmp;
	private static HashMap<Integer, List<Path>> foundSubgraphs1;
	private static HashMap<Integer, List<Path>> foundSubgraphs2;
	private static IEdge actEdge;
	public static boolean visualize = true;
	private static int maxSize;
	private static INode otherNode;
	private static HubManager hubManager;
	private static Collection<INode> stopNodes;
	private static boolean otherNodeAllowed;
	private static boolean stopAtHubs;
	
	public static Collection<Path> findSubgraphsForNode(INode node, int maxSize_, Collection<INode> stopNodes_){
		if(maxSize_ <= 0)
			return new ArrayList<Path>();
		
		maxSize = maxSize_;
		stopNodes = stopNodes_;
		otherNode = null;
		otherNodeAllowed = true;
		stopAtHubs = true;
		traverseWithHubs(node, new SmallGraph(), new ArrayList<INode>());
		
		ArrayList<Path> returnList = new ArrayList<>();
		for(List<Path> entry : foundSubgraphsTmp.values()) {
			returnList.addAll(entry);
		}
		
		return returnList;
	}
	
	public static List<Path> findSubgraphsForEdge(IEdge edge, int maxSize_, HubManager hubManager_){
		
		if(maxSize_ <= 0)
			return new ArrayList<Path>();
		
		maxSize = maxSize_;
		hubManager = hubManager_;
		actEdge = edge;
		stopAtHubs = false;
		
		otherNodeAllowed = true;
		foundSubgraphsTmp = new HashMap<Integer, List<Path>>();
		otherNode = edge.getN2();
		traverseWithHubs(edge.getN1(), new SmallGraph(Arrays.asList(otherNode), null), new ArrayList<INode>());
		foundSubgraphs1 = foundSubgraphsTmp;
		
		otherNodeAllowed = false;
		foundSubgraphsTmp = new HashMap<Integer, List<Path>>();
		otherNode = edge.getN1();
		traverseWithHubs(edge.getN2(), new SmallGraph(Arrays.asList(otherNode), null), new ArrayList<INode>());
		foundSubgraphs2 = foundSubgraphsTmp;
		
		List<Path> returnList = combineGraphsToDesiredSize();
		
		return returnList;
	}
	
	private static void traverseWithHubs(INode actNode, SmallGraph actPath, ArrayList<INode> openNeighbours){
		openNeighbours.remove(actNode);
		
		StoredPathInfo spi = hubManager.getStoredPathInfoForNode(actNode);
		if(spi != null){
			if(stopAtHubs) {
				SmallGraph clonedActPath = actPath.shallowClone();
				clonedActPath = addNodeWithConnectingEdgesToPath(actNode, clonedActPath);
				
				if(pathAlreadyFound(clonedActPath))
					return;
				addPathToFoundSubgraphs(clonedActPath, false, true);
				
				return;
			}
			
			StoredPathConverterOptions options = new StoredPathConverterOptions();
			options.setActPath(actPath.getNodes());
			options.setMaxSize(maxSize - actPath.getSize());
			options.setOtherNode(otherNode);
			
			List<Path> newPaths = StoredPathConverter.toGraphs(
										spi.getStoredPathRoot(), options);
			
			for(Path newPath : newPaths){
				SmallGraph combinedPath = combineStoredPath(actPath, newPath.getGraph());
				addPathToFoundSubgraphs(combinedPath, newPath.hasChanged());
			}
		} else {
			traverse(actNode, actPath, openNeighbours);
		}
	}
	
	private static SmallGraph combineStoredPath(SmallGraph actPath, SmallGraph graph) {
		SmallGraph clonedPath = actPath.shallowClone();
		clonedPath.getNodes().addAll(graph.getNodes());
		clonedPath.getEdges().addAll(graph.getEdges());
		
		return clonedPath;
	}

	private static void traverse(INode actNode, SmallGraph actPath, List<INode> openNeighbours){
		boolean graphChanged = actNode.equals(otherNode);
		
		if(!otherNodeAllowed && graphChanged)
			return;
		
		SmallGraph clonedActPath = actPath.shallowClone();
		
		clonedActPath = addNodeWithConnectingEdgesToPath(actNode, clonedActPath);
		
		if(pathAlreadyFound(clonedActPath))
			return;
		
		addPathToFoundSubgraphs(clonedActPath, graphChanged);
		
		if(clonedActPath.getSize() >= maxSize)
			return;
		
		Collection<INode> newNeighbours = getNeighboursOfNodeWithout(actNode, openNeighbours, clonedActPath);
		openNeighbours.addAll(newNeighbours);
		
		for(INode openNeighbour : openNeighbours){
			traverseWithHubs(openNeighbour, clonedActPath, new ArrayList<INode>(openNeighbours));
		}
	}
	
	private static SmallGraph addNodeWithConnectingEdgesToPath(INode node, SmallGraph path){
		path = addNodeToPath(node, path);
		path = addConnectingEdgesToPath(node, path);
			
		return path;
	}
	
	private static SmallGraph addNodeToPath(INode node, SmallGraph path){
		path.getNodes().add(node);
		return path;
	}
	
	private static SmallGraph addConnectingEdgesToPath(INode node, SmallGraph path){
		Collection<IEdge> edges = SmallGraphUtils.getAllConnectingEdges(node, path);
		path.getEdges().addAll(edges);
		
		return path;
	}
	
	private static boolean pathAlreadyFound(SmallGraph path){
		
		int size = path.getSize();
		
		List<Path> foundGraphs = foundSubgraphsTmp.get(size);
		if(foundGraphs != null) {
			for(Path foundSubgraph : foundGraphs){
				if(path.equals(foundSubgraph.getGraph()))
					return true;
			}
		}
		
		if(foundSubgraphs1 == null)
			return false;
		
		foundGraphs = foundSubgraphs1.get(size);
		if(foundGraphs != null) {
			for(Path foundSubgraph : foundGraphs){
				if(path.equals(foundSubgraph.getGraph()))
					return true;
			}
		}
		
		return false;
	}
	
	private static void addPathToFoundSubgraphs(SmallGraph path, boolean changed){
		addPathToFoundSubgraphs(path, changed, false);
	}
	
	private static void addPathToFoundSubgraphs(SmallGraph path, boolean changed, boolean endsWithHub){
		int size = path.getSize();
		List<Path> foundGraphs = foundSubgraphsTmp.get(size);
		Path possblChangedGraph = new Path(path);
		possblChangedGraph.setChanged(changed);
		possblChangedGraph.setEndsWithHub(endsWithHub);
		
		if(foundGraphs == null){
			List<Path> newFoundGraphs = new ArrayList<>();
			newFoundGraphs.add(possblChangedGraph);
			foundSubgraphsTmp.put(size, newFoundGraphs);
		} else{
			foundGraphs.add(possblChangedGraph);
		}
	}
	
	private static Collection<INode> getNeighboursOfNodeWithout(INode node, List<INode> openNeighbours, SmallGraph actPath){
		Collection<INode> neighbourNodes = GraphUtils.getNeighboursOfNode(node);
		neighbourNodes.removeAll(openNeighbours);
		neighbourNodes.removeAll(actPath.getNodes());
		
		return neighbourNodes;
	}
	
	public static SmallGraph combineGraphsAndAddConnectionEdges(SmallGraph graph1, SmallGraph graph2){
		SmallGraph clonedGraph = graph1.shallowClone();
		clonedGraph.getNodes().addAll(graph2.getNodes());
		clonedGraph.getEdges().addAll(graph2.getEdges());
		
		List<IEdge> newEdges = SmallGraphUtils.getAllConnectingEdges(graph1, graph2);
		clonedGraph.getEdges().addAll(newEdges);
		
		return clonedGraph;
	}
	
	private static List<Path> combineGraphsToDesiredSize(){
		
		List<Path> returnList = new ArrayList<>();
		
		for(int size1 : foundSubgraphs1.keySet()){
			if(size1 == maxSize) {
				returnList.addAll(foundSubgraphs1.get(maxSize));
			} else {
				int minSize2 = maxSize - size1;
				for(int size2 = minSize2; size2<maxSize; size2++) {
					List<Path> properOtherGraphs = foundSubgraphs2.get(size2);
					if(properOtherGraphs == null || properOtherGraphs.size() == 0) {
						continue;
					}
					
					List<Path> newPaths = intersectGraphsWithSizeFilter(foundSubgraphs1.get(size1), properOtherGraphs, maxSize);
					returnList.addAll(newPaths);
				}
			}
		}
		
		List<Path> foundGraphs2OfMaxSize = foundSubgraphs2.get(maxSize);
		if(foundGraphs2OfMaxSize != null)
			returnList.addAll(foundGraphs2OfMaxSize);
		
		return returnList;
	}

	private static List<Path> intersectGraphsWithSizeFilter(List<Path> graphs1, List<Path> graphs2, int desiredSize){
		List<Path> returnList = new ArrayList<>();
		
		if(graphs1 == null || graphs2 == null)
			return returnList;
		
		for(Path graph1 : graphs1){
			for(Path graph2 : graphs2){
				SmallGraph newGraph = intersectGraphs(graph1.getGraph(), graph2.getGraph());
				if(newGraph.getNodes().size() != desiredSize) {
					continue;
				}
				
				boolean changed = graph1.hasChanged() || graph2.hasChanged();
				
				Path newPath = new Path(newGraph);
				newPath.setChanged(changed);
				returnList.add(newPath);
			}
		}
		
		return returnList;
	}
	
	private static SmallGraph intersectGraphs(SmallGraph graph1, SmallGraph graph2){
		SmallGraph clonedGraph = graph1.shallowClone();
		
		for(INode node : graph2.getNodes()) {
			if(!clonedGraph.getNodes().contains(node)) {
				clonedGraph.getNodes().add(node);
			}
		}
		
		for(IEdge edge : graph2.getEdges()) {
			if(!clonedGraph.getEdges().contains(edge)) {
				clonedGraph.getEdges().add(edge);
			}
		}
		
		return clonedGraph;
	}
}
