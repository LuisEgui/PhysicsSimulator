package simulator.factories;

import org.json.JSONObject;
import simulator.model.bodies.FluentBuilder.Body;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BodyBasedFactory implements Factory<Body> {

    private List<Builder<? extends Body>> builders;

    public BodyBasedFactory(List<Builder<? extends Body>> builders) {
        this.builders = new ArrayList<>(builders);
    }

    @Override
    public Body createInstance(JSONObject info) throws IllegalArgumentException {
        Objects.requireNonNull(info);
        for(Builder<? extends Body> builder : builders) {
            try {
                return builder.createInstance(info);
            } catch (IllegalArgumentException exception) {
                // Continue
            }
        }
        throw new IllegalArgumentException("There's no valid template to create an instance!");
    }

    @Override
    public List<JSONObject> getInfo() {
        List<JSONObject> overallBuildersInfo = new ArrayList<>();
        for(Builder<? extends Body> builder : builders) {
            try {
                overallBuildersInfo.add(builder.getBuilderInfo());
            } catch (NullPointerException exception) {
                // Continue
            }
        }
        return overallBuildersInfo;
    }
}
