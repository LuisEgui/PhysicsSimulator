package simulator.model.forceLaws;

import simulator.model.bodies.Body;

import java.util.List;

public interface ForceLaws {
	public void apply(List<Body> bs);
}
