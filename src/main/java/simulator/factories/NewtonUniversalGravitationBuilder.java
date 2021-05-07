package simulator.factories;

import org.json.JSONObject;
import simulator.model.forcelaws.NewtonUniversalGravitation;

import java.util.Objects;

public class NewtonUniversalGravitationBuilder extends Builder<NewtonUniversalGravitation> {

    private static final String DESCRIPTION = "Newton's law of universal gravitation";
    private NewtonUniversalGravitation newtonUniversalGravitation;

    public NewtonUniversalGravitationBuilder() {
        super.type = TypeTag.NLUG;
        newtonUniversalGravitation = new NewtonUniversalGravitation();
    }

    @Override
    public NewtonUniversalGravitation createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        if(super.type == TypeTag.NLUG) {
            newtonUniversalGravitation = new NewtonUniversalGravitation();
            if (data.has("G")) {
                double g = data.getDouble("G");
                newtonUniversalGravitation = new NewtonUniversalGravitation(g);
            } else
                newtonUniversalGravitation = new NewtonUniversalGravitation();
            return newtonUniversalGravitation;
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
        data.put("G", "the gravitational constant (" + newtonUniversalGravitation.getG() + ")");
        return data;
    }
}
