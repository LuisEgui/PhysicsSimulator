package simulator.model.forcelaws;

import simulator.model.bodies.Body;

import java.util.List;

public interface ForceLaws {
	void apply(List<Body> bs);
}
