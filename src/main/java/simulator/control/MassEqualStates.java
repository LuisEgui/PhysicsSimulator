package simulator.control;
import simulator.model.bodies.FluentBuilder.Body;

import java.util.List;
import java.util.stream.Collectors;

public class MassEqualStates extends EqualStates {

    public MassEqualStates() {
        // Nothing to construct.
    }

    public boolean equalBodyList(List<Body> bodyListOne, List<Body> bodyListTwo) {
        List<Body> bodyList = bodyListOne.stream().filter(body2 -> bodyListTwo.stream()
                .anyMatch(body1 -> body1.getId().equals(body2.getId()) && body1.getMass() == body2.getMass()))
                .collect(Collectors.toList());
        return bodyListOne.size() == bodyList.size() && bodyListTwo.size() == bodyList.size();
    }
}
