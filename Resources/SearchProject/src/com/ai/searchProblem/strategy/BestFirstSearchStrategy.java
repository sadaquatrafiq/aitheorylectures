package com.ai.searchProblem.strategy;

import java.util.Comparator;
import java.util.PriorityQueue;

import com.ai.searchProblem.elements.Node;

public class BestFirstSearchStrategy implements SearchStrategy {

	private PriorityQueue<Node> queue;
	
	public BestFirstSearchStrategy(Comparator<Node> comparator)
	{
		queue = new PriorityQueue<>(comparator);
	}
	
	@Override
	public void addNode(Node node) {
		queue.add(node);
	}

	@Override
	public Node removeNode() {
		return queue.remove();
	}

	@Override
	public Boolean isEmpty() {
		return queue.isEmpty();
	}

}
