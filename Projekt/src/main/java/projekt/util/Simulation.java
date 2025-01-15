package projekt.util;

import projekt.model.Animal;
import projekt.model.Vector2d;
import projekt.model.WorldMap;

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

        this.worldMap = new WorldMap(new Vector2d(mapWidth - 1, mapHeight - 1),
                new Vector2d(0, 0), animalMap, 1, plantsPerDay);
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
        int deadAnimalsCount = 0;
        for (Vector2d mapPosition: this.worldMap.getAnimalMap().keySet()) {
            for (Animal animal: this.worldMap.getAnimalMap().get(mapPosition)) {
                if (animal.getDeathDay() > -1) {
                    deadAnimalsCount++;
                }
            }
        }

        for (Vector2d mapPosition: this.worldMap.getAnimalMap().keySet()) {
            this.worldMap.getAnimalMap().get(mapPosition).removeIf(animal -> animal.getDeathDay() > -1);
        }

        this.worldMap.getAnimalMap().entrySet().removeIf(entry -> entry.getValue().isEmpty());
        this.animalsCount -= deadAnimalsCount;
    }

    private void consumePlants() {
        for (Vector2d mapPosition: this.worldMap.getAnimalMap().keySet()) {
            if (this.worldMap.getPlantList().containsKey(mapPosition)) {
                HashSet<Animal> animals = new HashSet<>(this.worldMap.getAnimalMap().get(mapPosition));
                Animal winningAnimal = compareAnimals(animals);
                winningAnimal.eat(this.energyGainedOnConsumption);
                this.worldMap.getPlantList().remove(mapPosition);
            }
        }
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

    private Animal compareAnimals(HashSet<Animal> animals) {
        int maxEnergy = animals.stream().mapToInt(Animal::getEnergy).max().orElse(Integer.MIN_VALUE);
        List <Animal> result = animals.stream().filter(animal -> animal.getEnergy() == maxEnergy).toList();

        if (result.size() == 1) {
            return result.getFirst();
        }

        int maxAge = result.stream().mapToInt(Animal::getDaysLived).max().orElse(Integer.MIN_VALUE);
        result = result.stream().filter(animal -> animal.getDaysLived() == maxAge).toList();

        if (result.size() == 1) {
            return result.getFirst();
        }

        int maxChildren = result.stream().mapToInt(Animal::getChildrenMade).max().orElse(Integer.MIN_VALUE);
        result = result.stream().filter(animal -> animal.getChildrenMade() == maxChildren).toList();

        if (result.size() == 1) {
            return result.getFirst();
        }

        int randomIndex = (int)(Math.floor(Math.random() * result.size()));
        return result.get(randomIndex);
    }
}
