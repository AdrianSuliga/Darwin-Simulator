package projekt.model;

import java.util.List;
import java.util.Random;

public class StepMutator extends AbstractGeneMutator{
    public StepMutator(int minMutations, int maxMutations) {
        super(minMutations, maxMutations);
    }

    @Override
    protected List<Integer> mutateGenome(List<Integer> genome) {
        Random random = new Random();
        int mutationsCount = random.nextInt(this.minMutations, this.maxMutations + 1);
        for (int i = 0; i < mutationsCount; i++) {
            int mutationIndex = random.nextInt(genome.size());
            int change = random.nextBoolean() ? -1 : 1;
            int newGene = genome.get(mutationIndex)+change;
            if(newGene<0){
                newGene=7;
            } else if (newGene>7){
                newGene = 0;
            }
            genome.set(mutationIndex,  newGene);
        }
        return genome;
    }

}
