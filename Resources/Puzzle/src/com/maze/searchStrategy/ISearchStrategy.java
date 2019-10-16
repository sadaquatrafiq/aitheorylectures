package com.maze.searchStrategy;

import com.org.mazebuilder.Node;

public interface ISearchStrategy {
	public boolean isEmpty();
	public Node removeNode();
	public void addNode(Node node);
}
