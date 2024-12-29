package projekt;

import projekt.model.MapDirection;
import projekt.model.Vector2d;
import projekt.model.WorldMap;

public class DarwinWorld {

    public static void main(String[] args) {
        System.out.println("działa");
        Vector2d v = new Vector2d(1,1);
        System.out.println(v);
        System.out.println(MapDirection.NORTHWEST.reverse());
        WorldMap map = new WorldMap(new Vector2d(15, 15), new Vector2d(0, 0), 10, 30);
        map.spawnPlants();
        System.out.println(map);
    }
}
