package projekt.util;

import projekt.model.*;

import java.util.*;

public class Simulation {
    private WorldMap worldMap;
    private int animalsCount;
    private final boolean specialMapLogic;
    private final int energyGainedOnConsumption;
    private final int animalsStartingEnergy;
    private final int energyForBreeding;
    private final int energyConsumedOnBreeding;
    private final int minMutationCount;
    private final int maxMutationCount;
    private final boolean specialMutationLogic; // false - [1], true - [2]
    private final int animalsGeneLength;

    private AbstractGeneMutator geneMutator;

    public Simulation(int mapWidth, int mapHeight, int energyGainedOnConsumption,
                      int plantsPerDay, int animalsCount, int animalsGeneLength, int animalsStartingEnergy,
                      int energyForBreeding, int energyConsumedOnBreeding, int minMutationCount,
                      int maxMutationCount, boolean specialMutationLogic, boolean specialMapLogic) {
        this.specialMapLogic = specialMapLogic;
        this.energyGainedOnConsumption = energyGainedOnConsumption;
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

        if (specialMapLogic) {
            movementLogicHandler = new PolarLogic();
        } else {
            movementLogicHandler = new GlobeLogic();
        }

        this.worldMap = new WorldMap(new Vector2d(mapWidth - 1, mapHeight - 1),
                new Vector2d(0, 0), animalMap, 1,
                plantsPerDay, movementLogicHandler);
    }

    public void run() {
        System.out.println(this.worldMap);
        while (!this.worldMap.getAnimalMap().isEmpty()) {
            removeDeadAnimals();
            moveAnimals();
            consumePlants();
            breedAnimals();
            this.worldMap.spawnPlants();
            System.out.println(this.worldMap.toString());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeDeadAnimals() {
        this.animalsCount = this.worldMap.removeDeadAnimals();
    }

    private void moveAnimals() {
        this.worldMap.moveAnimals();
    }

    private void consumePlants() {
        this.worldMap.consumePlants(this.energyGainedOnConsumption);
    }

    private void breedAnimals() {
        for (Vector2d position: this.worldMap.getAnimalMap().keySet()) {
            List<Animal> animals = this.worldMap.getBreedingAnimalsList(this.worldMap.getAnimalMap().get(position), energyForBreeding);
            if (animals.size() < 2) {
                continue;
            }

            List<Integer> newGenes = geneMutator.generateNewGenome(animals.get(0),animals.get(1));

            animals.get(0).breed(this.energyConsumedOnBreeding);
            animals.get(1).breed(this.energyConsumedOnBreeding);


            Animal newBorn = new Animal(position, 2 * this.energyForBreeding, newGenes);
            this.worldMap.getAnimalMap().get(position).add(newBorn);
        }
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
