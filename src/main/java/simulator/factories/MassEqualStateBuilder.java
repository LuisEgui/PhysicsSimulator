package simulator.factories;

import org.json.JSONObject;
import simulator.control.MassEqualStates;

import java.util.Objects;

public class MassEqualStateBuilder extends Builder<MassEqualStates> {

    private static final String DESCRIPTION = "A mass equal state comparator";

    public MassEqualStateBuilder() {
        super.type = TypeTag.MASSEQ;
    }

    @Override
    public MassEqualStates createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        if(super.type == TypeTag.MASSEQ) {
            return new MassEqualStates();
        } else
            throw new IllegalArgumentException("Typetag doesn't match with the builder constructor!");
    }

    @Override
    public JSONObject createData() {
        return new JSONObject();
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
