package simulator.control;

import simulator.model.bodies.FluentBuilder.Body;

import java.util.List;
import java.util.stream.Collectors;

public class EpsilonEqualStates extends EqualStates {

    private final double epsilon;

    public EpsilonEqualStates(double epsilon) {
        this.epsilon = epsilon;
    }

    @Override
    public boolean equalBodyList(List<Body> bodyListOne, List<Body> bodyListTwo) {
        List<Body> bodyList = bodyListOne.stream().filter(body2 -> bodyListTwo.stream()
                .anyMatch(body1 -> body1.getId().equals(body2.getId()) && body1.getMass() == body2.getMass() &&
                        body1.getVelocity().distanceTo(body2.getVelocity()) <= epsilon &&
                        body1.getPosition().distanceTo(body2.getPosition()) <= epsilon &&
                        body1.getForce().distanceTo(body2.getForce()) <= epsilon))
                .collect(Collectors.toList());
        return bodyListOne.size() == bodyList.size() && bodyListTwo.size() == bodyList.size();
    }
}
