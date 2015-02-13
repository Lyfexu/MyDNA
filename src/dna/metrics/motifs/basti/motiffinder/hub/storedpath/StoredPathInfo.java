package dna.metrics.motifs.basti.motiffinder.hub.storedpath;

import dna.graph.nodes.INode;

public class StoredPathInfo {
	private INode associatedNode;
	private StoredPathVertex storedPathRoot;
	
	public INode getAssociatedNode() {
		return associatedNode;
	}
	
	public void setAssociatedNode(INode associatedNode) {
		this.associatedNode = associatedNode;
	}
	
	public StoredPathVertex getStoredPathRoot() {
		return storedPathRoot;
	}
	
	public void setStoredPathRoot(StoredPathVertex storedPathRoot) {
		this.storedPathRoot = storedPathRoot;
	}
	
	public StoredPathInfo(INode associatedNode, StoredPathVertex storedPathRoot) {
		super();
		this.associatedNode = associatedNode;
		this.storedPathRoot = storedPathRoot;
	}
}
