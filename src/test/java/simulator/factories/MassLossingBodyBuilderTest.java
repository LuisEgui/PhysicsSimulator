package simulator.factories;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.misc.Vector2D;
import simulator.model.bodies.MassLossingBody;
import static org.junit.jupiter.api.Assertions.*;

class MassLossingBodyBuilderTest {
    MassLossingBodyBuilder massLossingBodyBuilder;
    MassLossingBody body;
    String id;
    Vector2D velocity;
    Vector2D position;
    double mass;
    private double lossFactor;
    private double lossFrequency;
    JSONObject info = new JSONObject();

    @BeforeEach
    void before() {
        massLossingBodyBuilder = new MassLossingBodyBuilder();
        id = "b1";
        velocity = new Vector2D(0.0e00, 1.4e03);
        position = new Vector2D(-3.5e10, 0.0e00);
        mass = 3.0e28;
        lossFactor = 0.50;
        lossFrequency = 1;
        body = new MassLossingBody.Builder().id(id).velocity(velocity).position(position).mass(mass)
                .lossFactor(lossFactor).lossFrequency(lossFrequency)
                .build();
        info.put("type", "mlb");
    }

    @Test
    void testCreateInstance() {
        info.put("data", body.getState());
        MassLossingBody b2 = massLossingBodyBuilder.createInstance(info);
        assertEquals(b2, body);
    }

    @Test
    void testGetDescription() {
        String expectedDescription = "A body that losses mass each time its moves!";
        assertEquals(expectedDescription, massLossingBodyBuilder.getDescription());
    }

    @Test
    void testInvalidCreateInstance() {
        info.put("type", "basic");
        info.put("data", body.getState());
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidMassLossingBody);
        assertEquals("Typetag doesn't match with the builder constructor!", exception.getMessage());
    }

    private void executeInvalidMassLossingBody() {
        massLossingBodyBuilder.createInstance(info);
    }
}
