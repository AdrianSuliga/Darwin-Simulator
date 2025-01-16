package projekt.model;

import java.util.List;

public record Statistics(int animalCount, int plantCount, int freeSpacesCount,
                         List<List<Integer>> popularGenes, double averageEnergy,
                         double averageLifespan, double averageChildCount) {
    @Override
    public String toString() {

        String popGenes = popularGenes.stream()
                .map(gene -> gene.toString() + "\n")
                .reduce((a,b)->a+b)
                .orElse("");

        return  "ilosc zwierzat \t" + animalCount +
                "\nilosc roslin \t" + plantCount +
                "\nwolnych pol \t" + freeSpacesCount +
                "\ntopowe genomy:\n" + popGenes + '\'' +
                "\nsrednia energia \t" + (double)(Math.round(averageEnergy*100))/100.00 +
                "\nsredni zywot \t" + (double)(Math.round(averageLifespan*100))/100.00 +
                "\nsrednia dzietnosc \t" + (double)(Math.round(averageChildCount*100))/100.00;
    }
}
