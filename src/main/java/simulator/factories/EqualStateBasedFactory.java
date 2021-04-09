package simulator.factories;

import org.json.JSONObject;
import simulator.control.StateComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EqualStateBasedFactory implements Factory<StateComparator> {

    private List<Builder<? extends StateComparator>> builders;

    public EqualStateBasedFactory(List<Builder<? extends StateComparator>> builders) {
        this.builders = new ArrayList<>(builders);
    }

    @Override
    public StateComparator createInstance(JSONObject info) throws IllegalArgumentException {
        Objects.requireNonNull(info);
        for(Builder<? extends StateComparator> builder : builders) {
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
        for(Builder<? extends StateComparator> builder : builders) {
            try {
                overallBuildersInfo.add(builder.getBuilderInfo());
            } catch (NullPointerException exception) {
                // Continue
            }
        }
        return overallBuildersInfo;
    }
}
