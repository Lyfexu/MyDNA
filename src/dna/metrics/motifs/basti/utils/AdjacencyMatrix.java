package dna.metrics.motifs.basti.utils;

import java.util.Arrays;
import java.util.List;

import dna.graph.edges.IEdge;
import dna.graph.nodes.INode;
import dna.graph.nodes.Node;
import dna.metrics.motifs.basti.datastructures.SmallGraph;

public class AdjacencyMatrix {
	private boolean[][] matrix;
	private int size;
	
	public boolean[][] getMatrix() {
		return matrix;
	}
	
	public void setMatrix(boolean[][] matrix) {
		this.matrix = matrix;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	AdjacencyMatrix() { }
	
	AdjacencyMatrix(SmallGraph g){
		this(g.getNodes());
	}
	
	AdjacencyMatrix(List<INode> nodes) {
		int nodeCount = nodes.size();
		size = nodeCount;
		matrix = new boolean[nodeCount][];
		
		Iterable<Node> castedNodes = (Iterable<Node>)(Object)nodes;
		int nodeCounter = 0;
		for(Node node : castedNodes) {
			matrix[nodeCounter] = new boolean[nodeCount];
			Iterable<IEdge> edges = (Iterable<IEdge>)(Object)node.getEdges();
			for(IEdge edge : edges) {
				if(edge.getN2().equals(node)) {
					continue;
				}
				int index = nodes.indexOf(edge.getN2());
				matrix[nodeCounter][index] = true;
			}
			
			nodeCounter++;
		}
	}
	
	public void exchangeRows(int row1, int row2) {
		boolean[] tmp = matrix[row1];
		matrix[row1] = matrix[row2];
		matrix[row2] = tmp;
		
		for(boolean[] row : matrix) {
			boolean tmpElement = row[row1];
			row[row1] = row[row2];
			row[row2] = tmpElement;
		}
	}
	
	public AdjacencyMatrix shallowClone() {
		AdjacencyMatrix newAdjMatrix = new AdjacencyMatrix();
		boolean[][] clonedMatrix = deepCopy(matrix);
		newAdjMatrix.setMatrix(clonedMatrix);
		
		return newAdjMatrix;
	}
	
	public static boolean[][] deepCopy(boolean[][] original) {
    if (original == null) {
        return null;
    }

    final boolean[][] result = new boolean[original.length][];
    for (int i = 0; i < original.length; i++) {
        result[i] = Arrays.copyOf(original[i], original[i].length);
    }
    return result;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(boolean[] row : matrix) {
			for(boolean element : row) {
				if(element) {
					sb.append(1);
				} else {
					sb.append(0);
				}
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
