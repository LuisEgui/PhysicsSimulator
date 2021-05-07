package simulator.model;

import simulator.model.bodies.FluentBuilder.Body;
import java.util.List;

public interface SimulatorObserver {
    void onRegister(List<? extends Body> bodies, double time, double dt, String fLawsDesc);
    void onReset(List<? extends Body> bodies, double time, double dt, String fLawsDesc);
    void onBodyAdded(List<? extends Body> bodies, Body b);
    void onAdvance(List<? extends Body> bodies, double time);
    void onDeltaTimeChanged(double dt);
    void onForceLawsChanged(String fLawsDesc);
}
