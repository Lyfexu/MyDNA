package dna.metrics.motifs.basti.motiffinder.hub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import dna.graph.Graph;
import dna.graph.edges.Edge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.DynamicDirectedMotifsUListener;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathInfo;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathVertex;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.RedundantHubExplorer;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter.StoredPathConverter;

public class HubManager implements DynamicDirectedMotifsUListener {
	private Graph graph;
	private int maxHubs = 3;
	private int subgraphSize;
	private IHubAmountChecker amountChecker = new HubAmountCheckerImpl();
	private IHubAddCandidatePicker hubAdder = new HubAddCandidatePickerImpl2();
	private IHubRemoveCandidatePicker hubRemover = new HubRemoveCandidatePickerImpl();
	
	/** Node-Id : StoredPathInfo */
	private HashMap<Integer, StoredPathInfo> storedPathInfos;
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Getter - Setter
	
	public HashMap<Integer, StoredPathInfo> getStoredPathInfos() {
		return storedPathInfos;
	}
	
	public StoredPathInfo getStoredPathInfoForNode(INode node){
		return storedPathInfos.get(node.getIndex());
	}
	
	public IHubAmountChecker getAmountChecker() {
		return amountChecker;
	}

	public void setAmountChecker(IHubAmountChecker amountChecker) {
		this.amountChecker = amountChecker;
	}

	public IHubAddCandidatePicker getHubAdder() {
		return hubAdder;
	}

	public void setHubAdder(IHubAddCandidatePicker hubAdder) {
		this.hubAdder = hubAdder;
	}

	public IHubRemoveCandidatePicker getHubRemover() {
		return hubRemover;
	}

	public void setHubRemover(IHubRemoveCandidatePicker hubRemover) {
		this.hubRemover = hubRemover;
	}

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Constructor

	public HubManager(Graph graph, int subgraphSize){
		this(graph , subgraphSize, -1);
	}
	
	public HubManager(Graph graph, int subgraphSize, int maxHubs_){
		this.graph = graph;
		this.subgraphSize = subgraphSize;
		storedPathInfos = new HashMap<>();
		
		if(maxHubs_ != -1) {
			maxHubs = maxHubs_;
		}
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Init
	
	public void initializeHubs(){
		Collection<INode> storedPathInfoCandidates = getHubCandidates();
		
		initializeHubs(storedPathInfoCandidates);
	}

	public void initializeHubs(Collection<INode> hubCandidates) {
		Collection<INode> dummyHubs = new ArrayList<>();
		
		for(INode storedPathInfoCandidate : hubCandidates){
			Collection<Path> foundPaths = HubTraverser.getSubgraphsForHub(
					storedPathInfoCandidate, subgraphSize, dummyHubs);
			
			
//			StoredPathVertex storedPathRoot = StoredPathConverter.toStoredPath(foundPaths);
//			
			StoredPathVertex storedPathRoot = new StoredPathVertex();
			storedPathRoot.setVertex(storedPathInfoCandidate);
			RedundantHubExplorer.addPaths(foundPaths, storedPathRoot);
			
			storedPathInfos.put( storedPathInfoCandidate.getIndex(),
					new StoredPathInfo(storedPathInfoCandidate, storedPathRoot));
		}
	}
	
	private List<INode> getHubCandidates() {
		Iterable<INode> nodes = (Iterable<INode>)(Object)graph.getNodes();
		List<INode> nodeList = Lists.newArrayList(nodes);
		
		Collections.sort(nodeList, new Comparator<INode>()
			{
				public int compare(INode n1, INode n2){
					return n2.getDegree() - n1.getDegree();
				}
			});
		
		if(maxHubs < nodeList.size()) {
			return nodeList.subList(0, maxHubs);
		}
		return nodeList;
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////
	// Update Events

	@Override
	public void edgeAddedEvent(Edge e) {
		manageHubAmount();
	}

	@Override
	public void edgeRemovedEvent(Edge e) {
		manageHubAmount();
	}

	@Override
	public void nodeAddedEvent(Node n) {
		manageHubAmount();
	}

	@Override
	public void nodeRemovedEvent(Node n) {
		storedPathInfos.remove(n.getIndex());
		
		manageHubAmount();
	}

	@Override
	public void initialized() {
		//manageHubAmount();
	}
	
	/**
	 * Tests if new hubs can be added or old hubs should be removed and adds / removes them.
	 */
	private void manageHubAmount() {
		int newHubAmount = amountChecker.checkOptimalHubAmount(graph, this);
		newHubAmount = 0;
		
		if (newHubAmount > 0) {
			addHubs(newHubAmount);
		} else if (newHubAmount < 0) {
			removeHubs(newHubAmount);
		}
		
	}
	
	private void removeHubs(int amount) {
		Collection<INode> hubCandidates = hubRemover.getNextRemoveHubCandidate(amount, graph, this);
		
		for(INode hubCandidate : hubCandidates){
			storedPathInfos.remove(hubCandidate.getIndex());
		}
	}

	private void addHubs(int amount) {
		Collection<INode> hubCandidates = hubAdder.getNextHubAddCandidate(amount, graph, this);
		
		Collection<INode> emptyDummyHubs = new ArrayList<>();
		for(INode hubCandidate : hubCandidates){
			Collection<Path> foundPaths = HubTraverser.getSubgraphsForHub(hubCandidate,
					subgraphSize, emptyDummyHubs);
			
			StoredPathVertex storedPathRoot = new StoredPathVertex();
			storedPathRoot.setVertex(hubCandidate);
			RedundantHubExplorer.addPaths(foundPaths, storedPathRoot);
			
			storedPathInfos.put( hubCandidate.getIndex(),
					new StoredPathInfo(hubCandidate, storedPathRoot));
		}
	}
}
