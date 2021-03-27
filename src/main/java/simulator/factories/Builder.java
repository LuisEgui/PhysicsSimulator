package simulator.factories;

import org.json.JSONObject;
import java.util.Objects;

public abstract class Builder<T> {

    protected TypeTag type;

    protected Builder() {
        // Nothing to initialize.
    }

    public T createInstance(JSONObject info) {
        Objects.requireNonNull(info);
        type = TypeTag.valueOf(((String) info.get("type")).toUpperCase());
        return createTheInstance((JSONObject) info.get("data"));
    }

    public JSONObject getBuilderInfo() {
        JSONObject template = new JSONObject();
        template.put("type", type.toString().toLowerCase());
        template.put("desc", getDescription());
        template.put("data", createData());
        return template;
    }

    public abstract T createTheInstance(JSONObject data);
    public abstract JSONObject createData();
    public abstract String getDescription();
}
