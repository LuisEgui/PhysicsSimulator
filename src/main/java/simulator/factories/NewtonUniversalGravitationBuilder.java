package simulator.factories;

import org.json.JSONObject;
import simulator.model.forcelaws.NewtonUniversalGravitation;

import java.util.Objects;

public class NewtonUniversalGravitationBuilder extends Builder<NewtonUniversalGravitation> {

    private static final String DESCRIPTION = "Newton’s law of universal gravitation";

    @Override
    public NewtonUniversalGravitation createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        NewtonUniversalGravitation newtonUniversalGravitation;
        if(super.type == TypeTag.NLUG) {
            newtonUniversalGravitation = new NewtonUniversalGravitation();
            return newtonUniversalGravitation;
        } else
            throw new IllegalArgumentException("Typetag doesn't match with the builder constructor!");
    }

    @Override
    public JSONObject createData() {
        JSONObject template = new JSONObject();
        template.put("type", super.type.toString().toLowerCase());
        template.put("desc", DESCRIPTION);
        return template;
    }
}
