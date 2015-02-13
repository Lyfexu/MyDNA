package dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter;

import java.util.ArrayList;
import java.util.Collection;

import dna.graph.nodes.INode;

public class StoredPathConverterOptions {
	private boolean addConnectingEdges;
	private Collection<INode> actPath;
	
	/**
	 * When the StoredPathConverter reaches a forbiddenNode, it
	 * stops and doesn't add any further paths in this direction.
	 * The path with the forbidenNode is not added but every
	 * other path until then.
	 */
	private Collection<INode> forbiddenNodes;
	
	
	/**
	 * When the StoredPathConverter reaches the other node the found
	 * path is stored as 'changed'.
	 */
	private INode otherNode;
	private INode startNode;
	private Collection<INode> stopNodes;
	private boolean excludeStopNodes;
	private boolean excludeOtherNode;
	private boolean onlyExactSize;
	private boolean excludeStartHub;
	
	/**
	 * If the hub is the startnode it is automatically connected to the
	 * other node and therefore the path would normally be set to changed.
	 * But with this option activated the path is not marked as changed, if
	 * only the hub is connected to the other node.
	 * Standard: true
	 */
	private boolean dontSetChangedWhenHubIsStartnode;
	
	public boolean isOnlyExactSize() {
		return onlyExactSize;
	}

	public void setOnlyExactSize(boolean onlyExactSize) {
		this.onlyExactSize = onlyExactSize;
	}

	/**
	 * The maximum size of the returned paths.
	 */
	private int maxSize;
	private boolean useHubRecursion;
	
	public boolean getAddConnectingEdges() {
		return addConnectingEdges;
	}
	
	public void setAddConnectingEdges(boolean addConnectingEdges) {
		this.addConnectingEdges = addConnectingEdges;
	}
	
	public Collection<INode> getActPath() {
		return actPath;
	}
	
	public void setActPath(Collection<INode> actPath) {
		this.actPath = actPath;
	}
	
	public Collection<INode> getForbiddenNodes() {
		return forbiddenNodes;
	}

	public void setForbiddenNodes(Collection<INode> forbiddenNodes) {
		this.forbiddenNodes = forbiddenNodes;
	}

	public INode getOtherNode() {
		return otherNode;
	}
	
	public void setOtherNode(INode otherNode) {
		this.otherNode = otherNode;
	}
	
	public Collection<INode> getStopNodes() {
		return stopNodes;
	}
	
	public void setStopNodes(Collection<INode> stopNodes) {
		this.stopNodes = stopNodes;
	}
	
	public boolean getExcludeStopNodes() {
		return excludeStopNodes;
	}
	
	public void setExcludeStopNodes(boolean excludeStopNodes) {
		this.excludeStopNodes = excludeStopNodes;
	}
	
	public boolean getExcludeOtherNode() {
		return excludeOtherNode;
	}
	
	public void setExcludeOtherNode(boolean excludeOtherNode) {
		this.excludeOtherNode = excludeOtherNode;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	public boolean getUseHubRecursion() {
		return useHubRecursion;
	}

	public void setUseHubRecursion(boolean useHubRecursion) {
		this.useHubRecursion = useHubRecursion;
	}

	public boolean getExcludeStartHub() {
		return excludeStartHub;
	}

	public void setExcludeStartHub(boolean excludeStartHub) {
		this.excludeStartHub = excludeStartHub;
	}

	public boolean getDontSetChangedWhenHubIsStartnode() {
		return dontSetChangedWhenHubIsStartnode;
	}

	public void setDontSetChangedWhenHubIsStartnode(
			boolean dontSetChangedWhenHubIsStartnode) {
		this.dontSetChangedWhenHubIsStartnode = dontSetChangedWhenHubIsStartnode;
	}
	
	public INode getStartNode() {
		return startNode;
	}
	
	public void setStartNode(INode startNode) {
		this.startNode = startNode;
	}
	
	public StoredPathConverterOptions() {
		this.addConnectingEdges = true;
		this.actPath = new ArrayList<INode>();
		this.otherNode = null;
		this.stopNodes = new ArrayList<INode>();
		this.excludeStopNodes = false;
		this.excludeOtherNode = true;
		this.setMaxSize(Integer.MAX_VALUE);
		this.useHubRecursion = false;
		this.onlyExactSize = false;
		this.excludeStartHub = false;
		this.dontSetChangedWhenHubIsStartnode = true;
	}
}
