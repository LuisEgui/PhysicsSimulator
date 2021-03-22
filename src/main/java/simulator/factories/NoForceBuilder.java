package simulator.factories;

import org.json.JSONObject;
import simulator.model.forcelaws.NoForce;

import java.util.Objects;

public class NoForceBuilder extends Builder<NoForce> {

    private static final String DESCRIPTION = "A force law that doesn't apply any aditional force";

    @Override
    public NoForce createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        NoForce noForce;
        if(super.type == TypeTag.NF) {
            noForce = new NoForce();
            return noForce;
        } else
            throw new IllegalArgumentException("Typetag doesn't match with the builder constructor!");
    }

    @Override
    public JSONObject createData() {
        JSONObject template = new JSONObject();
        template.put("type", super.type.toString().toLowerCase());
        template.put("desc", DESCRIPTION);
        template.put("data", new JSONObject());
        return template;
    }
}
