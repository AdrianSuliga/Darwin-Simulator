package projekt.model;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

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

    private Label animalStatLabel;

    private GridPane mainGrid;
    private final Simulation simulation;
    private Thread simulationThread;

    private int cell_width = 20;
    private int cell_height = 20;
    private int maxX = -1;
    private int maxY = -1;
    private HashMap<Vector2d, HashSet<Animal>> animalMap;
    private HashMap<Vector2d, Plant> plantList;

    private boolean running=true;

    private String mapStats;

    private Statistics statistics;

    public SimulationWindow(Simulation simulation) {
        this.stage = new Stage();

        // guziki
        Button stopButton = new Button("Pause");
        Button continueButton = new Button("Continue");

        stopButton.setOnAction(e -> running = false);

        continueButton.setOnAction(e -> {
            synchronized (this){
                running=true;
                Platform.runLater(()->{animalStatLabel.setText("");});
                notifyAll();
            }

        });


        HBox buttons = new HBox(stopButton,continueButton);

        //staty
        statLabel = new Label("Statistics");
        animalStatLabel = new Label();
        VBox statLayout = new VBox(10,statLabel,animalStatLabel);
        statLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        //mapa

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
        //zawiera wszystko
        BorderPane pane = new BorderPane();
        pane.setTop(buttons);
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

        synchronized (map) {
            map.updateStatistics();
            mapStats=map.getStatistics().toString();
            this.animalMap = new HashMap<>(map.getAnimalMap());
            this.plantList = new HashMap<>(map.getPlantList());
        }
        this.doWork();
    }

    public void doWork(){
        try {
            Platform.runLater(() -> {
            statLabel.setText(mapStats);
            this.drawMap();
            });

            synchronized (this) {
                while (!running) {
                    wait(); // Pause the thread
                }
            }
            // Update the UI and draw the map when running

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.out.println("Thread interrupted!");
        }
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
                label.setStyle("-fx-border-color: black; -fx-border-width: 1;");
                if(this.simulation.isPositionInEquator(j) && !running){
                    label.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: green;");
                }
                label.setAlignment(Pos.CENTER);
                label.setPrefSize(cell_width, cell_height);
                label.setOnMouseClicked(e->{
                    if(this.animalMap.containsKey(pos) && !this.animalMap.get(pos).isEmpty() && !running){
                        String animalStats = this.simulation.getStrongest(this.animalMap.get(pos)).getStatistics();
                        Platform.runLater(()->{animalStatLabel.setText(animalStats);});
                    }
                });
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
