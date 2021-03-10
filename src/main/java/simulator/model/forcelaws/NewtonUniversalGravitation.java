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

        for(Body b : bodies) {
            for(int i = 1; i < bodies.size(); ++i) {
                force = calculateForceBetweenBodies(b, bodies.get(i));
                b.addForce(force);
                bodies.get(i).addForce(force.scale(-1));
            }
        }

        // Reset force for no mass bodies:
        bs.stream().filter(body -> body.getMass() <= 0).forEach(Body::resetForce);
    }

    private Vector2D calculateForceBetweenBodies(Body bi, Body bj) {
        Vector2D direction = bj.getPosition().minus(bi.getPosition());
        Vector2D force = direction.unitVector();
        force.scale(G* bi.getMass() * bj.getMass()).scale(1/direction.magnitude());
        return force;
    }

}
