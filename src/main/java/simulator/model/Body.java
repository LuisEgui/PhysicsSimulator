package simulator.model;

import org.json.JSONObject;
import simulator.misc.Vector2D;

import java.util.*;

public class Body {
    protected String id;
    private Vector2D velocity;
    private Vector2D force;
    private Vector2D position;
    private double mass;

    public Body() {
    }

    public IBodyBuilder.Id builder() {
        return new BodyBuilder();
    }

    public static class BodyBuilder implements IBodyBuilder.Id, IBodyBuilder.Velocity, IBodyBuilder.Position,
            IBodyBuilder.Mass, IBodyBuilder.Optionals {
        private Body body;

        public BodyBuilder() {
            this.body = new Body();
        }

        @Override
        public IBodyBuilder.Velocity id(String id) {
            Objects.requireNonNull(id);
            this.body.id = id;
            return this;
        }

        @Override
        public IBodyBuilder.Position velocity(Vector2D v) {
            Objects.requireNonNull(v);
            this.body.velocity = new Vector2D(v);
            return this;
        }

        @Override
        public IBodyBuilder.Mass position(Vector2D p) {
            Objects.requireNonNull(p);
            this.body.position = new Vector2D(p);
            return this;
        }

        @Override
        public IBodyBuilder.Optionals mass(double m) {
            this.body.mass = m;
            return this;
        }

        @Override
        public Body build() {
            this.body.force = new Vector2D();
            return this.body;
        }
    }

    public String getId() {
        return id;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public Vector2D getForce() {
        return force;
    }

    public Vector2D getPosition() {
        return position;
    }

    public double getMass() {
        return mass;
    }

    public void addForce(Vector2D force) {
        this.force.plus(force);
    }

    public void resetForce() {
        force.scale(0);
    }

    public void move(double time) {
        Vector2D acceleration = force.scale(1/mass);
        velocity = velocity.plus(acceleration.scale(time));
        position = position.plus(velocity.scale(time).plus(acceleration.scale(0.5*Math.pow(time, 2))));
    }

    public JSONObject getState() {
        JSONObject state = new JSONObject();
        state.put("id", id);
        state.put("p", position.toString());
        state.put("v", velocity.toString());
        state.put("m", String.valueOf(mass));
        return state;
    }

    public String toString() {
        return getState().toString();
    }

}
