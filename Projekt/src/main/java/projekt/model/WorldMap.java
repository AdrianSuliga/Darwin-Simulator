package projekt.model;

import projekt.interfaces.WorldElement;
import projekt.util.MapVisualizer;
import projekt.util.RandomPositionGenerator;

import java.util.*;

public class WorldMap {
    private Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
    private Map<Vector2d, Plant> plantList = new HashMap<>();
    private final Boundary boundary;// mapa to obszar od (0,0) do (x,y)
    private final RandomPositionGenerator rpg;
    private final MapVisualizer visualizer;

    private final int defaultEnergyConsumption;
    private final int plantsPerDay;

    private MapMovementLogicHandler mapLogic; // uzywamy, by określić zmiane pozycji i koszt energii

    public WorldMap(Vector2d upperRight, Vector2d lowerLeft, Map<Vector2d, HashSet<Animal>> animalMap,
                    int defaultEnergyConsumption, int plantsPerDay) {
        this.boundary = new Boundary(upperRight,lowerLeft);
        this.defaultEnergyConsumption = defaultEnergyConsumption;
        this.plantsPerDay = plantsPerDay;
        this.rpg = new RandomPositionGenerator(this);
        this.visualizer  = new MapVisualizer(this);
        this.animalMap = animalMap;
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

    public Map<Vector2d, HashSet<Animal>> getAnimalMap() {
        return animalMap;
    }

    public void spawnPlants() {
        List<Vector2d> chosenPositions = rpg.generateNewPlants();
        for (Vector2d position: chosenPositions) {
            plantList.put(position, new Plant(position));
        }
    }

    public boolean isOccupied(Vector2d position) {
        return animalMap.containsKey(position) || plantList.containsKey(position);
    }

    public WorldElement objectAt(Vector2d position) {
        if (animalMap.containsKey(position)) {
            return animalMap.get(position).stream().findFirst().orElse(null);
        } else return plantList.getOrDefault(position, null);
    }

    @Override
    public String toString() {
        return this.visualizer.draw(this.boundary.lowerLeft(), this.boundary.upperRight());
    }
}
