package dna.metrics.motifs.basti.motifcounter;

import dna.metrics.motifs.basti.datastructures.SmallGraph;


public abstract class MotifType {
	
	SmallGraph baseGraph;
	
	public SmallGraph getBaseGraph() {
		return baseGraph;
	}

	public MotifType(SmallGraph graph){
		
		baseGraph = graph;
	}
}
