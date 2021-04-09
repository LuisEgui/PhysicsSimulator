package simulator.factories;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.control.EpsilonEqualStates;
import static org.junit.jupiter.api.Assertions.*;

class EpsilonEqualStateBuilderTest {
    EpsilonEqualStateBuilder epsilonEqualStateBuilder;
    EpsilonEqualStates epsilonEqualStates;
    JSONObject info = new JSONObject();
    JSONObject data = new JSONObject();

    @BeforeEach
    void before() {
        info.put("type", "epseq");
        data.put("eps", 0.1);
        epsilonEqualStateBuilder = new EpsilonEqualStateBuilder();
        epsilonEqualStates = new EpsilonEqualStates(0.1);
    }

    @Test
    void testCreateDefaultInstance() {
        info.put("data", new JSONObject());
        EpsilonEqualStates expectedStateComparator = new EpsilonEqualStates(0.0);
        epsilonEqualStates = epsilonEqualStateBuilder.createInstance(info);
        assertEquals(expectedStateComparator.getEpsilon(), epsilonEqualStates.getEpsilon());
    }

    @Test
    void testGetDescription() {
        String expectedDescription = "An epsilon equal state comparator";
        assertEquals(expectedDescription, epsilonEqualStateBuilder.getDescription());
    }

    @Test
    void testCreateCustomInstance() {
        info.put("data", data);
        EpsilonEqualStates expectedStateComparator = new EpsilonEqualStates(0.1);
        epsilonEqualStates = epsilonEqualStateBuilder.createInstance(info);
        assertEquals(expectedStateComparator.getEpsilon(), epsilonEqualStates.getEpsilon());
    }
}
