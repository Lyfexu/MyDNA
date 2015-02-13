package dna.metrics.motifs.basti.motiffinder.hub;

import java.util.Collection;

import dna.graph.Graph;
import dna.graph.nodes.INode;

public interface IHubRemoveCandidatePicker {
	Collection<INode> getNextRemoveHubCandidate(int amount, Graph graph, HubManager hubManager);
}
