package simulator.factories;

import org.json.JSONObject;
import simulator.model.forcelaws.MovingTowardsFixedPoint;

import java.util.Objects;

public class MovingTowardsFixedPointBuilder extends Builder<MovingTowardsFixedPoint> {

    private static final String DESCRIPTION = "A force law that does apply force to a fixed point in the space";
    private MovingTowardsFixedPoint forceLaw;

    @Override
    public MovingTowardsFixedPoint createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        if(super.type == TypeTag.MTCP) {
            forceLaw = new MovingTowardsFixedPoint();
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
