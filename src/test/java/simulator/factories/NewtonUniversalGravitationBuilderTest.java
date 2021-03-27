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
        data.put("G", 3.35e10-11);
        newtonUniversalGravitationBuilder = new NewtonUniversalGravitationBuilder();
        newtonUniversalGravitation = new NewtonUniversalGravitation();
    }

    @Test
    void testCreateDefaultInstance() {
        info.put("data", new JSONObject());
        NewtonUniversalGravitation expectedForceLaw = new NewtonUniversalGravitation();
        newtonUniversalGravitation = newtonUniversalGravitationBuilder.createInstance(info);
        assertEquals(expectedForceLaw.getG(), newtonUniversalGravitation.getG());
    }

    @Test
    void testGetDescription() {
        String expectedDescription = "Newton's law of universal gravitation";
        assertEquals(expectedDescription, newtonUniversalGravitationBuilder.getDescription());
    }

    @Test
    void testCreateCustomInstance() {
        info.put("data", data);
        NewtonUniversalGravitation expectedForceLaw = new NewtonUniversalGravitation(3.35e10-11);
        newtonUniversalGravitation = newtonUniversalGravitationBuilder.createInstance(info);
        assertEquals(expectedForceLaw.getG(), newtonUniversalGravitation.getG());
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
