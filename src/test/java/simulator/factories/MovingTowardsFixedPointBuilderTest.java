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
        data.put("c", new Vector2D(2, 0).asJSONArray());
        data.put("g", 5);
        forceLawBuilder = new MovingTowardsFixedPointBuilder();
        forceLaw = new MovingTowardsFixedPoint();
    }

    @Test
    void testCreateDefaultInstance() {
        info.put("data", new JSONObject());
        MovingTowardsFixedPoint expectedForceLaw = new MovingTowardsFixedPoint(new Vector2D(), 9.81);
        forceLaw = forceLawBuilder.createInstance(info);
        assertEquals(expectedForceLaw.getG(), forceLaw.getG());
        assertEquals(expectedForceLaw.getOrigin(), forceLaw.getOrigin());
    }

    @Test
    void testGetDescription() {
        String expectedDescription = "Moving towards a fixed point";
        assertEquals(expectedDescription, forceLawBuilder.getDescription());
    }

    @Test
    void testCreateCustomInstance() {
        info.put("data", data);
        MovingTowardsFixedPoint expectedForceLaw = new MovingTowardsFixedPoint(new Vector2D(2,0), 5);
        forceLaw = forceLawBuilder.createInstance(info);
        assertEquals(expectedForceLaw.getOrigin(), forceLaw.getOrigin());
        assertEquals(expectedForceLaw.getG(), forceLaw.getG());
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
