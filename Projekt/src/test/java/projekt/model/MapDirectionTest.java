package projekt.model;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapDirectionTest {
    @Test
    public void toStringTest() {
        MapDirection[] dirs = MapDirection.values();
        String[] expectedVals = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};

        for (int i = 0; i < dirs.length; i++) {
            assertEquals(expectedVals[i], dirs[i].toString());
        }
    }

    @Test
    public void toUnitVectorTest() {
        MapDirection[] dirs = MapDirection.values();
        Vector2d[] expectedVectors = {new Vector2d(0, 1), new Vector2d(1, 1), new Vector2d(1, 0),
                                    new Vector2d(1, -1), new Vector2d(0, -1), new Vector2d(-1, -1),
                                    new Vector2d(-1, 0), new Vector2d(-1, 1)};

        for (int i = 0; i < dirs.length; i++) {
            assertEquals(expectedVectors[i], dirs[i].toUnitVector());
        }
    }

    @Test
    public void fromIntTest() {
        MapDirection[] expectedDirs = MapDirection.values();
        int[] values = {0, 1, 2, 3, 4, 5, 6, 7};

        for (int i = 0; i < expectedDirs.length; i++) {
            assertEquals(expectedDirs[i], MapDirection.fromInt(values[i]));
        }
        assertEquals(MapDirection.NORTH, MapDirection.fromInt(8));
    }

    @Test
    public void toIntTest() {
        MapDirection[] dirs = MapDirection.values();
        int[] expectedVals = {0, 1, 2, 3, 4, 5, 6, 7};

        for (int i = 0; i < dirs.length; i++) {
            assertEquals(expectedVals[i], dirs[i].toInt());
        }
    }

    @Test
    public void addTest() {
        MapDirection dir1 = MapDirection.NORTH;
        MapDirection dir2 = MapDirection.WEST;

        dir1 = dir1.add(7);
        dir2 = dir2.add(3);

        assertEquals(MapDirection.NORTHWEST, dir1);
        assertEquals(MapDirection.NORTHEAST, dir2);
    }

    @Test
    public void reverseTest() {
        MapDirection dir1 = MapDirection.NORTH;
        MapDirection dir2 = MapDirection.NORTHWEST;
        MapDirection dir3 = MapDirection.SOUTHEAST;

        dir1 = dir1.reverse();
        dir2 = dir2.reverse();
        dir3 = dir3.reverse();

        assertEquals(MapDirection.SOUTH, dir1);
        assertEquals(MapDirection.SOUTHEAST, dir2);
        assertEquals(MapDirection.NORTHWEST, dir3);
    }
}
