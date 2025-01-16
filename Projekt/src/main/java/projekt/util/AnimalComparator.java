package projekt.util;

import projekt.model.Animal;

import java.util.*;

public class AnimalComparator  {

    public Animal compare(HashSet<Animal> animals) {
        Optional<Animal> maxEnergyAnimal = new HashSet<>(animals)
                .stream()
                .max(Comparator.comparing(Animal::getEnergy));

        if (maxEnergyAnimal.isPresent()) {
            return maxEnergyAnimal.get();
        }

        Optional<Animal> maxAgeAnimal = new HashSet<>(animals)
                .stream()
                .max(Comparator.comparing(Animal::getDaysLived));

        if (maxAgeAnimal.isPresent()) {
            return maxAgeAnimal.get();
        }

        Optional<Animal> maxChildrenAnimal = new HashSet<>(animals)
                .stream()
                .max(Comparator.comparing(Animal::getChildrenMade));

        if (maxChildrenAnimal.isPresent()) {
            return maxChildrenAnimal.get();
        }

        int randIdx = (int)(Math.floor(Math.random() * animals.size()));
        ArrayList<Animal> result = new ArrayList<>(animals);
        return result.get(randIdx);
    }
}
