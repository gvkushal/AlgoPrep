package com.algo.project;

import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * 
 * @author Kushal Guduru Venkateshgupta (801209704)
 * 
 *         Driver class to perform all the operations on the graph.
 *
 */
public class Main {

	public static void main(String[] args) {
		Graph graph = new Graph();
		Scanner in = new Scanner(System.in);
		while (true) {
			System.out.println("Enter the Query: ");
			String query = in.nextLine();
			String[] queryArr = query.split(" ");
			switch (queryArr[0]) {
			case "graph":
				generateGraph(queryArr, graph);
				break;
			case "addedge":
				if (queryArr.length != 4) {
					System.err.println("Please insert correct query");
					continue;
				}
				graph.addEdge(queryArr[1], queryArr[2], Float.parseFloat(queryArr[3]), false);
				System.out.println("Edge " + queryArr[1] + "->" + queryArr[2] + " added successfully..!!");
				break;
			case "deleteedge":
				if (queryArr.length != 3) {
					System.err.println("Please insert correct query");
					continue;
				}
				graph.deleteEdge(queryArr[1], queryArr[2]);
				break;
			case "edgedown":
				if (queryArr.length != 3) {
					System.err.println("Please insert correct query");
					continue;
				}
				graph.updateExistingEdgeState(false, queryArr[1], queryArr[2]);
				break;
			case "edgeup":
				if (queryArr.length != 3) {
					System.err.println("Please insert correct query");
					continue;
				}
				graph.updateExistingEdgeState(true, queryArr[1], queryArr[2]);
				break;
			case "vertexdown":
				if (queryArr.length != 2) {
					System.err.println("Please insert correct query");
					continue;
				}
				graph.updateVertexState(false, queryArr[1]);
				break;
			case "vertexup":
				if (queryArr.length != 2) {
					System.err.println("Please insert correct query");
					continue;
				}
				graph.updateVertexState(true, queryArr[1]);
				break;
			case "path":
				if (queryArr.length != 3) {
					System.err.println("Please insert correct query");
					continue;
				}
				String path = graph.getShortestPath(queryArr[1], queryArr[2]);
				if (path != null)
					System.out.println(path);
				break;
			case "print":
				graph.printGraph();
				break;
			case "reachable":
				graph.printReachableVertices();
				break;
			case "quit":
				System.out.println("GoodBye....");
				System.exit(-1);
				break;
			default:
				System.out.println("\"" + queryArr[0] + "\"" + " query not found..!!");
				break;

			}
		}
	}

	/**
	 * Generates a graph from the input source file.
	 * 
	 * @param args
	 * @param graph
	 */
	private static void generateGraph(String[] args, Graph graph) {
		FileReader fin = null;
		Scanner graphFile = null;
		try {
			fin = new FileReader(args[1]);
			graphFile = new Scanner(fin);

			String line;
			while (graphFile.hasNextLine()) {
				line = graphFile.nextLine();
				StringTokenizer st = new StringTokenizer(line);

				try {
					if (st.countTokens() != 3) {
						System.err.println("Skipping ill-formatted line " + line);
						continue;
					}
					String source = st.nextToken();
					String dest = st.nextToken();
					float trasmitTime = Float.parseFloat(st.nextToken());
					graph.addEdge(source, dest, trasmitTime, true);
				} catch (NumberFormatException e) {
					System.err.println("Skipping ill-formatted line " + line);
				}
			}
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			graphFile.close();
		}
	}
}
