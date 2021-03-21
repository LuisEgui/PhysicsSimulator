package simulator.factories;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.misc.Vector2D;
import simulator.model.forcelaws.MovingTowardsFixedPoint;

import static org.junit.jupiter.api.Assertions.*;

class MovingTowardsFixedPointBuilderTest {
    MovingTowardsFixedPointBuilder forceLawBuilder;
    MovingTowardsFixedPoint forceLaw;
    JSONObject info = new JSONObject();
    JSONObject data = new JSONObject();

    @BeforeEach
    void before() {
        info.put("type", "mtcp");
        data.put("c", new Vector2D().toString());
        data.put("g", 9.81);
        forceLawBuilder = new MovingTowardsFixedPointBuilder();
        forceLaw = new MovingTowardsFixedPoint();
    }

    @Test
    void testCreateInstance() {
        info.put("data", data);
        MovingTowardsFixedPoint expectedForceLaw = forceLawBuilder.createInstance(info);
        assertNotEquals(null, expectedForceLaw);
    }

    @Test
    void testInvalidCreateInstance() {
        info.put("type", "basic");
        info.put("data", data);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidMTFP);
        assertEquals("Typetag doesn't match with the builder constructor!", exception.getMessage());
    }

    private void executeInvalidMTFP() {
        forceLawBuilder.createInstance(info);
    }
}
