package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.bodies.Body;
import java.util.Objects;

public class BasicBodyBuilder extends Builder<Body> {

    public BasicBodyBuilder() {
        super();
    }

    @Override
    public Body createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        Body body;
        if(super.type == TypeTag.BASIC) {
            JSONArray jVelocity = data.getJSONArray("v");
            JSONArray jPosition = data.getJSONArray("p");
            String id = data.getString("id");
            Vector2D velocity = new Vector2D((double) jVelocity.get(0), (double) jVelocity.get(1));
            Vector2D position = new Vector2D((double) jPosition.get(0), (double) jPosition.get(1));
            double mass = data.getDouble("m");
            body = new Body.Builder().id(id).velocity(velocity).position(position).mass(mass).build();
            return body;
        } else
            throw new IllegalArgumentException("Typetag doesn't match with the builder constructor!");
    }

    @Override
    public JSONObject createData() {
        return null;
    }

}
