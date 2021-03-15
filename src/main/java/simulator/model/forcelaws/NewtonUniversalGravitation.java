package simulator.model.forcelaws;

import simulator.misc.Vector2D;
import simulator.model.bodies.FluentBuilder.Body;
import java.util.List;
import java.util.stream.Collectors;

public class NewtonUniversalGravitation implements ForceLaws {

    private static final double G = 6.67E-11;

    public NewtonUniversalGravitation() {
        // Nothing to construct.
    }


    @Override
    public void apply(List<? extends Body> bs) {
        List<Body> bodies = bs.stream().filter(body -> body.getMass() > 0).collect(Collectors.toList());
        Vector2D force;

        for(int i = 0; i < bodies.size(); i++) {
            for(int j = i+1; j < bodies.size(); j++) {
                force = calculateForceBetweenBodies(bodies.get(i), bodies.get(j));
                bodies.get(i).addForce(force);
                bodies.get(j).addForce(force.scale(-1));
            }
        }

        // Reset force for no mass bodies:
        bs.stream().filter(body -> body.getMass() <= 0).forEach(Body::resetForce);
    }

    private Vector2D calculateForceBetweenBodies(Body bi, Body bj) {
        Vector2D direction = bj.getPosition().minus(bi.getPosition());
        Vector2D force = direction.unitVector();
        force = force.scale(G* bi.getMass() * bj.getMass()).scale(1/Math.pow(direction.magnitude(), 2));
        return force;
    }

}
