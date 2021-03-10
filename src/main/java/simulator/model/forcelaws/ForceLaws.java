package simulator.model.forcelaws;

import simulator.model.bodies.FluentBuilder.Body;
import java.util.List;

public interface ForceLaws {
	void apply(List<? extends Body> bs);
}
