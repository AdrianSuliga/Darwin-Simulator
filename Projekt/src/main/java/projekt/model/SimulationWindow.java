package projekt.model;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import projekt.interfaces.MapChangeListener;
import projekt.util.AnimalComparator;
import projekt.util.Simulation;

import java.util.HashMap;
import java.util.HashSet;

public class SimulationWindow implements MapChangeListener {
    private final Stage stage;


    private Label statLabel;

    private GridPane mainGrid;
    private final Simulation simulation;
    private Thread simulationThread;

    private int cell_width = 20;
    private int cell_height = 20;
    private int maxX = -1;
    private int maxY = -1;
    private HashMap<Vector2d, HashSet<Animal>> animalMap;
    private HashMap<Vector2d, Plant> plantList;

    public SimulationWindow(Simulation simulation) {
        this.stage = new Stage();
        statLabel = new Label("Statistics");

        VBox statLayout = new VBox(10,statLabel);
        BorderPane pane = new BorderPane();
        statLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        pane.setLeft(statLayout);
        pane.setCenter(layout);
        
        this.mainGrid = new GridPane();
        mainGrid.setGridLinesVisible(true);
        mainGrid.setAlignment(Pos.CENTER);
        this.simulation = simulation;
        this.simulation.getWorldMap().registerObserver(this);
        this.maxX = simulation.getWorldMap().getWidth() - 1;
        this.maxY = simulation.getWorldMap().getHeight() - 1;
        this.cell_width = 500 / maxX;
        this.cell_height = 500 / maxY;
        VBox layout = new VBox(10, mainGrid);
        layout.setStyle("-fx-alignment: center;");
        pane.setLeft(statLayout);
        pane.setCenter(layout);

        Scene scene = new Scene(pane, 2 * maxX * cell_width, 2 * maxY * cell_height);
        stage.setScene(scene);
        stage.setTitle("Simulation");
        stage.setOnCloseRequest(windowEvent -> stopSimulationInNewWindow());
        stage.show();
        startSimulationInNewWindow();
    }

    public void startSimulationInNewWindow() {
        simulationThread = new Thread(this.simulation::run);
        simulationThread.start();
    }

    private void stopSimulationInNewWindow() {
        if (simulationThread != null) {
            this.simulation.getWorldMap().unregisterObserver(this);;
            simulationThread.interrupt();
            this.simulationThread = null;
        }
    }

    @Override
    public void mapChanged(WorldMap map) {
        String mapSnapshot;
        String mapStats;
        synchronized (map) {
            map.updateStatistics();
            mapStats=map.getStatistics().toString();
            this.animalMap = new HashMap<>(map.getAnimalMap());
            this.plantList = new HashMap<>(map.getPlantList());
        }
        Platform.runLater(()->{
          statLabel.setText(mapStats);
          this.drawMap();});
    }

    private void drawMap() {
        clearGrid();
        addXYLabel();
        addColumnHeader();
        addRowColumn();
        addMapElements();
    }

    private void addXYLabel() {
        mainGrid.getColumnConstraints().add(new ColumnConstraints(cell_height));
        mainGrid.getRowConstraints().add(new RowConstraints(cell_width));
        Label xyLabel = new Label("y\\x");
        xyLabel.setStyle("-fx-border-color: black;" +
                "-fx-border-width: 1");
        xyLabel.setAlignment(Pos.CENTER);
        xyLabel.setPrefSize(cell_width, cell_height);
        mainGrid.add(xyLabel, 0, 0);
        GridPane.setHalignment(xyLabel, HPos.CENTER);
    }

    private void addRowColumn() {
        for (int i = maxY + 1; i >= 1; i--) {
            Label label = new Label(String.valueOf(i - 1));
            label.setStyle("-fx-border-color: black;" +
                    "-fx-border-width: 1");
            label.setAlignment(Pos.CENTER);
            label.setPrefSize(cell_width, cell_height);
            mainGrid.getRowConstraints().add(new RowConstraints(cell_height));
            mainGrid.add(label, 0, maxY - i + 2);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    private void addColumnHeader() {
        for (int i = 1; i <= this.maxX + 1; i++) {
            Label label = new Label(String.valueOf(i - 1));
            label.setStyle("-fx-border-color: black;" +
                    "-fx-border-width: 1");
            label.setAlignment(Pos.CENTER);
            label.setPrefSize(cell_width, cell_height);
            mainGrid.getColumnConstraints().add(new ColumnConstraints(cell_width));
            mainGrid.add(label, i, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
    }

    private void addMapElements() {
        for (int i = 0; i <= maxX; i++) {
            for (int j = maxY; j >= 0; j--) {
                Vector2d pos = new Vector2d(i, j);
                Label label;
                if (this.animalMap.containsKey(pos) && !this.animalMap.get(pos).isEmpty()) {
                    label = new Label("X");
                } else if (this.plantList.containsKey(pos)) {
                    label = new Label(this.plantList.get(pos).toString());
                } else {
                    label = new Label(" ");
                }
                label.setStyle("-fx-border-color: black;" +
                        "-fx-border-width: 1");
                label.setAlignment(Pos.CENTER);
                label.setPrefSize(cell_width, cell_height);
                mainGrid.add(label, i + 1, maxY - j + 1);
                GridPane.setHalignment(label, HPos.CENTER);
            }
        }
    }

    private void clearGrid() {
        if (!mainGrid.getChildren().isEmpty()) {
            mainGrid.getChildren().clear();
        }
        mainGrid.getColumnConstraints().clear();
        mainGrid.getRowConstraints().clear();
    }
}
