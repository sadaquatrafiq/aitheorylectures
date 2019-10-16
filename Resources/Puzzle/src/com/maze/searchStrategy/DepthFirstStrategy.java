package com.maze.searchStrategy;

import java.util.Stack;

import com.org.mazebuilder.Node;

public class DepthFirstStrategy implements ISearchStrategy {

	private Stack<Node> stack = new Stack<>();
	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public Node removeNode() {
		return stack.pop();
	}

	@Override
	public void addNode(Node node) {
		stack.push(node);
	}

}
