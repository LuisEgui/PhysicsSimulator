package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.bodies.Body;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BasicBodyBuilder extends Builder<Body> {

    private static final String DESCRIPTION = "Basic body";
    private Body body;

    public BasicBodyBuilder() {
        super.type = TypeTag.BASIC;
        body = new Body.Builder().id("").velocity(new Vector2D())
                .position(new Vector2D()).mass(1.0).build();
    }

    @Override
    public Body createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        if(super.type == TypeTag.BASIC) {
            return create(data);
        } else
            throw new IllegalArgumentException("Typetag doesn't match with the builder constructor!");
    }

    public Body create(JSONObject data) {
        JSONArray jVelocity = data.getJSONArray("v");
        JSONArray jPosition = data.getJSONArray("p");
        String id = data.getString("id");
        Vector2D velocity = new Vector2D(Double.parseDouble(jVelocity.get(0).toString()),
                Double.parseDouble(jVelocity.get(1).toString()));
        Vector2D position = new Vector2D(Double.parseDouble(jPosition.get(0).toString()),
                Double.parseDouble(jPosition.get(1).toString()));
        double mass = data.getDouble("m");
        body = new Body.Builder().id(id).velocity(velocity).position(position).mass(mass).build();
        return body;
    }

    public List<simulator.model.bodies.FluentBuilder.Body> getBodiesFromJSONArray(JSONArray jsonArray) {
        Objects.requireNonNull(jsonArray);
        BasicBodyBuilder bodyBuilder = new BasicBodyBuilder();
        List<simulator.model.bodies.FluentBuilder.Body> bodies = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jBody = jsonArray.getJSONObject(i);
            simulator.model.bodies.FluentBuilder.Body newBody = bodyBuilder.create(jBody);
            bodies.add(newBody);
        }
        return bodies;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public JSONObject createData() {
        return body.getState();
    }
}
