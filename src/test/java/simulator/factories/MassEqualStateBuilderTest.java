package simulator.factories;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import simulator.control.MassEqualStates;

class MassEqualStateBuilderTest {
    MassEqualStateBuilder massEqualStateBuilder;
    MassEqualStates massEqualStates;
    JSONObject info = new JSONObject();

    @BeforeEach
    void before() {
        info.put("type", "masseq");
        massEqualStateBuilder = new MassEqualStateBuilder();
        massEqualStates = new MassEqualStates();
    }

    @Test
    void testCreateInstance() {
        info.put("data", new JSONObject());
        MassEqualStates expectedStateComparator = massEqualStateBuilder.createInstance(info);
        assertNotEquals(null, expectedStateComparator);
    }

    @Test
    void testGetDescription() {
        String expectedDescription = "A mass equal state comparator";
        assertEquals(expectedDescription, massEqualStateBuilder.getDescription());
    }


    @Disabled
    void testInvalidCreateInstance() {
        // To be fixed
        info.put("type", "nlug");
        info.put("data", new JSONObject());
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidStateComparator);
        assertEquals("Typetag doesn't match with the builder constructor!", exception.getMessage());
    }

    private void executeInvalidStateComparator() {
        massEqualStateBuilder.createTheInstance(info);
    }
}
