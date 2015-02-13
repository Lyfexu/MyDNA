package dna.metrics.motifs.acc;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;


public class IdMapper {
	private final String idMapping3File = "accIdMapping3.txt";
	private final String idMapping4File = "accIdMapping4.txt";
	private Properties idMappings3;
	private Properties idMappings4;
	

//	public static void main(String[] args) {
//		IdMapper idm = new IdMapper();
//		idm.genAccIdMappings3();
//		idm.genAccIdMappings4();
//	}
//	
//	public void genAccIdMappings3() {
//		CanonicalLabelGenerator clb = new CanonicalLabelGenerator();
//		StringBuilder output = new StringBuilder();
//		
//		String accId = "1120";
//		SmallGraph g = new GraphFactory().addEdges(3, 1, 3, 2).genSmallGraph();
//		long localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "11011";
//		g = new GraphFactory().addEdges(3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "11121";
//		g = new GraphFactory().addEdges(3, 1, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "21010";
//		g = new GraphFactory().addEdges(3, 1, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "21120";
//		g = new GraphFactory().addEdges(3, 1, 2, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "22121";
//		g = new GraphFactory().addEdges(3, 1, 2, 1, 3, 2, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "101112";
//		g = new GraphFactory().addEdges(1, 3, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "111111";
//		g = new GraphFactory().addEdges(1, 3, 2, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "111122";
//		g = new GraphFactory().addEdges(3, 1, 1, 3, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "111221";
//		g = new GraphFactory().addEdges(1, 3, 3, 1, 3, 2, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "121220";
//		g = new GraphFactory().addEdges(1, 3, 3, 1, 2, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "122122";
//		g = new GraphFactory().addEdges(1, 3, 3, 1, 2, 3, 3, 2, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "222222";
//		g = new GraphFactory().addEdges(1, 3, 3, 1, 2, 3, 3, 2, 1, 2, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		writeToFile(output.toString(), idMapping3File);
//	}
//	
//	public void genAccIdMappings4() {
//		CanonicalLabelGenerator clb = new CanonicalLabelGenerator();
//		StringBuilder output = new StringBuilder();
//		
//		String accId = "4118";
//		SmallGraph g = new GraphFactory().addEdges(4, 1, 4, 2, 3, 2).genSmallGraph();
//		long localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4119";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 4, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4126";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 3, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4127";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 4, 3, 3, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4173";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4189";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 3, 3, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4190";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 2, 3, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4191";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 2, 3, 4, 4, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4197";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 3, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4203";
//		g = new GraphFactory().addEdges(4, 2, 2, 4, 4, 3, 3, 4, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4205";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 3, 3, 4, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4206";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 2, 3, 4, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4207";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 3, 1, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4214";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4215";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 4, 3, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4223";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 4, 3, 3, 4, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4237";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4239";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 4, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4245";
//		g = new GraphFactory().addEdges(4, 2, 2, 4, 3, 1, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4255";
//		g = new GraphFactory().addEdges(4, 2, 2, 4, 3, 4, 4, 3, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4259";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4261";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 1, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4263";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 4, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		//10:54
//		accId = "4271";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 4, 4, 3, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4279";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 2, 3, 4, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4335";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 3, 4, 4, 3, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4365";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 2, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4382";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 3, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4383";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 4, 3, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4389";
//		g = new GraphFactory().addEdges(4, 1, 4, 3, 3, 1, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4398";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 1, 3, 1, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4399";
//		g = new GraphFactory().addEdges(4, 1, 4, 3, 3, 4, 4, 2, 3, 1, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4407";
//		g = new GraphFactory().addEdges(4, 1, 4, 3, 4, 2, 3, 1, 3, 2, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4461";
//		g = new GraphFactory().addEdges(4, 1, 4, 3, 3, 4, 3, 1, 2, 1, 2, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4479";
//		g = new GraphFactory().addEdges(4, 1, 4, 3, 3, 4, 4, 2, 2, 4, 3, 1, 3, 2, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4527";
//		g = new GraphFactory().addEdges(4, 1, 4, 3, 4, 2, 2, 4, 3, 1, 3, 2, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4625";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4657";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4699";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 4, 3, 3, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		//11:00
//		accId = "4702";
//		g = new GraphFactory().addEdges(4, 1, 1, 4, 4, 2, 2, 4, 3, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4703";
//		g = new GraphFactory().addEdges(4, 1, 1, 4, 4, 2, 2, 4, 4, 3, 3, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4711";
//		g = new GraphFactory().addEdges(4, 1, 1, 4, 4, 2, 2, 4, 4, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4723";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 4, 3, 3, 2, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4747";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 4, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		// 11:02
//		accId = "4753";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4766";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4767";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4829";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 2, 3, 3, 2, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4843";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 4, 3, 3, 4, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4845";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 1, 4, 3, 3, 4, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4847";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 4, 3, 3, 4, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4863";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 4, 3, 3, 4, 3, 1, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4891";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 4, 3, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4973";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 2, 1, 4, 3, 3, 4, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "4991";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 4, 3, 3, 4, 3, 1, 3, 2, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5004";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5023";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 4, 3, 3, 1, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5039";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 4, 3, 3, 1, 3, 2, 2, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5178";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 4, 3, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5195";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5214";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 3, 4, 2, 3, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5215";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 4, 3, 3, 4, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5219";
//		g = new GraphFactory().addEdges(1, 4, 2, 3, 3, 2, 4, 3, 4, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5259";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5267";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5271";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 1, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5279";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 1, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5291";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5343";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5359";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5367";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5377";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5401";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5442";
//		g = new GraphFactory().addEdges(4, 2, 2, 4, 3, 1, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5460";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 3, 1, 2, 3, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5487";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 1, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5498";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 3, 1, 4, 2, 4, 3, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5535";
//		g = new GraphFactory().addEdges(1, 4, 2, 1, 4, 2, 2, 4, 3, 1, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5551";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 3, 1, 3, 2, 2, 4, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5559";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5582";
//		g = new GraphFactory().addEdges(1, 4, 2, 1, 4, 2, 3, 1, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5855";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 4, 2, 1, 3, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "5971";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 1, 3, 2, 2, 1, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6002";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6026";
//		g = new GraphFactory().addEdges(1, 4, 2, 1, 4, 2, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6033";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 1, 2, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6138";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6184";
//		g = new GraphFactory().addEdges(4, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6235";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 3, 2, 2, 3, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6237";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6238";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 3, 1, 2, 3, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6239";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6251";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 4, 2, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6258";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6262";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 1, 4, 2, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6263";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 3, 1, 3, 2, 2, 1, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6271";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 1, 4, 2, 3, 1, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6285";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6287";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 3, 1, 2, 3, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6303";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 1, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6306";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 4, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6307";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 3, 1, 2, 3, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6327";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 3, 1, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6379";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6430";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 1, 2, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6431";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 1, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6507";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 4, 2, 3, 1, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6527";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6561";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6581";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6627";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 2, 3, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6911";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 1, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6915";
//		g = new GraphFactory().addEdges(4, 2, 2, 4, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "6963";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 2, 4, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7001";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 3, 1, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7010";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 3, 3, 2, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7019";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7059";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 2, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7086";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 2, 2, 4, 1, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7196";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7210";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7233";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7370";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 1, 2, 3, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "7399";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 3, 1, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "8021";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 2, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "8088";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 4, 2, 3, 1, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "787878520";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "8888672672";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 3, 1, 2, 3, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "9292327461";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//
//		accId = "59131814814";
//		g = new GraphFactory().addEdges(4, 1, 2, 1, 3, 1, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//
//		accId = "61198749749";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//
//		accId = "61642795795";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//
//		accId = "76845845849";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//
//		accId = "79517517929";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 1, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//
//		accId = "80122122312";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//
//		accId = "80637637639";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//
//		accId = "101223223480";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "111111455624";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "118118725866";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "123123618618";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 3, 1, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "128128413413";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "152294648648";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 3, 1, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "152663663673";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 3, 1, 2, 3, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "176176176546";
//		g = new GraphFactory().addEdges(2, 1, 4, 1, 4, 2, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "180243709709";
//		g = new GraphFactory().addEdges(4, 1, 2, 4, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "188188297455";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "193239239877";
//		g = new GraphFactory().addEdges(4, 2, 2, 4, 3, 1, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "196196330697";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 1, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "196279912912";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "201300300414";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 2, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "203203203203";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 2, 2, 4, 1, 3, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "208208210840";
//		g = new GraphFactory().addEdges(2, 1, 4, 1, 4, 2, 3, 1, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "209209751751";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 2, 4, 3, 1, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "216863914914";
//		g = new GraphFactory().addEdges(2, 1, 4, 1, 4, 2, 3, 1, 3, 2, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "219469469929";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "234234399651";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "243329587587";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "243624632632";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "255293293849";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "259259276276";
//		g = new GraphFactory().addEdges(4, 1, 4, 3, 3, 4, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "274314350350";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "291544652652";
//		g = new GraphFactory().addEdges(2, 1, 4, 1, 4, 2, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "293293293293";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 1, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "293293726726";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 1, 3, 3, 1, 4, 2, 2, 4, 2, 3, 3, 2, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "294294327377";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 2, 4, 3, 1, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "302302551551";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 2, 4, 1, 3, 2, 3, 3, 2, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "310894894894";
//		g = new GraphFactory().addEdges(2, 1, 4, 1, 2, 4, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "312312532911";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 2, 4, 1, 3, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "320320411547";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "325325377411";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "327547914914";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 3, 1, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "330446530530";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "334334529592";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 2, 2, 4, 3, 1, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "339340340860";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "348348811811";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 3, 1, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "387650650650";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 2, 4, 1, 3, 3, 1, 2, 3, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "405549694694";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 2, 3, 3, 2, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "405618618877";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "423423423573";
//		g = new GraphFactory().addEdges(2, 1, 4, 1, 3, 1).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "444444444746";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 2, 3, 1, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "453453843843";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 3, 3, 4, 2, 3, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "459782899899";
//		g = new GraphFactory().addEdges(2, 1, 4, 1, 4, 2, 2, 4, 3, 1, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "494618618618";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 2, 4, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "497497789789";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 2, 4, 3, 1, 2, 3, 3, 2, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "498498808808";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 1, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "507557800800";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "509529845845";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 2, 3, 1, 2, 3, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "542633633782";
//		g = new GraphFactory().addEdges(2, 1, 4, 1, 3, 1, 4, 2, 2, 4, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "567567781781";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 3, 1, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "632632671671";
//		g = new GraphFactory().addEdges(1, 4, 4, 1, 4, 2, 3, 1, 2, 3, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "637637637637";
//		g = new GraphFactory().addEdges(1, 4, 4, 2, 3, 1, 2, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "677677677677";
//		g = new GraphFactory().addEdges(1, 2, 2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 1, 2, 3, 3, 2, 3, 4, 4, 3)
//				.genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "682682684684";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 3, 1, 3, 2).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "709726795795";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 3, 1, 3, 2, 3, 4, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "714765765798";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 4, 2, 2, 4, 1, 3, 3, 1, 2, 3, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "747929947947";
//		g = new GraphFactory().addEdges(2, 1, 1, 4, 4, 1, 2, 4, 1, 3, 3, 1, 3, 2, 4, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "809809857890";
//		g = new GraphFactory().addEdges(1, 4, 2, 4, 3, 1, 3, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "830830850886";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 2, 4, 3, 1, 2, 3, 3, 4).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		accId = "905928928928";
//		g = new GraphFactory().addEdges(4, 1, 4, 2, 4, 3).genSmallGraph();
//		localId = clb.genCanonicalLabelFor(g);
//		output.append(String.format("%s = %s\n", accId, localId));
//		
//		writeToFile(output.toString(), idMapping4File);
//	}
//	
//	private void writeToFile(String content, String file) {
//		PrintWriter pw = null;
//		try {
//			pw = new PrintWriter(file);
//			pw.write(content);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} finally {
//			if (pw != null) {
//				pw.close();
//			}
//		}
//	}
	
