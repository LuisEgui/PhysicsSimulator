package simulator.model.forcelaws;

import simulator.misc.Vector2D;
import simulator.model.bodies.FluentBuilder.Body;

import java.util.List;
import java.util.stream.Collectors;

public class MovingTowardsFixedPoint implements ForceLaws {

    private static final double G = 9.81;

    public MovingTowardsFixedPoint() {
        // Nothing to construct.
    }

    @Override
    public void apply(List<? extends Body> bs) {
        List<Body> bodies = bs.stream().filter(body -> body.getMass() > 0).collect(Collectors.toList());
        Vector2D force;

        for (Body b : bodies) {
            force = calculateForceTowardsOrigin(b);
            b.addForce(force);
        }

        // Reset force for no mass bodies:
        bs.stream().filter(body -> body.getMass() <= 0).forEach(Body::resetForce);
    }

    private Vector2D calculateForceTowardsOrigin(Body bi) {
        return bi.getPosition().scale(-G * bi.getMass());
    }
}
