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
    private double realTimePerStep;
    private ForceLaws forceLaw;
    private List<SimulatorObserver> observers;

    public PhysicsSimulator(double realTimePerStep, ForceLaws forceLaw) {
        Objects.requireNonNull(forceLaw);
        if(realTimePerStep < 0)
            throw new IllegalArgumentException("realTimePerStep must be > 0");
        actualTime = 0.0;
        this.realTimePerStep = realTimePerStep;
        this.forceLaw = forceLaw;
        bodies = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public void advance() {
        bodies.forEach(Body::resetForce);
        forceLaw.apply(bodies);
        bodies.forEach(body -> body.move(realTimePerStep));
        actualTime += realTimePerStep;
        observers.forEach(observer -> observer.onAdvance(bodies, actualTime));
    }

    public void addBody(Body body) {
        Objects.requireNonNull(body);
        if(bodies.contains(body))
            throw new IllegalArgumentException("The body has already been added!");
        else {
            bodies.add(body);
            observers.forEach(observer -> observer.onBodyAdded(bodies, body));
        }
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

    public void reset() {
        bodies.clear();
        actualTime = 0;
        observers.forEach(observer -> observer.onReset(bodies, actualTime, realTimePerStep, forceLaw.toString()));
    }

    public void setRealTimePerStep(double realTimePerStep) {
        if(realTimePerStep < 0)
            throw new IllegalArgumentException("realTimePerStep must be > 0");
        this.realTimePerStep = realTimePerStep;
        observers.forEach(observer -> observer.onDeltaTimeChanged(realTimePerStep));
    }

    public void setForceLaw(ForceLaws forceLaw) {
        Objects.requireNonNull(forceLaw);
        this.forceLaw = forceLaw;
        observers.forEach(observer -> observer.onForceLawsChanged(forceLaw.toString()));
    }

    public void addObserver(SimulatorObserver observer) {
        Objects.requireNonNull(observer);
        if(!observers.contains(observer)) {
            observers.add(observer);
            observer.onRegister(bodies,actualTime, realTimePerStep, forceLaw.toString());
        }
    }

    @Override
    public String toString() {
        return this.getState().toString();
    }
}
