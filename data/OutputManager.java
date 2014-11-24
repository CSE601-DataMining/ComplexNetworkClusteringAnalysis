package biolab.network.dynamics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import biolab.network.dynamics.analysis.AnalysisManager;
import biolab.network.dynamics.common.Constants;
import biolab.network.dynamics.common.Edge;
import biolab.network.dynamics.common.Node;

//need to update for FileWriter (using PrintUtil)
public class OutputManager {

	protected String outputFileName = null;

	public void initOutputFile() throws IOException {
		// TODO Auto-generated method stub
		FileWriter file = new FileWriter(outputFileName, false);
		BufferedWriter bw = new BufferedWriter(file);

		bw.close();
		file.close();
	}

	public void printNumOfEdgeByNode(String fileName) throws IOException {
		// TODO Auto-generated method stub
		FileWriter file = new FileWriter(fileName, false);
		BufferedWriter bw = new BufferedWriter(file);

		HashMap histogramEdgeMap = AnalysisManager.getHistogramEdgeMap();
		Iterator iterator = histogramEdgeMap.keySet().iterator();
		while (iterator.hasNext()) {
			String keyName = (String) iterator.next();
			Integer count = (Integer) histogramEdgeMap.get(keyName);
			bw.write(keyName + "\t");
			bw.write(count + "\r\n");
		}

		bw.close();
		file.close();
	}

	// printing results
	public void printHeaderInfo() throws IOException {
		// TODO Auto-generated method stub

		// contents for time slide node and edge info
		StringBuffer contents = new StringBuffer();
		// read time slice node info
		File checkfile = new File(outputFileName);
		if (checkfile.exists()) {
			FileReader fileReader = new FileReader(outputFileName);
			BufferedReader bfReader = new BufferedReader(fileReader);

			String line = null;

			while ((line = bfReader.readLine()) != null) {
				contents.append(line);
				contents.append("\r\n");
			}
			bfReader.close();
			fileReader.close();
		}

		// write whole network info
		printWholeNetworkInfo();

		// write init network info
		printInitNetworkInfo();

		// write nodes and edges dynamics info
		printDynamicsContents(contents.toString());
	}

	protected void printWholeNetworkInfo() throws IOException {
		// TODO Auto-generated method stub
		// header
		FileWriter file = new FileWriter(outputFileName, false);
		BufferedWriter bw = new BufferedWriter(file);
		int numOfNode = NetworkManager.nodeMap.size();

		bw.write(Constants.PAJEK_NETWORK_HEADER + "\r\n");
		bw.write(Constants.PAJEK_VERTICES_HEADER + " " + numOfNode + "\r\n");

		printNodeInfoByTimeSlide(bw);

		// header for edge info
		bw.write(Constants.PAJEK_EDGES_HEADER + "\r\n");
		printInitEdgeInfoByTimeSlide(bw);
		printEdgeInfoByTimeSlide(bw);
		bw.write("\r\n");

		bw.close();
		file.close();
	}

	protected void printInitNetworkInfo() throws IOException {
		// TODO Auto-generated method stub
		// header
		FileWriter file = new FileWriter(outputFileName, true);
		BufferedWriter bw = new BufferedWriter(file);
		int numOfNode = NetworkManager.nodeMap.size();
		bw.write(Constants.PAJEK_TIME_HEADER + " 1" + "\r\n");
		bw.write(Constants.PAJEK_VERTICES_HEADER + " " + numOfNode + "\r\n");

		printNodeInfoByTimeSlide(bw);

		// header
		bw.write(Constants.PAJEK_EDGES_HEADER + "\r\n");
		printInitEdgeInfoByTimeSlide(bw);
		printEdgeInfoByTimeSlide(bw);
		bw.write("\r\n");

		bw.close();
		file.close();
	}

	private void printDynamicsContents(String contents) throws IOException {
		// TODO Auto-generated method stub
		// header
		FileWriter file = new FileWriter(outputFileName, true);
		BufferedWriter bw = new BufferedWriter(file);

		bw.write(contents.toString());

		bw.close();
		file.close();
	}

	public void printCurrentNodeByTimeSlide(int currentTime, boolean isInit)
			throws IOException {
		// TODO Auto-generated method stub
		FileWriter file = new FileWriter(outputFileName, true);
		BufferedWriter bw = new BufferedWriter(file);
		int numOfNode = NetworkManager.nodeMap.size();

		// header
		bw.write(Constants.PAJEK_TIME_HEADER + " " + currentTime + "\r\n");
		bw.write(Constants.PAJEK_VERTICES_HEADER + " " + numOfNode + "\r\n");

		printNodeInfoByTimeSlide(bw);

		// header
		bw.write(Constants.PAJEK_EDGES_HEADER + "\r\n");
//		if (isInit) {	//temporary
			printInitEdgeInfoByTimeSlide(bw);
//		}

		printEdgeInfoByTimeSlide(bw);
		bw.write("\r\n");

		bw.close();
		file.close();
	}

