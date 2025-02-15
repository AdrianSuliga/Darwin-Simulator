package projekt.model;

import javafx.util.Pair;
import projekt.interfaces.MapChangeListener;
import projekt.interfaces.WorldElement;
import projekt.util.AnimalComparator;
import projekt.util.MapVisualizer;
import projekt.util.RandomPositionGenerator;
import projekt.util.StatisticsManager;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {
    private Map<Vector2d, HashSet<Animal>> animalMap;
    private Map<Vector2d, Plant> plantList = new HashMap<>();
    private final Boundary boundary;// mapa to obszar od (0,0) do (x,y)
    private final RandomPositionGenerator rpg;
    private final AnimalComparator comparator;
    private final MapVisualizer visualizer;

    private final Set<Animal> changedAnimalsBuffer = new HashSet<>();
    private List<MapChangeListener> mapObservers = new ArrayList<>();
    private final int defaultEnergyConsumption;
    private final int plantsPerDay;

    private List<Pair<Vector2d, Animal>> animalChangeBuffer = new ArrayList<>();

    private MapMovementLogicHandler movementLogicHandler;

    private MapMovementLogicHandler mapLogic; // uzywamy, by określić zmiane pozycji i koszt energii

    private StatisticsManager statManager;

    private Statistics statistics;

    public WorldMap(Vector2d upperRight, Vector2d lowerLeft, Map<Vector2d, HashSet<Animal>> animalMap,
                    int defaultEnergyConsumption, int plantsPerDay, MapMovementLogicHandler movementLogicHandler) {
        this.boundary = new Boundary(upperRight, lowerLeft);
        this.defaultEnergyConsumption = defaultEnergyConsumption;
        this.plantsPerDay = plantsPerDay;
        this.rpg = new RandomPositionGenerator(this);
        this.visualizer = new MapVisualizer(this);
        this.animalMap = animalMap;
        this.movementLogicHandler = movementLogicHandler;
        this.movementLogicHandler.setMap(this);
        this.comparator = new AnimalComparator();
        this.statManager = new StatisticsManager();
    }

    public void registerObserver(MapChangeListener listener) {
        if (!mapObservers.contains(listener)) {
            mapObservers.add(listener);
        }
    }

    public void unregisterObserver(MapChangeListener listener) {
        mapObservers.remove(listener);
    }

    public void mapChanged() {
        for (MapChangeListener mapChangeListener : mapObservers) {
            mapChangeListener.mapChanged(this);
        }
    }

    public int getDefaultEnergyConsumption() {
        return defaultEnergyConsumption;
    }

    public int getHeight() {
        return boundary.upperRight().getY() + 1 - boundary.lowerLeft().getY();
    }

    public int getWidth() {
        return boundary.upperRight().getX() + 1 - boundary.lowerLeft().getX();
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
        for (Vector2d position : chosenPositions) {
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

    public void placeAnimal(Animal animal){
        if (animalMap.get(animal.getPosition()) == null) {//jeśli set nie istnieje
            HashSet<Animal> newSet = new HashSet<>();
            newSet.add(animal);
            animalMap.put(animal.getPosition(), newSet);
        } else {
            animalMap.get(animal.getPosition()).add(animal);
        }
        //mapChanged();
    }
    public void updateAnimals() {
        //pobieramy po kolei zmiany z bufora,
        for (Pair<Vector2d, Animal> pair : animalChangeBuffer) {
            animalMap.get(pair.getKey()).remove(pair.getValue());
            placeAnimal(pair.getValue());

        }
        animalChangeBuffer.clear();
//        mapChanged();
    }

    public void moveAnimals() {
        //zmiany pozycji
        for (Vector2d mapPosition : animalMap.keySet()) {
            HashSet<Animal> animals = animalMap.get(mapPosition);
            for (Animal animal : animals) {
                animal.move(this.movementLogicHandler);
                if (!animal.getPosition().equals(mapPosition)) {
                    animalChangeBuffer.add(new Pair<>(mapPosition, animal));
                }
            }
        }
        //update setów
        updateAnimals();
    }

    public HashSet<Animal> getBreedingAnimalsSet(HashSet<Animal> animals, int requiredEnergy) {
        return animals
                .stream()
                .filter(animal -> animal.getEnergy() >= requiredEnergy)
                .filter(animal -> animal.getDeathDay() == -1)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public void consumePlants(int energyPerPlant) {
        for (Vector2d mapPosition : animalMap.keySet()) {
            if (plantList.containsKey(mapPosition) && !animalMap.get(mapPosition).isEmpty()) {
                Animal winningAnimal = comparator.compare(animalMap.get(mapPosition));
                winningAnimal.eat(energyPerPlant);
                plantList.remove(mapPosition);
            }
        }
    }

    public int removeDeadAnimals() {
        //update statystyk
        for (Vector2d mapPosition : animalMap.keySet()) {
            animalMap.get(mapPosition).stream()
                    .filter(animal -> animal.getDeathDay() > -1)
                    .forEach(animal -> statManager.updateAverageLifespan(animal));
        }
        //usuniecie
        for (Vector2d mapPosition : animalMap.keySet()) {
            animalMap.get(mapPosition).removeIf(animal -> animal.getDeathDay() > -1);
        }
        animalMap.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        return getAnimalsCount();
    }

    public int getAnimalsCount() {
        return animalMap.values().stream()
                .mapToInt(HashSet::size)
                .sum();
    }

    public Statistics getStatistics() {
        this.updateStatistics();
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public void updateStatistics(){
        this.statManager.updateStatistics(this);
    }

    @Override
    public String toString() {
        return this.visualizer.draw(this.boundary.lowerLeft(), this.boundary.upperRight());
    }

    public boolean isPositionInEquator(int y){
        return this.rpg.isPositionInEquator(y);
    }
}
