package simulator.factories;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import simulator.model.forcelaws.ForceLaws;
import simulator.model.forcelaws.NewtonUniversalGravitation;

import java.util.ArrayList;
import java.util.List;

class ForceLawBasedFactoryTest {
    JSONObject info;
    List<Builder<? extends ForceLaws>> forceLawBuilders;
    Factory<ForceLaws> factory;

    @BeforeEach
    void before() {
        info = createTestData();
        forceLawBuilders = new ArrayList<>();
        forceLawBuilders.add(new NewtonUniversalGravitationBuilder());
        forceLawBuilders.add(new NoForceBuilder());
        factory = new ForceLawBasedFactory(forceLawBuilders);
    }

    private static JSONObject createTestData() {
        JSONObject info = new JSONObject();
        info.put("type", "nlug");
        info.put("data", new JSONObject());
        return info;
    }

    @Test
    void testCreateInstance() {
        NewtonUniversalGravitation nug = (NewtonUniversalGravitation) factory.createInstance(info);
        assertEquals(6.67E-11, nug.getG());
    }

    @Ignore
    void testGetInfo() {
        NewtonUniversalGravitation nug = (NewtonUniversalGravitation) factory.createInstance(info);
        List<JSONObject> desc = factory.getInfo();
        info.put("desc", "Newton's law of universal gravitation");
        info.put("data", new JSONObject().put("G", 6.67E-11));
        for (JSONObject jsonObject : desc) {
            assertEquals(info.toString(), jsonObject.toString());
        }
    }

    @Test
    void testInvalidCreateInstance() {
        info.put("type", "basic");
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidType);
        assertEquals("There's no valid template to create an instance!", exception.getMessage());
    }

    private void executeInvalidType() {
        NewtonUniversalGravitation nug = (NewtonUniversalGravitation) factory.createInstance(info);
    }
}
