package simulator.factories;

import org.json.JSONObject;
import simulator.model.forcelaws.MovingTowardsFixedPoint;

import java.util.Objects;

public class MovingTowardsFixedPointBuilder extends Builder<MovingTowardsFixedPoint> {

    private static final String DESCRIPTION = "A force law that does apply force to a fixed point in the space";

    @Override
    public MovingTowardsFixedPoint createTheInstance(JSONObject data) {
        Objects.requireNonNull(data);
        MovingTowardsFixedPoint forceLaw;
        if(super.type == TypeTag.MTCP) {
            forceLaw = new MovingTowardsFixedPoint();
            return forceLaw;
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
