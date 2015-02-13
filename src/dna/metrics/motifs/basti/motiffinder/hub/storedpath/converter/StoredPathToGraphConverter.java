package dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.Iterables;

import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathLastVertex;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathVertex;

class StoredPathToGraphConverter {
	private static StoredPathConverterOptions actOptions;
	private static List<Path> convertedGraphs;
	private static List<INode> foundHubs;
	private static Collection<INode> deliveredPath;
	private static StoredPathVertex startSpv;
	private static HashSet<INode> connectedToOtherNode;
	
	/**
	 * Provides a collection of all neighbours for a node.
	 */
	private static HashMap<INode, Collection<IEdge>> neighbours = new HashMap<>();
	
	public static List<Path> toGraphs(StoredPathVertex spv){
		return toGraphs(spv, new StoredPathConverterOptions());
	}
	
	public static List<Path> toGraphs(StoredPathVertex spv, StoredPathConverterOptions options){
		actOptions = options;
		neighbours = new HashMap<>();
		convertedGraphs = new ArrayList<>();
		foundHubs = new ArrayList<>();
		deliveredPath = options.getActPath();
		startSpv = spv;
		
		if(options.getAddConnectingEdges()) {
			createNeighbourHashMapForActPath();
		}
		
		toGraphsRec(spv, new Path(new SmallGraph()));
		
		return convertedGraphs;
	}
	
	private static void toGraphsRec(StoredPathVertex spv, Path actPath){
		if(spv.getVertex().equals(actOptions.getOtherNode())){
			return;
		}
		
		if(actOptions.getActPath().contains(spv.getVertex()) && !spv.equals(startSpv))
			return;
		
		if(actOptions.getUseHubRecursion() && spv instanceof StoredPathLastVertex){
			StoredPathLastVertex splv = (StoredPathLastVertex)spv;
			if(splv.isHub()){
				Collection<Path> newConvertedGraphs = getGraphsFromNextHub(spv, actPath.getGraph()); 
				convertedGraphs.addAll(newConvertedGraphs);
				
				return;
			}
		}
		
		Path clonedPath = actPath.shallowClone();
		
		if(!actOptions.getExcludeStartHub() || spv != startSpv) {
			clonedPath.getGraph().getNodes().add(spv.getVertex());
			clonedPath.getGraph().getEdges().addAll(spv.getEdges());
			
			if(actOptions.getAddConnectingEdges()){
				clonedPath.getGraph().getEdges().addAll(getEdgesFor(spv));
			}
		}
		
		if(!clonedPath.hasChanged() &&
				!(actOptions.getDontSetChangedWhenHubIsStartnode() &&
					spv.getVertex() == actOptions.getStartNode())) {
			clonedPath.setChanged(connectedToOtherNode.contains(spv.getVertex()));
		}
		
		if(actOptions.isOnlyExactSize()) {
			if(clonedPath.getGraph().getNodes().size() == actOptions.getMaxSize()) {
				convertedGraphs.add(clonedPath);
			}
		} else {
			convertedGraphs.add(clonedPath);
		}
		
		if(clonedPath.getGraph().getNodes().size() >= actOptions.getMaxSize())
			return;
		
		for(StoredPathVertex nextSpv : spv.getNextVertices()){
			toGraphsRec(nextSpv, clonedPath);
		}
	}
	
	private static void createNeighbourHashMapForActPath(){
		connectedToOtherNode = new HashSet<>();
		
		for(INode node : actOptions.getActPath()){
			
			if(actOptions.getExcludeOtherNode() && node.equals(startSpv.getVertex())) {
				continue;
			}
			
			Iterable<IEdge> nodeEdges = (Iterable<IEdge>)(Object)node.getEdges(); 
			for(IEdge edge : nodeEdges){
				INode diffNode = edge.getDifferingNode((Node)node);
				
				Collection<IEdge> tmpNeighbours = neighbours.get(diffNode);
				if(tmpNeighbours == null){
					tmpNeighbours = new ArrayList<>();
					tmpNeighbours.add(edge);
					neighbours.put(diffNode, tmpNeighbours);
				} else {
					tmpNeighbours.add(edge);
				}
				
				if(node.equals(actOptions.getOtherNode())) {
					connectedToOtherNode.add(diffNode);
				}
			}
		}
	}
	
	private static Collection<IEdge> getEdgesFor(StoredPathVertex spv){
		Collection<IEdge> edges = neighbours.get(spv.getVertex());
		if(edges == null)
			edges = new ArrayList<>();
		
		return edges;
	}
	
	private static Collection<Path> getGraphsFromNextHub(StoredPathVertex spv, SmallGraph actGraph){
		Collection<INode> composedPath = new ArrayList<>(actGraph.getNodes());
		composedPath.addAll(deliveredPath);
		actOptions.setActPath(composedPath);
		
		return toGraphs(spv, actOptions);
	}
}
