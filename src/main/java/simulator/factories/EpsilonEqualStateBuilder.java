package simulator.factories;

import org.json.JSONObject;
import simulator.control.EpsilonEqualStates;

import java.util.Objects;

public class EpsilonEqualStateBuilder extends Builder<EpsilonEqualStates> {

    private static final String DESCRIPTION = "An epsilon equal state comparator";
    private EpsilonEqualStates epsilonEqualStates;

    public EpsilonEqualStateBuilder() {
        super.type = TypeTag.EPSEQ;
        epsilonEqualStates = new EpsilonEqualStates(0.5);
    }

    @Override
    public EpsilonEqualStates createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        if(super.type == TypeTag.EPSEQ) {
            if(data.has("eps")) {
                double eps = data.getDouble("eps");
                epsilonEqualStates = new EpsilonEqualStates(eps);
            } else
                epsilonEqualStates = new EpsilonEqualStates(0.0);
            return epsilonEqualStates;
        } else
            throw new IllegalArgumentException("Typetag doesn't match with the builder constructor!");
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public JSONObject createData() {
        JSONObject data = new JSONObject();
        data.put("eps", epsilonEqualStates.getEpsilon());
        return data;
    }
}
