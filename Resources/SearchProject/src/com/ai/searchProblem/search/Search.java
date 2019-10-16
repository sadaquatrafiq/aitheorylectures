package com.ai.searchProblem.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.searchProblem.elements.Node;
import com.ai.searchProblem.elements.SearchProblem;
import com.ai.searchProblem.elements.State;
import com.ai.searchProblem.listeners.SearchListener;
import com.ai.searchProblem.strategy.SearchStrategy;

public class Search {

	private SearchProblem searchProblem;
	private SearchStrategy  searchStrategy;
	private List<SearchListener> listeners;

	public Search(SearchProblem searchProblem, SearchStrategy searchStrategy)
	{
		this.searchProblem = searchProblem;
		this.searchStrategy = searchStrategy;
		listeners = new ArrayList<SearchListener>();
	}
	
	public void addListener(SearchListener sl)
	{
		listeners.add(sl);
	}
	
	public Node solve()
	{
		int nodesExpanded = 0;
		
		State initialState = searchProblem.getInitialState();
		Node rootNode = new Node(initialState,null,0,0.0,"Start");
		
		Map<String, Node> duplicateMap = new HashMap<String, Node>();
		
		duplicateMap.put(rootNode.toString(), rootNode);
		
		searchStrategy.addNode(rootNode);
		
		while(!searchStrategy.isEmpty())
		{
			Node currentNode = searchStrategy.removeNode();
			
			nodesExpanded++;
			
			fireListeners(currentNode,nodesExpanded);
			
			if(searchProblem.isGoal(currentNode.getState()))
			{
				fireGoalListeners(currentNode);
				return currentNode;
			}						
			
			//Generate successors
			
			List<State> successorList = searchProblem.generateSuccessors(currentNode.getState());
			
			fireSuccessorListener(successorList);
			
			successorList.forEach(state->{
				if(!duplicateMap.containsKey(state.toString()))
				{
					Node nextNode = new Node(state,currentNode,currentNode.getDepth() + 1, (currentNode.getCost() + state.getActionCost()),state.getAction());
					searchStrategy.addNode(nextNode);
					duplicateMap.put(nextNode.toString(), nextNode);
				}
			});
		}
		
		return null;
	}
	
	
	public void fireListeners(Node node, int nodesExpanded)
	{
		listeners.forEach(x->{
			x.fireChange(node,nodesExpanded);
		});
	}
	
	public void fireGoalListeners(Node node)
	{
		listeners.forEach(x->{
			x.goalFound(node);			
		});
	}
	
	public void fireSuccessorListener(List<State> states)
	{
		listeners.forEach(x->{
			x.successorsGenerated(states);			
		});
	}
}
