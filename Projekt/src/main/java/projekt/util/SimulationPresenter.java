package projekt.util;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class SimulationPresenter {
    private Simulation simulation;
    // Map controls
    @FXML
    private TextField widthInput;
    @FXML
    private TextField heightInput;
    @FXML
    private ChoiceBox<String> mapVariantBox;
    // Plants controls
    @FXML
    private TextField initPlantsCountInput;
    @FXML
    private TextField energyOnConsumptionInput;
    @FXML
    private TextField plantsPerDayInput;
    // Animals controls
    @FXML
    private TextField initAnimalCountInput;
    @FXML
    private TextField initAnimalEnergyInput;
    @FXML
    private TextField energyForBreedingInput;
    @FXML
    private TextField energyOnBreedingInput;
    @FXML
    private TextField minMutationInput;
    @FXML
    private TextField maxMutationInput;
    @FXML
    private ChoiceBox<String> mutationVariantChoice;
    @FXML
    private TextField geneLengthInput;

    public SimulationPresenter() {

    }

    @FXML
    private void createSimulation() {
        // Get map data from controls
        int mapWidth = parseIntInput(widthInput);
        if (mapWidth < 0) {return;}

        int mapHeight = parseIntInput(heightInput);
        if (mapHeight < 0) {return;}

        boolean specialMapLogic = mapVariantBox.getValue().equals("poles");

        // Get plants data from controls
        int initialPlantsCount = parseIntInput(initPlantsCountInput);
        if (initialPlantsCount < 0) {return;}

        int energyGainedOnConsumption = parseIntInput(energyOnConsumptionInput);
        if (energyGainedOnConsumption < 0) {return;}

        int plantsPerDay = parseIntInput(plantsPerDayInput);
        if (plantsPerDay < 0) {return;}

        // Get animal data from controls
        int initialAnimalCount = parseIntInput(initAnimalCountInput);
        if (initialAnimalCount < 0) {return;}

        int initialAnimalEnergy = parseIntInput(initAnimalEnergyInput);
        if (initialAnimalEnergy < 0) {return;}

        int energyForBreeding = parseIntInput(energyForBreedingInput);
        if (energyForBreeding < 0) {return;}

        int energyOnBreeding = parseIntInput(energyOnBreedingInput);
        if (energyOnBreeding < 0) {return;}

        int minMutation = parseIntInput(minMutationInput);
        if (minMutation < 0) {return;}

        int maxMutation = parseIntInput(maxMutationInput);
        if (maxMutation < 0) {return;}

        int geneLength = parseIntInput(geneLengthInput);
        if (geneLength < 0) {return;}

        boolean specialMutationLogic = mutationVariantChoice.getValue().equals("slight correction");

        this.simulation = new Simulation(mapWidth, mapHeight, energyGainedOnConsumption, plantsPerDay,
                initialAnimalCount, geneLength, initialAnimalEnergy, energyForBreeding, energyOnBreeding, minMutation,
                maxMutation, specialMutationLogic, specialMapLogic);
        run();
    }

    private void run() {
        this.simulation.run();
    }

    private int parseIntInput(TextField input) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input error");
        int result;

        try {
            result = Integer.parseInt(input.getText());
        } catch (NumberFormatException e) {
            alert.setHeaderText("Please, enter valid and positive integer number");
            alert.showAndWait();
            return -1;
        }

        if (result <= 0) {
            return -1;
        }
        return result;
    }
}
