package simulator.factories;

import org.json.JSONObject;
import java.util.Objects;

public abstract class Builder<T> {

    protected TypeTag type;

    public Builder() {
        // Nothing to initialize.
    }

    public T createInstance(JSONObject info) {
        Objects.requireNonNull(info);
        type = TypeTag.valueOf(((String) info.get("type")).toUpperCase());
        return createTheInstance((JSONObject) info.get("data"));
    }

    // TODO
    public JSONObject getBuilderInfo() {
        return new JSONObject();
    }

    public abstract T createTheInstance(JSONObject data);
    public abstract JSONObject createData();
}
