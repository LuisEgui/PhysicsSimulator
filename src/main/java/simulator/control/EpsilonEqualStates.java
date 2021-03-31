package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.factories.BasicBodyBuilder;
import simulator.model.bodies.FluentBuilder.Body;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EpsilonEqualStates implements StateComparator {

    private final double epsilon;

    public EpsilonEqualStates(double epsilon) {
        this.epsilon = epsilon;
    }

    private boolean equalBodyList(List<Body> bodyListOne, List<Body> bodyListTwo) {
        List<Body> bodyList = bodyListOne.stream().filter(body2 -> bodyListTwo.stream()
                .anyMatch(body1 -> body1.getId().equals(body2.getId()) && body1.getMass() == body2.getMass() &&
                        body1.getVelocity().distanceTo(body2.getVelocity()) <= epsilon &&
                        body1.getPosition().distanceTo(body2.getPosition()) <= epsilon &&
                        body1.getForce().distanceTo(body2.getForce()) <= epsilon))
                .collect(Collectors.toList());
        return bodyListOne.size() == bodyList.size() && bodyListTwo.size() == bodyList.size();
    }

    @Override
    public boolean compare(JSONObject s1, JSONObject s2) {
        Objects.requireNonNull(s1); Objects.requireNonNull(s2);
        JSONArray s1JSONArray;
        JSONArray s2JSONArray;
        List<Body> s1Bodies;
        List<Body> s2Bodies;
        BasicBodyBuilder bodyBuilder = new BasicBodyBuilder();

        if(s1.getDouble("time") != s2.getDouble("time"))
            return false;

        s1JSONArray = s1.getJSONArray("bodies");
        s2JSONArray = s2.getJSONArray("bodies");
        s1Bodies = bodyBuilder.getBodiesFromJSONArray(s1JSONArray);
        s2Bodies = bodyBuilder.getBodiesFromJSONArray(s2JSONArray);
        return equalBodyList(s1Bodies, s2Bodies);
    }
}
