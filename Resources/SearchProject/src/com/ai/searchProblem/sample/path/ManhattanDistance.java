package com.ai.searchProblem.sample.path;

import com.ai.searchProblem.elements.Heuristic;
import com.ai.searchProblem.elements.State;

public class ManhattanDistance implements Heuristic{

	private Location goalLocation;
	
	public ManhattanDistance()
	{
		this.goalLocation = new Location(0,0);
	}
	
	public ManhattanDistance(Location goalLocation)
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
		double ds = Math.abs(goalLocation.getRow() - ps.getLocation().getRow() ) + Math.abs(goalLocation.getColumn() - ps.getLocation().getColumn() );
		return ds;
	}

}
