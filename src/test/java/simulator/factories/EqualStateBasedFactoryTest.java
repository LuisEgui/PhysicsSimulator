package simulator.factories;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import simulator.control.EpsilonEqualStates;
import simulator.control.StateComparator;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EqualStateBasedFactoryTest {
    JSONObject info;
    List<Builder<? extends StateComparator>> stateBuilders;
    Factory<StateComparator> factory;

    @BeforeEach
    void before() {
        info = createTestData();
        stateBuilders = new ArrayList<>();
        stateBuilders.add(new MassEqualStateBuilder());
        stateBuilders.add(new EpsilonEqualStateBuilder());
        factory = new EqualStateBasedFactory(stateBuilders);
    }

    private static JSONObject createTestData() {
        JSONObject info = new JSONObject();
        info.put("type", "epseq");
        info.put("data", new JSONObject().put("eps", 0.1));
        return info;
    }

    @Test
    void testCreateInstance() {
        EpsilonEqualStates eps = (EpsilonEqualStates) factory.createInstance(info);
        assertEquals(0.1, eps.getEpsilon());
    }

    @Disabled
    void testGetInfo() {
        EpsilonEqualStates eps = (EpsilonEqualStates) factory.createInstance(info);
        List<JSONObject> desc = factory.getInfo();
        info.put("desc", "An epsilon equal state comparator");
        info.put("data", new JSONObject().put("eps", 0.1));
        for (JSONObject jsonObject : desc) {
            assertEquals(info.toString(), jsonObject.toString());
        }
    }
}
