package com.algo.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Kushal Guduru Venkateshgupta (801209704)
 * 
 *         Graph class to construct and perform all graph operations
 */
class Graph {
	/**
	 * Edge class to store edge information in a graph.
	 **/
	class Edge {
		public Vertex sourceRouter;
		public Vertex destinationRouter;
		public float transmitTime;
		public boolean isActive;

		Edge(Vertex s, Vertex d, float t) {
			this.sourceRouter = s;
			this.destinationRouter = d;
			this.transmitTime = t;
			this.isActive = true;
		}
	}

	/**
	 * Vertex class to store vertex information in a graph.
	 **/
	class Vertex {
		public String name;
		public boolean isActive;
		public float distance;
		public Vertex prev;
		public List<Edge> edges;

		Vertex(String n) {
			this.name = n;
			this.isActive = true;
			this.edges = new ArrayList<>();
			reset();
		}

		public void reset() {
			this.distance = Float.POSITIVE_INFINITY;
			this.prev = null;
		}
	}

	private Map<String, Vertex> vertexMap = new HashMap<>();
	private Map<String, Edge> edgeMap = new HashMap<>();

	/**
	 * Adds edge to a graph.
	 * 
	 * @param sourceName
	 * @param destinationName
	 * @param transmitTime
	 * @param isBiDirectional
	 *            - decides which edge to add
	 */
	public void addEdge(String sourceName, String destinationName, Float transmitTime, boolean isBiDirectional) {
		Vertex source = getVertex(sourceName);
		Vertex destination = getVertex(destinationName);

		if (!isBiDirectional) {
			if (isEdgeExist(source, destination))
				this.edgeMap.get(sourceName + "_" + destinationName).transmitTime = transmitTime;
			else {
				Edge forward = new Edge(source, destination, transmitTime);
				source.edges.add(forward);
				edgeMap.put(sourceName + "_" + destinationName, forward);
			}
			return;
		}

		Edge forwardEdge = new Edge(source, destination, transmitTime);
		source.edges.add(forwardEdge);
		edgeMap.put(sourceName + "_" + destinationName, forwardEdge);

		Edge backwardEdge = new Edge(destination, source, transmitTime);
		destination.edges.add(backwardEdge);
		edgeMap.put(destinationName + "_" + sourceName, backwardEdge);
	}

	/**
	 * Deletes an edge between source and destination nodes
	 * 
	 * @param srcName
	 * @param destName
	 */
	public void deleteEdge(String srcName, String destName) {
		Vertex source = vertexMap.get(srcName);
		Vertex destination = vertexMap.get(destName);
		if (source == null || destination == null) {
			System.out.println("Either " + srcName + " or " + destName + " not exist");
			return;
		}
		if (!edgeMap.containsKey(srcName + "_" + destName)) {
			System.out.println("Edge " + srcName + " -> " + destName + " not exist, Unable to delete an edge");
			return;
		}
		Iterator<Edge> itr = source.edges.iterator();
		while (itr.hasNext()) {
			if (itr.next().destinationRouter.name.equals(destName)) {
				itr.remove();
				this.edgeMap.remove(srcName + "_" + destName);
				System.out.println("Edge " + srcName + "->" + destName + " deleted successfully..!!");
				break;
			}
		}
	}

	/**
	 * Changes the active status of the an edge between source and destination
	 * 
	 * @param activeStatus
	 * @param srcName
	 * @param destName
	 */
	public void updateExistingEdgeState(boolean activeStatus, String srcName, String destName) {
		if (this.edgeMap.containsKey(srcName + "_" + destName)) {
			this.edgeMap.get(srcName + "_" + destName).isActive = activeStatus;
			System.out.println("Edge " + srcName + "->" + destName + " updated successfully..!!");
		} else
			System.out.println("No Edge exist between " + srcName + " and " + destName);

	}

	/**
	 * Changes the status of a vertex in a graph
	 * 
	 * @param activeStatus
	 * @param vertex
	 */
	public void updateVertexState(boolean activeStatus, String vertex) {
		if (vertexMap.get(vertex) == null) {
			System.out.println("Vertex: " + vertex + " does not exist, Cannot update the status");
			return;
		}
		vertexMap.get(vertex).isActive = activeStatus;
		System.out.println("Vertex " + vertex + " updated successfully..!!");
	}

