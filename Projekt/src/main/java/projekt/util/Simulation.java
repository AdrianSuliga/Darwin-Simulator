package projekt.util;

import projekt.model.*;

import java.util.*;

public class Simulation {
    private int animalsCount;
    private final int energyGainedOnConsumption;
    private final int animalsStartingEnergy;
    private final int energyForBreeding;
    private final int energyConsumedOnBreeding;
    private final int animalsGeneLength;
    private final boolean writeToCSV;

    private final WorldMap worldMap;
    private final AbstractGeneMutator geneMutator;
    private final AnimalComparator comparator;
    private final CSVDataWriter writer;

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public Simulation(int mapWidth, int mapHeight, int energyGainedOnConsumption,
                      int plantsPerDay, int animalsCount, int animalsGeneLength, int animalsStartingEnergy,
                      int energyForBreeding, int energyConsumedOnBreeding, int minMutationCount,
                      int maxMutationCount, boolean specialMutationLogic, boolean specialMapLogic,
                      boolean writeToCSV) {
        this.energyGainedOnConsumption = energyGainedOnConsumption;
        this.animalsCount = animalsCount;
        this.animalsStartingEnergy = animalsStartingEnergy;
        this.energyForBreeding = energyForBreeding;
        this.energyConsumedOnBreeding = energyConsumedOnBreeding;
        this.animalsGeneLength = animalsGeneLength;
        this.writeToCSV = writeToCSV;
        this.comparator = new AnimalComparator();

        if (this.writeToCSV) {
            this.writer = new CSVDataWriter();
        } else {
            this.writer = null;
        }

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

        if (specialMutationLogic) {
            this.geneMutator = new StepMutator(minMutationCount, maxMutationCount);
        } else {
            this.geneMutator = new RandomMutator(minMutationCount, maxMutationCount);
        }

        this.worldMap = new WorldMap(new Vector2d(mapWidth - 1, mapHeight - 1),
                new Vector2d(0, 0), animalMap, 1,
                plantsPerDay, movementLogicHandler);
    }

    public void run() {
        while (!this.worldMap.getAnimalMap().isEmpty() && !Thread.currentThread().isInterrupted()) {
            this.worldMap.mapChanged();
            removeDeadAnimals();
            moveAnimals();
            consumePlants();
            breedAnimals();
            this.worldMap.spawnPlants();
            if (this.writeToCSV) {
                this.writer.writeStatistics(this.worldMap.getStatistics());
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void removeDeadAnimals() {
        this.animalsCount = this.worldMap.removeDeadAnimals();
        this.worldMap.mapChanged();
    }

    private void moveAnimals() {
        this.worldMap.moveAnimals();
        this.worldMap.mapChanged();
    }

    private void consumePlants() {
        this.worldMap.consumePlants(this.energyGainedOnConsumption);
        this.worldMap.mapChanged();
    }

    public Animal getStrongest(HashSet<Animal> animals) {
        return this.comparator.compare(animals);
    }

    private void breedAnimals() {
        for (Vector2d position: this.worldMap.getAnimalMap().keySet()) {
            HashSet<Animal> animals = this.worldMap.getBreedingAnimalsSet(this.worldMap.getAnimalMap().get(position), energyForBreeding);

            if (animals.size() < 2) {
                continue;
            }

            Animal alpha = this.comparator.compare(animals);
            animals.remove(alpha); // Removing from copy of a set so it is fine
            Animal beta = this.comparator.compare(animals);

            List<Integer> newGenes = geneMutator.generateNewGenome(alpha, beta);

            alpha.breed(this.energyConsumedOnBreeding);
            beta.breed(this.energyConsumedOnBreeding);

            Animal newBorn = new Animal(position, 2 * this.energyForBreeding, newGenes);
            this.worldMap.placeAnimal(newBorn);
        }
        this.worldMap.mapChanged();
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

    public boolean isPositionInEquator(int y) {
        return this.worldMap.isPositionInEquator(y);
    }
}
