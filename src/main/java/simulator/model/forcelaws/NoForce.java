package simulator.model.forcelaws;

import simulator.model.bodies.FluentBuilder.Body;
import java.util.List;

public class NoForce implements ForceLaws {

    public NoForce() {
        // Nothing to construct.
    }

    @Override
    public void apply(List<? extends Body> bs) {
        // No force to apply.
    }
}
