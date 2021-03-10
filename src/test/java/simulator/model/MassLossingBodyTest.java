package simulator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import simulator.misc.Vector2D;
import simulator.model.bodies.Body;
import simulator.model.bodies.MassLossingBody;

import static org.junit.jupiter.api.Assertions.*;

class MassLossingBodyTest {
    private MassLossingBody body;
    private String id;
    private Vector2D velocity;
    private Vector2D position;
    private double mass;
    private double lossFactor;
    private double lossFrequency;

    @BeforeEach
    void before() {
        id = "b1";
        velocity = new Vector2D(0.0e00, 1.4e03);
        position = new Vector2D(-3.5e10, 0.0e00);
        mass = 3.0e28;
        lossFactor = 0.50;
        lossFrequency = 1;
        body = new MassLossingBody.Builder().id(id).velocity(velocity).position(position).mass(mass)
                .lossFactor(lossFactor).lossFrequency(lossFrequency)
                .build();
    }

    @Test
    void testBody() {
        Vector2D force = new Vector2D(0,0);
        assertEquals("b1", body.getId());
        assertEquals(velocity, body.getVelocity());
        assertEquals(position, body.getPosition());
        assertEquals(mass, body.getMass());
        assertEquals(force, body.getForce());
        assertEquals(lossFactor, body.getLossFactor());
        assertEquals(lossFrequency, body.getLossFrequency());
    }

    @Test
    void testBodyWithInvalidMass() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidMass);
        assertEquals("Body mass has to be > 0!", exception.getMessage());
    }

    @Test
    void testBodyWithInvalidLossFactor() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidLossFactor);
        assertEquals("Loss factor must be in range: [0-1]!", exception.getMessage());
    }

    @Test
    void testBodyWithInvalidLossFrequency() {
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidLossFrequency);
        assertEquals("A MassLossingBody cant gain mass! \"lossFrequency\" must be >= 0",
                        exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, 1.5, 1, 2, 3, 4, 5, 10})
    void testMove(double time) {
        Vector2D force = new Vector2D(1.00e29, 0.00);
        double expectedMass = mass / Math.floor(Math.pow(2, time));
        body.addForce(force);
        body.move(time);
        assertEquals(expectedMass, body.getMass());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, -2, -2.5})
    void testMoveInvalidTime(double time) {
        Vector2D force = new Vector2D(1.00e29, 0.00);
        double expectedMass = mass;
        body.addForce(force);
        body.move(time);
        assertEquals(expectedMass, mass);
    }

    private void executeInvalidMass() {
        new Body.Builder().id(id)
                .velocity(velocity).position(position)
                .mass(0).build();
    }

    private void executeInvalidLossFactor() {
        new MassLossingBody.Builder().id(id)
                .velocity(velocity).position(position)
                .mass(mass).lossFactor(-1).build();
    }

    private void executeInvalidLossFrequency() {
        new MassLossingBody.Builder().id(id)
                .velocity(velocity).position(position)
                .mass(mass).lossFactor(lossFactor).lossFrequency(-1).build();
    }
}
