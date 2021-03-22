package simulator.factories;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.model.forcelaws.NewtonUniversalGravitation;

import static org.junit.jupiter.api.Assertions.*;

class NewtonUniversalGravitationBuilderTest {
    NewtonUniversalGravitationBuilder newtonUniversalGravitationBuilder;
    NewtonUniversalGravitation newtonUniversalGravitation;
    JSONObject info = new JSONObject();
    JSONObject data = new JSONObject();

    @BeforeEach
    void before() {
        info.put("type", "nlug");
        data.put("G", 6.67e10-11);
        newtonUniversalGravitationBuilder = new NewtonUniversalGravitationBuilder();
        newtonUniversalGravitation = new NewtonUniversalGravitation();
    }

    @Test
    void testCreateInstance() {
        info.put("data", data);
        NewtonUniversalGravitation expectedForceLaw = newtonUniversalGravitationBuilder.createInstance(info);
        assertNotEquals(null, expectedForceLaw);
        System.out.println(newtonUniversalGravitationBuilder.getBuilderInfo().toString());
    }

    @Test
    void testInvalidCreateInstance() {
        info.put("type", "basic");
        info.put("data", data);
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidNLUG);
        assertEquals("Typetag doesn't match with the builder constructor!", exception.getMessage());
    }

    private void executeInvalidNLUG() {
        newtonUniversalGravitationBuilder.createInstance(info);
    }
}
