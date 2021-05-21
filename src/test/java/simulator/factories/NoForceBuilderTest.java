package simulator.factories;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import simulator.model.forcelaws.NoForce;
import static org.junit.jupiter.api.Assertions.*;

class NoForceBuilderTest {
    NoForceBuilder noForceBuilder;
    NoForce noForce;
    JSONObject info = new JSONObject();

    @BeforeEach
    void before() {
        info.put("type", "nf");
        noForceBuilder = new NoForceBuilder();
        noForce = new NoForce();
    }

    @Test
    void testCreateInstance() {
        info.put("data", new JSONObject());
        NoForce expectedNoForce = noForceBuilder.createInstance(info);
        assertNotEquals(null, expectedNoForce);
    }

    @Test
    void testGetDescription() {
        String expectedDescription = "No Force";
        assertEquals(expectedDescription, noForceBuilder.getDescription());
    }


    @Disabled
    void testInvalidCreateInstance() {
        // To be fixed
        info.put("type", "nlug");
        info.put("data", new JSONObject());
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidNoForce);
        assertEquals("Typetag doesn't match with the builder constructor!", exception.getMessage());
    }

    private void executeInvalidNoForce() {
        noForceBuilder.createTheInstance(info);
    }
}
