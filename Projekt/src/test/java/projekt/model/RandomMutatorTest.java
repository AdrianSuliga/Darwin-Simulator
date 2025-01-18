package projekt.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class RandomMutatorTest {

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

        RandomMutator randomMutator = new RandomMutator(1,10);
        RandomMutator noMutator = new RandomMutator(0,0);

        List<Integer> result1 = randomMutator.generateNewGenome(a1,a2);
        assertEquals(10,result1.size());

        List<Integer> result2 = noMutator.generateNewGenome(a1,a2);
        assertTrue(result2.equals(r1)||result2.equals(r2));
        assertEquals(10,result2.size());

        List<Integer> result3 = noMutator.generateNewGenome(a3,a4);
        assertEquals(g4,result3);
        assertEquals(1,result3.size());



    }
}
