package dna.metrics.motifs.acc;

import java.util.HashMap;

import org.joda.time.DateTime;

import dna.metrics.algorithms.IAfterEA;
import dna.metrics.algorithms.IAfterNA;
import dna.metrics.algorithms.IBeforeER;
import dna.metrics.algorithms.IBeforeNR;
import dna.metrics.algorithms.IRecomputation;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.utils.GraphTransformer;
import dna.updates.update.EdgeAddition;
import dna.updates.update.EdgeRemoval;
import dna.updates.update.NodeAddition;
import dna.updates.update.NodeRemoval;
import dna.util.parameters.IntParameter;

public class AccMetricR extends AccMetric implements IAfterEA, IBeforeER, IBeforeNR, IAfterNA {
	private static int counter = 0; 

	public AccMetricR(int motifSize) {
		super("AccMetricR", new IntParameter("motifSize", motifSize));
	}

	public boolean recompute() {
		
		DateTime start = DateTime.now();
		SmallGraph sGraph = GraphTransformer.transformToSmallGraph(getGraph());
		HashMap<Long, Integer> motifs = accCaller.getMotifs(sGraph, getMotifSize());
		DateTime end = DateTime.now();
		long diff = end.getMillis() - start.getMillis();
		
		System.out.println(String.format("%s: %s", counter, diff));
		counter++;
		
		return true;
	}

	@Override
	public boolean init() {
		return true;
	}

	@Override
	public boolean applyAfterUpdate(NodeAddition na) {
		return true;
	}

	@Override
	public boolean applyBeforeUpdate(NodeRemoval nr) {
		recompute();
		return true;
	}

	@Override
	public boolean applyBeforeUpdate(EdgeRemoval er) {
		recompute();
		return true;
	}

	@Override
	public boolean applyAfterUpdate(EdgeAddition ea) {
		recompute();
		return true;
	}

}
