package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.bodies.MassLossingBody;

import java.util.Objects;

public class MassLossingBodyBuilder extends Builder<MassLossingBody> {

    private static final String DESCRIPTION = "A body that losses mass each time its moves!";
    private MassLossingBody body;

    public MassLossingBodyBuilder() {
        super.type = TypeTag.MLB;
        body = new MassLossingBody.Builder().id("").velocity(new Vector2D())
                .position(new Vector2D()).mass(1.0)
                .lossFactor(0.5).lossFrequency(0.5)
                .build();
    }

    @Override
    public MassLossingBody createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        if(super.type == TypeTag.MLB) {
            return create(data);
        } else
            throw new IllegalArgumentException("Typetag doesn't match with the builder constructor!");
    }

    public MassLossingBody create(JSONObject data) {
        JSONArray jVelocity = data.getJSONArray("v");
        JSONArray jPosition = data.getJSONArray("p");
        String id = data.getString("id");
        Vector2D velocity = new Vector2D(Double.parseDouble(jVelocity.get(0).toString()),
                Double.parseDouble(jVelocity.get(1).toString()));
        Vector2D position = new Vector2D(Double.parseDouble(jPosition.get(0).toString()),
                Double.parseDouble(jPosition.get(1).toString()));
        double mass = data.getDouble("m");
        double lossFrequency = data.getDouble("freq");
        double lossFactor = data.getDouble("factor");
        body = new MassLossingBody.Builder().id(id).velocity(velocity).position(position).mass(mass)
                .lossFactor(lossFactor).lossFrequency(lossFrequency)
                .build();
        return body;
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
