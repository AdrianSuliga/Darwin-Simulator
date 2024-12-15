package projekt.model;

import org.junit.jupiter.api.Test;

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
}
