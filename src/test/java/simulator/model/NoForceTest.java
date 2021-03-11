package simulator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.misc.Vector2D;
import simulator.model.bodies.Body;
import simulator.model.forcelaws.NewtonUniversalGravitation;
import simulator.model.forcelaws.NoForce;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NoForceTest {
    Body b1, b2;
    List<Body> bodies;
    NoForce nf;

    @BeforeEach
    void before() {
        b1 = new Body.Builder().id("b1").velocity(new Vector2D())
                .position(new Vector2D()).mass(10).build();
        b2 = new Body.Builder().id("b2").velocity(new Vector2D())
                .position(new Vector2D(0,1)).mass(5).build();
        nf = new NoForce();
        bodies = new ArrayList<>();
        bodies.add(b1); bodies.add(b2);
    }

    @Test
    void testApply() {
        Vector2D expectedForceOverBodyOne = new Vector2D();
        nf.apply(bodies);
        assertEquals(expectedForceOverBodyOne, bodies.get(0).getForce());
        assertEquals(expectedForceOverBodyOne, bodies.get(1).getForce());
    }

}
