package dna.metrics.motifs.basti.utils;
import dna.graph.Graph;
import dna.graph.edges.IEdge;


public class DnsGraphTransformer {

	public static String transformToTxt(Graph input) {
		StringBuilder sb = new StringBuilder();
		
		Iterable<IEdge> edges = (Iterable<IEdge>)(Object)input.getEdges();
		for(IEdge edge : edges) {
			sb.append(edge.getN1().getIndex());
			sb.append(" ");
			sb.append(edge.getN2().getIndex());
			sb.append(" 1\n");
		}
		
		return sb.toString();
	}

}