	protected void printNodeInfoByTimeSlide(BufferedWriter bw)
			throws IOException {
		Iterator keyIterator = NetworkManager.nodeMap.keySet().iterator();
		while (keyIterator.hasNext()) {
			String key = (String) keyIterator.next();
			Node node = (Node) NetworkManager.nodeMap.get(key);

			StringBuffer sb = new StringBuffer();
			// sb.append(i);
			sb.append(node.getSequence());
			sb.append(" ");
			sb.append("\"");
			sb.append(node.getNodeName());
			sb.append("\"");
			sb.append(" ");
			sb.append(node.getCoordX());
			sb.append(" ");
			sb.append(node.getCoordY());
			sb.append(" ");
			sb.append(node.getCoordZ());
			// sb.append(" ");
			// sb.append("x_fact 5 y_fact 5 ic Black bc White");
			// sb.append(" ");
			sb.append("\r\n");

			bw.write(sb.toString());
		}

	}

	protected void printEdgeInfoByTimeSlide(BufferedWriter bw)
			throws IOException {
		Iterator edgeIterator = NetworkManager.edgeMap.values().iterator();
		while (edgeIterator.hasNext()) {
			Edge edge = (Edge) edgeIterator.next();

			if (edge.getEdgeWeight() <= 0)
				continue;

			StringBuffer sb = new StringBuffer();
			Node node1 = (Node) NetworkManager.nodeMap.get(edge.getNameNode1());
			Node node2 = (Node) NetworkManager.nodeMap.get(edge.getNameNode2());

			sb.append(node1.getSequence());
			sb.append(" ");
			sb.append(node2.getSequence());
			sb.append(" ");
			sb.append(Constants.PAJEK_EDGE_WEIGHT_DEFAULT);
			sb.append(" ");
			sb.append("c");
			sb.append(" ");
			
			// for immune simulation
			if (edge.getImmune() > 0)
				sb.append(Constants.PAJEK_EDGE_COLOR_IMMUNE);
			else
				sb.append(Constants.PAJEK_EDGE_COLOR_HEALTH);
				
			sb.append("\r\n");

			bw.write(sb.toString());
		}
		// Logger.println("num of edge: " + edgeMap.size());
	}

	 protected void printInitEdgeInfoByTimeSlide(BufferedWriter bw)
	 throws IOException {
	 // TODO Auto-generated method stub
	 Iterator edgePathologicalIterator =
	 NetworkManager.damagedEdgeByOffenseMap
	 .values().iterator();
	 while (edgePathologicalIterator.hasNext()) {
	 Edge edge = (Edge) edgePathologicalIterator.next();
	
	 StringBuffer sb = new StringBuffer();
	 Node node1 = (Node) NetworkManager.nodeMap.get(edge.getNameNode1());
	 Node node2 = (Node) NetworkManager.nodeMap.get(edge.getNameNode2());
	
	 sb.append(node1.getSequence());
	 sb.append(" ");
	 sb.append(node2.getSequence());
	 sb.append(" ");
	 sb.append("8");
	 sb.append(" ");
	 sb.append("c");
	 sb.append(" ");
	 sb.append(Constants.PAJEK_EDGE_COLOR_ATTACTED);
	 sb.append("\r\n");
	
	 bw.write(sb.toString());
	 }
	
	 Iterator edgeProgramedIterator = NetworkManager.damagedEdgeByDefenseMap
	 .values().iterator();
	 while (edgeProgramedIterator.hasNext()) {
	 Edge edge = (Edge) edgeProgramedIterator.next();
	
	 StringBuffer sb = new StringBuffer();
	 Node node1 = (Node) NetworkManager.nodeMap.get(edge.getNameNode1());
	 Node node2 = (Node) NetworkManager.nodeMap.get(edge.getNameNode2());
	
	 sb.append(node1.getSequence());
	 sb.append(" ");
	 sb.append(node2.getSequence());
	 sb.append(" ");
	 sb.append(Constants.PAJEK_EDGE_WEIGHT_DEFAULT);
	 sb.append(" ");
	 sb.append("c");
	 sb.append(" ");
	 sb.append(Constants.PAJEK_EDGE_COLOR_CULLED);
	 sb.append("\r\n");
	
	 bw.write(sb.toString());
	 }
	 // Logger.println("num of edge: " + edgeMap.size());
	 }

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}

}
