package simulator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.misc.Vector2D;
import simulator.model.bodies.Body;
import simulator.model.forcelaws.MovingTowardsFixedPoint;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class MovintTowardsFixedPointTest {

    MovingTowardsFixedPoint mtfp;
    double G = 9.81;
    Body b1, b2;
    List<Body> bodies;

    @BeforeEach
    void before() {
        b1 = new Body.Builder().id("b1").velocity(new Vector2D())
                .position(new Vector2D()).mass(10).build();
        b2 = new Body.Builder().id("b2").velocity(new Vector2D())
                .position(new Vector2D(0,2)).mass(5).build();
        mtfp = new MovingTowardsFixedPoint();
        bodies = new ArrayList<>();
        bodies.add(b1); bodies.add(b2);
    }

    @Test
    void testMovingTowardsFixedPoint() {
        Vector2D expectedOrigin = new Vector2D(1,0);
        double expectedG = 5;
        mtfp = new MovingTowardsFixedPoint(new Vector2D(1,0), 5);
        assertEquals(expectedG, mtfp.getG());
        assertEquals(expectedOrigin, mtfp.getOrigin());
    }

    @Test
    void testApply() {
        Vector2D expectedForce;
        mtfp.apply(bodies);
        for (Body b : bodies) {
            expectedForce = b.getPosition().scale(-G * b.getMass());
            assertEquals(expectedForce, b.getForce());
        }
    }
}
