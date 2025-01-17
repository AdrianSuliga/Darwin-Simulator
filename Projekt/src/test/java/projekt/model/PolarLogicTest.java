package projekt.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
public class PolarLogicTest {

    private Vector2d defLowerLeft = new Vector2d(0,0);
    private Vector2d defUpperRight = new Vector2d(9,9);
    @Test
    public void polarLogicEnergyTest(){
        Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
        MapMovementLogicHandler handler = new PolarLogic();
        Animal animal1 = new Animal(defLowerLeft,10, List.of(1,2));
        Animal animal2 = new Animal(defUpperRight,10, List.of(1,2));

        WorldMap map = new WorldMap(defUpperRight,defLowerLeft, animalMap, 1,1, handler);

        assertTrue(handler.getEnergyConsumption(animal1)> map.getDefaultEnergyConsumption());
        assertTrue(handler.getEnergyConsumption(animal2)> map.getDefaultEnergyConsumption());

    }

    @Test
    public void polarLogicMovementTest(){
        Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
        MapMovementLogicHandler handler = new PolarLogic();
        List<Integer> deafultTestingGenome = List.of(0);

        Vector2d pos1 = new Vector2d(0,5);
        Vector2d pos2 = new Vector2d(9,4);
        Vector2d pos3 = new Vector2d(1,0);
        Vector2d pos6 = new Vector2d(1,9);
        Vector2d pos9 = new Vector2d(5,5);


        Animal animal1 = new Animal(pos1,10,deafultTestingGenome);
        Animal animal2 = new Animal(pos2,10,deafultTestingGenome);
        Animal animal3 = new Animal(pos3,10,deafultTestingGenome);
        Animal animal6 = new Animal(pos6,10,deafultTestingGenome);
        Animal animal9 = new Animal(pos9,10,List.of(1));

        animal1.setDirection(MapDirection.WEST);
        animal2.setDirection(MapDirection.EAST);
        animal3.setDirection(MapDirection.SOUTH);
        animal6.setDirection(MapDirection.NORTH);
        animal9.setDirection(MapDirection.NORTH);


        WorldMap map = new WorldMap(defUpperRight,defLowerLeft, animalMap, 1,1, handler);

        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal6);
        map.placeAnimal(animal9);

        map.moveAnimals();

        assertEquals(pos3,animal3.getPosition());
        assertEquals(pos1,animal1.getPosition());
        assertEquals(pos2,animal2.getPosition());
        assertEquals(pos6,animal6.getPosition());


        assertEquals(MapDirection.SOUTH,animal3.getDirection());
        assertEquals(MapDirection.NORTH,animal6.getDirection());
        assertEquals(MapDirection.EAST,animal2.getDirection());
        assertEquals(MapDirection.WEST,animal1.getDirection());

        assertEquals(new Vector2d(6,6),animal9.getPosition());
        assertEquals(MapDirection.NORTHEAST,animal9.getDirection());
    }
}


