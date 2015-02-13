package dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterables;

import dna.graph.edges.Edge;
import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motiffinder.hub.HubTraverser;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathVertex;
import dna.metrics.motifs.basti.utils.GraphUtils;

class GraphInStoredPathIncorporator {
	private static List<StoredPathVertex> bestMatchingStoredPath;
	private static int maxSize;
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Remove Path
	
	/**
	 * Removes the edge from all stored paths and cuts off paths if necessary. 
	 * 
	 * @param edgeToRemove
	 * @param spv
	 * @param maxSize
	 */
	static void removePathFromHub(IEdge edgeToRemove, StoredPathVertex spv, int maxSize_) {
		maxSize = maxSize_;
		
		Collection<INode> nextSpvs = getNextSpvNodes(spv, edgeToRemove.getN2());
		
		removePathFromHubRec(spv, edgeToRemove, null, spv);
		
		reorganizeHubPaths(spv, (Edge)edgeToRemove, nextSpvs, false, false, 1, new SmallGraph());
	}
	
	/**
	 * Recursive method for {@link #removePathFromHub(IEdge, StoredPathVertex)}.
	 * Returns true if the node has to be removed.
	 * 
	 * @param edgeToRemove
	 * @param actSpv
	 * @return 
	 */
	private static boolean removePathFromHubRec(StoredPathVertex startSpv, IEdge edgeToRemove,
			StoredPathVertex lastSpv, StoredPathVertex actSpv) {
		if (spvHasEdge(edgeToRemove, actSpv)) {
			actSpv.getEdges().remove(edgeToRemove);
			
			if (cutPath(edgeToRemove, lastSpv, actSpv)) {
				return true;
			}
		}
		
		Iterator<StoredPathVertex> nextVertixIterator = actSpv.getNextVertices().iterator();
		while (nextVertixIterator.hasNext()) {
			StoredPathVertex nextSpv = nextVertixIterator.next();
			boolean nodeRemoved = removePathFromHubRec(startSpv, edgeToRemove, actSpv, nextSpv);
			if (nodeRemoved) {
				nextVertixIterator.remove();
			}
		}
		
		return false;
	}

	private static Collection<INode> getNextSpvNodes(StoredPathVertex actSpv, INode remDst) {
		Collection<INode> foundNextSpvNodes = new ArrayList<>();
		
		if (actSpv.getVertex().equals(remDst)) {
			Collection<INode> graphNeighbours = GraphUtils.getNeighboursOfNode(actSpv.getVertex());
			Collection<INode> hubNeighbours = spvCollToNodeColl(actSpv.getNextVertices());
			hubNeighbours.retainAll(graphNeighbours);
			return hubNeighbours;
		}
		
		for (StoredPathVertex nextSpv : actSpv.getNextVertices()) {
			foundNextSpvNodes.addAll(getNextSpvNodes(nextSpv, remDst));
		}
		
		return foundNextSpvNodes;
	}
	
	private static Collection<INode> spvCollToNodeColl(Collection<StoredPathVertex> spvColl) {
		Collection<INode> returnList = new ArrayList<>();
		
		for (StoredPathVertex spv : spvColl) {
			returnList.add(spv.getVertex());
		}
		
		return returnList;
	}
	
	private static void reorganizeHubPaths(StoredPathVertex actSpv,
			Edge remEdge,
			Collection<INode> remNextSpvs,
			boolean remSrcFound, boolean remDstFound, int size,
			SmallGraph actPath) {
		
		actPath.getNodes().add(actSpv.getVertex());
		
		if (actPath.getNodes().size() > maxSize) {
			return;
		}
		
		if (actSpv.getVertex().equals(remEdge.getN2())) {
			remDstFound = true;
		}
		
		if (actSpv.getVertex().equals(remEdge.getN1())) {
			remSrcFound = true;
		}
		
		StoredPathVertex newNextSpv = null;
		if (remDstFound) {
			Collection<IEdge> edges = GraphUtils.getAllConnectingEdges(remEdge.getN1(), actPath);
			newNextSpv = new StoredPathVertex(remEdge.getN1(), edges);
			actSpv.getNextVertices().add(newNextSpv);
		} else if (remSrcFound) {
			if (remNextSpvs.contains(actSpv.getVertex()) ) {
				int desiredSize = maxSize - size;
				newNextSpv = HubTraverser.getSubgraphsForNodeAsSpv(remEdge.getN2(), actPath, desiredSize);
				newNextSpv.getEdges().remove(remEdge);
				actSpv.getNextVertices().add(newNextSpv);
			}
		}
		
		for (StoredPathVertex nextSpv : actSpv.getNextVertices()) {
			if (!nextSpv.equals(newNextSpv)) {
				reorganizeHubPaths(nextSpv, remEdge, remNextSpvs, remSrcFound, remDstFound,
						size + 1, actPath);
			}
		}
		actPath.getNodes().remove(actSpv.getVertex());
	}

	/**
	 * When the {@code edgeToRemove} connected the {@code lastSpv} and the {@code actSpv} the path
	 * gets cut. That means {@code actSpv} is no longer a successor of {@code lastSpv}. In this case
	 * the method returns {@code true}. In any other case {@code false}. 
	 * 
	 * @param edgeToRemove
	 * @param lastSpv
	 * @param actSpv
	 * @return
	 */
	private static boolean cutPath(IEdge edgeToRemove, StoredPathVertex lastSpv,
			StoredPathVertex actSpv) {
		for (IEdge edge : actSpv.getEdges()) {
			if (lastSpv.getVertex().equals(edge.getN1())
					|| lastSpv.getVertex().equals(edge.getN2())) {
				return false;
			}
		}
		return true;
	}

