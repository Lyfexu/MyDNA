package dna.metrics.motifs.basti.motifcounter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.experimental.isomorphism.AdaptiveIsomorphismInspectorFactory;
import org.jgrapht.experimental.isomorphism.GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import dna.graph.datastructures.GDS;
import dna.graph.edges.UndirectedEdge;
import dna.graph.nodes.UndirectedNode;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.utils.GraphUtils;

public class UndirectedMotifType extends MotifType {
	List<UndirectedNode> nodes;
	List<UndirectedEdge> edges;
	
	public List<UndirectedNode> getNodes() {
		return nodes;
	}

	public List<UndirectedEdge> getEdges() {
		return edges;
	}
	
	public UndirectedMotifType(SmallGraph graph) {
		super(graph);
		
		nodes = (List<UndirectedNode>)(Object)graph.getNodes();
		edges = (List<UndirectedEdge>)(Object)graph.getEdges();
	}
	
	@Override
	public int hashCode(){
		HashCodeBuilder hcb = new HashCodeBuilder();
		
		for(UndirectedNode n : nodes){
			hcb.append(n.getDegree());
		}
		
		return hcb.toHashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof MotifType))
			return false;
		
		return GraphUtils.testIsomorphism(this.getBaseGraph(), ((MotifType)o).getBaseGraph());
	}
	
	private void sortNodes(){
		Collections.sort(nodes, new Comparator<UndirectedNode>(){
			@Override
			public int compare(UndirectedNode n1, UndirectedNode n2) {
				int n1Deg = n1.getDegree();
				int n2Deg = n2.getDegree();
				
				if(n1Deg < n2Deg)
					return -1;
				if(n1Deg > n2Deg)
					return 1;
				return 0;
			}
			
		});
	}
	
	public void makePersistend(){
		ArrayList<UndirectedNode> newNodes = new ArrayList<UndirectedNode>(baseGraph.getNodes().size());
		for(UndirectedNode n : nodes){
			UndirectedNode newNode = new UndirectedNode(n.getIndex(), GDS.undirected());
			newNodes.add(newNode);
		}
		nodes = newNodes;
		
		ArrayList<UndirectedEdge> newEdges = new ArrayList<UndirectedEdge>(baseGraph.getEdges().size());
		for(UndirectedEdge e : edges){
			UndirectedEdge newEdge = new UndirectedEdge(e.getNode1(), e.getNode2());
			newEdges.add(newEdge);
		}
		edges = newEdges;
	}
	
	private boolean testIsomorphism(UndirectedMotifType otherUndirectedMotif){
		UndirectedGraph<Integer, DefaultEdge> g1 = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);
		UndirectedGraph<Integer, DefaultEdge> g2 = new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);

		for(UndirectedNode node : nodes){
			g1.addVertex(node.getIndex());
		}
		
		for(UndirectedEdge edge : edges){
			g1.addEdge(edge.getN1Index(), edge.getN2Index());
		}
		
		for(UndirectedNode node : otherUndirectedMotif.getNodes()){
			g2.addVertex(node.getIndex());
		}
		
		for(UndirectedEdge edge : otherUndirectedMotif.getEdges()){
			g2.addEdge(edge.getN1Index(), edge.getN2Index());
		}
		
		GraphIsomorphismInspector iso =
	            AdaptiveIsomorphismInspectorFactory.createIsomorphismInspector(
	                g1, g2, null, null);
		
		return iso.hasNext();
	}
}
