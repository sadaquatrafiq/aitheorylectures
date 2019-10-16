package com.org.mazebuilder;

public class State {
	private Integer x;
	private Integer y;
	private Double heuristicValue;
	
	
	public State(Integer x, Integer y) {
		super();
		this.x = x;
		this.y = y;
		heuristicValue = 0.0;
	}
	public Integer getX() {
		return x;
	}
	public void setX(Integer x) {
		this.x = x;
	}
	public Integer getY() {
		return y;
	}
	public void setY(Integer y) {
		this.y = y;
	}
	
	public Double getHeuristicValue() {
		return heuristicValue;
	}
	public void setHeuristicValue(Double heuristicValue) {
		this.heuristicValue = heuristicValue;
	}
	@Override
	public String toString()
	{
		return x+"_"+y;
	}
}
