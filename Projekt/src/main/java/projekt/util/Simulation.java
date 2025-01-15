package projekt.util;

import projekt.model.*;

import java.util.*;

public class Simulation {
    private WorldMap worldMap;
    private final boolean specialMapLogic;
    private final int plantsCount;
    private final int energyGainedOnConsumption;
    private final int plantsPerDay;
    private final int animalsCount;
    private final int animalsStartingEnergy;
    private final int energyForBreeding;
    private final int energyConsumedOnBreeding;
    private final int minMutationCount;
    private final int maxMutationCount;
    private final boolean specialMutationLogic;
    private final int animalsGeneLength;

    public Simulation(int mapWidth, int mapHeight, int defaultPlantsCount, int energyGainedOnConsumption,
                      int plantsPerDay, int animalsCount, int animalsGeneLength, int animalsStartingEnergy,
                      int energyForBreeding, int energyConsumedOnBreeding, int minMutationCount,
                      int maxMutationCount, boolean specialMutationLogic, boolean specialMapLogic) {
        this.specialMapLogic = specialMapLogic;
        this.plantsCount = defaultPlantsCount;
        this.energyGainedOnConsumption = energyGainedOnConsumption;
        this.plantsPerDay = plantsPerDay;
        this.animalsCount = animalsCount;
        this.animalsStartingEnergy = animalsStartingEnergy;
        this.energyForBreeding = energyForBreeding;
        this.energyConsumedOnBreeding = energyConsumedOnBreeding;
        this.minMutationCount = minMutationCount;
        this.maxMutationCount = maxMutationCount;
        this.specialMutationLogic = specialMutationLogic;
        this.animalsGeneLength = animalsGeneLength;

        Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
        for (int i = 0; i < this.animalsCount; i++) {
            HashSet<Animal> animalSet = new HashSet<>();
            Animal animal = getRandomAnimal(mapWidth, mapHeight);
            animalSet.add(animal);
            animalMap.put(animal.getPosition(), animalSet);
        }

        MapMovementLogicHandler movementLogicHandler;

        if(specialMapLogic){
            movementLogicHandler=new PolarLogic();
        } else {
            movementLogicHandler = new GlobeLogic();
        }

        this.worldMap = new WorldMap(new Vector2d(mapWidth - 1, mapHeight - 1),
                new Vector2d(0, 0), animalMap, 1,
                this.plantsPerDay,movementLogicHandler);
    }

    public void run() {
        int testCnt = 0;
        while (testCnt < 20) {
            removeDeadAnimals();
            moveAnimals();
            consumePlants();
            breedAnimals();
            this.worldMap.spawnPlants();
            System.out.println(this.worldMap.toString());
            testCnt++;
        }
    }

    private void removeDeadAnimals() {
//        for (Vector2d mapPosition: this.worldMap.getAnimalMap().keySet()) {
//            this.worldMap.getAnimalMap().get(mapPosition).removeIf(animal -> animal.getDeathDay() > -1);
//        }
//        this.worldMap.getAnimalMap().entrySet().removeIf(entry -> entry.getValue().isEmpty());
        this.worldMap.removeDeadAnimals();
    }

    private void moveAnimals() {
        this.worldMap.moveAnimals();
    }

    private void consumePlants() {
        this.worldMap.consumePlants(this.energyGainedOnConsumption);
    }

    private void breedAnimals() {
        // breeding logic
    }

    private Animal getRandomAnimal(int maxX, int maxY) {
        List<Integer> genes = new ArrayList<>();
        int randX = (int)(Math.floor(Math.random() * maxX));
        int randY = (int)(Math.floor(Math.random() * maxY));

        for (int i = 0; i < this.animalsGeneLength; i++) {
            genes.add((int)(Math.floor(Math.random() * 8)));
        }

        return new Animal(new Vector2d(randX, randY), this.animalsStartingEnergy, genes);
    }



}
