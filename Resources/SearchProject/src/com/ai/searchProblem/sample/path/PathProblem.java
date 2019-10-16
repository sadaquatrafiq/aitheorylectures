package com.ai.searchProblem.sample.path;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;



import com.ai.searchProblem.elements.Heuristic;
import com.ai.searchProblem.elements.SearchProblem;
import com.ai.searchProblem.elements.State;
import com.ai.searchProblem.sample.path.utils.Constant;




public class PathProblem implements SearchProblem {

	
	private int maze[][];
	private Location playerLocation;
	private Location goalLocation;
	
	private Integer mazeRows = 0;
	private Integer mazeColumns = 0;
	

	
	private Double emptyShallowCost;
	private Double emptyDeepCost;
	
	private Heuristic heuristic;
	
	
	
	public PathProblem(String fileName, Double shallowCost, Double deepCost, Heuristic heuristic) throws IOException
	{
		emptyDeepCost = deepCost;
		emptyShallowCost = shallowCost;
		playerLocation = new Location();
		goalLocation =  new Location();
		setUpMaze(fileName);
		setHeuristicFunction(heuristic);
	}
	
	private void setUpMaze(String fileName) throws IOException
	{
		List<List<Integer>> maz = new ArrayList<>();
		Path path = FileSystems.getDefault().getPath(fileName);

		Files.lines(path).forEach(x -> {
			maz.add(new ArrayList<>());
			for (int i = 0; i < x.length(); i++) {
				if (x.charAt(i) == '%') {
					maz.get(maz.size() - 1).add(Constant.WALL);
				} else if (x.charAt(i) == ' ') {
					maz.get(maz.size() - 1).add(Constant.EMPTY_SHALLOW);
				} else if (x.charAt(i) == 'd') {
					maz.get(maz.size() - 1).add(Constant.EMPTY_DEEP);
				}else if (x.charAt(i) == 'P') {
					maz.get(maz.size() - 1).add(Constant.PLAYER);
					playerLocation.setRow( maz.size() - 1);
					playerLocation.setColumn(i);
				} else if (x.charAt(i) == '.') {
					maz.get(maz.size() - 1).add(Constant.GOAL);
					goalLocation.setRow( maz.size() - 1);
					goalLocation.setColumn(i);
				}
			}
		});


		mazeRows = maz.size();
		mazeColumns = maz.get(0).size();
		
		maze = new int[mazeRows][mazeColumns];

		for (int i = 0; i < mazeRows; i++) {
			for (int j = 0; j < mazeColumns; j++) {

				maze[i][j] = maz.get(i).get(j);
			}
		}
	}
	
	
	@Override
	public State getInitialState() {
		PathState initialState = new PathState(playerLocation);
		initialState.setActionCost(0.0);
		initialState.setHeuristicValue(getHeuristicValue(initialState));		
		return initialState;
	}

	@Override
	public List<State> generateSuccessors(State s) {
		List<State> returnStates = new ArrayList<>();
		
		PathState currentState = (PathState)s;
		
		//check east move
		if(currentState.getLocation().getColumn() + 1 < mazeColumns && maze[currentState.getLocation().getRow()][currentState.getLocation().getColumn() + 1] != Constant.WALL)
		{
			returnStates.add(createState(currentState.getLocation().getRow(), currentState.getLocation().getColumn() + 1, "East"));
		}
				
		//check west move
		if(currentState.getLocation().getColumn() -1 >= 0 && maze[currentState.getLocation().getRow()][currentState.getLocation().getColumn() - 1] != Constant.WALL)
		{
			returnStates.add(createState(currentState.getLocation().getRow(), currentState.getLocation().getColumn() - 1, "West"));
		}

		
		//check north move
		if(currentState.getLocation().getRow() - 1 >= 0 && maze[currentState.getLocation().getRow() - 1][currentState.getLocation().getColumn()] != Constant.WALL)
		{
			returnStates.add(createState(currentState.getLocation().getRow() - 1, currentState.getLocation().getColumn(), "North"));
		}

		
		//check south move
		if(currentState.getLocation().getRow() + 1 < mazeRows && maze[currentState.getLocation().getRow() + 1][currentState.getLocation().getColumn()] != Constant.WALL)
		{
			returnStates.add(createState(currentState.getLocation().getRow() + 1, currentState.getLocation().getColumn(), "South"));
		}

		return returnStates;
	}
	
	private PathState createState(Integer row, Integer column,String action)
	{
		Double actionCost = maze[row][column] == Constant.EMPTY_DEEP ? emptyDeepCost : emptyShallowCost;
		PathState ps = new PathState(new Location(row,column));
		ps.setHeuristicValue(getHeuristicValue(ps));
		ps.setActionCost(actionCost);
		ps.setAction("East");
		return ps;
	}
	
	private Double getHeuristicValue(PathState s)
	{
		return heuristic == null ? 0.0 : heuristic.evaluateState(s);
	}

	@Override
	public Boolean isGoal(State s) {		
		return goalLocation.equals(((PathState)s).getLocation());
	}

	@Override
	public void setHeuristicFunction(Heuristic heuristic) {
		this.heuristic = heuristic;
	}

	public int[][] getMaze() {
		return maze;
	}

	public void setMaze(int[][] maze) {
		this.maze = maze;
	}

	public Location getPlayerLocation() {
		return playerLocation;
	}

	public void setPlayerLocation(Location playerLocation) {
		this.playerLocation = playerLocation;
	}

	public Location getGoalLocation() {
		return goalLocation;
	}

	public void setGoalLocation(Location goalLocation) {
		this.goalLocation = goalLocation;
	}

	public Integer getMazeRows() {
		return mazeRows;
	}

	public void setMazeRows(Integer mazeRows) {
		this.mazeRows = mazeRows;
	}

	public Integer getMazeColumns() {
		return mazeColumns;
	}

	public void setMazeColumns(Integer mazeColumns) {
		this.mazeColumns = mazeColumns;
	}
	
	

}
