package simulator.factories;

import org.json.JSONObject;
import simulator.model.forcelaws.ForceLaws;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForceLawBasedFactory implements Factory<ForceLaws> {

    private List<Builder<? extends ForceLaws>> builders;

    public ForceLawBasedFactory(List<Builder<? extends ForceLaws>> builders) {
        this.builders = new ArrayList<>(builders);
    }

    @Override
    public ForceLaws createInstance(JSONObject info) throws IllegalArgumentException {
        Objects.requireNonNull(info);
        for(Builder<? extends ForceLaws> builder : builders) {
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
        for(Builder<? extends ForceLaws> builder : builders) {
            overallBuildersInfo.add(builder.getBuilderInfo());
        }
        return overallBuildersInfo;
    }
}
