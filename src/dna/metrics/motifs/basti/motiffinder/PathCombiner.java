package dna.metrics.motifs.basti.motiffinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;

public class PathCombiner {

	public static Collection<Path> combineToSize(Collection<Path> paths1, Collection<Path> paths2, int size){
		List<Path> returnList = new ArrayList<>();
		
		for(Path p1 : paths1) {
			SmallGraph g1 = p1.getGraph();
			int minSize = size - g1.getSize();
			
			if(minSize == 0) {
				returnList.add(p1);
				continue;
			}
			
			for(Path p2 : paths2) {
				SmallGraph g2 = p2.getGraph();
				if(g2.getSize() < minSize) {
					continue;
				}
				
				SmallGraph newGraph = intersectGraphs(g1, g2);
				boolean changed = p1.hasChanged() || p2.hasChanged();
				Path newPath = new Path(newGraph);
				newPath.setChanged(changed);
				
				if(newPath.getGraph().getSize() == size) {
					returnList.add(newPath);
				}
			}
		}
		
		for(Path p2 : paths2) {
			if(p2.getGraph().getSize() == size) {
				returnList.add(p2);
			}
		}
		
		return returnList;
	}
	
	private static SmallGraph intersectGraphs(SmallGraph graph1, SmallGraph graph2){
		SmallGraph clonedGraph = graph1.shallowClone();
		
		for(INode node : graph2.getNodes()) {
			if(!clonedGraph.getNodes().contains(node)) {
				clonedGraph.getNodes().add(node);
			}
		}
		
		for(IEdge edge : graph2.getEdges()) {
			if(!clonedGraph.getEdges().contains(edge)) {
				clonedGraph.getEdges().add(edge);
			}
		}
		
		return clonedGraph;
	}
	
	public static void createPrevGraphs(Collection<Path> paths, IEdge edge) {
		for(Path p : paths) {
			if(!p.hasChanged())
				continue;
			
			SmallGraph clonedGraph = p.getGraph().shallowClone();
			clonedGraph.getEdges().remove(edge);
			p.setPrevGraph(clonedGraph);
		}
	}
}
