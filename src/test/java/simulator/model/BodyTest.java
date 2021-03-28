package simulator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import simulator.misc.Vector2D;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import simulator.model.bodies.Body;

class BodyTest {
    private Body body;
    private String id;
    private Vector2D velocity;
    private Vector2D position;
    private double mass;

    @BeforeEach
    void before() {
        id = "b1";
        velocity = new Vector2D(0.0e00, 1.4e03);
        position = new Vector2D(-3.5e10, 0.0e00);
        mass = 3.0e28;
        body = new Body.Builder().id(id).velocity(velocity).position(position).mass(mass).build();
    }

    @Test
    void testBody() {
        Vector2D force = new Vector2D(0,0);
        assertEquals("b1", body.getId());
        assertEquals(velocity, body.getVelocity());
        assertEquals(position, body.getPosition());
        assertEquals(mass, body.getMass());
        assertEquals(force, body.getForce());
    }

    @Test
    void testBodyWithInvalidMass() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidMass);
        assertEquals("Body mass has to be > 0!", exception.getMessage());
    }

    @Test
    void testAddForce() {
        Vector2D force = new Vector2D(510, 0);
        body.addForce(force);
        assertEquals(force, body.getForce());
    }

    @Test
    void testResetForce() {
        Vector2D force = new Vector2D(510, 0);
        Vector2D reset = new Vector2D();
        body.addForce(force);
        body.resetForce();
        assertEquals(reset, body.getForce());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 1, 5, 10, 20, 30})
    void testMove(double time) {
        Vector2D force = new Vector2D(1.00e29, 0.00);
        double expectedVelocity = velocity.plus(force.scale(1/mass).scale(time)).magnitude();
        double expectedPosition = position.plus(
                velocity.scale(time).plus(force.scale(1/mass).scale(0.5*Math.pow(time, 2)))
        ).magnitude();
        body.addForce(force);
        body.move(time);
        assertEquals(expectedVelocity, body.getVelocity().magnitude(), 0.01);
        assertEquals(Double.valueOf(expectedPosition).longValue(),
                Double.valueOf(body.getPosition().magnitude()).longValue(),
                1e4);
    }

    @Test
    void testEquals() {
        Body expectedEqualsBody = new Body.Builder().id("b1").velocity(velocity).position(position)
                .mass(mass).build();
        Body notEqualBody = new Body.Builder().id("b2").velocity(velocity).position(position)
                .mass(mass).build();
        assertNotEquals(notEqualBody, body);
        assertEquals(expectedEqualsBody, body);
        assertTrue(expectedEqualsBody.equals(body) && body.equals(expectedEqualsBody));
        assertNotEquals(null, body.equals(null));
        assertEquals(true, body.equals(body));
        assertFalse(body.equals("This is not a body class"));
        assertEquals(expectedEqualsBody.hashCode(), body.hashCode());
        assertNotEquals(notEqualBody.hashCode(), body.hashCode());
    }

    private void executeInvalidMass() {
        new Body.Builder().id(id)
                .velocity(velocity).position(position)
                .mass(0).build();
    }
}
