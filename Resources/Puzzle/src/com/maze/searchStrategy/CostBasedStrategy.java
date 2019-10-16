package com.maze.searchStrategy;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.org.mazebuilder.Node;

public class CostBasedStrategy implements ISearchStrategy {

	private PriorityQueue<Node> queue;
	
	public CostBasedStrategy(Comparator<Node> comparator) {
		queue = new PriorityQueue<>(comparator);
	} 
	
	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public Node removeNode() {
		return queue.remove();
	}

	@Override
	public void addNode(Node node) {
		queue.add(node);
	}
}
