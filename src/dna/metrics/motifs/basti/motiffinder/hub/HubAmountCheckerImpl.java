package dna.metrics.motifs.basti.motiffinder.hub;

import dna.graph.Graph;

public class HubAmountCheckerImpl implements IHubAmountChecker {

	private long minFreeMemory = 10 * 1024 * 1024;
	private long sizePerNewHub = 100;
	
	@Override
	public int checkOptimalHubAmount(Graph graph, HubManager hubManager) {
		long availableMemory = Runtime.getRuntime().freeMemory() - minFreeMemory;
		int numOfHubsToAddRemove = (int) Math.floor(availableMemory / sizePerNewHub);
		
		int maxHubsToAdd = (graph.getNodeCount()) - hubManager.getStoredPathInfos().size();
		if (numOfHubsToAddRemove > maxHubsToAdd) {
			return maxHubsToAdd;
		}
		
		int maxHubsToRemove = hubManager.getStoredPathInfos().size() * -1;
		if (numOfHubsToAddRemove < maxHubsToRemove) {
			return maxHubsToRemove;
		}
		
		return numOfHubsToAddRemove;
	}

}
