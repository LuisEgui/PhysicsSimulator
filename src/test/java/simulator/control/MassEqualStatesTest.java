package simulator.control;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.misc.Vector2D;
import simulator.model.PhysicsSimulator;
import simulator.model.bodies.Body;
import simulator.model.forcelaws.MovingTowardsFixedPoint;

import static org.junit.jupiter.api.Assertions.*;

class MassEqualStatesTest {
    MassEqualStates massEqualStates;
    JSONObject info1, info2;
    MovingTowardsFixedPoint mtfp;
    simulator.model.bodies.Body b1, b2;
    PhysicsSimulator physicsSimulator;

    @BeforeEach
    void before() {
        info1 = createInfoTestData(1, 1, 2, 3.0e28, 3.0e28);
        info2 = createInfoTestData(1, 1,2, 3.0e28, 3.0e28);
        massEqualStates = new MassEqualStates();
    }

    @Test
    void testCompareEqualStates() {
        info2 = createInfoTestData(1, 1, 2, 3.0e28, 3.0e28);
        boolean actual = massEqualStates.compare(info1, info2);
        assertTrue(actual);
    }

    @Test
    void testCompareNotEqualStates() {
        info2 = createInfoTestData(1, 1,3, 3.0e28, 3.0e28);
        boolean actual = massEqualStates.compare(info1, info2);
        assertFalse(actual);
        info2 = createInfoTestData(1, 1,2, 3.0e28, 3.0e28);
        physicsSimulator.advance();
        info2 = physicsSimulator.getState();
        actual = massEqualStates.compare(info1, info2);
        assertFalse(actual);
    }

    private JSONObject createInfoTestData(double time, int id1, int id2, double mass1, double mass2) {
        JSONObject info;
        b1 = new simulator.model.bodies.Body.Builder().id("b"+id1).velocity(new Vector2D())
                .position(new Vector2D()).mass(mass1).build();
        b2 = new Body.Builder().id("b"+id2).velocity(new Vector2D())
                .position(new Vector2D(0,2)).mass(mass2).build();
        mtfp = new MovingTowardsFixedPoint();
        physicsSimulator = new PhysicsSimulator(time, mtfp);
        physicsSimulator.addBody(b1); physicsSimulator.addBody(b2);
        info = physicsSimulator.getState();
        return info;
    }
}
