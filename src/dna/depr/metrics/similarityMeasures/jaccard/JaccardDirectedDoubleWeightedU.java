package dna.depr.metrics.similarityMeasures.jaccard;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dna.graph.IElement;
import dna.graph.edges.DirectedEdge;
import dna.graph.edges.DirectedWeightedEdge;
import dna.graph.edges.Edge;
import dna.graph.nodes.DirectedNode;
import dna.graph.nodes.Node;
import dna.graph.weights.DoubleWeight;
import dna.updates.batch.Batch;
import dna.updates.update.EdgeAddition;
import dna.updates.update.EdgeRemoval;
import dna.updates.update.EdgeWeight;
import dna.updates.update.NodeAddition;
import dna.updates.update.NodeRemoval;
import dna.updates.update.Update;
import dna.util.parameters.Parameter;

/**
 * The class implements the changes of {@link DirectedNode}s and weighted
 * {@link DirectedEdge}s by updating the jaccard similarity measure.
 * 
 * @see JaccardUndirectedDoubleWeighted
 */
public class JaccardDirectedDoubleWeightedU extends
		JaccardDirectedDoubleWeighted {

	/**
	 * Initializes {@link JaccardDirectedDoubleWeightedU}. Implicitly sets
	 * degree type for directed graphs to outdegree.
	 */
	public JaccardDirectedDoubleWeightedU() {
		super("JaccardDirectedDoubleWeightedU",
				ApplicationType.BeforeAndAfterUpdate);
	}

	/**
	 * Initializes {@link JaccardDirectedDoubleWeightedU}.
	 * 
	 * @param directedDegreeType
	 *            <i>in</i> or <i>out</i>, determining whether to use in- or
	 *            outdegree for directed graphs.
	 */
	public JaccardDirectedDoubleWeightedU(Parameter directedDegreeType) {
		super("JaccardDirectedDoubleWeightedU",
				ApplicationType.BeforeAndAfterUpdate, directedDegreeType);
	}

	/**
	 * Add the Src node of the new {@link DirectedEdge} to the neighbors
	 * {@link Map} entry of the Dst node.
	 */
	private void addNeighborNodesDst(DirectedWeightedEdge newEdge) {
		if (this.neighborNodes.containsKey(newEdge.getDst())) {
			this.neighborNodes.get(newEdge.getDst()).put(newEdge.getSrc(),
					((DoubleWeight) newEdge.getWeight()).getWeight());
		} else {
			HashMap<DirectedNode, Double> map = new HashMap<DirectedNode, Double>();
			map.put(newEdge.getSrc(),
					((DoubleWeight) newEdge.getWeight()).getWeight());
			this.neighborNodes.put(newEdge.getDst(), map);
		}
	}

	/**
	 * Add the Dst node of the new {@link DirectedEdge} to the neighbors
	 * {@link Map} entry of the Src node.
	 */
	private void addNeighborNodesSrc(DirectedWeightedEdge newEdge) {
		if (this.neighborNodes.containsKey(newEdge.getSrc())) {
			this.neighborNodes.get(newEdge.getSrc()).put(newEdge.getDst(),
					((DoubleWeight) newEdge.getWeight()).getWeight());
		} else {
			HashMap<DirectedNode, Double> map = new HashMap<DirectedNode, Double>();
			map.put(newEdge.getDst(),
					((DoubleWeight) newEdge.getWeight()).getWeight());
			this.neighborNodes.put(newEdge.getSrc(), map);
		}
	}

	@Override
	public boolean applyAfterBatch(Batch b) {
		return false;
	}

	/**
	 * Called after the update is applied to the graph.
	 * 
	 * @param addedEdgeUpdate
	 *            The update from the {@link Edge} which has been added.
	 * @return true, if successful;
	 */
	private boolean applyAfterEdgeAddition(
			DirectedWeightedEdge directedDoubleWeightedEdge) {
		final DirectedWeightedEdge newEdge = directedDoubleWeightedEdge;

		HashMap<DirectedNode, Double> neighborsIn = this.getNeighborsIn(newEdge
				.getDst());
		HashMap<DirectedNode, Double> neighborsOut = this
				.getNeighborsOut(newEdge.getSrc());

		this.getNeighborsOut(newEdge.getSrc());
		if (isOutgoingMeasure()) {
			addNeighborNodesSrc(newEdge);
			this.increaseMatching(neighborsIn, newEdge.getSrc());
		} else {
			addNeighborNodesDst(newEdge);
			this.increaseMatching(neighborsOut, newEdge.getDst());
		}

		this.update(newEdge, neighborsIn, neighborsOut);

		return true;
	}

	@Override
	public boolean applyAfterUpdate(Update u) {
		if (u instanceof NodeAddition)
			for (int i = 0; i < this.g.getNodeCount(); i++)
				this.binnedDistribution.incr(0.0);

		else if (u instanceof NodeRemoval)
			// New jaccard similarity measure for NodeRemovals are calculated
			// before the update.
			;
		else if (u instanceof EdgeAddition)
			return applyAfterEdgeAddition(((DirectedWeightedEdge) ((EdgeAddition) u)
					.getEdge()));
		else if (u instanceof EdgeRemoval)
			// New jaccard similarity measure for EdgeRemovals are calculated
			// before the update.
			;
		else if (u instanceof EdgeWeight)
			// New jaccard similarity measure for EdgeWeight updates are
			// calculated
			// before the update.
			;

		return false;
	}

	@Override
	public boolean applyBeforeBatch(Batch b) {
		return false;
	}

	/**
	 * Called before the update is applied to the graph.
	 * 
	 * @param EdgeRemoval
	 *            The update from the {@link Edge} which is to be removed.
	 * @return true, if successful; 
	 */
	private boolean applyBeforeEdgeRemoval(
			DirectedWeightedEdge directedDoubleWeightedEdge) {
		final DirectedWeightedEdge edgeToRemove = directedDoubleWeightedEdge;

		HashMap<DirectedNode, Double> neighborsIn = this
				.getNeighborsIn(edgeToRemove.getDst());
		HashMap<DirectedNode, Double> neighborsOut = this
				.getNeighborsOut(edgeToRemove.getSrc());
		if (isOutgoingMeasure()) {
			this.decreaseMatching(neighborsIn, edgeToRemove.getSrc());
			this.neighborNodes.get(edgeToRemove.getSrc()).remove(
					edgeToRemove.getDst());

		} else {
			this.decreaseMatching(neighborsOut, edgeToRemove.getDst());
			this.neighborNodes.get(edgeToRemove.getDst()).remove(
					edgeToRemove.getSrc());

		}

		this.update(edgeToRemove, neighborsIn, neighborsOut);

		return true;
	}

	/**
	 * Called before the edge weight update is applied to the graph.
	 * 
	 * @param directedDoubleWeightedEdge
	 *            The {@link Edge} whose edge weight changes.
	 * @param weight
	 *            The new weight of the Edge after the Update.
	 * @return true, if successful;
	 */
	private boolean applyBeforeEdgeWeightUpdate(
			DirectedWeightedEdge directedDoubleWeightedEdge, double weight) {
		applyEdgeWeightedUpdate(directedDoubleWeightedEdge, weight);
		return false;
	}

	/**
	 * Called before the update is applied to the graph.
	 * 
	 * @param NodeRemoval
	 *            The update from the {@link Node} which is to be removed.
	 * @return true, if successful;
	 */
	private boolean applyBeforeNodeRemoval(NodeRemoval u) {
		final DirectedNode nodeToRemove = (DirectedNode) u.getNode();
		if (isOutgoingMeasure())
			this.decreaseMatchingNodeRemove(this.getNeighborsIn(nodeToRemove));
		else
			this.decreaseMatchingNodeRemove(this.getNeighborsOut(nodeToRemove));

		this.removeFromNeighborNodes(nodeToRemove);

		for (IElement iterable_element : this.g.getNodes()) {
			Node node = (Node) iterable_element;
			if (this.result.get(nodeToRemove, node) == null)
				this.binnedDistribution.decr(0);
			else
				this.binnedDistribution.decr(this.result
						.get(nodeToRemove, node));
		}

		if (isOutgoingMeasure()) {
			this.updateDirectedNeighborsMeasure(this
					.getNeighborsIn(nodeToRemove));
			this.updateNodeRemoveMeasuresOutgoing(nodeToRemove);
		} else {
			this.updateDirectedNeighborsMeasure(this
					.getNeighborsOut(nodeToRemove));
			this.updateNodeRemoveMeasuresIncoming(nodeToRemove);
		}

		// remove the results of the removed node calculated so far
		this.neighborNodes.remove(nodeToRemove);
		this.matching.removeRow(nodeToRemove);
		this.matching.removeColumn(nodeToRemove);
		this.result.removeRow(nodeToRemove);
		this.result.removeColumn(nodeToRemove);

		return true;
	}

	@Override
	public boolean applyBeforeUpdate(Update u) {
		if (u instanceof NodeAddition)
			// New matchings for NodeAdditions are calculated after the update.
			;
		else if (u instanceof NodeRemoval)
			return applyBeforeNodeRemoval((NodeRemoval) u);
		else if (u instanceof EdgeAddition)
			// New matchings for EdgeAdditions are calculated after the update.
			;
		else if (u instanceof EdgeRemoval)
			return applyBeforeEdgeRemoval(((DirectedWeightedEdge) ((EdgeRemoval) u)
					.getEdge()));
		else if (u instanceof EdgeWeight) {
			DirectedWeightedEdge edgeD = ((DirectedWeightedEdge) ((EdgeWeight) u)
					.getEdge());
			return applyBeforeEdgeWeightUpdate(edgeD,
					((DoubleWeight) ((EdgeWeight) u).getWeight()).getWeight());
		}
		return false;
	}

	/**
	 * Applied the edge weight update to the graph.
	 * 
	 * @param directedDoubleWeightedEdge
	 *            The {@link Edge} whose edge weight changes.
	 * @param weight
	 *            The new weight of the Edge after the Update.
	 * @return true, if successful;
	 */
	private void applyEdgeWeightedUpdate(
			DirectedWeightedEdge directedDoubleWeightedEdge, double weight) {
		final DirectedWeightedEdge edgeToBeUpdated = directedDoubleWeightedEdge;
		if (isOutgoingMeasure()) {
			this.decreaseMatching(
					this.getNeighborsIn(edgeToBeUpdated.getDst()),
					edgeToBeUpdated.getSrc());
			this.neighborNodes.get(edgeToBeUpdated.getSrc()).remove(
					edgeToBeUpdated.getDst());

		} else {
			this.decreaseMatching(
					this.getNeighborsOut(edgeToBeUpdated.getSrc()),
					edgeToBeUpdated.getDst());
			this.neighborNodes.get(edgeToBeUpdated.getDst()).remove(
					edgeToBeUpdated.getSrc());
		}

		edgeToBeUpdated.setWeight(new DoubleWeight(weight));

		HashMap<DirectedNode, Double> neighborsIn = this
				.getNeighborsIn(edgeToBeUpdated.getDst());
		HashMap<DirectedNode, Double> neighborsOut = this
				.getNeighborsOut(edgeToBeUpdated.getSrc());

		if (isOutgoingMeasure()) {
			this.increaseMatching(
					this.getNeighborsIn(edgeToBeUpdated.getDst()),
					edgeToBeUpdated.getSrc());
			this.neighborNodes.get(edgeToBeUpdated.getSrc()).put(
					edgeToBeUpdated.getDst(),
					((DoubleWeight) edgeToBeUpdated.getWeight()).getWeight());

		} else {
			this.increaseMatching(
					this.getNeighborsOut(edgeToBeUpdated.getSrc()),
					edgeToBeUpdated.getDst());
			this.neighborNodes.get(edgeToBeUpdated.getDst()).put(
					edgeToBeUpdated.getSrc(),
					((DoubleWeight) edgeToBeUpdated.getWeight()).getWeight());

		}
		update(edgeToBeUpdated, neighborsIn, neighborsOut);

	}

	/**
	 * Removes a {@link Node} from the neighborNodes {@link Map}.
	 * 
	 * @param nodeToRemove
	 *            {@link Node} to remove from the {@link Map}
	 */
	private void removeFromNeighborNodes(DirectedNode nodeToRemove) {
		this.neighborNodes.remove(nodeToRemove);
		for (Entry<Node, HashMap<DirectedNode, Double>> iterable_element : this.neighborNodes
				.entrySet())
			if (iterable_element.getValue().containsKey(nodeToRemove))
				this.neighborNodes.get(iterable_element.getKey()).remove(
						nodeToRemove);

	}

	/**
	 * Updates the jaccard similarity measure between the given nodes.
	 */
	@Override
	protected void update(DirectedNode node1, DirectedNode node2) {
		HashMap<DirectedNode, Double> denominator = this.getUnion(
				this.neighborNodes.get(node1), this.neighborNodes.get(node2));
		double newJaccard;
		if (this.matching.get(node1, node2) == null
				|| this.matching.get(node1, node2) == 0
				|| denominator.size() == 0)
			newJaccard = 0;
		else
			newJaccard = (double) this.matching.get(node1, node2)
					/ (double) getMapValueSum(denominator);

		if (newJaccard < 0.0 && Math.abs(newJaccard) <= 1.0E-4) {
			newJaccard = 0.0;
		}
		Double jaccardG = this.result.get(node1, node2);
		if (jaccardG == null)
			this.binnedDistribution.decr(0.0);
		else
			this.binnedDistribution.decr(jaccardG);
		this.result.put(node1, node2, newJaccard);
		this.binnedDistribution.incr(newJaccard);
	}
}
