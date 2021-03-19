package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.model.bodies.FluentBuilder.Body;
import simulator.model.forcelaws.ForceLaws;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhysicsSimulator {

    private List<Body> bodies;
    private double actualTime;
    private final double realTimePerStep;
    private final ForceLaws forceLaw;

    public PhysicsSimulator(double realTimePerStep, ForceLaws forceLaw) {
        Objects.requireNonNull(forceLaw);
        if(realTimePerStep < 0)
            throw new IllegalArgumentException("realTimePerStep must be > 0");
        actualTime = 0.0;
        this.realTimePerStep = realTimePerStep;
        this.forceLaw = forceLaw;
        bodies = new ArrayList<>();
    }

    public void advance() {
        bodies.forEach(Body::resetForce);
        forceLaw.apply(bodies);
        bodies.forEach(body -> body.move(realTimePerStep));
        actualTime += realTimePerStep;
    }

    public void addBody(Body body) {
        Objects.requireNonNull(body);
        if(bodies.contains(body))
            throw new IllegalArgumentException("The body has already been added!");
        else
            bodies.add(body);
    }

    public JSONObject getState() {
        JSONObject state = new JSONObject();
        JSONArray jBodies = new JSONArray();
        for(Body b : bodies) {
            JSONObject js = b.getState();
            jBodies.put(js);
        }
        state.put("time", actualTime);
        state.put("bodies", jBodies);
        return state;
    }

    @Override
    public String toString() {
        return this.getState().toString();
    }
}
