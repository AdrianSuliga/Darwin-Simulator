package projekt.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AnimalTest {
    private final int defaultEng = 75;
    private final List<Integer> defaultGenes = List.of(0, 0, 0, 2, 1, 0, 0, 0);
    @Test
    public void constructorTest() {
        Animal animal = new Animal(new Vector2d(2, 2), defaultEng, defaultGenes);

        assertEquals(new Vector2d(2, 2), animal.getPosition());
        assertEquals(defaultEng, animal.getEnergy());
        assertEquals(defaultGenes, animal.getGenes());
        assertEquals(1, animal.getDaysLived());
        assertEquals(0, animal.getPlantsEaten());
        assertEquals(0, animal.getChildrenMade());
        assertEquals(-1, animal.getDeathDay());
    }

    @Test
    public void isAtTest() {
        Animal animal1 = new Animal(new Vector2d(0, 0), defaultEng, defaultGenes);
        Animal animal2 = new Animal(new Vector2d(2, 4), defaultEng, defaultGenes);

        assertTrue(animal1.isAt(new Vector2d(0, 0)));
        assertTrue(animal2.isAt(new Vector2d(2, 4)));
        assertFalse(animal1.isAt(new Vector2d(2, 4)));
        assertFalse(animal2.isAt(new Vector2d(0, 0)));
        assertFalse(animal1.isAt(new Vector2d(-10, -10)));
        assertFalse(animal2.isAt(new Vector2d(-10, -10)));
    }

//    @Test
//    public void moveTest() {
//        Animal animal1 = new Animal(new Vector2d(0, 0), defaultEng, List.of(0, 0));
//        Animal animal2 = new Animal(new Vector2d(2, 4), defaultEng, List.of(1, 7));
//
//        animal1.move(50);
//        animal2.move(defaultEng);
//
//        assertEquals(25, animal1.getEnergy());
//        assertEquals(2, animal2.getDaysLived());
//        assertEquals(-1, animal1.getDeathDay());
//
//        assertEquals(0, animal2.getEnergy());
//        assertEquals(2, animal2.getDaysLived());
//        assertEquals(2, animal2.getDeathDay());
//    }

    @Test
    public void eatTest() {
        Animal animal1 = new Animal(new Vector2d(0, 0), defaultEng, defaultGenes);
        Animal animal2 = new Animal(new Vector2d(3, 3), defaultEng, defaultGenes);

        animal1.eat(20);
        animal2.eat(30);
        animal2.eat(5);

        assertEquals(95, animal1.getEnergy());
        assertEquals(110, animal2.getEnergy());
        assertEquals(1, animal1.getPlantsEaten());
        assertEquals(2, animal2.getPlantsEaten());
    }

    @Test
    public void breedTest() {
        Animal animal1 = new Animal(new Vector2d(0, 0), defaultEng, defaultGenes);
        Animal animal2 = new Animal(new Vector2d(0, 0), defaultEng + 25, defaultGenes);

        animal1.breed(40);
        animal2.breed(40);
        animal2.breed(40);

        assertEquals(35, animal1.getEnergy());
        assertEquals(1, animal1.getChildrenMade());
        assertEquals(20, animal2.getEnergy());
        assertEquals(2, animal2.getChildrenMade());
    }
}