	/**
	 * Gets shortest path from source to destination vertex.
	 * 
	 * @param sourceRouterName
	 * @param destinationRouterName
	 * @return
	 */
	public String getShortestPath(String sourceRouterName, String destinationRouterName) {
		Vertex sourceRouter = vertexMap.get(sourceRouterName);
		Vertex destinationRouter = vertexMap.get(destinationRouterName);
		clearAll();
		if (sourceRouter == null || destinationRouter == null) {
			System.out.println(sourceRouterName + " or " + destinationRouterName + " Vertex is not avilable");
			return null;
		}
		if (!sourceRouter.isActive || !destinationRouter.isActive) {
			System.out.println(sourceRouterName + " or " + destinationRouterName + " Vertex is not  active");
			return null;
		}
		BinaryMinHeap heap = new BinaryMinHeap();
		sourceRouter.distance = 0;
		heap.add(sourceRouter.name, sourceRouter.distance);
		while (!heap.isEmpty()) {
			BinaryMinHeap.Node min = heap.poll();
			Vertex router = vertexMap.get(min.name);
			for (Edge e : router.edges) {

				Vertex adjRouter = e.destinationRouter;

				// skipping if the edge or router is down
				if (!e.isActive || !adjRouter.isActive)
					continue;

				if (Float.isInfinite(adjRouter.distance)) {
					adjRouter.distance = router.distance + e.transmitTime;
					adjRouter.prev = router;
					heap.add(adjRouter.name, adjRouter.distance);
					continue;
				}

				if (!heap.contains(adjRouter.name))
					continue;

				float curTransmitTime = router.distance + e.transmitTime;
				if (curTransmitTime < adjRouter.distance) {
					adjRouter.distance = curTransmitTime;
					heap.decreaseKey(adjRouter.name, curTransmitTime);
					adjRouter.prev = router;
				}
			}
		}
		if (Float.isInfinite(vertexMap.get(destinationRouterName).distance)) {
			System.out.println(destinationRouterName + " is unreachable..!!");
			return null;
		}
		return getPath(destinationRouter, new StringBuilder()).toString() + destinationRouter.distance;
	}

	/**
	 * Helper function to generate a path string from the source to destination
	 * 
	 * @param router
	 * @param path
	 * @return
	 */
	private StringBuilder getPath(Vertex router, StringBuilder path) {
		if (router.prev != null) {
			getPath(router.prev, path);
		}
		return path.append(router.name + " ");
	}

	/**
	 * Prints all the reachable and active routers/vertices for all the existing
	 * vertices in a graph
	 * 
	 * Algorithm: In the reachable algorithm we perform DFS on every vertex to
	 * know which all the vertices can be reached from it. Here we will use
	 * HashSet as a Visited array, because at every edge we should check weather
	 * that node is visited. We can do the search in O(1) with HashSet. Once we
	 * get reachable set for a vertex we will put them into TreeMap to sort the
	 * vertices and print them for that vertex.
	 * 
	 * Time Complexity - O (V * (|V| + |E|)
	 */
	public void printReachableVertices() {
		TreeMap<String, Vertex> sortedVertexMap = new TreeMap<>(vertexMap);
		for (Map.Entry<String, Vertex> entry : sortedVertexMap.entrySet()) {
			System.out.println(entry.getKey());
			Set<String> set = new HashSet<>();
			dfs(entry.getValue(), set);
			set.remove(entry.getKey());
			TreeSet<String> sorted = new TreeSet<>(set);
			for (String s : sorted) {
				System.out.println("\t" + s);
			}
			System.out.println();
		}
	}

	/**
	 * Helper function to get the reachable and active routers/vertices for a
	 * single vertex.
	 * 
	 * @param vertex
	 * @param visited
	 */
	private void dfs(Vertex vertex, Set<String> visited) {

		visited.add(vertex.name);

		for (Edge e : vertex.edges) {
			if (visited.contains(e.destinationRouter.name))
				continue;
			if (!e.isActive || !e.destinationRouter.isActive)
				continue;
			dfs(e.destinationRouter, visited);
		}

	}

	/**
	 * Prints the graph current states about the routers/vertices and edges.
	 */
	public void printGraph() {
		TreeMap<String, Vertex> sortedVertexMap = new TreeMap<>(vertexMap);
		for (Map.Entry<String, Vertex> entry : sortedVertexMap.entrySet()) {
			List<Edge> edgesList = entry.getValue().edges;
			Collections.sort(edgesList, new Comparator<Edge>() {
				@Override
				public int compare(Edge e1, Edge e2) {
					return e1.destinationRouter.name.compareTo(e2.destinationRouter.name);
				}
			});
			System.out.println(entry.getKey().toString() + " " + (!entry.getValue().isActive ? "DOWN" : " "));
			for (Edge edge : edgesList) {
				System.out.println("\t" + edge.destinationRouter.name + " " + edge.transmitTime + " "
						+ (!edge.isActive ? "DOWN" : " "));
			}
			System.out.println();

		}

	}

	/**
	 * Helper function to know edge between source and destination vertex is
	 * present in the graph
	 * 
	 * @param source
	 * @param destination
	 * @return
	 */
	private boolean isEdgeExist(Vertex source, Vertex destination) {
		return this.edgeMap.containsKey(source.name + "_" + destination.name);
	}

	/**
	 * Helper function to get the new or existing vertex in the graph
	 * 
	 * @param name
	 * @return
	 */
	private Vertex getVertex(String name) {
		Vertex existing = vertexMap.get(name);
		if (existing == null) {
			existing = new Vertex(name);
			vertexMap.put(name, existing);
		}
		return existing;
	}

	/**
	 * Helper function to clear all the saved information for all the existing
	 * vertices in the current graph
	 */
	private void clearAll() {
		for (Vertex v : vertexMap.values())
			v.reset();
	}

}
