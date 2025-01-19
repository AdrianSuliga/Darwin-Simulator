package projekt.util;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import projekt.model.SimulationWindow;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SimulationPresenter {
    private List<SimulationWindow> windows = new ArrayList<>();

    // Configurations
    @FXML
    private ChoiceBox<String> configurationChoice;

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
    @FXML
    private ChoiceBox<String> statisticsChoice;

    @FXML
    private void createSimulation() {
        // Get map data from controls
        int mapWidth = parseIntInput(widthInput, false);
        if (mapWidth < 0) {return;}

        int mapHeight = parseIntInput(heightInput, false);
        if (mapHeight < 0) {return;}

        boolean specialMapLogic = mapVariantBox.getValue().equals("poles");

        // Get plants data from controls
        int initialPlantsCount = parseIntInput(initPlantsCountInput, true);
        if (initialPlantsCount < 0) {return;}

        int energyGainedOnConsumption = parseIntInput(energyOnConsumptionInput, true);
        if (energyGainedOnConsumption < 0) {return;}

        int plantsPerDay = parseIntInput(plantsPerDayInput, true);
        if (plantsPerDay < 0) {return;}

        // Get animal data from controls
        int initialAnimalCount = parseIntInput(initAnimalCountInput, true);
        if (initialAnimalCount < 0) {return;}

        int initialAnimalEnergy = parseIntInput(initAnimalEnergyInput, true);
        if (initialAnimalEnergy < 0) {return;}

        int energyForBreeding = parseIntInput(energyForBreedingInput, true);
        if (energyForBreeding < 0) {return;}

        int energyOnBreeding = parseIntInput(energyOnBreedingInput, true);
        if (energyOnBreeding < 0) {return;}

        int minMutation = parseIntInput(minMutationInput, true);
        if (minMutation < 0) {return;}

        int maxMutation = parseIntInput(maxMutationInput, true);
        if (maxMutation < 0) {return;}

        int geneLength = parseIntInput(geneLengthInput, false);
        if (geneLength < 0) {return;}

        if (!(minMutation <= maxMutation && maxMutation <= geneLength)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Genes error");
            alert.setHeaderText("Condition 0 <= minMutationCount <= maxMutationCount <= geneLength is not met");
            alert.showAndWait();
            return;
        }

        boolean specialMutationLogic = mutationVariantChoice.getValue().equals("slight correction");

        boolean writeDataToCSV = statisticsChoice.getValue().equals("yes");

        this.windows.add(new SimulationWindow(
                new Simulation(
                        mapWidth, mapHeight, energyGainedOnConsumption, plantsPerDay, initialAnimalCount,
                        geneLength, initialAnimalEnergy, energyForBreeding, energyOnBreeding, minMutation,
                        maxMutation, specialMutationLogic, specialMapLogic, writeDataToCSV)));
    }

    @FXML
    private void handleConfigurationChange() {
        switch (configurationChoice.getValue()) {
            case "All 10s" -> readDataFromFileToUI("configuration_all_10s");
            case "Alternative variants" -> readDataFromFileToUI("configuration_alt_variants");
            case "A lot of genes" -> readDataFromFileToUI("configuration_lots_of_genes");
            default -> System.out.println("Unsupported configuration");
        }
    }

    private void readDataFromFileToUI(String path) {
        Scanner scanner = null;
        try {
            Path filePath = Path.of("src/main/resources/" + path);
            scanner = new Scanner(filePath);
            widthInput.setText(scanner.nextLine().trim());
            heightInput.setText(scanner.nextLine().trim());
            mapVariantBox.setValue(Integer.parseInt(scanner.nextLine().trim()) == 1 ? "poles" : "globe");

            initPlantsCountInput.setText(scanner.nextLine().trim());
            energyOnConsumptionInput.setText(scanner.nextLine().trim());
            plantsPerDayInput.setText(scanner.nextLine().trim());

            initAnimalCountInput.setText(scanner.nextLine().trim());
            initAnimalEnergyInput.setText(scanner.nextLine().trim());
            energyForBreedingInput.setText(scanner.nextLine().trim());
            energyOnBreedingInput.setText(scanner.nextLine().trim());
            minMutationInput.setText(scanner.nextLine().trim());
            maxMutationInput.setText(scanner.nextLine().trim());
            mutationVariantChoice.setValue(Integer.parseInt(scanner.nextLine().trim()) == 1 ? "slight correction" : "random");
            geneLengthInput.setText(scanner.nextLine().trim());

            statisticsChoice.setValue(Integer.parseInt(scanner.nextLine().trim()) == 1 ? "yes" : "no");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private int parseIntInput(TextField input, boolean canBeZero) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input error");
        int result;

        try {
            result = Integer.parseInt(input.getText());
        } catch (NumberFormatException e) {
            alert.setHeaderText("Please, enter valid integer number");
            alert.showAndWait();
            return -1;
        }

        if (canBeZero && result < 0) {
            alert.setHeaderText("Please, enter valid non-negative number");
            alert.showAndWait();
            return -1;
        } else if (!canBeZero && result <= 0) {
            alert.setHeaderText("Please, enter valid positive number");
            alert.showAndWait();
            return -1;
        }

        return result;
    }
}
