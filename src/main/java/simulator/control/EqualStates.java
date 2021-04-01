package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import simulator.factories.BasicBodyBuilder;
import simulator.model.bodies.FluentBuilder.Body;

import java.util.List;
import java.util.Objects;

public abstract class EqualStates implements StateComparator {

    public abstract boolean equalBodyList(List<Body> bodyListOne, List<Body> bodyListTwo);

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