	private void readAccIdMappings3() {
		try {
			idMappings3 = new Properties();
			idMappings3.load(new FileReader(idMapping3File));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void readAccIdMappings4() {
		try {
			idMappings4 = new Properties();
			idMappings4.load(new FileReader(idMapping4File));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public long toLocalId(String accKey, int motifSize) {
		if (motifSize == 3) {
			if(idMappings3 == null) {
				readAccIdMappings3();
			}
			return Long.parseLong(idMappings3.getProperty(accKey));
		} else if (motifSize == 4) {
			if(idMappings4 == null) {
				readAccIdMappings4();
			}
			return Long.parseLong(idMappings4.getProperty(accKey));
		}
		
		throw new RuntimeException("Unsupported motifSize: " + motifSize);
	}
	
	public long toAccId(String localId, int motifSize) {
		if (motifSize == 3) {
			if(idMappings3 == null) {
				readAccIdMappings3();
			}
			
			for (Entry<Object, Object> entry : idMappings3.entrySet()) {
				if (entry.getValue().equals(localId)) {
					return Long.parseLong(entry.getKey().toString());
				}
			}
		} else if (motifSize == 4) {
			if(idMappings4 == null) {
				readAccIdMappings4();
			}

			for (Entry<Object, Object> entry : idMappings4.entrySet()) {
				if (entry.getValue().equals(localId)) {
					return Long.parseLong(entry.getKey().toString());
				}
			}
		}
		
		throw new RuntimeException("Unsupported motifSize: " + motifSize);
	}
}
