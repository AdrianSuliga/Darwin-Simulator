package projekt;

import projekt.model.Vector2d;
import projekt.model.WorldMap;

import java.sql.SQLOutput;

public class DarwinWorld {

    public static void main(String[] args) {
        WorldMap map = new WorldMap(new Vector2d(20, 20), new Vector2d(0, 0), 10, 5);
        map.spawnPlants();
    }
}
