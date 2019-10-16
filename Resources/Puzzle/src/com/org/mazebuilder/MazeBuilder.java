package com.org.mazebuilder;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.maze.searchStrategy.BreadthFirstStrategy;
import com.maze.searchStrategy.CostBasedStrategy;
import com.maze.searchStrategy.DepthFirstStrategy;
import com.maze.searchStrategy.ISearchStrategy;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MazeBuilder extends Application {

	private static final Integer wall = -1;
	private static final Integer empty = 0;
	private static final Integer player = 1;
	private static final Integer goal = 2;

	private Button clearButton = new Button("Clear");
	private ComboBox<String> comboBox = new ComboBox<>();
	private Button dfsButton = new Button("Run Algorithm");
	private Label nodesExploredLabel = new Label("# of Nodes explored: ");
	private Label searchStatusLabel = new Label("Search Status");
	private Label pathLengthLabel = new Label("Path Length");
	private TextField nodesExploredField = new TextField();
	private TextField statusField = new TextField();
	private TextField pathLengthField = new TextField();

	public Integer playerX = 0;
	public Integer playerY = 0;
	public Integer goalX = 0;
	public Integer goalY = 0;
	private int[][] maze = new int[50][50];

	private Double boxHeight = 21.0;
	private Double boxWidth = 23.0;

	public Integer mazeRows;
	public Integer mazeColumns;

	private Map<String, String> duplicateMap = new HashMap<>();

	private static ObservableList<Rectangle> rectangles = FXCollections.observableArrayList();

	private MazeColor wallColor = new MazeColor(0.0, 0.0, 0.0, 1.0);
	private MazeColor emptyColor = new MazeColor(1.0, 1.0, 1.0, 1.0);
	private MazeColor playerColor = new MazeColor(0.0, 0.9, 0.0, 1.0);
	private MazeColor goalColor = new MazeColor(0.9, 0.0, 0.0, 0.85);
	private MazeColor searchColor = new MazeColor(0.25, 0.6, .8, .50);
	private MazeColor pathColor = new MazeColor(0.8, 0.6, .8, 1.0);

	IntegerProperty nodesCount = new SimpleIntegerProperty();
	IntegerProperty pathCount = new SimpleIntegerProperty();

	@Override
	public void start(Stage primaryStage) throws Exception {

		maze = getMaze();
		GridPane pane = buildMaze(maze);
		
		VBox vbox = new VBox(15.0);

		HBox hbox = new HBox(10.0);

		nodesCount.set(0);

		nodesExploredField.textProperty().bind(nodesCount.asString());
		pathLengthField.textProperty().bind(pathCount.asString());

		nodesExploredField.setMaxWidth(50);
		pathLengthField.setMaxWidth(50);
		
        comboBox.getItems().addAll(
            "DFS",
            "BFS",
            "UCS",
            "A-Star",
            "Greedy" 
        );
        comboBox.getSelectionModel().select(0);

		hbox.getChildren().addAll(clearButton, comboBox,dfsButton, nodesExploredLabel, nodesExploredField,
				searchStatusLabel, statusField, pathLengthLabel, pathLengthField);

		vbox.getChildren().addAll(hbox,getLegend());
		
		pane.add(vbox, 0, maze.length, maze[0].length - 1, 1);


		clearButton.setOnAction(x -> {
			nodesCount.set(0);
			pathCount.set(0);
			statusField.setText("");
			clearGrid();
		});

		dfsButton.setOnAction(x -> {
			statusField.setText("Searching .... ");
			new Thread(() -> {
				if(comboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("dfs"))
					findPath(new DepthFirstStrategy());
				else if(comboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("bfs"))
					findPath(new BreadthFirstStrategy());
				else if(comboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("ucs"))
					findPath(new CostBasedStrategy((x1, x2) -> {
						Double heuristic1 = x1.getNodeValue();
						Double heuristic2 = x2.getNodeValue();
						return heuristic1.compareTo(heuristic2);
					}));
				else if(comboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("a-star"))
					findPath(new CostBasedStrategy((x1, x2) -> {
						Double heuristic1 = x1.getNodeValue() + x1.getState().getHeuristicValue();
						Double heuristic2 = x2.getNodeValue() + x2.getState().getHeuristicValue();
						return heuristic1.compareTo(heuristic2);
					}));
				else if(comboBox.getSelectionModel().getSelectedItem().equalsIgnoreCase("greedy"))
					findPath(new CostBasedStrategy((x1, x2) -> {
						Double heuristic1 = x1.getState().getHeuristicValue();
						Double heuristic2 = x2.getState().getHeuristicValue();
						return heuristic1.compareTo(heuristic2);
					}));
 
			}).start();
		});

		primaryStage.setScene(new Scene(pane));
		primaryStage.show();
	}

	public HBox getLegend() {
		HBox hbox = new HBox();
		hbox.setSpacing(15.0);
		Rectangle wallRect = new Rectangle(15, 15);
		wallRect.setFill(new Color(wallColor.getR(), wallColor.getG(), wallColor.getB(), wallColor.getOpacity()));

		Rectangle playerRect = new Rectangle(15, 15);
		playerRect.setFill(
				new Color(playerColor.getR(), playerColor.getG(), playerColor.getB(), playerColor.getOpacity()));

		Rectangle emptyRect = new Rectangle(15, 15);
		emptyRect.setFill(new Color(emptyColor.getR(), emptyColor.getG(), emptyColor.getB(), emptyColor.getOpacity()));

		Rectangle goalRect = new Rectangle(15, 15);
		goalRect.setFill(new Color(goalColor.getR(), goalColor.getG(), goalColor.getB(), goalColor.getOpacity()));

		Rectangle searchRect = new Rectangle(15, 15);
		searchRect.setFill(
				new Color(searchColor.getR(), searchColor.getG(), searchColor.getB(), searchColor.getOpacity()));

		Rectangle pathRect = new Rectangle(15, 15);
		pathRect.setFill(new Color(pathColor.getR(), pathColor.getG(), pathColor.getB(), pathColor.getOpacity()));

		hbox.getChildren().addAll(new Label("Wall"), wallRect, new Label("Player"), playerRect, new Label("Empty"),
				emptyRect, new Label("Goal"), goalRect, new Label("Search"), searchRect, new Label("Path"), pathRect);

		return hbox;
	}

	public void clearGrid() {
		for (int i = 0; i < mazeRows; i++) {
			for (int j = 0; j < mazeColumns; j++) {
				try {

					Rectangle r = rectangles.get(i * mazeColumns + j);
					if (maze[i][j] == empty) {
						r.setFill(new javafx.scene.paint.Color(emptyColor.getR(), emptyColor.getG(), emptyColor.getB(),
								emptyColor.getOpacity()));
					}

				} catch (ArrayIndexOutOfBoundsException ae) {
					System.out.println(i + "   " + j);
					ae.printStackTrace();
				}
			}
		}
	}

	public void findPath(ISearchStrategy searchStrategy) {
		State startState = new State(playerX, playerY);
		Node node = new Node(startState);
		node.setNodeValue(0.0);
		boolean goalFound = false;
		searchStrategy.addNode(node);
		duplicateMap.clear();
		duplicateMap.put(node.toString(), node.toString());
		Node currNode = new Node(null);

		try {
			while (!searchStrategy.isEmpty()) {
				nodesCount.set(nodesCount.get() + 1);
				currNode = searchStrategy.removeNode();

				if (currNode.getState().getX().equals(goalX) && currNode.getState().getY().equals(goalY)) {
					statusField.setText("Goal Found");
					goalFound = true;
					break;
				}

				int rectangleNumber = (currNode.getState().getX()) * (mazeColumns) + currNode.getState().getY();

				rectangles.get(rectangleNumber).setFill(new Color(searchColor.getR(), searchColor.getG(),
						searchColor.getB(), searchColor.getOpacity()));

				// Check possible moves

				Node newNode = new Node(null);
				// Check north move
				if (maze[currNode.getState().getX() - 1][currNode.getState().getY()] != wall) {
					State state = new State(currNode.getState().getX() - 1, currNode.getState().getY());
					state.setHeuristicValue(getDistance(state.getX(), state.getY(), goalX, goalY));
					if (!duplicateMap.containsKey(state.toString())) {
						newNode = new Node(currNode, state, currNode.getNodeValue() + 1.0);
						searchStrategy.addNode(newNode);
						duplicateMap.put(newNode.toString(), newNode.toString());
					}
				}
				// Check south move
				if (maze[currNode.getState().getX() + 1][currNode.getState().getY()] != wall) {

					State state = new State(currNode.getState().getX() + 1, currNode.getState().getY());
					state.setHeuristicValue(getDistance(state.getX(), state.getY(), goalX, goalY));
					if (!duplicateMap.containsKey(state.toString())) {
						newNode = new Node(currNode, state, currNode.getNodeValue() + 1.0);

						searchStrategy.addNode(newNode);
						duplicateMap.put(newNode.toString(), newNode.toString());
					}
				}
				// Check east move
				if (maze[currNode.getState().getX()][currNode.getState().getY() - 1] != wall) {
					State state = new State(currNode.getState().getX(), currNode.getState().getY() - 1);
					state.setHeuristicValue(getDistance(state.getX(), state.getY(), goalX, goalY));
					if (!duplicateMap.containsKey(state.toString())) {
						newNode = new Node(currNode, state, currNode.getNodeValue() + 1.0);
						searchStrategy.addNode(newNode);
						duplicateMap.put(newNode.toString(), newNode.toString());
					}
				}
				// Check west move
				if (maze[currNode.getState().getX()][currNode.getState().getY() + 1] != wall) {
					State state = new State(currNode.getState().getX(), currNode.getState().getY() + 1);
					state.setHeuristicValue(getDistance(state.getX(), state.getY(), goalX, goalY));
					if (!duplicateMap.containsKey(state.toString())) {
						newNode = new Node(currNode, state, currNode.getNodeValue() + 1.0);
						searchStrategy.addNode(newNode);
						duplicateMap.put(newNode.toString(), newNode.toString());
					}
				}
				Thread.sleep(100);
			}

			if (goalFound)
				visitPath(currNode);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
	
	public Double getDistance(Integer x1, Integer y1, Integer x2, Integer y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public void visitPath(Node currNode) throws InterruptedException {
		Stack<Node> stack = new Stack<>();
		while (currNode != null) {
			stack.push(currNode);
			currNode = currNode.getParent();
		}

		while (!stack.isEmpty()) {
			pathCount.set(pathCount.get() + 1);
			currNode = stack.pop();

			int rectangleNumber = currNode.getState().getX() * (mazeColumns) + currNode.getState().getY();
			rectangles.get(rectangleNumber)
					.setFill(new Color(pathColor.getR(), pathColor.getG(), pathColor.getB(), pathColor.getOpacity()));

			currNode = currNode.getParent();

			Thread.sleep(100);
		}

	}

	public int[][] getMaze() throws IOException {

		int[][] m;
		List<List<Integer>> maz = new ArrayList<>();
		Path path = FileSystems.getDefault().getPath("mediumMaze.txt");

		Files.lines(path).forEach(x -> {
			maz.add(new ArrayList<>());
			for (int i = 0; i < x.length(); i++) {
				if (x.charAt(i) == '%') {
					maz.get(maz.size() - 1).add(wall);
				} else if (x.charAt(i) == ' ') {
					maz.get(maz.size() - 1).add(empty);
				} else if (x.charAt(i) == 'P') {
					maz.get(maz.size() - 1).add(player);
					playerX = maz.size() - 1;
					playerY = i;
				} else if (x.charAt(i) == '.') {
					maz.get(maz.size() - 1).add(goal);
					goalX = maz.size() - 1;
					goalY = i;
				}
			}
		});

		m = new int[maz.size()][maz.get(0).size()];

		mazeRows = m.length;
		mazeColumns = m[0].length;

		for (int i = 0; i < mazeRows; i++) {
			for (int j = 0; j < mazeColumns; j++) {

				m[i][j] = maz.get(i).get(j);
			}
		}
		return m;
	}

	public GridPane buildMaze(int[][] maze) {
		Integer rows = maze.length;
		Integer columns = maze[0].length;

		GridPane pane = new GridPane();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				try {

					Rectangle r = new Rectangle();
					r.setWidth(boxWidth);
					r.setHeight(boxHeight);
					if (maze[i][j] == wall) {
						r.setFill(new javafx.scene.paint.Color(wallColor.getR(), wallColor.getG(), wallColor.getB(),
								wallColor.getOpacity()));
					} else if (maze[i][j] == empty) {
						r.setFill(new javafx.scene.paint.Color(emptyColor.getR(), emptyColor.getG(), emptyColor.getB(),
								emptyColor.getOpacity()));
					} else if (maze[i][j] == player) {
						r.setFill(new javafx.scene.paint.Color(playerColor.getR(), playerColor.getG(),
								playerColor.getB(), playerColor.getOpacity()));
					} else if (maze[i][j] == goal) {
						r.setFill(new javafx.scene.paint.Color(goalColor.getR(), goalColor.getG(), goalColor.getB(),
								goalColor.getOpacity()));
					}
					rectangles.add(r);
					pane.add(r, j, i);

				} catch (ArrayIndexOutOfBoundsException ae) {
					System.out.println(i + "   " + j);
					ae.printStackTrace();
				}
			}
		}

		Rectangle r = new Rectangle();
		r.setWidth(15);
		r.setHeight(15);
		r.setFill(new javafx.scene.paint.Color(playerColor.getR(), playerColor.getG(), playerColor.getB(),
				playerColor.getOpacity()));

		Rectangle en = new Rectangle();
		en.setWidth(boxWidth);
		en.setHeight(boxHeight);
		en.setFill(new javafx.scene.paint.Color(emptyColor.getR(), emptyColor.getG(), emptyColor.getB(),
				emptyColor.getOpacity()));

		StackPane sp = new StackPane(en, r);
		pane.add(sp, playerY, playerX);

		Rectangle r1 = new Rectangle();
		r1.setWidth(15);
		r1.setHeight(15);
		r1.setFill(new javafx.scene.paint.Color(goalColor.getR(), goalColor.getG(), goalColor.getB(),
				goalColor.getOpacity()));

		Rectangle en1 = new Rectangle();
		en1.setWidth(boxWidth);
		en1.setHeight(boxHeight);
		en1.setFill(new javafx.scene.paint.Color(emptyColor.getR(), emptyColor.getG(), emptyColor.getB(),
				emptyColor.getOpacity()));

		StackPane sp1 = new StackPane(en1, r1);
		pane.add(sp1, goalY, goalX);

		return pane;
	}

	public static void main(String[] args) {

		launch(args);

	}

}
