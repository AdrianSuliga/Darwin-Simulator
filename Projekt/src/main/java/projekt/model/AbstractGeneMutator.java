package projekt.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractGeneMutator {

    protected int maxMutations;
    protected int minMutations;

    public AbstractGeneMutator(int minMutations, int maxMutations) {
        this.maxMutations = maxMutations;
        this.minMutations = minMutations;
    }

    protected abstract List<Integer> mutateGenome(List<Integer> genome);
    protected List<Integer> splitGenes(Animal alpha, Animal beta){ //beta jest słabsza lub równa alphie
        Random random = new Random();
        int totalEnergy = alpha.getEnergy() + beta.getEnergy();
        int geneLenght = alpha.getGenes().size();
        List<Integer> resultGenome = new ArrayList<>();
        //co najmniej 1 gen od słabszego (bety)
        int betaGeneConut = ((beta.getEnergy()*geneLenght)/totalEnergy) < 1.0 ? 1 : (int) Math.floor((beta.getEnergy()*geneLenght)/totalEnergy) ;
        if(random.nextBoolean()){
            //wariant "lewo"
            resultGenome.addAll(alpha.getGenes().subList(0,geneLenght-betaGeneConut));
            resultGenome.addAll(beta.getGenes().subList(geneLenght-betaGeneConut,geneLenght));
        }else{
            //wariant "prawo"
            resultGenome.addAll(beta.getGenes().subList(0,betaGeneConut));
            resultGenome.addAll(alpha.getGenes().subList(betaGeneConut,geneLenght));
        }
        return resultGenome;

    }

    public List<Integer> generateNewGenome(Animal a1, Animal a2){
        List<Integer> resultGenome = new ArrayList<>();

        if (a1.getEnergy() > a2.getEnergy()) {
            resultGenome = mutateGenome(splitGenes(a1, a2));
        } else {
            resultGenome = mutateGenome(splitGenes(a2,a1));
        }

        return  resultGenome;
    }
}
