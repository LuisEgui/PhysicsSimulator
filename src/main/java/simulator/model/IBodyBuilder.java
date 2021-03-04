package simulator.model;

import simulator.misc.Vector2D;

public interface IBodyBuilder {
    interface Id {
        Velocity id(String id);
    }

    interface Velocity {
        Position velocity(Vector2D v);
    }

    interface Position {
        Mass position(Vector2D p);
    }

    interface Mass {
        Optionals mass(double m);
    }

    interface Optionals {
        Body build();
    }
}
