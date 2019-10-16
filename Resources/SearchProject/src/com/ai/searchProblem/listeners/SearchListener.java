package com.ai.searchProblem.listeners;

import java.util.List;

import com.ai.searchProblem.elements.Node;
import com.ai.searchProblem.elements.State;

public interface SearchListener {
	public void fireChange(Node node, int nodesExpanded);
	public void goalFound(Node node);
	public void successorsGenerated(List<State> states);
}
