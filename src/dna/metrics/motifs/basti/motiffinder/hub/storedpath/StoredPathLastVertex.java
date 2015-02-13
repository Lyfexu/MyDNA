package dna.metrics.motifs.basti.motiffinder.hub.storedpath;

import java.util.Collection;

import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;

public class StoredPathLastVertex extends StoredPathVertex {
	boolean isHub;
	
	public boolean isHub() {
		return isHub;
	}

	public void setHub(boolean isHub) {
		this.isHub = isHub;
	}

	public StoredPathLastVertex() {
		super();
	}

	public StoredPathLastVertex(INode vertex, Collection<IEdge> edges, boolean isHub) {
		super(vertex, edges);
		this.isHub = isHub;
	}
}
