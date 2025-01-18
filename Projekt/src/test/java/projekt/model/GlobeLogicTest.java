package projekt.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobeLogicTest {

    private Vector2d defLowerLeft = new Vector2d(0,0);
    private Vector2d defUpperRight = new Vector2d(9,9);
    @Test
    public void globeLogicEnergyTest(){
        Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
        MapMovementLogicHandler handler = new GlobeLogic();
        Animal testAnimal = new Animal(defLowerLeft,10, List.of(1,2));

        WorldMap map = new WorldMap(defUpperRight,defLowerLeft, animalMap, 1,1, handler);

        assertEquals(1,handler.getEnergyConsumption(testAnimal));
        assertEquals(map.getDefaultEnergyConsumption(),handler.getEnergyConsumption(testAnimal));
    }

    @Test
    public void globeLogicMovementTest(){
        Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
        MapMovementLogicHandler handler = new GlobeLogic();
        List<Integer> deafultTestingGenome = List.of(0);

        Vector2d pos1 = new Vector2d(0,5);
        Vector2d pos2 = new Vector2d(9,4);
        Vector2d pos3 = new Vector2d(1,0);
        Vector2d pos4 = new Vector2d(2,0);
        Vector2d pos5 = new Vector2d(3,0);
        Vector2d pos6 = new Vector2d(1,9);
        Vector2d pos7 = new Vector2d(2,9);
        Vector2d pos8 = new Vector2d(3,9);
        Vector2d pos9 = new Vector2d(5,5);


        Animal animal1 = new Animal(pos1,10,deafultTestingGenome);
        Animal animal2 = new Animal(pos2,10,deafultTestingGenome);
        Animal animal3 = new Animal(pos3,10,deafultTestingGenome);
        Animal animal4 = new Animal(pos4,10,deafultTestingGenome);
        Animal animal5 = new Animal(pos5,10,deafultTestingGenome);
        Animal animal6 = new Animal(pos6,10,deafultTestingGenome);
        Animal animal7 = new Animal(pos7,10,deafultTestingGenome);
        Animal animal8 = new Animal(pos8,10,deafultTestingGenome);
        Animal animal9 = new Animal(pos9,10,List.of(1));

        animal1.setDirection(MapDirection.WEST);
        animal2.setDirection(MapDirection.EAST);
        animal3.setDirection(MapDirection.SOUTH);
        animal4.setDirection(MapDirection.SOUTHWEST);
        animal5.setDirection(MapDirection.SOUTHEAST);
        animal6.setDirection(MapDirection.NORTH);
        animal7.setDirection(MapDirection.NORTHWEST);
        animal8.setDirection(MapDirection.NORTHEAST);
        animal9.setDirection(MapDirection.NORTH);


        WorldMap map = new WorldMap(defUpperRight,defLowerLeft, animalMap, 1,1, handler);

        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        map.placeAnimal(animal5);
        map.placeAnimal(animal6);
        map.placeAnimal(animal7);
        map.placeAnimal(animal8);
        map.placeAnimal(animal9);

        map.moveAnimals();

        //dookoła świata
        assertNotNull(map.getAnimalMap().get(new Vector2d(9,5)));
        assertEquals(0,map.getAnimalMap().get(pos1).size());
        assertEquals(new Vector2d(9,5),animal1.getPosition());

        assertNotNull(map.getAnimalMap().get(new Vector2d(0,4)));
        assertEquals(0,map.getAnimalMap().get(pos2).size());
        assertEquals(new Vector2d(0,4),animal2.getPosition());

        //przez biegun

        assertEquals(pos3,animal3.getPosition());
        assertEquals(pos4,animal4.getPosition());
        assertEquals(pos5,animal5.getPosition());
        assertEquals(pos6,animal6.getPosition());
        assertEquals(pos7,animal7.getPosition());
        assertEquals(pos8,animal8.getPosition());

        assertEquals(MapDirection.NORTH,animal3.getDirection());
        assertEquals(MapDirection.NORTHEAST,animal4.getDirection());
        assertEquals(MapDirection.NORTHWEST,animal5.getDirection());
        assertEquals(MapDirection.SOUTH,animal6.getDirection());
        assertEquals(MapDirection.SOUTHEAST,animal7.getDirection());
        assertEquals(MapDirection.SOUTHWEST,animal8.getDirection());

        assertEquals(new Vector2d(6,6),animal9.getPosition());
        assertEquals(MapDirection.NORTHEAST,animal9.getDirection());
    }
}
