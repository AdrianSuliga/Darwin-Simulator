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
        System.out.println(this.worldMap);
        while (!this.worldMap.getAnimalMap().isEmpty()) {
            removeDeadAnimals();
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
            HashSet<Animal> animals = new HashSet<>(this.worldMap.getAnimalMap().get(position).stream().
                    filter(animal -> animal.getEnergy() >= this.energyForBreeding).toList());
            if (animals.size() < 2) {
                continue;
            }
            Animal animal1 = compareAnimals(animals);
            animals.remove(animal1);
            Animal animal2 = compareAnimals(animals);

            animal1.breed(this.energyConsumedOnBreeding);
            animal2.breed(this.energyConsumedOnBreeding);

            int energy1 = animal1.getEnergy(), energy2 = animal2.getEnergy();
            double divisionPoint = (double)energy1 / (energy1 + energy2);
            int divisionGenIndex = (int)(divisionPoint * this.animalsGeneLength);
            ArrayList <Integer> newGenes;

            if (Math.random() < 0.5) { // take left genes from first parent, right from second
                newGenes = new ArrayList<>(animal1.getGenes().subList(0, divisionGenIndex));
                newGenes.addAll(animal2.getGenes().subList(divisionGenIndex, this.animalsGeneLength));
            } else { // take right genes from first parent, left from second
                newGenes = new ArrayList<>(animal2.getGenes().subList(0, this.animalsGeneLength - divisionGenIndex));
                newGenes.addAll(animal1.getGenes().subList(this.animalsGeneLength - divisionGenIndex, this.animalsGeneLength));
            }

            mutateGenes(newGenes, this.minMutationCount, this.maxMutationCount);

            Animal newBorn = new Animal(position, 2 * this.energyForBreeding, newGenes);
            this.worldMap.getAnimalMap().get(position).add(newBorn);
        }
    }

    private void mutateGenes(ArrayList<Integer> genes, int minMutationCount, int maxMutationCount) {
        int mutationCount = (int)(Math.random() * (maxMutationCount - minMutationCount + 1) + minMutationCount);
        mutationCount = Math.min(mutationCount, this.animalsGeneLength);

        ArrayList<Integer> resultIndexes = new ArrayList<>();

        for (int i = 0; i < this.animalsGeneLength; i++) {
            resultIndexes.add(i);
        }

        for (int i = 0; i < mutationCount; i++) {
            int randIdx = (int)(Math.floor(Math.random() * resultIndexes.size()));
            int randomGeneIdx = resultIndexes.get(randIdx);
            int gene = genes.get(randomGeneIdx);
            if (specialMutationLogic) {
                gene = Math.random() < 0.5 ? gene - 1 : gene + 1;
            } else {
                gene = (int)(Math.floor(Math.random() * 8));
            }
            genes.set(randomGeneIdx, gene);
            resultIndexes.remove(randIdx);
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
