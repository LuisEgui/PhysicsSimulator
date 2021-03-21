package simulator.misc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.lang.Math.pow;
import static org.junit.jupiter.api.Assertions.*;

class Vector2DTest {
    private Vector2D vector;

    @BeforeEach
    void before() {
        vector = new Vector2D(5, 10);
    }

    @Test
    void testVector() {
        vector = new Vector2D();
        assertEquals(0, vector.getX());
        assertEquals(0, vector.getY());
    }

    @Test
    void testVector2DDoubleDouble() {
        assertEquals(5, vector.getX());
        assertEquals(10, vector.getY());
    }

    @Test void testVector2D_Vector2D() {
        vector = new Vector2D(new Vector2D(1,0));
        assertEquals(1, vector.getX());
        assertEquals(0, vector.getY());
    }

    @Test
    void testDot() {
        Vector2D vAux = new Vector2D(1, 0);
        assertEquals(5, vector.dot(vAux));
    }

    @Test
    void testMagnitude() {
        assertEquals(5*pow(5, 0.5), vector.magnitude());
    }

    @Test
    void testPlus() {
        Vector2D vAux ;
        vAux = vector.plus(vector);
        assertEquals(10, vAux.getX());
        assertEquals(20, vAux.getY());
    }

    @Test
    void testMinus() {
        Vector2D vAux = new Vector2D(1,2);
        Vector2D vRef;
        vRef = vector.minus(vAux);
        assertEquals(4, vRef.getX());
        assertEquals(8, vRef.getY());

    }

    @Test
    void testDistanceTo() {
        Vector2D vAux = new Vector2D(10, 20);
        assertEquals(pow(125, 0.5), vector.distanceTo(vAux));
    }

    @Test
    void testScale() {
        vector = vector.scale(2);
        assertEquals(10, vector.getX());
        assertEquals(20, vector.getY());
    }

    @Test
    void testUnitVector() {
        vector = new Vector2D(5, 0);
        vector = vector.unitVector();
        assertEquals(1, vector.getX());
        assertEquals(0, vector.getY());

        vector = new Vector2D(0, 0);
        vector = vector.unitVector();
        assertEquals(0, vector.getX());
        assertEquals(0, vector.getY());
    }

    @Test
    void testToString() {
        assertEquals("[5.0,10.0]", vector.toString());
    }

    @Test
    void testEquals() {
        Vector2D vRef = new Vector2D(5, 10);
        Vector2D vAux = new Vector2D();
        assertEquals(vRef, vector);
        assertNotEquals(vAux, vector);
    }
}
