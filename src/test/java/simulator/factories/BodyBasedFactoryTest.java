package simulator.factories;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.misc.Vector2D;
import simulator.model.bodies.FluentBuilder.Body;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class BodyBasedFactoryTest {
    JSONObject info;
    List<Builder<? extends Body>> bodyBuilders;
    Factory<? extends Body> factory;

    @BeforeEach
    void before() {
        info = createTestData();
        bodyBuilders = new ArrayList<>();
        bodyBuilders.add(new MassLossingBodyBuilder());
        bodyBuilders.add(new BasicBodyBuilder());
        factory = new BodyBasedFactory(bodyBuilders);
    }

    private static JSONObject createTestData() {
        String id = "b1";
        Vector2D velocity = new Vector2D(0.0e00, 1.4e03);
        Vector2D position = new Vector2D(-3.5e10, 0.0e00);
        double mass = 3.0e28;
        Body body = new simulator.model.bodies.Body.Builder().id(id).velocity(velocity).position(position).mass(mass).build();
        JSONObject info = new JSONObject();
        info.put("type", "basic");
        info.put("data", body.getState());
        return info;
    }

    @Test
    void testCreateInstance() {
        Body body = factory.createInstance(info);
        assertEquals("b1", body.getId());
        assertEquals(new Vector2D(0.0e00, 1.4e03) , body.getVelocity());
        assertEquals(new Vector2D(-3.5e10, 0.0e00), body.getPosition());
        assertEquals(3.0e28, body.getMass());
    }

    @Test
    void testGetInfo() {
        Body body = factory.createInstance(info);
        List<JSONObject> desc = factory.getInfo();
        info.put("desc", "Basic body");
        for (JSONObject jsonObject : desc) {
            assertEquals(info.toString(), jsonObject.toString());
        }
    }

    @Test
    void testInvalidCreateInstance() {
        info.put("type", "nf");
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidType);
        assertEquals("There's no valid template to create an instance!", exception.getMessage());
    }

    private void executeInvalidType() {
        Body body = factory.createInstance(info);
    }
}
