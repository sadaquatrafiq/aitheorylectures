package com.ai.searchProblem.elements;

import java.util.List;

public interface SearchProblem {

	public State getInitialState();
	public List<State> generateSuccessors(State s);
	public Boolean isGoal(State s);
	public boolean equals(Object s);
	public void setHeuristicFunction(Heuristic heuristic);
}
