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
import java.util.List;

public class SimulationWindow implements MapChangeListener {
    private final Stage stage;
    private final VBox statLayout;

    private final HBox trackedAnimalStatsLayout;
    private final Label animalCountLabel;
    private final Label freeSpaceLabel;
    private final Label plantCountLabel;
    private final VBox mapStatsLayout;
    private final Label topGenomesLabel;
    private final Label averageEnergyLabel;
    private final Label averageLifespanLabel;
    private final Label averageChildCountLabel;

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

    private boolean running = true;

    private Statistics statistics;

    private Animal trackedAnimal;

    public SimulationWindow(Simulation simulation) {
        this.stage = new Stage();

        // guziki
        Button stopButton = new Button("Pause");
        Button continueButton = new Button("Continue");

        stopButton.setOnAction(e -> running = false);

        continueButton.setOnAction(e -> {
            synchronized (this) {
                if (trackedAnimal == null) {
                    hideTrackedAnimalStats();
                }
                running=true;
                notifyAll();
            }
        });

        HBox buttons = new HBox(stopButton,continueButton);
        buttons.setStyle("-fx-maxheigth: 50px; -fx-alignment: center;");

        //staty
        statLabel = new Label("Statystyki");
        animalCountLabel = new Label("Ilosc zwierzat");
        plantCountLabel = new Label("Ilosc roslin");
        freeSpaceLabel = new Label("Ilosc wolnych pol");
        topGenomesLabel = new Label("Genomy");
        averageEnergyLabel = new Label("Srednia energia");
        averageLifespanLabel = new Label("Sredni zywot");
        averageChildCountLabel = new Label("Srednia dzietnosc");
        mapStatsLayout = new VBox(10,statLabel,animalCountLabel,
                plantCountLabel,freeSpaceLabel,topGenomesLabel,
                averageEnergyLabel,averageLifespanLabel,averageChildCountLabel);
        mapStatsLayout.setStyle(" -fx-padding: 10;-fx-border-color: black; -fx-border-width: 3px; -fx-border-radius: 10px; -fx-margin: 10px");

        animalStatLabel = new Label();
        trackedAnimalStatsLayout = new HBox(10,animalStatLabel);
        statLayout = new VBox(10,buttons,mapStatsLayout,trackedAnimalStatsLayout);
        statLayout.setStyle("-fx-padding: 20; -fx-alignment: center; ");

        //mapa
        this.mainGrid = new GridPane();
        mainGrid.setGridLinesVisible(true);
        mainGrid.setAlignment(Pos.CENTER);
        this.simulation = simulation;
        this.simulation.getWorldMap().registerObserver(this);
        this.maxX = simulation.getWorldMap().getWidth() - 1;
        this.maxY = simulation.getWorldMap().getHeight() - 1;
        this.cell_width = (int) Math.floor(500.0 / ((Math.max(this.maxX,this.maxY)+1)));
        this.cell_height = this.cell_width;
        VBox layout = new VBox(10, mainGrid);
        layout.setStyle("-fx-alignment: center;");

        //zawiera wszystko
        BorderPane pane = new BorderPane();
        pane.setLeft(statLayout);
        pane.setCenter(layout);

        Scene scene = new Scene(pane, (2 * maxX * cell_width)+200, (2 * maxY * cell_height)+200);
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
            statistics = map.getStatistics();
            this.animalMap = new HashMap<>(map.getAnimalMap());
            this.plantList = new HashMap<>(map.getPlantList());
        }
        this.doWork();
    }

    public void doWork(){
        try {
            Platform.runLater(() -> {
                this.drawStatistics();
                this.drawMap();
                this.drawTrackedAnimal();
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

    private void drawStatistics(){
        this.animalCountLabel.setText("Ilosc zwierzat: " + statistics.animalCount());
        this.plantCountLabel.setText("Ilosc roslin: " + statistics.plantCount());
        this.freeSpaceLabel.setText("Ilosc wolnych pol: " + statistics.freeSpacesCount());
        this.averageEnergyLabel.setText("Srednia energia: " + (double)Math.round(statistics.averageEnergy() * 100) / 100);
        this.averageLifespanLabel.setText("Sredni zywot: " + (double)Math.round(statistics.averageLifespan() * 100) / 100);
        this.averageChildCountLabel.setText("Srednia dzietnosc: " + (double)Math.round(statistics.averageChildCount() * 100) / 100);
        String popularGenomes = "Topowe genomy\n";
        for (int i = 0; i < statistics.popularGenes().size(); i++) {
            popularGenomes = popularGenomes + (i + 1) + ". " + statistics.popularGenes().get(i) + '\n';
        }
        this.topGenomesLabel.setText(popularGenomes);
    }
    private void drawTrackedAnimal() {
        if(trackedAnimal!=null) {
            this.animalStatLabel.setText(trackedAnimal.getStatistics());
            //trackedAnimalStatsLayout.setStyle("-fx-padding: 10;-fx-border-color: black; -fx-border-width: 3px; -fx-border-radius: 10px; -fx-margin: 10px");
        }
    }

    public void showTrackedAnimalStats() {
        trackedAnimalStatsLayout.setStyle("-fx-padding: 10;-fx-border-color: black; -fx-border-width: 3px; -fx-border-radius: 10px; -fx-margin: 10px");
    }

    public void hideTrackedAnimalStats() {
        animalStatLabel.setText("");
        trackedAnimalStatsLayout.setStyle("");
        trackedAnimalStatsLayout.getChildren().clear();
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
                if (this.animalMap.containsKey(pos) && this.animalMap.get(pos)!=null  && !this.animalMap.get(pos).isEmpty()) {
                    label = new Label("X");
                } else if (this.plantList.containsKey(pos)) {
                    label = new Label(this.plantList.get(pos).toString());
                } else {
                    label = new Label(" ");
                }

                if(trackedAnimal!=null) {
                    if (pos.equals(trackedAnimal.getPosition())) {
                        label.setText("#");
                    }
                }
                label.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-font-weight: bold; -fx-font-size: 24px;");
                if (!running) {
                    if (this.simulation.isPositionInEquator(j)) {
                        label.setStyle(label.getStyle()+" -fx-background-color: green;");
                    }
                    if (this.animalMap.containsKey(pos) && !this.animalMap.get(pos).isEmpty()) {
                        List<List<Integer>> genesInPosition = this.animalMap.get(pos).stream()
                                .map(Animal::getGenes)
                                .toList();
                        if (genesInPosition.contains(statistics.popularGenes().getFirst())) {
                            label.setStyle(label.getStyle()+" -fx-text-fill: red;");
                        }
                    }
                }


                label.setAlignment(Pos.CENTER);
                label.setPrefSize(cell_width, cell_height);
                label.setOnMouseClicked(e -> {
                    if (this.animalMap.containsKey(pos) && !this.animalMap.get(pos).isEmpty() && !running && trackedAnimal == null) {
                        Animal animal = this.simulation.getStrongest(this.animalMap.get(pos));
                        Button startTracking = new Button("sledz");
                        Button stopTracking = new Button("odsledz");
                        showTrackedAnimalStats();
                        startTracking.setOnAction(event -> {
                            trackedAnimal = animal;
                            //trackedAnimalStatsLayout.getChildren().clear();
                            //trackedAnimalStatsLayout.getChildren().add(animalStatLabel);
                            trackedAnimalStatsLayout.getChildren().remove(startTracking);
                            trackedAnimalStatsLayout.getChildren().add(stopTracking);
                            drawTrackedAnimal();
                        });
                        stopTracking.setOnAction(event -> {
                            trackedAnimal = null;
                            trackedAnimalStatsLayout.getChildren().clear();
                            hideTrackedAnimalStats();
                        });
                        trackedAnimalStatsLayout.getChildren().clear();
                        trackedAnimalStatsLayout.getChildren().add(animalStatLabel);
                        trackedAnimalStatsLayout.getChildren().add(startTracking);

                        Platform.runLater(()->{animalStatLabel.setText(animal.getStatistics());});
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