	private static boolean spvHasEdge(IEdge edgeToRemove, StoredPathVertex actSpv) {
		return (actSpv.getVertex().equals(edgeToRemove.getN1())
				|| actSpv.getVertex().equals(edgeToRemove.getN2()))
				&& actSpv.getEdges().contains(edgeToRemove);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Add Path
	
	static void incorporatePathIntoHub(IEdge actEdge, SmallGraph path, StoredPathVertex spv) {
		List<StoredPathVertex> matchingSpvs = getBestMatchingStoredPath(path, spv);
		
		if (matchingSpvs.size() >= path.getSize()) {
			boolean foundOneNode = false;
			for(StoredPathVertex mspv : matchingSpvs) {
				INode actNode = mspv.getVertex();
				
				if(actNode.equals(actEdge.getN1()) ||
						actNode.equals(actEdge.getN2())) {
					if(!foundOneNode) {
						foundOneNode = true;
					} else {
						if(!mspv.getEdges().contains(actEdge)) {
							mspv.getEdges().add(actEdge);
						}
						return;
					}
				}
			}
		} else {
			addRemainingNodeToStoredPath(matchingSpvs, path, Iterables.getLast(matchingSpvs));
		}
	}
	
	private static void addRemainingNodeToStoredPath(List<StoredPathVertex> matchingSpvs,
			SmallGraph path, StoredPathVertex lastSpv) {
		INode remainingNode = findRemainingNode(matchingSpvs, path);
		Collection<IEdge> connectingEdges = getEdgesBetween(matchingSpvs, remainingNode);
		StoredPathVertex newSpv = new StoredPathVertex(remainingNode, connectingEdges);
		lastSpv.getNextVertices().add(newSpv);
	}

	private static INode findRemainingNode(List<StoredPathVertex> matchingSpvs, SmallGraph path) {
		for (INode actNode : path.getNodes()) {
			boolean found = false;
			
			for (StoredPathVertex actSpv : matchingSpvs) {
				if (actSpv.getVertex().equals(actNode)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				return actNode;
			}
		}
		
		return null;
	}

	private static void addRemainingPathToStoredPath(Collection<StoredPathVertex> storedPathList,
			SmallGraph graph, StoredPathVertex lastSpv){
		Collection<INode> neighbours = getNeighboursOfNodeWithout(lastSpv.getVertex(), storedPathList);
		for(INode neighbour : neighbours) {
			if(graph.getNodes().contains(neighbour)) {
				Collection<IEdge> newEdges = getEdgesBetween(storedPathList, neighbour);
				StoredPathVertex newSpv = new StoredPathVertex(neighbour, newEdges);
				
				lastSpv.getNextVertices().add(newSpv);
				storedPathList.add(newSpv);
				
				addRemainingPathToStoredPath(storedPathList, graph, newSpv);
			}
		}
	}
	
	private static Collection<INode> getNeighboursOfNodeWithout(INode node,
			Collection<StoredPathVertex> storedPathList){
		List<INode> returnList = new ArrayList<INode>();
		
		Iterable<IEdge> edges = (Iterable<IEdge>)(Object)node.getEdges();
		for(IEdge edge : edges){
			INode diffNode = edge.getDifferingNode((Node)node);
			
			if(!returnList.contains(diffNode)) {
				boolean forbidden = false;
				for(StoredPathVertex spv : storedPathList) {
					if(spv.getVertex().equals(diffNode)) {
						forbidden = true;
						break;
					}
				}
				
				if(!forbidden) {
					returnList.add(diffNode);
				}
			}
		}
		
		
		return returnList;
	}
	
	private static Collection<IEdge> getEdgesBetween(Collection<StoredPathVertex> storedPathList,
			INode node){
		ArrayList<IEdge> returnList = new ArrayList<IEdge>();
		
		for(StoredPathVertex storedPath : storedPathList) {
			Iterable<IEdge> edges = (Iterable<IEdge>)(Object)node.getEdges();
			for(IEdge edge : edges) {
				if(edge.isConnectedTo((Node)storedPath.getVertex(), (Node)node)) {
					returnList.add(edge);
				}
			}
		}
		
		return returnList;
	}
	
	private static List<StoredPathVertex> getBestMatchingStoredPath(SmallGraph graph,
			StoredPathVertex spv) {
		bestMatchingStoredPath = new ArrayList<StoredPathVertex>();
		getBestMatchingStoredPathRec(graph, spv, new ArrayList<StoredPathVertex>());
		
		return bestMatchingStoredPath;
	}
	
	private static void getBestMatchingStoredPathRec(SmallGraph graph, StoredPathVertex spv,
			List<StoredPathVertex> storedPathList) {
		ArrayList<StoredPathVertex> clonedStoredPathList = new ArrayList<>(storedPathList);
		clonedStoredPathList.add(spv);
		
		if(clonedStoredPathList.size() == graph.getSize() + 1) {
			bestMatchingStoredPath = clonedStoredPathList;
			return;
		}
		
		boolean found = false;
		for(StoredPathVertex nextSpv : spv.getNextVertices()) {
			if(graph.getNodes().contains(nextSpv.getVertex())) {
				found = true;
				getBestMatchingStoredPathRec(graph, nextSpv, clonedStoredPathList);
			}
		}
		
		if(!found) {
			if(clonedStoredPathList.size() > bestMatchingStoredPath.size()) {
				bestMatchingStoredPath = clonedStoredPathList;
			}
		}
	}
}
