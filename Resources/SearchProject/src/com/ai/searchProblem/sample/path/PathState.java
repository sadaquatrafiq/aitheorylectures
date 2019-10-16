package com.ai.searchProblem.sample.path;

import com.ai.searchProblem.elements.State;

public class PathState implements State{

	private Double heuristicValue;
	private Location location;
	private String action;
	private Double actionCost;
	
	public PathState(Location location)
	{
		this.location = location;
	}
	
	
	
	public PathState(Location location, Double heuristicValue,  String action, Double actionCost) {
		super();
		this.heuristicValue = heuristicValue;
		this.location = location;
		this.action = action;
		this.actionCost = actionCost;
	}



	@Override
	public void setHeuristicValue(Double d) {
		this.heuristicValue = d;
	}

	@Override
	public Double getHeuristicValue() {
		return this.heuristicValue;
	}

	@Override
	public Double getActionCost() {
		return actionCost;
	}
	

	@Override
	public void setActionCost(Double d) {
		this.actionCost = d;
	}

	@Override
	public String getAction() {
		return action;
	}		

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathState other = (PathState) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PathState [" + location + "]";
	}	

}
