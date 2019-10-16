package com.ai.searchProblem.sample.path.gui;

import java.io.IOException;
import java.util.List;

import com.ai.searchProblem.elements.Heuristic;
import com.ai.searchProblem.elements.Node;
import com.ai.searchProblem.elements.State;
import com.ai.searchProblem.listeners.SearchListener;
import com.ai.searchProblem.sample.path.EuclideanDistance;
import com.ai.searchProblem.sample.path.ManhattanDistance;
import com.ai.searchProblem.sample.path.PathProblem;
import com.ai.searchProblem.sample.path.PathState;
import com.ai.searchProblem.sample.path.utils.Constant;
import com.ai.searchProblem.search.Search;
import com.ai.searchProblem.strategy.BestFirstSearchStrategy;
import com.ai.searchProblem.strategy.BreadthFirstSearchStrategy;
import com.ai.searchProblem.strategy.DepthFirstSearchStrategy;
import com.ai.searchProblem.strategy.SearchStrategy;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PathGUI extends Application implements SearchListener {

	private static final double SCREEN_WIDTH = 800;
	private static final double SCREEN_HEIGHT = 800;

	private ComboBox<String> mazeCombo;
	private ComboBox<String> searchCombo;
	private ComboBox<String> heuristicCombo;

	private TextField searchStatus = new TextField();
	private TextField deepNodeCost = new TextField("5");
	private TextField nodesExpanded = new TextField();
	private TextField pathLength = new TextField();
	private TextField pathCost = new TextField();
	private Slider slider = new Slider();

	private Button nextButton = new Button("Next");
	private Button startButton = new Button("Start");
	private Button pauseButton = new Button("Pause");
	private Button resetButton = new Button("Reset");

	private StackPane[][] gridSquare;

	private GridPane centerScreen;
	private PathProblem problem = null;
	private Heuristic heuristic = null;
	private SearchStrategy strategy = null;

	private GridPane root;

	private int width;
	private int height;

	@Override
	public void start(Stage primaryStage) throws Exception {

		root = new GridPane();

		root.add(createMenu(), 1, 0);
		root.add(bottomMenu(), 0, 1, 2, 1);

		centerScreen = centerScreen();

		root.add(centerScreen, 0, 0);

		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

	public static void main(String[] args) {

		new Thread(() -> {
			launch(args);
		}).start();

	}

	public GridPane centerScreen() {
		String fileName = "";

		if (mazeCombo.getSelectionModel().getSelectedItem().equals("TinyMaze"))
			fileName = "maze.txt";
		else if (mazeCombo.getSelectionModel().getSelectedItem().equals("MediumMaze"))
			fileName = "mediumMaze.txt";
		else if (mazeCombo.getSelectionModel().getSelectedItem().equals("BigMaze"))
			fileName = "BigMaze.txt";
		else if (mazeCombo.getSelectionModel().getSelectedItem().equals("ShallowMaze"))
			fileName = "testMaze.txt";

		try {
			problem = new PathProblem(fileName, 1.0, Double.valueOf(deepNodeCost.getText()), null);

		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		handleHeuristicComboChange();

		problem.setHeuristicFunction(heuristic);

		handleSearchComboChange();
		gridSquare = new StackPane[problem.getMazeRows()][problem.getMazeColumns()];

		width = (int)SCREEN_WIDTH / problem.getMazeColumns();
		height = (int)SCREEN_HEIGHT / problem.getMazeRows();

		GridPane pane = new GridPane();

		for (int i = 0; i < problem.getMazeRows(); i++) {
			for (int j = 0; j < problem.getMazeColumns(); j++) {
				if (problem.getMaze()[i][j] == Constant.WALL)
					gridSquare[i][j] = new Block(width, height, Color.BLACK, false, false, false, false);
				else if (problem.getMaze()[i][j] == Constant.EMPTY_SHALLOW)
					gridSquare[i][j] = new Block(width, height, Color.LIGHTBLUE, false, false, false, false);
				else if (problem.getMaze()[i][j] == Constant.EMPTY_DEEP)
					gridSquare[i][j] = new Block(width, height, Color.BLUE, false, false, false, false);
				else if (problem.getMaze()[i][j] == Constant.PLAYER)
					gridSquare[i][j] = new Block(width, height, Color.LIGHTBLUE, false, false, true, false);
				else if (problem.getMaze()[i][j] == Constant.GOAL)
					gridSquare[i][j] = new Block(width, height, Color.LIGHTBLUE, false, false, false, true);

				pane.add(gridSquare[i][j], j, i);
			}
		}

		return pane;
	}

	private GridPane createMenu() {
		GridPane sideMenu = new GridPane();
		Label mazeLabel = new Label("   Select Maze");
		mazeLabel.setFont(Font.font(16));

		int row = 0;

		ObservableList<String> options = FXCollections.observableArrayList("TinyMaze", "MediumMaze", "BigMaze",
				"ShallowMaze");

		ObservableList<String> searchOptions = FXCollections.observableArrayList("BFS", "DFS", "UCS", "Greedy",
				"A-Star");

		ObservableList<String> heuristicOptions = FXCollections.observableArrayList("Manhattan", "Euclidean");

		mazeCombo = new ComboBox<String>(options);
		mazeCombo.getSelectionModel().select(0);

		mazeCombo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				handleChange();

			}
		});

		sideMenu.add(mazeLabel, 0, row++, 2, 1);
		sideMenu.add(new Label("       "), 0, row);
		sideMenu.add(mazeCombo, 1, row);

		sideMenu.add(new Label("       "), 2, row);

		sideMenu.add(new Label("       "), 0, row++);
		sideMenu.add(new Label("       "), 0, row++);

		Label searchLabel = new Label("   Select Search");
		searchLabel.setFont(Font.font(16));

		searchCombo = new ComboBox<String>(searchOptions);
		searchCombo.getSelectionModel().select(0);

		searchCombo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				handleSearchComboChange();

			}
		});

		sideMenu.add(searchLabel, 0, row++, 2, 1);
		sideMenu.add(new Label("       "), 0, row);
		sideMenu.add(searchCombo, 1, row);

		sideMenu.add(new Label("       "), 0, row++);
		sideMenu.add(new Label("       "), 0, row++);

		Label heuristicLabel = new Label("   Select Heuristic");
		heuristicLabel.setFont(Font.font(16));

		heuristicCombo = new ComboBox<String>(heuristicOptions);
		heuristicCombo.getSelectionModel().select(0);

		heuristicCombo.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				handleHeuristicComboChange();

			}
		});

		sideMenu.add(heuristicLabel, 0, row++, 2, 1);
		sideMenu.add(new Label("       "), 0, row);
		sideMenu.add(heuristicCombo, 1, row);

		sideMenu.add(new Label("       "), 0, row++);
		sideMenu.add(new Label("       "), 0, row++);

		Label searchStatusLabel = new Label("   Search Status");
		searchStatusLabel.setFont(Font.font(16));

		sideMenu.add(searchStatusLabel, 0, row++, 2, 1);
		sideMenu.add(new Label("       "), 0, row);
		sideMenu.add(searchStatus, 1, row);

		sideMenu.add(new Label("       "), 0, row++);
		sideMenu.add(new Label("       "), 0, row++);

		Label deepStatusLabel = new Label("   Deep node cost");
		deepStatusLabel.setFont(Font.font(16));

		sideMenu.add(deepStatusLabel, 0, row++, 2, 1);
		sideMenu.add(new Label("       "), 0, row);
		sideMenu.add(deepNodeCost, 1, row);

		sideMenu.add(new Label("       "), 0, row++);
		sideMenu.add(new Label("       "), 0, row++);

		Label expandedStatusLabel = new Label("   Nodes Expanded");
		expandedStatusLabel.setFont(Font.font(16));

		sideMenu.add(expandedStatusLabel, 0, row++, 2, 1);
		sideMenu.add(new Label("       "), 0, row);
		sideMenu.add(nodesExpanded, 1, row);

		sideMenu.add(new Label("       "), 0, row++);
		sideMenu.add(new Label("       "), 0, row++);

		Label pathStatusLabel = new Label("   Path length");
		pathStatusLabel.setFont(Font.font(16));

		sideMenu.add(pathStatusLabel, 0, row++, 2, 1);
		sideMenu.add(new Label("       "), 0, row);
		sideMenu.add(pathLength, 1, row);

		sideMenu.add(new Label("       "), 0, row++);
		sideMenu.add(new Label("       "), 0, row++);
		
		Label pathCostStatusLabel = new Label("   Path Cost");
		pathCostStatusLabel.setFont(Font.font(16));

		sideMenu.add(pathCostStatusLabel, 0, row++, 2, 1);
		sideMenu.add(new Label("       "), 0, row);
		sideMenu.add(pathCost, 1, row);

		sideMenu.add(new Label("       "), 0, row++);
		sideMenu.add(new Label("       "), 0, row++);
		
		Label speedLabel = new Label("   Speed");
		speedLabel.setFont(Font.font(16));

		slider.setMin(1);
		slider.setMax(10);
		slider.setValue(4);
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);
		slider.setMajorTickUnit(1);
		slider.setMinorTickCount(1);
		slider.setBlockIncrement(1);
		
		sideMenu.add(speedLabel, 0, row++, 2, 1);
		sideMenu.add(new Label("       "), 0, row);
		sideMenu.add(slider, 1, row);

		sideMenu.add(new Label("       "), 0, row++);
		sideMenu.add(new Label("       "), 0, row++);

		return sideMenu;
	}

	private void handleHeuristicComboChange() {
		if (heuristicCombo.getSelectionModel().getSelectedItem().equalsIgnoreCase("Manhattan"))
			heuristic = new ManhattanDistance(problem.getGoalLocation());
		else
			heuristic = new EuclideanDistance(problem.getGoalLocation());
		
		problem.setHeuristicFunction(heuristic);
	}

	private void handleSearchComboChange() {
		if (searchCombo.getSelectionModel().getSelectedItem().equalsIgnoreCase("bfs"))
			strategy = new BreadthFirstSearchStrategy();
		else if (searchCombo.getSelectionModel().getSelectedItem().equalsIgnoreCase("dfs"))
			strategy = new DepthFirstSearchStrategy();
		else if (searchCombo.getSelectionModel().getSelectedItem().equalsIgnoreCase("ucs")) {

			strategy = new BestFirstSearchStrategy((x1, x2) -> {
				if (x1.getCost() > x2.getCost())
					return 1;
				else if (x1.getCost() < x2.getCost())
					return -1;
				return 0;
			});
		} else if (searchCombo.getSelectionModel().getSelectedItem().equalsIgnoreCase("greedy")) {

			strategy = new BestFirstSearchStrategy((x1, x2) -> {
				if (x1.getState().getHeuristicValue() > x2.getState().getHeuristicValue())
					return 1;
				else if (x1.getState().getHeuristicValue() < x2.getState().getHeuristicValue())
					return -1;
				return 0;
			});
		} else if (searchCombo.getSelectionModel().getSelectedItem().equalsIgnoreCase("a-star")) {

			strategy = new BestFirstSearchStrategy((x1, x2) -> {
				if ((x1.getCost() + x1.getState().getHeuristicValue()) > (x2.getCost()
						+ x2.getState().getHeuristicValue()))
					return 1;
				else if ((x1.getCost() + x1.getState().getHeuristicValue()) < (x2.getCost()
						+ x2.getState().getHeuristicValue()))
					return -1;
				return 0;
			});
		}

	}

	private void handleChange() {
		root.getChildren().remove(2);

		centerScreen = centerScreen();

		root.add(centerScreen(), 0, 0);
		
		searchStatus.setText("");
		nodesExpanded.setText("");
		pathLength.setText("");
		pathCost.setText("");

	}

	public GridPane bottomMenu() {
		GridPane pane = new GridPane();

		int col = 0;

		pane.add(new Text("  "), col++, 0);
		pane.add(nextButton, col++, 0);
		pane.add(new Text("  "), col++, 0);
		pane.add(startButton, col++, 0);
		pane.add(new Text("  "), col++, 0);
		pane.add(pauseButton, col++, 0);
		pane.add(new Text("  "), col++, 0);
		pane.add(resetButton, col++, 0);
		pane.add(new Text("  "), col++, 0);

		startButton.setOnAction(x -> {
			Search search = new Search(problem, strategy);
			search.addListener(this);
			new Thread(() -> {
				search.solve();
			}).start();
		});
		
		resetButton.setOnAction(x->{
			Platform.runLater(() -> {
				handleChange();
			});
		});

		return pane;
	}

	@Override
	public void fireChange(Node node, int nodesExpanded) {
		try {

			Platform.runLater(() -> {
				PathState ps = (PathState) node.getState();
				if (problem.getMaze()[ps.getLocation().getRow()][ps.getLocation().getColumn()] != Constant.GOAL
						&& problem.getMaze()[ps.getLocation().getRow()][ps.getLocation()
								.getColumn()] != Constant.PLAYER) {
					((GridPane) root.getChildren().get(2)).getChildren()
							.remove(gridSquare[ps.getLocation().getRow()][ps.getLocation().getColumn()]);
					Color col = problem.getMaze()[ps.getLocation().getRow()][ps.getLocation().getColumn()] == Constant.EMPTY_DEEP ? Color.BLUE : Color.LIGHTBLUE;
					gridSquare[ps.getLocation().getRow()][ps.getLocation().getColumn()] = new Block(width, height,
							col, true, false, false, false);
					//
					((GridPane) root.getChildren().get(2)).add(
							gridSquare[ps.getLocation().getRow()][ps.getLocation().getColumn()],
							ps.getLocation().getColumn(), ps.getLocation().getRow());
				}
				this.nodesExpanded.setText(String.valueOf(nodesExpanded));
				searchStatus.setText("Searching");
			});
			Thread.sleep(100/(long)slider.getValue());

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void goalFound(Node node) {
		searchStatus.setText("Goal Found");
		pathCost.setText(String.valueOf(node.getCost()));
		
		int count = 0;
		try {
			while (node != null) {
				final int i = count++;
				PathState ps = (PathState) node.getState();
				if (problem.getMaze()[ps.getLocation().getRow()][ps.getLocation().getColumn()] != Constant.GOAL
						&& problem.getMaze()[ps.getLocation().getRow()][ps.getLocation()
								.getColumn()] != Constant.PLAYER) {
					
					Platform.runLater(() -> {
						pathLength.setText(String.valueOf(i));
						((GridPane) root.getChildren().get(2)).getChildren()
								.remove(gridSquare[ps.getLocation().getRow()][ps.getLocation().getColumn()]);
						Color col = problem.getMaze()[ps.getLocation().getRow()][ps.getLocation().getColumn()] == Constant.EMPTY_DEEP ? Color.BLUE : Color.LIGHTBLUE;
						gridSquare[ps.getLocation().getRow()][ps.getLocation().getColumn()] = new Block(width, height,
								col, false, true, false, false);
						//
						((GridPane) root.getChildren().get(2)).add(
								gridSquare[ps.getLocation().getRow()][ps.getLocation().getColumn()],
								ps.getLocation().getColumn(), ps.getLocation().getRow());
					});
				}
				node = node.getParentNode();
				Thread.sleep(100/(long)slider.getValue());

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void successorsGenerated(List<State> states) {
		// TODO Auto-generated method stub

	}

}
