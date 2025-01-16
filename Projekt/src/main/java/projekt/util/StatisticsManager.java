package projekt.util;

import javafx.util.Pair;
import projekt.model.Animal;
import projekt.model.Statistics;
import projekt.model.Vector2d;
import projekt.model.WorldMap;

import java.util.*;

public class StatisticsManager {

    private int totalLifespan=0;
    private int totalDeadAnimals=0;

    private double averageLifespan=0.0;

    //    średniej długości życia zwierzaków dla martwych zwierzaków (wartość uwzględnia wszystkie nieżyjące zwierzaki - od początku symulacji),
    public void updateAverageLifespan(Animal a){
        this.totalLifespan+=a.getDaysLived();
        this.totalDeadAnimals++;
        this.averageLifespan= (double) this.totalLifespan /this.totalDeadAnimals;
    }

    private boolean areGenomesEqual(List<Integer> g1,List<Integer> g2){
        List<Integer> tempGene = new ArrayList<>();
        tempGene.addAll(g1);
        tempGene.addAll(g1);
        return Collections.indexOfSubList(tempGene,g2) != -1;
    }

    private List<Integer> getMatchingGenomeKey(Map<List<Integer>,Integer> genomeRanking,List<Integer> newGenome){
        for(List<Integer> genome:genomeRanking.keySet()){
            if(areGenomesEqual(genome,newGenome)){
                return genome;
            }
        }
        return null;
    }

    private String popoularGenomes(Map<Vector2d, HashSet<Animal>> animalMap){
        Map<List<Integer>,Integer> genomeRanking = new HashMap<>();
        for (Vector2d mapPosition : animalMap.keySet()) {
            HashSet<Animal> animals = animalMap.get(mapPosition);
            for (Animal animal : animals) {
                List<Integer> matchingGenomeKey = getMatchingGenomeKey(genomeRanking,animal.getGenes());
                if(matchingGenomeKey!=null) {
                    genomeRanking.put(matchingGenomeKey, genomeRanking.get(matchingGenomeKey) + 1);
                }else{
                    genomeRanking.put(animal.getGenes(),1);
                }
            }
        }

        return genomeRanking.entrySet().stream()
                .sorted(Map.Entry.<List<Integer>,Integer>comparingByValue().reversed())
                .limit(10)
                .map(entry -> entry.getKey().toString()+' '+ entry.getValue() +"\n")
                .reduce((a,b) -> a+b)
                .orElse("");
    }

    private int getTotalEnergy(Map<Vector2d, HashSet<Animal>> animalMap){
        int energySum=0;
        for (Vector2d mapPosition : animalMap.keySet()) {
            HashSet<Animal> animals = animalMap.get(mapPosition);
            energySum += animals.stream()
                    .mapToInt(Animal::getEnergy)
                    .sum();
        }
        return energySum;
    }

    private int getTotalChildren(Map<Vector2d, HashSet<Animal>> animalMap){
        int childrenSum=0;
        for (Vector2d mapPosition : animalMap.keySet()) {
            HashSet<Animal> animals = animalMap.get(mapPosition);
            childrenSum += animals.stream()
                    .mapToInt(Animal::getChildrenMade)
                    .sum();
        }
        return childrenSum;
    }

    public void updateStatistics(WorldMap map){
        int animalCount = map.getAnimalsCount();
        int plantCount = map.getPlantList().size();
        int freeSpacesCount = map.getHeight()*map.getWidth() - map.getAnimalMap().size();
        String populargGenes = popoularGenomes(map.getAnimalMap());
        double averageEnergyLevel = (double) getTotalEnergy(map.getAnimalMap()) /animalCount;
        double averageChildCount = (double) getTotalChildren(map.getAnimalMap()) /animalCount;

        map.setStatistics(new Statistics(animalCount,plantCount,freeSpacesCount,
                populargGenes,averageEnergyLevel,this.averageLifespan,averageChildCount));
    }

//    liczby wszystkich zwierzaków,
//    liczby wszystkich roślin,
//    liczby wolnych pól,
//    najpopularniejszych genotypów,
//    średniego poziomu energii dla żyjących zwierzaków,
//    średniej długości życia zwierzaków dla martwych zwierzaków (wartość uwzględnia wszystkie nieżyjące zwierzaki - od początku symulacji),
//    średniej liczby dzieci dla żyjących zwierzaków (wartość uwzględnia wszystkie powstałe zwierzaki, a nie tylko zwierzaki powstałe w danej epoce).
}
