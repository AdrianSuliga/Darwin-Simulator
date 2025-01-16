package projekt.model;

import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import projekt.interfaces.MapChangeListener;
import projekt.util.Simulation;

public class SimulationWindow implements MapChangeListener {
    private final Stage stage;
    private Label mapLabel;

    private Label statLabel;
    private final Simulation simulation;
    private Thread simulationThread;

    public SimulationWindow(Simulation simulation) {
        stage = new Stage();
        mapLabel = new Label("Simulation map");
        statLabel = new Label("Statistics");
        this.simulation = simulation;
        this.simulation.getWorldMap().registerObserver(this);
        VBox statLayout = new VBox(10,statLabel);
        VBox layout = new VBox(10,mapLabel);
        BorderPane pane = new BorderPane();
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        statLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        pane.setLeft(statLayout);
        pane.setCenter(layout);

        Scene scene = new Scene(pane, 300, 600);
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
            mapSnapshot = map.toString();
        }
        Platform.runLater(() -> {mapLabel.setText(mapSnapshot);
                                    statLabel.setText(mapStats);});
    }
}
