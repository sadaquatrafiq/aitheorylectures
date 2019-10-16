package com.maze.searchStrategy;

import java.util.ArrayList;
import java.util.List;

import com.org.mazebuilder.Node;

public class BreadthFirstStrategy implements ISearchStrategy {

	private List<Node> stack = new ArrayList<>();
	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public Node removeNode() {
		return stack.remove(0);
	}

	@Override
	public void addNode(Node node) {
		stack.add(node);
	}

}
