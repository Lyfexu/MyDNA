package dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Iterables;

import dna.graph.IElement;
import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathLastVertex;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathVertex;

class GraphToStoredPathConverter {
	
	static StoredPathVertex toStoredPath(Collection<Path> paths){
		StoredPathVertex spv = null;
		
		for(Path path : paths){
			spv = incorporate(path.getGraph(), path.endsWithHub(), spv);
		}
		
		return spv;
	}
	
	private static StoredPathVertex incorporate(SmallGraph path, boolean lastNodeIsHub,
			StoredPathVertex spv){
		if(path == null || path.getNodes().size() == 0)
			return spv;
		
		StoredPathVertex firstSpv = spv;
		StoredPathVertex lastSpv = null;
		List<INode> seenNodes = new ArrayList<INode>();
		Collection<INode> pathNodes = path.getNodes();
		
		for(INode n : pathNodes){
			if(lastSpv == null){
				if(spv == null){
					lastSpv = new StoredPathVertex(n, new ArrayList<IEdge>());
					firstSpv = lastSpv;
				}
				else if(!n.equals(spv.getVertex())){
					return spv;
				}
				else{
					lastSpv = spv;
				}
			} else{
				spv = lastSpv.getNextVertexById(n.getIndex());
				
				if(spv == null){
					Collection<IEdge> newEdges = getEdgesBetwNodes(n, seenNodes);
					
					if(Iterables.getLast(pathNodes).equals(n)){
						spv = new StoredPathLastVertex(n, newEdges, lastNodeIsHub);
					} else {
						spv = new StoredPathVertex(n, newEdges);
					}
					
					lastSpv.getNextVertices().add(spv);
				}
				lastSpv = spv;
			}
			seenNodes.add(n);
		}
		
		return firstSpv;
	}
	
	private static Collection<IEdge> getEdgesBetwNodes(INode node, Collection<INode> seenNodes){
		Collection<IEdge> returnList = new ArrayList<>();
		
		for(IElement elem : node.getEdges()){
			if(!(elem instanceof IEdge))
				continue;
			IEdge edge = (IEdge)elem;
			
			INode diffNode = edge.getDifferingNode((Node)node);
			if(seenNodes.contains(diffNode))
				returnList.add(edge);
		}
		
		return returnList;
	}
}
