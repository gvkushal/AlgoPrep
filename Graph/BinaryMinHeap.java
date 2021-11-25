package com.algo.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Kushal Guduru Venkateshgupta (801209704)
 * 
 *         Min Heap Implementation using binary heap. Time Complexity for all
 *         the insert, delete, decrease key is O(log n)
 *
 */
public class BinaryMinHeap {

	private List<Node> nodes;
	private Map<String, Integer> positionMap;

	BinaryMinHeap() {
		this.nodes = new ArrayList<>();
		this.positionMap = new HashMap<>();
	}

	/**
	 * Class to store vertex information to store in a heap
	 */
	class Node {
		String name;
		Float val;

		Node(String v, Float d) {
			this.name = v;
			this.val = d;
		}
	}

	/**
	 * Adds a vertex information into a min heap
	 * 
	 * @param name
	 * @param val
	 */
	public void add(String name, Float val) {
		Node newNode = new Node(name, val);
		this.nodes.add(newNode);
		int current = nodes.size() - 1;
		positionMap.put(name, current);
		shiftUp(current);

	}

	/**
	 * Removes the top element from the heap queue
	 * 
	 * @return
	 */
	public Node poll() {
		if (nodes.isEmpty()) {
			return null;
		}
		Node res = new Node(nodes.get(0).name, nodes.get(0).val);
		int size = nodes.size();
		if (size == 1) {
			nodes.remove(size - 1);
			positionMap.remove(res.name);
			return res;
		}
		String lastIndexed = nodes.get(size - 1).name;
		nodes.get(0).name = lastIndexed;
		nodes.get(0).val = nodes.get(size - 1).val;
		nodes.remove(size - 1);
		positionMap.remove(res.name);
		positionMap.remove(lastIndexed);
		positionMap.put(lastIndexed, 0);

		int cur = 0;
		while (true) {
			int left = 2 * cur + 1;
			int right = 2 * cur + 2;
			if (left > size - 2) {
				break;
			}
			if (right > size - 2) {
				right = left;
			}
			int choosenIndex = (nodes.get(left).val <= nodes.get(right).val) ? left : right;
			if (nodes.get(cur).val > nodes.get(choosenIndex).val) {
				swap(cur, choosenIndex);
				updatePositionMap(nodes.get(cur).name, cur, nodes.get(choosenIndex).name, choosenIndex);
				cur = choosenIndex;
			} else {
				break;
			}
		}
		return res;
	}

	/**
	 * Decreases the key value of a node in a heap
	 * 
	 * @param name
	 * @param val
	 */
	public void decreaseKey(String name, Float val) {
		int pos = positionMap.get(name);
		if (val > nodes.get(pos).val)
			return;
		nodes.get(pos).val = val;
		shiftUp(pos);
	}

	/**
	 * Method to check a node present in the heap queue
	 * 
	 * @param name
	 * @return
	 */
	public boolean contains(String name) {
		return this.positionMap.containsKey(name);
	}

	/**
	 * Method to check heap is empty or not
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.nodes.isEmpty();
	}

	/**
	 * Shifts up the current node respective to heap rules
	 * 
	 * @param current
	 */
	private void shiftUp(int current) {
		int parent = (current - 1) / 2;
		while (parent >= 0) {
			if (nodes.get(current).val < nodes.get(parent).val) {
				swap(current, parent);
				updatePositionMap(nodes.get(current).name, current, nodes.get(parent).name, parent);
				current = parent;
				parent = (current - 1) / 2;
			} else {
				break;
			}
		}
	}

	/**
	 * Helper function to update position maps
	 * 
	 * @param curName
	 * @param current
	 * @param parentName
	 * @param parent
	 */
	private void updatePositionMap(String curName, int current, String parentName, int parent) {
		positionMap.remove(curName);
		positionMap.remove(parentName);
		positionMap.put(curName, current);
		positionMap.put(parentName, parent);
	}

	/**
	 * Helper function to swap two vertices in a heap
	 * 
	 * @param current
	 * @param parent
	 */
	private void swap(int current, int parent) {
		String tempName = nodes.get(current).name;
		float tempDist = nodes.get(current).val;
		nodes.get(current).name = nodes.get(parent).name;
		nodes.get(current).val = nodes.get(parent).val;
		nodes.get(parent).name = tempName;
		nodes.get(parent).val = tempDist;
	}
}
