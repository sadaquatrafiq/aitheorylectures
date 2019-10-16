package com.ai.searchProblem.sample.path;

import com.ai.searchProblem.elements.Heuristic;
import com.ai.searchProblem.elements.State;

public class EuclideanDistance implements Heuristic{

	private Location goalLocation;
	
	public EuclideanDistance()
	{
		this.goalLocation = new Location(0,0);
	}
	
	public EuclideanDistance(Location goalLocation)
	{
		this.goalLocation = goalLocation;
	}
	
	
	
	public Location getGoalLocation() {
		return goalLocation;
	}

	public void setGoalLocation(Location goalLocation) {
		this.goalLocation = goalLocation;
	}

	@Override
	public Double evaluateState(State s) {
		PathState ps = (PathState)s;
		double ds = Math.pow(Math.pow( (goalLocation.getRow() - ps.getLocation().getRow()),2 ) + Math.pow( (goalLocation.getColumn() - ps.getLocation().getColumn()),2 ),0.5);
		return ds;
	}

}
