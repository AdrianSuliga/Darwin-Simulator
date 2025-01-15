package projekt.util;

import projekt.model.Animal;

import java.util.Comparator;
import java.util.Random;

public class AnimalComparator implements Comparator<Animal> {
    @Override
    public int compare(Animal o1, Animal o2) {
        if(o1.getEnergy() != o2.getEnergy()){
            return o2.getEnergy()-o1.getEnergy();
        } else if(o1.getDaysLived() != o2.getDaysLived()){
            return o2.getDaysLived()-o1.getDaysLived();
        } else if (o1.getChildrenMade() != o2.getChildrenMade()) {
            return o2.getChildrenMade()-o1.getChildrenMade();
        } else {
            Random random=new Random();
            if(random.nextBoolean()) {
                return 1;
            }
            return -1;
        }
    }
}
