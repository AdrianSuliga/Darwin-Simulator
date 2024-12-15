package projekt.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Vector2dTest {
    @Test
    public void getXTest() {
        Vector2d vec1 = new Vector2d(10, 10);
        Vector2d vec2 = new Vector2d(-7, 3);
        Vector2d vec3 = new Vector2d(-3, -7);
        Vector2d vec4 = new Vector2d(0, 0);

        assertEquals(10, vec1.getX());
        assertEquals(-7, vec2.getX());
        assertEquals(-3, vec3.getX());
        assertEquals(0, vec4.getX());
    }

    @Test
    public void getYTest() {
        Vector2d vec1 = new Vector2d(10, 10);
        Vector2d vec2 = new Vector2d(-7, 3);
        Vector2d vec3 = new Vector2d(-3, -7);
        Vector2d vec4 = new Vector2d(0, 0);

        assertEquals(10, vec1.getY());
        assertEquals(3, vec2.getY());
        assertEquals(-7, vec3.getY());
        assertEquals(0, vec4.getY());
    }

    @Test
    public void toStringTest() {
        Vector2d vec1 = new Vector2d(10, 13);
        Vector2d vec2 = new Vector2d(-9, 1);
        Vector2d vec3 = new Vector2d(0, -2);

        assertEquals("(10,13)", vec1.toString());
        assertEquals("(-9,1)", vec2.toString());
        assertEquals("(0,-2)", vec3.toString());
    }

    @Test
    public void precedesTest() {
        Vector2d vec1 = new Vector2d(0, 3);
        Vector2d vec2 = new Vector2d(-3, -3);
        Vector2d vec3 = new Vector2d(0, 1);
        Vector2d vec4 = new Vector2d(0, 4);
        Vector2d vec5 = new Vector2d(2, 5);

        assertTrue(vec1.precedes(vec1));
        assertTrue(vec2.precedes(vec1));
        assertTrue(vec3.precedes(vec1));
        assertFalse(vec4.precedes(vec1));
        assertFalse(vec5.precedes(vec1));
    }

    @Test
    public void followsTest() {
        Vector2d vec1 = new Vector2d(0, 3);
        Vector2d vec2 = new Vector2d(-3, -3);
        Vector2d vec3 = new Vector2d(0, 1);
        Vector2d vec4 = new Vector2d(0, 4);
        Vector2d vec5 = new Vector2d(2, 5);

        assertTrue(vec1.follows(vec1));
        assertTrue(vec4.follows(vec1));
        assertTrue(vec5.follows(vec1));
        assertFalse(vec2.follows(vec1));
        assertFalse(vec3.follows(vec1));
    }

    @Test
    public void addTest() {
        Vector2d vec1 = new Vector2d(2, 3);
        Vector2d vec2 = new Vector2d(-3, -3);
        Vector2d vec3 = new Vector2d(0, 1);

        assertEquals(new Vector2d(-1, 0), vec1.add(vec2));
        assertEquals(new Vector2d(-1, 0), vec2.add(vec1));
        assertEquals(new Vector2d(2, 4), vec1.add(vec3));
        assertEquals(new Vector2d(2, 4), vec3.add(vec1));
        assertEquals(new Vector2d(-3, -2), vec2.add(vec3));
        assertEquals(new Vector2d(-3, -2), vec3.add(vec2));
    }

    @Test
    public void subtractTest() {
        Vector2d vec1 = new Vector2d(5, 1);
        Vector2d vec2 = new Vector2d(-5, 1);
        Vector2d vec3 = new Vector2d(0, 0);

        assertEquals(new Vector2d(10, 0), vec1.subtract(vec2));
        assertEquals(new Vector2d(-10, 0), vec2.subtract(vec1));
        assertEquals(vec1, vec1.subtract(vec3));
        assertEquals(new Vector2d(-5, -1), vec3.subtract(vec1));
        assertEquals(vec2, vec2.subtract(vec3));
        assertEquals(new Vector2d(5, -1), vec3.subtract(vec2));
    }

    @Test
    public void upperRightTest() {
        Vector2d vec1 = new Vector2d(5, 1);
        Vector2d vec2 = new Vector2d(2, 7);
        Vector2d vec3 = new Vector2d(-3, -6);
        Vector2d vec4 = new Vector2d(-2, -9);

        assertEquals(new Vector2d(5, 7), vec1.upperRight(vec2));
        assertEquals(new Vector2d(5, 7), vec2.upperRight(vec1));
        assertEquals(new Vector2d(-2, -6), vec3.upperRight(vec4));
        assertEquals(new Vector2d(-2, -6), vec4.upperRight(vec3));
    }

    @Test
    public void lowerLeftTest() {
        Vector2d vec1 = new Vector2d(5, 1);
        Vector2d vec2 = new Vector2d(-5, 1);
        Vector2d vec3 = new Vector2d(0, 0);
        Vector2d vec4 = new Vector2d(-2, -9);

        assertEquals(new Vector2d(-5, 1), vec1.lowerLeft(vec2));
        assertEquals(new Vector2d(-5, 1), vec2.lowerLeft(vec1));
        assertEquals(new Vector2d(-2, -9), vec3.lowerLeft(vec4));
        assertEquals(new Vector2d(-2, -9), vec4.lowerLeft(vec3));
    }

    @Test
    public void oppositeTest() {
        Vector2d vec1 = new Vector2d(5, 1);
        Vector2d vec2 = new Vector2d(-5, 1);
        Vector2d vec3 = new Vector2d(0, 0);
        Vector2d vec4 = new Vector2d(-2, -9);

        assertEquals(new Vector2d(-5, -1), vec1.opposite());
        assertEquals(new Vector2d(5, -1), vec2.opposite());
        assertEquals(new Vector2d(0, 0), vec3.opposite());
        assertEquals(new Vector2d(2, 9), vec4.opposite());
    }

    @Test
    public void equalsTest() {
        Vector2d vec1 = new Vector2d(5, 1);
        Vector2d vec2 = new Vector2d(5, 1);
        Vector2d vec3 = new Vector2d(0, 0);
        int x = 1;

        assertTrue(vec1.equals(vec1));
        assertTrue(vec1.equals(vec2));
        assertFalse(vec1.equals(vec3));
        assertFalse(vec1.equals(null));
        assertFalse(vec1.equals(x));
    }
}
