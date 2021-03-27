package simulator.control;

import org.json.JSONObject;

public interface StateComparator {
	boolean compare(JSONObject s1, JSONObject s2);
}
