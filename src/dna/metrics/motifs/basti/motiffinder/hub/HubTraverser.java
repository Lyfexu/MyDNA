package dna.metrics.motifs.basti.motiffinder.hub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motiffinder.Traverser;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathInfo;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathVertex;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverter;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverterOptions;
import dna.metrics.motifs.basti.utils.GraphUtils;

public class HubTraverser extends Traverser{
	
	private static int maxPathSize;
	private static Collection<INode> otherHubs;
	private static Collection<Path> foundSubgraphs;
	private static INode initialHub;
	
	public static Collection<Path> getSubgraphsForHub(INode hub, int size) {
		return getSubgraphsForHub(hub, size, new ArrayList<INode>());
	}
			
	public static Collection<Path> getSubgraphsForHub(INode hub, int size,
			Collection<INode> otherHubs_) {
		if(size <= 0)
			return new ArrayList<Path>();
		
		maxPathSize = size;
		otherHubs = otherHubs_;
		foundSubgraphs = new ArrayList<>();
		initialHub = hub;
		
		traverseForHubRec(hub, new SmallGraph(), new ArrayList<INode>());
		
		return foundSubgraphs;
	}
	
	private static void traverseForHubRec(INode actNode, SmallGraph actPath,
			List<INode> openNeighbours){
		openNeighbours.remove(actNode);
		
		SmallGraph clonedActPath = actPath.shallowClone();
		
		clonedActPath = addNodeWithConnectingEdgesToPath(actNode, clonedActPath);
		
		if(pathAlreadyFound(clonedActPath, foundSubgraphs))
			return;
		
		if(otherHubs.contains(actNode) && actNode != initialHub) {
			addPathToFoundSubgraphs(clonedActPath, true);
			return;
		}
		
		addPathToFoundSubgraphs(clonedActPath, false);
		
		if(clonedActPath.getSize() >= maxPathSize)
			return;
		
		Collection<INode> newNeighbours = getNeighboursOfNodeWithout(actNode, openNeighbours,
				clonedActPath);
		openNeighbours.addAll(newNeighbours);
		
		for(INode openNeighbour : openNeighbours){
			traverseForHubRec(openNeighbour, clonedActPath, new ArrayList<INode>(openNeighbours));
		}
	}

	private static void addPathToFoundSubgraphs(SmallGraph clonedActPath, boolean endsWithHub) {
		Path p = new Path(clonedActPath);
		p.setEndsWithHub(endsWithHub);
		foundSubgraphs.add(p);
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// 
	
	public static StoredPathVertex getSubgraphsForNodeAsSpv(Node node, SmallGraph actPath,
			int maxSize) {
		SmallGraph clonedActPath = actPath.shallowClone();
		return getSubgraphsForNodeAsSpv(node, clonedActPath, 1, maxSize);
	}
	
	private static StoredPathVertex getSubgraphsForNodeAsSpv(Node node, SmallGraph actPath,
			int actSize, int maxSize) {
		Collection<IEdge> connectingEdges = GraphUtils.getAllConnectingEdges(node, actPath);
		StoredPathVertex spv = new StoredPathVertex(node, connectingEdges);
		
		if (actSize >= maxSize) {
			return spv;
		}
		
		actPath.getNodes().add(node);
		
		Collection<INode> neighbours = GraphUtils.getNeighboursOfNode(node);
		for (INode neighbour : neighbours) {
			if (!actPath.getNodes().contains(neighbour)) {
				StoredPathVertex nextSpv = getSubgraphsForNodeAsSpv((Node)neighbour, actPath, actSize + 1,
						maxSize);
				spv.getNextVertices().add(nextSpv);
			}
		}
		
		actPath.getNodes().remove(node);
		
		return spv;
	}
}
