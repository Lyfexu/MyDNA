package dna.metrics.motifs.basti.motiffinder.hub;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.mockito.cglib.core.CollectionUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import dna.graph.Graph;
import dna.graph.nodes.INode;

public class HubAddCandidatePickerImpl2 implements IHubAddCandidatePicker{

	private final int minNeighbours = 3;
	
	@Override
	public Collection<INode> getNextHubAddCandidate(int amount, Graph graph, HubManager hubManager) {
		Iterable<INode> nodes = (Iterable<INode>)(Object)graph.getNodes();
		Iterables.filter(nodes, new Predicate<INode>() {
			@Override
			public boolean apply(INode input) {
				return input.getDegree() >= minNeighbours;
			}
		});
		
		List<INode> nodeList = Lists.newArrayList(nodes);
		Collections.sort(nodeList, new Comparator<INode>()
			{
				public int compare(INode n1, INode n2){
					return n2.getDegree() - n1.getDegree();
				}
			});
		
		return nodeList.subList(0, amount);
	}

}
