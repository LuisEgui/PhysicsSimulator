package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Vector2D;
import simulator.model.forcelaws.MovingTowardsFixedPoint;

import java.util.Objects;

public class MovingTowardsFixedPointBuilder extends Builder<MovingTowardsFixedPoint> {

    private static final String DESCRIPTION = "A force law that does apply force to a fixed point in the space";
    private MovingTowardsFixedPoint forceLaw;

    public MovingTowardsFixedPointBuilder() {
        super.type = TypeTag.MTCP;
        forceLaw = new MovingTowardsFixedPoint();
    }

    @Override
    public MovingTowardsFixedPoint createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        double g;
        Vector2D origin;
        if(super.type == TypeTag.MTCP) {
            if(data.has("c") && data.has("g")) {
                JSONArray jOrigin = data.getJSONArray("c");
                origin = new Vector2D((double) jOrigin.get(0), (double) jOrigin.get(1));
                g = data.getDouble("g");
                forceLaw = new MovingTowardsFixedPoint(origin, g);
            } else
                forceLaw = new MovingTowardsFixedPoint();
            return forceLaw;
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
        data.put("c", forceLaw.getOrigin().asJSONArray());
        data.put("g", forceLaw.getG());
        return data;
    }
}
