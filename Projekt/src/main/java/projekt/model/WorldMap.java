package projekt.model;

import java.util.*;

public class WorldMap {
    private Boundary boundary;// mapa to obszar od (0,0) do (x,y)

    private Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
    private Map<Vector2d, Plant> plantList = new HashMap<>();

    private int deafultEnergyConsumption;

    private int plantsPerDay;

    private MapMovementLogicHandler mapLogic;//uzywamy, by określić zmiane pozycji i koszt energii

    public int getPlantsPerDay() {
        return plantsPerDay;
    }

    public WorldMap(Vector2d upperRight, Vector2d lowerLeft, int deafultEnergyConsumption, int plantsPerDay) {
        this.boundary = new Boundary(upperRight,lowerLeft);
        this.deafultEnergyConsumption = deafultEnergyConsumption;
        this.plantsPerDay = plantsPerDay;
    }

    public int getDeafultEnergyConsumption() {
        return deafultEnergyConsumption;
    }


}
