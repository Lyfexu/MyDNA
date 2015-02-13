package dna.metrics.motifs.basti.motiffinder.hub;

import java.util.Collection;

import dna.graph.Graph;
import dna.graph.nodes.INode;

public interface IHubAddCandidatePicker {

	Collection<INode> getNextHubAddCandidate(int amount, Graph graph, HubManager hubManager);
}