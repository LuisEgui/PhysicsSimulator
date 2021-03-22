package simulator.factories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.forcelaws.MovingTowardsFixedPoint;

import java.util.Objects;

public class MovingTowardsFixedPointBuilder extends Builder<MovingTowardsFixedPoint> {

    private static final String DESCRIPTION = "A force law that does apply force to a fixed point in the space";
    private MovingTowardsFixedPoint forceLaw;

    @Override
    public MovingTowardsFixedPoint createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        double g;
        Vector2D origin;
        if(super.type == TypeTag.MTCP) {
            try {
                JSONArray jOrigin = data.getJSONArray("c");
                origin = new Vector2D((double) jOrigin.get(0), (double) jOrigin.get(1));
                g = data.getDouble("g");
                forceLaw = new MovingTowardsFixedPoint(origin, g);
            } catch (JSONException jsonException) {
                forceLaw = new MovingTowardsFixedPoint();
            }
            return forceLaw;
        } else
            throw new IllegalArgumentException("Typetag doesn't match with the builder constructor!");
    }

    @Override
    public JSONObject createData() {
        JSONObject template = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("c", forceLaw.getOrigin().asJSONArray());
        data.put("g", forceLaw.getG());
        template.put("type", super.type.toString().toLowerCase());
        template.put("desc", DESCRIPTION);
        template.put("data", data);
        return template;
    }
}
