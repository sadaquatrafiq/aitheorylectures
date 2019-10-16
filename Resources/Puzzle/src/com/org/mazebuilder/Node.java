package com.org.mazebuilder;
public class Node{

	private Node parent;
	private Double pathCost;	
	private State state;
	private Double nodeValue;
	
	
	
	public Node(State state)
	{
		this.state = state;
	}
	
	
	public Node(Node parent, State state, Double nodeValue) {
		super();
		this.parent = parent;
		this.state = state;
		this.nodeValue = nodeValue;
	}




	public Node getParent() {
		return parent;
	}



	public void setParent(Node parent) {
		this.parent = parent;
	}



	public Double getPathCost() {
		return pathCost;
	}



	public void setPathCost(Double pathCost) {
		this.pathCost = pathCost;
	}



	public State getState() {
		return state;
	}



	public void setState(State state) {
		this.state = state;
	}



	public Double getNodeValue() {
		return nodeValue;
	}



	public void setNodeValue(Double nodeValue) {
		this.nodeValue = nodeValue;
	}

	@Override
	public String toString()
	{
		return this.getState().toString();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((state == null) ? 0 : state.hashCode());
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
		Node other = (Node) obj;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		return true;
	}
	
	
}