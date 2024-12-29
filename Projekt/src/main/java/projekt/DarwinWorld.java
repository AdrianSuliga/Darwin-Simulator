package projekt;

import projekt.model.MapDirection;
import projekt.model.Vector2d;
import projekt.model.WorldMap;

import java.sql.SQLOutput;

public class DarwinWorld {

    public static void main(String[] args) {
        System.out.println("działa");
        Vector2d v = new Vector2d(1,1);
        System.out.println(v);
        System.out.println(MapDirection.NORTHWEST.reverse());
    }
}
