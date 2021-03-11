package simulator.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.misc.Vector2D;
import simulator.model.bodies.Body;
import simulator.model.forcelaws.NewtonUniversalGravitation;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class NewtonUniversalGravitationTest {

    NewtonUniversalGravitation nug;
    Body b1, b2;
    List<Body> bodies;

    @BeforeEach
    void before() {
            b1 = new Body.Builder().id("b1").velocity(new Vector2D())
                    .position(new Vector2D()).mass(10).build();
            b2 = new Body.Builder().id("b2").velocity(new Vector2D())
                    .position(new Vector2D(0,1)).mass(5).build();
            nug = new NewtonUniversalGravitation();
            bodies = new ArrayList<>();
            bodies.add(b1); bodies.add(b2);
    }

    @Test
    void testApply() {
        Vector2D expectedForceOverBodyOne = new Vector2D(0,6.67E-11*b1.getMass()*b2.getMass());
        nug.apply(bodies);
        assertEquals(expectedForceOverBodyOne, bodies.get(0).getForce());
        assertEquals(expectedForceOverBodyOne.scale(-1), bodies.get(1).getForce());
    }
}
