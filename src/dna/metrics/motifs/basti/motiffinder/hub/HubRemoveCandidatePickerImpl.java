package dna.metrics.motifs.basti.motiffinder.hub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dna.graph.Graph;
import dna.graph.nodes.INode;

public class HubRemoveCandidatePickerImpl implements IHubRemoveCandidatePicker {

	@Override
	public Collection<INode> getNextRemoveHubCandidate(int amount, Graph graph, HubManager hubManager) {
		List<INode> nodes = new ArrayList<>();
		for (Integer nodeId : hubManager.getStoredPathInfos().keySet()) {
			nodes.add(graph.getNode(nodeId));
		}
		
		Collections.sort(nodes, new Comparator<INode>() {
			@Override
			public int compare(INode o1, INode o2) {
				return o1.getDegree() - o2.getDegree();
			}
		});
		
		return nodes.subList(0, amount);
	}

}
