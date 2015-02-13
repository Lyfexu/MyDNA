package dna.metrics.motifs.basti.motifcounter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.utils.CanonicalLabelGenerator;
import dna.metrics.motifs.basti.utils.GraphTransformer;

public class DirectedMotifCounter {
	private HashMap<DirectedMotifType, Integer> motifCounter = new HashMap<>();
	private List<DirectedMotifType> orderedMotifs = new ArrayList<>();
	
	public HashMap<DirectedMotifType, Integer> getMotifCounter() {
		return motifCounter;
	}

	public List<DirectedMotifType> getOrderedMotifs() {
		return orderedMotifs;
	}

	public void setOrderedMotifs(List<DirectedMotifType> orderedMotifs) {
		this.orderedMotifs = orderedMotifs;
	}

	public void incrementCounterFor(Collection<Path> graphs){
		for (Path g : graphs) {
			incrementCounterFor(g.getGraph());
		}
	}
	
	public int incrementCounterFor(SmallGraph graph){
		return incrementCounterFor(new DirectedMotifType(graph));
	}
	
	private int incrementCounterFor(DirectedMotifType motifType){
		Integer counter = motifCounter.get(motifType);
		
		if(counter == null){
			counter = 1;
			motifCounter.put(motifType, counter);
			orderedMotifs.add(motifType);
		}
		else{
			counter++;
			motifCounter.put(motifType, counter);
		}
		
		return counter;
	}
	
	public int decrementCounterFor(SmallGraph graph){
		return decrementCounterFor(new DirectedMotifType(graph));
	}
	
	public int decrementCounterFor(DirectedMotifType motifType){
		Integer counter = motifCounter.get(motifType);
		
		if(counter == null){
			counter = -1;
			motifCounter.put(motifType, counter);
			orderedMotifs.add(motifType);
			
			//todo remove
			if(counter < 0) {
				System.out.println("counter smaller than 0");
			}
			//
		} else {
			counter--;
			motifCounter.put(motifType, counter);
		}
		
		return counter;
	}
	
	public String showOutput() {
		TreeMap<Long, Integer> outputMap = new TreeMap<>();
		
		CanonicalLabelGenerator clg = new CanonicalLabelGenerator();
		for (Entry<DirectedMotifType, Integer> motif : motifCounter.entrySet()) {
			SmallGraph sg = GraphTransformer.transformToSmallGraph(motif.getKey().getGraph());
			long localId = clg.genCanonicalLabelFor(sg);
			outputMap.put(localId, motif.getValue());
		}
		
		return outputMap.toString();
	}
}
