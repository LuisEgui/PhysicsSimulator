package simulator.factories;

import org.json.JSONException;
import org.json.JSONObject;
import simulator.model.forcelaws.NewtonUniversalGravitation;

import java.util.Objects;

public class NewtonUniversalGravitationBuilder extends Builder<NewtonUniversalGravitation> {

    private static final String DESCRIPTION = "Newton's law of universal gravitation";
    private NewtonUniversalGravitation newtonUniversalGravitation;

    @Override
    public NewtonUniversalGravitation createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        if(super.type == TypeTag.NLUG) {
            newtonUniversalGravitation = new NewtonUniversalGravitation();
            try {
                double g = data.getDouble("G");
                newtonUniversalGravitation = new NewtonUniversalGravitation(g);
            } catch (JSONException jsonException) {
                newtonUniversalGravitation = new NewtonUniversalGravitation();
            }
            return newtonUniversalGravitation;
        } else
            throw new IllegalArgumentException("Typetag doesn't match with the builder constructor!");
    }

    @Override
    public JSONObject createData() {
        JSONObject template = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("G", newtonUniversalGravitation.getG());
        template.put("type", super.type.toString().toLowerCase());
        template.put("desc", DESCRIPTION);
        template.put("data", data);
        return template;
    }
}
