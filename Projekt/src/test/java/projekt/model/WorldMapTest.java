package projekt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class WorldMapTest {
    private MapMovementLogicHandler handler = new GlobeLogic();

    private Vector2d defLowerLeft = new Vector2d(0,0);
    private Vector2d defUpperRight = new Vector2d(9,9);

    private List<Integer> defGenome = List.of(0);

    @Test
    public void creationAndPlacementTest(){
        Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
        Animal a1 = new Animal(defLowerLeft,10,defGenome);
        Animal a2 = new Animal(defUpperRight,10,defGenome);
        Vector2d pos = new Vector2d(5,5);
        animalMap.put(defLowerLeft,new HashSet<Animal>(List.of(a1)));
        animalMap.put(defUpperRight,new HashSet<Animal>(List.of(a2)));

        WorldMap map = new WorldMap(defUpperRight,defLowerLeft,animalMap,
                1,1,handler);
        assertEquals(2,map.getAnimalsCount());
        assertEquals(1,map.getAnimalMap().get(defUpperRight).size());
        assertEquals(1,map.getAnimalMap().get(defLowerLeft).size());
        Animal a3 = new Animal(defLowerLeft,10,defGenome);
        Animal a4 = new Animal(defUpperRight,10,defGenome);
        Animal a5 = new Animal(pos,10,defGenome);
        map.placeAnimal(a3);
        assertEquals(3,map.getAnimalsCount());
        assertEquals(2,map.getAnimalMap().get(defLowerLeft).size());
        map.placeAnimal(a4);
        assertEquals(4,map.getAnimalsCount());
        assertEquals(2,map.getAnimalMap().get(defUpperRight).size());
        map.placeAnimal(a5);
        assertEquals(5,map.getAnimalsCount());
        assertEquals(1,map.getAnimalMap().get(pos).size());
    }

    @Test
    public void removeDeadAnimalsTest(){
        Map<Vector2d, HashSet<Animal>> animalMap = new HashMap<>();
        Animal a1 = new Animal(defLowerLeft,0,defGenome);
        Animal a2 = new Animal(defUpperRight,10,defGenome);
        Vector2d pos = new Vector2d(5,5);
        animalMap.put(defLowerLeft,new HashSet<Animal>(List.of(a1)));
        animalMap.put(defUpperRight,new HashSet<Animal>(List.of(a2)));

        WorldMap map = new WorldMap(defUpperRight,defLowerLeft,animalMap,
                6,1,handler);

        map.moveAnimals();
        map.removeDeadAnimals();
        assertEquals(1,map.getAnimalsCount());
        assertNull(map.getAnimalMap().get(defLowerLeft));
        map.moveAnimals();
        map.removeDeadAnimals();
        assertEquals(0,map.getAnimalsCount());
        assertNull(map.getAnimalMap().get(defUpperRight));

    }
}
