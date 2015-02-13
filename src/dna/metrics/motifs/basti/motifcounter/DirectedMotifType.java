package dna.metrics.motifs.basti.motifcounter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jgrapht.DirectedGraph;
import org.jgrapht.experimental.isomorphism.AdaptiveIsomorphismInspectorFactory;
import org.jgrapht.experimental.isomorphism.GraphIsomorphismInspector;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import dna.graph.IElement;
import dna.graph.datastructures.GDS;
import dna.graph.edges.DirectedEdge;
import dna.graph.edges.IEdge;
import dna.graph.nodes.DirectedNode;
import dna.graph.nodes.INode;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.utils.GraphUtils;

public class DirectedMotifType extends MotifType {

	private byte[] degreeHash;
	private int hashCode;
	private DirectedGraph<Integer, DefaultEdge> graph;
	private List<Integer> sortedNodes;
	
	public DirectedGraph<Integer, DefaultEdge> getGraph() {
		return graph;
	}
	
	public byte[] getDegreeHash() {
		return degreeHash;
	}

	public DirectedMotifType(SmallGraph baseGraph) {
		super(baseGraph);
		
		graph = createIsolatedGraph(baseGraph);
		// todo remove
		if(graph.vertexSet().size() != baseGraph.getSize()) {
			System.out.println("Double nodes.");
		}
		if(graph.edgeSet().size() != baseGraph.getEdges().size()) {
			System.out.println("Double edges.");
		}
		//
		
		sortedNodes = new ArrayList<>(graph.vertexSet());
		sortNodes();
		
		degreeHash = createDegreeHash();
		hashCode = createHashCode();
	}
	
	private int createHashCode() {
		HashCodeBuilder hcb = new HashCodeBuilder();
		
		hcb.append(graph.vertexSet().size());
		hcb.append(graph.edgeSet().size());
		
		for(Integer n : sortedNodes){
			hcb.append(graph.outDegreeOf(n));
			hcb.append(graph.inDegreeOf(n));
		}
		
		return hcb.toHashCode();
	}

	private DirectedGraph<Integer, DefaultEdge> createIsolatedGraph(SmallGraph baseGraph) {
		DirectedGraph<Integer, DefaultEdge> jGraphT = new DefaultDirectedGraph<Integer, DefaultEdge>(DefaultEdge.class);
		
		List<DirectedNode> nodes = (List<DirectedNode>)(Object)baseGraph.getNodes();
		for(DirectedNode node : nodes){
			jGraphT.addVertex(node.getIndex());
		}
		
		List<DirectedEdge> edges = (List<DirectedEdge>)(Object)baseGraph.getEdges();
		for(DirectedEdge edge : edges){
			jGraphT.addEdge(edge.getSrcIndex(), edge.getDstIndex());
		}
		
		return jGraphT;
	}

	private byte[] createDegreeHash() {
		ByteBuffer degreeHash = ByteBuffer.allocate(2 + graph.vertexSet().size()*2);
		degreeHash.put((byte)graph.vertexSet().size());
		degreeHash.put((byte)graph.edgeSet().size());
		
		for(Integer n : sortedNodes){
			degreeHash.put((byte)graph.outDegreeOf(n));
			degreeHash.put((byte)graph.inDegreeOf(n));
		}
		
		return degreeHash.array();
	}
	
	@Override
	public int hashCode(){
		return hashCode;
	}
	
	@Override
	public boolean equals(Object o){
		if(o == null || !(o instanceof DirectedMotifType))
			return false;
		
		DirectedMotifType otherMotif = (DirectedMotifType)o;
		if(!Arrays.equals(degreeHash, otherMotif.getDegreeHash()))
			return false;
		
		return testIsomorphism(otherMotif);
		//return IsoTester.testIso(getGraph(), otherMotif.getGraph());
	}
	
	private void sortNodes(){
		Collections.sort(sortedNodes, new Comparator<Integer>(){
			@Override
			public int compare(Integer n1, Integer n2) {
				int n1In = graph.inDegreeOf(n1);
				int n1Out = graph.outDegreeOf(n1);
				
				int n2In = graph.inDegreeOf(n2);
				int n2Out = graph.outDegreeOf(n2);
				
				if(n1Out < n2Out)
					return -1;
				if(n1Out > n2Out)
					return 1;
				if(n1In < n2In)
					return -1;
				if(n1In > n2In)
					return 1;
				return 0;
			}
			
		});
	}
	
	@Deprecated
	public void makePersistend(){
		//graph.
	}
	
	private boolean testIsomorphism(DirectedMotifType otherDirectedMotif){
		
		GraphIsomorphismInspector iso = null;
		Random ran = new Random();
		boolean success;
		int counter = 0;
		do {
			counter ++;
			success = true;
			
			try {
				iso = AdaptiveIsomorphismInspectorFactory.createIsomorphismInspector(
			                graph, otherDirectedMotif.getGraph(), null, null);
			} catch (Exception ex) {
				int ranId;
				do{
					ranId = ran.nextInt();
				} while (graph.containsVertex(ranId) || otherDirectedMotif.getGraph().containsVertex(ranId));
				
				graph.addVertex(ranId);
				otherDirectedMotif.getGraph().addVertex(ranId);
				
				success = false;
				
				if(counter > 10)
					throw ex;
			}
		} while (!success);
		
		return iso.hasNext();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(StringUtils.join(graph.vertexSet(), ", "));
		sb.append("][");
		sb.append(StringUtils.join(graph.edgeSet(), ", "));
		sb.append("]");
		
		return sb.toString();
	}
}
