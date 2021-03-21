package simulator.factories;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.misc.Vector2D;
import simulator.model.bodies.Body;
import static org.junit.jupiter.api.Assertions.*;

class BasicBodyBuilderTest {
    BasicBodyBuilder basicBodyBuilder;
    Body body;
    String id;
    Vector2D velocity;
    Vector2D position;
    double mass;
    JSONObject info = new JSONObject();

    @BeforeEach
    void before() {
        basicBodyBuilder = new BasicBodyBuilder();
        id = "b1";
        velocity = new Vector2D(0.0e00, 1.4e03);
        position = new Vector2D(-3.5e10, 0.0e00);
        mass = 3.0e28;
        body = new Body.Builder().id(id).velocity(velocity).position(position).mass(mass).build();
        info.put("type", "basic");
    }

    @Test
    void testCreateInstance() {
        info.put("data", body.getState());
        Body b2 = basicBodyBuilder.createInstance(info);

        assertEquals(b2, body);
    }

    @Test
    void testInvalidCreateInstance() {
        info.put("type", "mlb");
        info.put("data", body.getState());
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidBody);
        assertEquals("Typetag doesn't match with the builder constructor!", exception.getMessage());
    }

    private void executeInvalidBody() {
        basicBodyBuilder.createInstance(info);
    }
}
