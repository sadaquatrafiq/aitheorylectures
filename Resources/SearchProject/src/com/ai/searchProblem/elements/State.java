package com.ai.searchProblem.elements;

public interface State {

	public void setHeuristicValue(Double d);
	public Double getHeuristicValue();
	public Double getActionCost();
	public void setActionCost(Double d);
	public String getAction();
	public String toString();
	public int hashCode();
}
