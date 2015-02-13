package dna.metrics.motifs.basti.motiffinder.hub.storedpath.converter;

import java.util.Collection;
import java.util.List;

import dna.graph.edges.IEdge;
import dna.metrics.motifs.basti.datastructures.Path;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.motiffinder.hub.storedpath.StoredPathVertex;

public class StoredPathConverter {
	
	public static void incorporatePathIntoHub(IEdge actEdge, SmallGraph path, StoredPathVertex spv) {
		GraphInStoredPathIncorporator.incorporatePathIntoHub(actEdge, path, spv);
	}
	
	public static void removePathFromHub(IEdge actEdge, StoredPathVertex spv, int maxSize) {
		GraphInStoredPathIncorporator.removePathFromHub(actEdge, spv, maxSize);
	}
	
	public static StoredPathVertex toStoredPath(Collection<Path> paths){
		return GraphToStoredPathConverter.toStoredPath(paths);
	}
	
	public static List<Path> toGraphs(StoredPathVertex spv){
		return toGraphs(spv, new StoredPathConverterOptions());
	}
	
	public static List<Path> toGraphs(StoredPathVertex spv, StoredPathConverterOptions options){
		return StoredPathToGraphConverter.toGraphs(spv, options);
	}
}
