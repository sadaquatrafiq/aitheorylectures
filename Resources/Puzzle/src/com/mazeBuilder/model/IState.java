package com.mazeBuilder.model;

import java.util.List;


public interface IState {
	public void initialize();
	public List<IState> nextStates();
	public boolean isGoal();
	public Double getCost();
}
