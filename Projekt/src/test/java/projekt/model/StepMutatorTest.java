package projekt.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StepMutatorTest {

    @Test
    public void generateNewGenomeTest(){
        Vector2d pos = new Vector2d(0,0);
        List<Integer> g1 = List.of(1,1,1,1,1,1,1,1,1,1);
        List<Integer> g2 = List.of(2,2,2,2,2,2,2,2,2,2);
        List<Integer> g3 = List.of(1);
        List<Integer> g4 = List.of(2);
        List<Integer> r1 = List.of(2,2,1,1,1,1,1,1,1,1);
        List<Integer> r2 = List.of(1,1,1,1,1,1,1,1,2,2);


        Animal a1 = new Animal(pos,80,g1);
        Animal a2 = new Animal(pos,20,g2);
        Animal a3 = new Animal(pos,20,g3);
        Animal a4 = new Animal(pos,80,g4);

        StepMutator stepMutator = new StepMutator(1,10);
        StepMutator noMutator = new StepMutator(0,0);
        StepMutator onceMutator = new StepMutator(1,1);

        List<Integer> result1 = stepMutator.generateNewGenome(a1,a2);
        assertEquals(10,result1.size());

        List<Integer> result2 = noMutator.generateNewGenome(a1,a2);
        assertTrue(result2.equals(r1)||result2.equals(r2));
        assertEquals(10,result2.size());

        List<Integer> result3 = noMutator.generateNewGenome(a3,a4);
        assertEquals(g4,result3);
        assertEquals(1,result3.size());

        List<Integer> result4 = onceMutator.generateNewGenome(a1,a1);
        assertEquals(10,result4.size());
        assertTrue(result4.contains(0) || result4.contains(2));

    }


}
