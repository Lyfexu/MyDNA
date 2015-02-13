package dna.metrics.motifs.basti.motiffinder.hub;

import dna.graph.Graph;

/**
 * @author Bastian Laur
 *
 */
public interface IHubAmountChecker {
	int checkOptimalHubAmount(Graph graph, HubManager hubManager);
}
