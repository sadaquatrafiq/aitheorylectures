package com.ai.searchProblem.strategy;

import com.ai.searchProblem.elements.Node;

public interface SearchStrategy {

	public void addNode(Node node);
	public Node removeNode();
	public Boolean isEmpty();
	
}
