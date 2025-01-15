package projekt.model;

import javafx.util.Pair;
import projekt.interfaces.WorldElement;
import projekt.util.AnimalComparator;
import projekt.util.MapVisualizer;
import projekt.util.RandomPositionGenerator;

import java.util.*;

public class WorldMap {
    private Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
    private Map<Vector2d, Plant> plantList = new HashMap<>();
    private final Boundary boundary;// mapa to obszar od (0,0) do (x,y)
    private final RandomPositionGenerator rpg;
    private final MapVisualizer visualizer;

    private final Set<Animal> changedAnimalsBuffer = new HashSet<>();
    private final int defaultEnergyConsumption;
    private final int plantsPerDay;

    private List<Pair<Vector2d, Animal>> animalChangeBuffer = new ArrayList<>();

    private MapMovementLogicHandler movementLogicHandler;

    private MapMovementLogicHandler mapLogic; // uzywamy, by określić zmiane pozycji i koszt energii

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

    public void updateAnimals() {
        //pobieramy po kolei zmiany z bufora,
        for (Pair<Vector2d, Animal> pair : animalChangeBuffer) {
            animalMap.get(pair.getKey()).remove(pair.getValue());
            if (animalMap.get(pair.getValue().getPosition()) == null) {//jeśli set nie istnieje
                HashSet<Animal> newSet = new HashSet<>();
                newSet.add(pair.getValue());
                animalMap.put(pair.getValue().getPosition(), newSet);
            } else {
                animalMap.get(pair.getValue().getPosition()).add(pair.getValue());
            }

        }
        animalChangeBuffer.clear();
    }

        public void moveAnimals () {
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

        private List<Animal> sortAnimalsSet(HashSet<Animal> animals) {
            List<Animal> resultList = animals.stream()
                    .filter(animal -> animal.getDeathDay()==-1)//żywe
                    .sorted(new AnimalComparator())
                    .toList();
            return resultList;
        }

        public List<Animal> getBreedingAnimalsList(HashSet<Animal> animals, int requiredEnergy){
            List<Animal> breedableList = sortAnimalsSet(animals).stream()
                    .filter(animal -> animal.getEnergy()>=requiredEnergy)
                    .toList();
            return breedableList;
        }

        public Animal getStrongestAnimal(HashSet<Animal> animals){
            System.out.println(sortAnimalsSet(animals));
            return sortAnimalsSet(animals).get(0);
        }

        public void consumePlants(int energyPerPlant){
            for(Vector2d mapPosition: animalMap.keySet()){
                if(plantList.containsKey(mapPosition)){
                    Animal winningAnimal = getStrongestAnimal(animalMap.get(mapPosition));
                    winningAnimal.eat(energyPerPlant);
                    plantList.remove(mapPosition);
                }
            }
        }

        public int removeDeadAnimals(){
            for(Vector2d mapPosition: animalMap.keySet()){
                animalMap.get(mapPosition).removeIf(animal -> animal.getDeathDay()>-1);
            }
            animalMap.entrySet().removeIf(entry->entry.getValue().isEmpty());

            return getAnimalsCount();
        }

        private int getAnimalsCount(){
            return animalMap.values().stream()
                    .mapToInt(set -> set.size())
                    .sum();
        }

        @Override
        public String toString () {
            return this.visualizer.draw(this.boundary.lowerLeft(), this.boundary.upperRight());
        }
    }
