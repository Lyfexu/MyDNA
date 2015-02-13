package dna.metrics.motifs.basti;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.Lists;

import dna.graph.edges.DirectedEdge;
import dna.graph.edges.Edge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.algorithms.IAfterEA;
import dna.metrics.algorithms.IAfterNA;
import dna.metrics.algorithms.IBeforeER;
import dna.metrics.algorithms.IBeforeNR;
import dna.metrics.algorithms.IDynamicAlgorithm;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.motifcounter.DirectedMotifCounter;
import dna.metrics.motifs.basti.motiffinder.InitialNonIsoGraphFinder;
import dna.metrics.motifs.basti.motiffinder.MotifTraverser3;
import dna.metrics.motifs.basti.motiffinder.MotifTraverser4;
import dna.metrics.motifs.basti.motiffinder.hub.HubManager;
import dna.metrics.motifs.basti.utils.GraphUtils;
import dna.updates.update.EdgeAddition;
import dna.updates.update.EdgeRemoval;
import dna.updates.update.NodeAddition;
import dna.updates.update.NodeRemoval;
import dna.util.parameters.IntParameter;

public class DynamicDirectedMotifsU extends DynamicDirectedMotifs implements IAfterEA, IBeforeER,
		IBeforeNR, IAfterNA, IDynamicAlgorithm {
	
	private Collection<DynamicDirectedMotifsUListener> listeners = new ArrayList<>();
	private enum ListenerEvent {
		initialized,
		nodeAdded,
		nodeRemoved,
		edgeAdded,
		edgeRemoved
	}
	
	/**
	 * Adds a listener.
	 * 
	 * @param newListener
	 */
	public void addListener(DynamicDirectedMotifsUListener newListener) {
		listeners.add(newListener);
	}
	
	private void callListenerFunction(ListenerEvent event, Edge edge, Node node) {
		for (DynamicDirectedMotifsUListener listener : listeners) {
			switch (event) {
				case initialized : listener.initialized();
					break;
				case nodeAdded : listener.nodeAddedEvent(node);
					break;
				case nodeRemoved : listener.nodeRemovedEvent(node);
					break;
				case edgeAdded : listener.edgeAddedEvent(edge);
					break;
				case edgeRemoved : listener.edgeRemovedEvent(edge);
					break;
			}
		}
	}
	
	public DynamicDirectedMotifsU(int motifSize) {
		super("DynamicDirectedMotifsU", new IntParameter("motifSize", motifSize));
		
		hubManager = new HubManager(getGraph(), motifSize);
		//addListener(hubManager);
		directedMotifCounter = new DirectedMotifCounter();
	}

	@Override
	public boolean init() {
		return initWithHubs(null);
	}
	
	public boolean initWithHubs(Collection<INode> hubCandidates) {
		InitialNonIsoGraphFinder.initializeDirectedMotifCounter_old(getGraph(), directedMotifCounter,
				hubManager, getMotifSize());
		
		hubManager.setGraph(getGraph());
		hubManager.initializeHubs();
		
		callListenerFunction(ListenerEvent.initialized, null, null);
		
		return true;
	}

	@Override
	public boolean applyBeforeUpdate(NodeRemoval nr) {
		Collection<Edge> removedEdges = new ArrayList<>();
		
		ArrayList<Edge> edges = (ArrayList<Edge>)(Object)Lists.newArrayList(nr.getNode().getEdges());
		for (Edge edge : edges) {
			removedEdges.add(edge);
			GraphUtils.removeEdge(getGraph(), edge);
			
			applyBeforeUpdate(new EdgeRemoval(edge));
		}
		
		for(Edge removedEdge : removedEdges) {
			GraphUtils.addEdge(getGraph(), removedEdge);
		}
		
		hubManager.getStoredPathInfos().remove(nr.getNode().getIndex());
		
		callListenerFunction(ListenerEvent.nodeRemoved, null, (Node) nr.getNode());
		
		return true;
	}

	@Override
	public boolean applyBeforeUpdate(EdgeRemoval er) {
		DirectedEdge removedEdge = (DirectedEdge)er.getEdge();
		
		Collection<Path> foundSubgraphs = MotifTraverser3.getSubgraphsForEdge(removedEdge,
				getMotifSize(), hubManager, MotifTraverser3.EdgeAction.removed);
		
		for(Path g: foundSubgraphs) {
			if(g.hasChanged()) {
				directedMotifCounter.incrementCounterFor(g.getPrevGraph());
			}
			directedMotifCounter.decrementCounterFor(g.getGraph());
		}
		
		callListenerFunction(ListenerEvent.edgeRemoved, removedEdge, null);
		
		return true;
	}

	@Override
	public boolean applyAfterUpdate(EdgeAddition ea) {
		DirectedEdge addedEdge = (DirectedEdge)ea.getEdge();
		
		Collection<Path> foundSubgraphs = MotifTraverser3.getSubgraphsForEdge(addedEdge, getMotifSize(),
				hubManager, MotifTraverser3.EdgeAction.added);
		
		for(Path g: foundSubgraphs) {
			if(g.hasChanged()) {
				directedMotifCounter.decrementCounterFor(g.getPrevGraph());
			}
			directedMotifCounter.incrementCounterFor(g.getGraph());
		}
		
		callListenerFunction(ListenerEvent.edgeAdded, addedEdge, null);
		
		return true;
	}

	@Override
	public boolean applyAfterUpdate(NodeAddition na) {
		callListenerFunction(ListenerEvent.nodeAdded, null, (Node) na.getNode());
		return true;
	}
}
