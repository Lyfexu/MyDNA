package dna.metrics.motifs.basti.motiffinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.utils.GraphUtils;
import dna.metrics.motifs.basti.utils.SmallGraphUtils;

public abstract class Traverser {
	
	protected static SmallGraph addNodeWithConnectingEdgesToPath(INode node, SmallGraph path){
		path = addNodeToPath(node, path);
		path = addConnectingEdgesToPath(node, path);
			
		return path;
	}
	
	protected static SmallGraph addNodeToPath(INode node, SmallGraph path){
		path.getNodes().add(node);
		return path;
	}
	
	protected static SmallGraph addConnectingEdgesToPath(INode node, SmallGraph path){
		Collection<IEdge> edges = SmallGraphUtils.getAllConnectingEdges(node, path);
		path.getEdges().addAll(edges);
		
		return path;
	}
	
	protected static boolean pathAlreadyFound(SmallGraph actPath, Collection<Path> foundPaths){
		
		for(Path foundPath : foundPaths) {
			if(foundPath.getGraph().equals(actPath)) {
				return true;
			}
		}
		
		return false;
	}
	
	protected static Collection<INode> getNeighboursOfNodeWithout(INode node, List<INode> openNeighbours, SmallGraph actPath){
		Collection<INode> neighbourNodes = GraphUtils.getNeighboursOfNode(node);
		neighbourNodes.removeAll(openNeighbours);
		neighbourNodes.removeAll(actPath.getNodes());
		
		return neighbourNodes;
	}
}
