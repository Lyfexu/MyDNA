package dna.metrics.motifs.basti;

import dna.graph.edges.Edge;
import dna.graph.nodes.Node;

public interface DynamicDirectedMotifsUListener {
	public void edgeAddedEvent(Edge e);
	public void edgeRemovedEvent(Edge e);
	public void nodeAddedEvent(Node n);
	public void nodeRemovedEvent(Node n);
	public void initialized();
}
