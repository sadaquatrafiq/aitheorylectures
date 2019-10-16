package com.ai.searchProblem.sample.path.gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Block extends StackPane {

	public Block(int width, int height, Color color, boolean expanded, boolean path, boolean player, boolean goal)
	{
		super();
		
		Rectangle rectangle = new Rectangle(width,height);
		rectangle.setFill(color);
		getChildren().add(rectangle);
		
		if(player)
		{
			Rectangle playerBox = new Rectangle(width * 0.6,height * 0.6);
			playerBox.setFill(Color.GREEN);
			getChildren().add(playerBox);
		}
		else if(goal)
		{
			Rectangle playerBox = new Rectangle(width * 0.6,height * 0.6);
			playerBox.setFill(Color.RED);
			getChildren().add(playerBox);
		}
		else if(expanded)
		{
			Circle circle = new Circle(width*0.3,Color.WHITE);
			getChildren().add(circle);
		}
		else if(path)
		{
			Circle circle = new Circle(width*0.3,Color.YELLOW);
			getChildren().add(circle);
		}
		
	}
}
