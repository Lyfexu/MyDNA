package dna.metrics.motifs.acc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

import Main.mbr;
import dna.graph.datastructures.GDS;
import dna.graph.edges.DirectedEdge;
import dna.graph.edges.IEdge;
import dna.graph.nodes.DirectedNode;
import dna.graph.nodes.INode;
import dna.metrics.motifs.basti.datastructures.SmallGraph;
import dna.metrics.motifs.basti.utils.GraphTransformer;


public class AccCaller {
	
	private InputStream savedStandardInputStream;
	private PrintStream savedStandardOutputStream;
	
	public HashMap<Long, Integer> getMotifs_old(SmallGraph g, int motifSize) {
		final String tmpGraphFile = "tmpGraph.txt";
		
		SmallGraph convertedGraph = convertGraphToAccFormat(g);
		
		writeGraphToFile(convertedGraph, tmpGraphFile);
		
		setInputForAccCall(tmpGraphFile, motifSize);
		
		ByteArrayOutputStream output = catchOutput();
		
		mbr.main(new String[0]);
		
		resetStandardInputStream();
		resetStandardOutputStream();
		
		return convertOutput(output.toString(), motifSize);
	}
	
	public HashMap<Long, Integer> getMotifs(SmallGraph g, int motifSize) {
		final String tmpGraphFile = "tmpGraph.txt";
		
		SmallGraph convertedGraph = convertGraphToAccFormat(g);
		
		writeGraphToFile(convertedGraph, tmpGraphFile);
		
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("java -jar " +
					"\"Z:\\G\\My Dropbox\\Dropbox\\uni\\Masterthesis\\libraries\\AccMotifs_10_july_2014\\accMotifs.jar\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		OutputStream stdin = process.getOutputStream ();
		InputStream stdout = process.getInputStream ();
		
		BufferedReader reader = new BufferedReader (new InputStreamReader(stdout));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stdin));
		
		String simulatedUserInput = tmpGraphFile + System.getProperty("line.separator")
				+ motifSize + System.getProperty("line.separator")
				+ "0" + System.getProperty("line.separator")
				+ "0" + System.getProperty("line.separator")
				+ "0" + System.getProperty("line.separator");
		
		try {
			writer.write(simulatedUserInput);
			writer.flush();
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		StringBuffer sb = new StringBuffer();
		String line = "";
		try {
			while ((line = reader.readLine())!= null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return convertOutput(sb.toString(), motifSize);
	}
	
	/**
	 * An Acc graph needs incrementing node indices. This method converts a graph with nodes like
	 * 1, 4, 6, 7 to 0, 1, 2, 3.  
	 * 
	 * @param graph
	 * @return
	 */
	private SmallGraph convertGraphToAccFormat(SmallGraph graph) {
		SmallGraph convertedGraph = new SmallGraph();
		
		for (INode node : graph.getNodes()) {
			convertedGraph.getNodes().add(new DirectedNode(node.getIndex(), GDS.directed()));
		}
		
		for (IEdge edge : graph.getEdges()) {
			DirectedEdge newEdge = new DirectedEdge(
					(DirectedNode)convertedGraph.getNodeByIndex(edge.getN1().getIndex()),
					(DirectedNode)convertedGraph.getNodeByIndex(edge.getN2().getIndex()));
			convertedGraph.getEdges().add(newEdge);
		}
		
		Collections.sort(convertedGraph.getNodes(), new Comparator<INode>() {
			@Override
			public int compare(INode o1, INode o2) {
				return o1.getIndex() - o2.getIndex();
			}});
		
		for (int i=0; i<convertedGraph.getNodes().size(); i++) {
			convertedGraph.getNodes().get(i).setIndex(i);
		}
		
		return convertedGraph;
	}

	private void resetStandardOutputStream() {
		System.setOut(savedStandardOutputStream);
	}

	private HashMap<Long, Integer> convertOutput(String output, int motifSize) {
		String[] lines = output.split("\n");
		
		HashMap<String, Integer> accMotifCounts = new HashMap<>();
		boolean isMotifCount = false;
		for (String line : lines) {
			if(!isMotifCount) {
				if (line.contains("Original Network")) {
					isMotifCount = true;
					continue;
				}
			} else {
				if(line.trim().isEmpty()) {
					break;
				}
				
				String[] values = line.split("\\s+");
				String id = values[0];
				int count = Integer.parseInt(values[1]);
				accMotifCounts.put(id, count);
			}
		}
		
		return mapAccIdsToActualIds(accMotifCounts, motifSize);
	}

	private HashMap<Long, Integer> mapAccIdsToActualIds(HashMap<String,Integer> accMotifCounts,
			int motifSize) {
		IdMapper idm = new IdMapper();
		HashMap<Long, Integer> motifCounts = new HashMap<>();
		
		for (Entry<String, Integer> entry : accMotifCounts.entrySet()) {
			long localId = idm.toLocalId(entry.getKey(), motifSize);
			motifCounts.put(localId, entry.getValue()); 
		}
		
		return motifCounts;
	}

	private ByteArrayOutputStream catchOutput() {
		savedStandardOutputStream = System.out;
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		System.setOut(ps);
		
		return baos;
	}

	private void resetStandardInputStream() {
		System.setIn(savedStandardInputStream);
	}

	private void setInputForAccCall(String inputGraphPath, int patternSize) {
		savedStandardInputStream = System.in;
		
		String simulatedUserInput = inputGraphPath + System.getProperty("line.separator")
		    + patternSize + System.getProperty("line.separator")
		    + "0" + System.getProperty("line.separator")
		    + "0" + System.getProperty("line.separator")
		    + "0" + System.getProperty("line.separator");
		
		System.setIn(new ByteArrayInputStream(simulatedUserInput.getBytes()));
	}

	/**
	 * Writes a {@link SmallGraph} with the acc format to a file.
	 * 
	 * @param g
	 * @param tmpGraphFile
	 */
	private void writeGraphToFile(SmallGraph g, String tmpGraphFile) {
		String graphAsAccText = new GraphTransformer().transformToAccFile(g);
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(tmpGraphFile);
			pw.write(graphAsAccText);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
	}
}
