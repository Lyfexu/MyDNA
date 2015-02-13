package dna.metrics.motifs.basti;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Iterables;

import dna.graph.Graph;
import dna.graph.nodes.DirectedNode;
import dna.metrics.IMetric;
import dna.metrics.Metric;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motifcounter.DirectedMotifCounter;
import dna.metrics.motifs.basti.motifcounter.DirectedMotifType;
import dna.metrics.motifs.basti.motiffinder.hub.HubManager;
import dna.metrics.motifs.basti.utils.CanonicalLabelGenerator;
import dna.metrics.motifs.basti.utils.GraphTransformer;
import dna.series.data.Distribution;
import dna.series.data.NodeNodeValueList;
import dna.series.data.NodeValueList;
import dna.series.data.Value;
import dna.updates.batch.Batch;
import dna.util.parameters.IntParameter;
import dna.util.parameters.Parameter;

public abstract class DynamicDirectedMotifs extends Metric {
	
	protected HubManager hubManager;
	protected DirectedMotifCounter directedMotifCounter;
	private int counter = 0;
	
	public HubManager getHubManager() {
		return hubManager;
	}
	
	public DynamicDirectedMotifs(String name, Parameter... p) {
		super(name, p);
	}

	@Override
	public Value[] getValues() {
		//return new Value[0];
		
		List<Value> returnList = new ArrayList<>();
		
		try {
			StringBuilder motifIndexOutput = new StringBuilder();
			StringBuilder motifCounterOutput = new StringBuilder();
			
			if(getGraph() == null) {
				return new Value[0];
			}
			
			String graphString = "Graph: " + Iterables.toString(getGraph().nodes) + Iterables.toString(getGraph().edges) + "\n";
			motifIndexOutput.append(graphString);
			motifCounterOutput.append(graphString);
			
			for(DirectedMotifType motif : directedMotifCounter.getOrderedMotifs()) {
				SmallGraph sg = GraphTransformer.transformToSmallGraph(motif.getGraph());
				long canLabel = new CanonicalLabelGenerator().genCanonicalLabelFor(sg);
				Integer motifCount = directedMotifCounter.getMotifCounter().get(motif);
				
				motifIndexOutput.append(canLabel).append("\t").append(motif.toString())
					.append(Arrays.toString(motif.getDegreeHash()))
					.append("\n");
				motifCounterOutput
					.append(canLabel).append("\t")
					.append(motif.getGraph()).append("\t")
					.append(motifCount)
					.append("\n");
				
				returnList.add(new Value(Long.toString(canLabel), motifCount));
			}
			
			PrintWriter writer = new PrintWriter(getName() + "_motif_indexes.txt");
			writer.append(motifIndexOutput.toString());
			writer.close();
			
			writer = new PrintWriter(getName() + "_motif_counts.txt");
			writer.append(motifCounterOutput.toString());
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return returnList.toArray(new Value[0]);
	}

	@Override
	public Distribution[] getDistributions() {
		//return new Distribution[0];
		
		int arraySize = 100;
		double[] values = new double[arraySize];
		values[counter] = 1;
		
		counter++;
		counter = counter % arraySize;
		
		Distribution dist = new Distribution(getName(), values);
		return new Distribution[] { dist };
	}

	@Override
	public NodeValueList[] getNodeValueLists() {
		return new NodeValueList[] {};
	}

	@Override
	public NodeNodeValueList[] getNodeNodeValueLists() {
		return new NodeNodeValueList[] {};
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
		return g.getGraphDatastructures().isNodeType(DirectedNode.class);
	}

	@Override
	public boolean isApplicable(Batch b) {
		return b.getGraphDatastructures().isNodeType(DirectedNode.class);
	}
	
	public int getMotifSize() {
		return ((IntParameter)getParameters()[0]).getIntValue();
	}
}
