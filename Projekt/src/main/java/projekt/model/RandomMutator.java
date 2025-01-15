package projekt.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMutator extends AbstractGeneMutator {
    public RandomMutator(int minMutations, int maxMutations) {
        super(minMutations, maxMutations);
    }

    @Override
    protected List<Integer> mutateGenome(List<Integer> genome) {
        Random random = new Random();
        int mutationsCount = random.nextInt(this.minMutations, this.maxMutations + 1);
        for (int i = 0; i < mutationsCount; i++) {
            int mutationIndex = random.nextInt(genome.size());
            genome.set(mutationIndex, random.nextInt(0, 9));
        }
        return genome;
    }

}
