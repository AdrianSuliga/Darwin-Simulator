package projekt.model;

import com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import projekt.interfaces.MapChangeListener;
import projekt.util.Simulation;

public class SimulationWindow implements MapChangeListener {
    private final Stage stage;
    private Label mapLabel;
    private final Simulation simulation;
    private Thread simulationThread;

    public SimulationWindow(Simulation simulation) {
        stage = new Stage();
        mapLabel = new Label("Simulation map");
        this.simulation = simulation;
        this.simulation.getWorldMap().registerObserver(this);
        VBox layout = new VBox(10, mapLabel);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(layout, 300, 600);
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
        synchronized (map) {
            mapSnapshot = map.toString();
        }
        Platform.runLater(() -> {mapLabel.setText(mapSnapshot);});
    }
}
