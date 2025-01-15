package projekt.util;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import projekt.model.Vector2d;
import projekt.model.WorldMap;

import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;

public class SimulationApp extends Application {
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Darwin World");
        primaryStage.setResizable(true);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
        BorderPane viewRoot = null;
        try {
            viewRoot = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimulationPresenter presenter = loader.getController();
        configureStage(primaryStage, viewRoot);
        primaryStage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }
}
