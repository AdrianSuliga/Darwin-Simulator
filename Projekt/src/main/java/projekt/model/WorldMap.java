package projekt.model;

import projekt.util.RandomPositionGenerator;

import java.util.*;

public class WorldMap {
    private Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
    private Map<Vector2d, Plant> plantList = new HashMap<>();
    private final Boundary boundary;// mapa to obszar od (0,0) do (x,y)
    private final RandomPositionGenerator rpg;

    private final int defaultEnergyConsumption;
    private final int plantsPerDay;

    private MapMovementLogicHandler mapLogic;//uzywamy, by określić zmiane pozycji i koszt energii

    public WorldMap(Vector2d upperRight, Vector2d lowerLeft, int defaultEnergyConsumption, int plantsPerDay) {
        this.boundary = new Boundary(upperRight,lowerLeft);
        this.defaultEnergyConsumption = defaultEnergyConsumption;
        this.plantsPerDay = plantsPerDay;
        this.rpg = new RandomPositionGenerator(this);
    }

    public int getDefaultEnergyConsumption() {
        return defaultEnergyConsumption;
    }

    public int getHeight(){
        return boundary.upperRight().getY()+1-boundary.lowerLeft().getY();
    }

    public int getWidth(){
        return boundary.upperRight().getX()+1-boundary.lowerLeft().getX();
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public int getPlantsPerDay() {
        return plantsPerDay;
    }

    public Map<Vector2d, Plant> getPlantList() {
        return plantList;
    }

    public void spawnPlants() {
        List<Vector2d> chosenPositions = rpg.generateNewPlants();
        for (Vector2d position: chosenPositions) {
            plantList.put(position, new Plant(position));
        }
    }
}
