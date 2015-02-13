package dna.metrics.motifs.acc;

import dna.graph.Graph;
import dna.metrics.IMetric;
import dna.metrics.Metric;
import dna.series.data.Distribution;
import dna.series.data.NodeNodeValueList;
import dna.series.data.NodeValueList;
import dna.series.data.Value;
import dna.updates.batch.Batch;
import dna.util.parameters.IntParameter;
import dna.util.parameters.Parameter;

public class AccMetric extends Metric {
	
	protected AccCaller accCaller = new AccCaller();

	public AccMetric(String name, Parameter... p) {
		super(name, p);
	}

	@Override
	public Value[] getValues() {
		return new Value[0];
	}

	@Override
	public Distribution[] getDistributions() {
		return new Distribution[0];
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[0];
	}

	@Override
	public NodeNodeValueList[] getNodeNodeValueLists() {
		return new NodeNodeValueList[0];
	}

	@Override
	public boolean isComparableTo(IMetric m) {
		return false;
	}

	@Override
	public boolean equals(IMetric m) {
		return false;
	}

	@Override
	public boolean isApplicable(Graph g) {
		return true;
	}

	@Override
	public boolean isApplicable(Batch b) {
		return true;
	}
	
	protected int getMotifSize() {
		return ((IntParameter)getParameters()[0]).getIntValue();
	}
}
