package projekt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlantTest {
    @Test
    public void getPositionTest() {
        Plant plant1 = new Plant(new Vector2d(4, 2));
        Plant plant2 = new Plant(new Vector2d(0, 0));
        Plant plant3 = new Plant(new Vector2d(-8, 2));

        assertEquals(new Vector2d(4, 2), plant1.getPosition());
        assertEquals(new Vector2d(0, 0), plant2.getPosition());
        assertEquals(new Vector2d(-8, 2), plant3.getPosition());
    }

    @Test
    public void toStringTest() {
        Plant plant1 = new Plant(new Vector2d(4, 2));
        Plant plant2 = new Plant(new Vector2d(0, -7));

        assertEquals("*", plant1.toString());
        assertEquals("*", plant2.toString());
    }
}
