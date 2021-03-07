package simulator.model.FluentBluilder;

import org.json.JSONObject;
import simulator.misc.Vector2D;

import java.util.Objects;

public abstract class Body {
    protected String id;
    private Vector2D velocity;
    private Vector2D force;
    private Vector2D position;
    private double mass;

    protected Body(Builder<?> builder) {
        this.id = builder.id;
        this.velocity = new Vector2D(builder.velocity);
        this.force = new Vector2D(builder.force);
        this.position = new Vector2D(builder.position);
        this.mass = builder.mass;
    }

    public abstract static class Builder<T extends Builder<T>> {
        protected String id;
        protected Vector2D velocity;
        protected Vector2D force;
        protected Vector2D position;
        protected double mass;

        public Builder() {
            velocity = new Vector2D();
            force = new Vector2D();
            position = new Vector2D();
        }

        public T id(String id) {
            Objects.requireNonNull(id);
            this.id = id;
            return self();
        }

        public T velocity(Vector2D velocity) {
            Objects.requireNonNull(velocity);
            this.velocity = new Vector2D(velocity);
            return self();
        }

        public T position(Vector2D position) {
            Objects.requireNonNull(position);
            this.position = new Vector2D(position);
            return self();
        }

        public T mass(double mass) {
            if(mass <= 0)
                throw new IllegalArgumentException("Body mass has to be > 0!");
            this.mass = mass;
            return self();
        }

        public T force(Vector2D force) {
            Objects.requireNonNull(force);
            this.force = new Vector2D(force);
            return self();
        }

        protected abstract T self();
        public abstract Body build();
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
        this.force = this.force.plus(force);
    }

    public void resetForce() {
        force = force.scale(0);
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

    @Override
    public String toString() {
        return getState().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        simulator.model.Body other = (simulator.model.Body) obj;
        return id.equals(other.getId());
    }
}
