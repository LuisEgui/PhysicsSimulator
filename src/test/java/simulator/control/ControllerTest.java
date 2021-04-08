package simulator.control;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simulator.factories.*;
import simulator.model.PhysicsSimulator;
import simulator.model.bodies.FluentBuilder.Body;
import simulator.model.forcelaws.MovingTowardsFixedPoint;
import simulator.model.forcelaws.NewtonUniversalGravitation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

class ControllerTest {
    Controller controller;
    PhysicsSimulator physicsSimulator;
    List<Builder<? extends Body>> bodyBuilders;
    Factory<? extends Body> factory;

    @BeforeEach
    void before() {
        NewtonUniversalGravitation nlug = new NewtonUniversalGravitation();
        physicsSimulator = new PhysicsSimulator(10000, nlug);
        bodyBuilders = new ArrayList<>();
        bodyBuilders.add(new MassLossingBodyBuilder());
        bodyBuilders.add(new BasicBodyBuilder());
        factory = new BodyBasedFactory(bodyBuilders);
        controller = new Controller(physicsSimulator, factory);
    }

    @Test
    void testLoadBodies() throws FileNotFoundException {
        InputStream in = new FileInputStream("resources/examples/ex1.2body.json");
        controller.loadBodies(in);
        assertNotEquals("{\"bodies\":[],\"time\":0}", physicsSimulator.getState().toString());
    }

    @Test
    void testRunCase1() {
        try {
            InputStream in = new FileInputStream("resources/examples/ex1.2body.json");
            controller.loadBodies(in);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        assertDoesNotThrow(() -> {
            try {
                OutputStream out = new FileOutputStream("resources/output/myout.json");
                controller.run(10000, out);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Test
    void testComparisonRunCase1() {
        try {
            InputStream in = new FileInputStream("resources/examples/ex1.2body.json");
            controller.loadBodies(in);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        assertDoesNotThrow(() -> {
            try {
                EpsilonEqualStates eps = new EpsilonEqualStates(0.1);
                OutputStream out = new FileOutputStream("resources/output/myout.json");
                InputStream expectedOut = new FileInputStream("resources/output/out.1.json");
                controller.run(10000, expectedOut, out, eps);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Test
    void testRunCase2() {
        try {
            MovingTowardsFixedPoint mtfp = new MovingTowardsFixedPoint();
            physicsSimulator = new PhysicsSimulator(10000, mtfp);
            controller = new Controller(physicsSimulator, factory);
            InputStream in = new FileInputStream("resources/examples/ex1.2body.json");
            controller.loadBodies(in);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        assertDoesNotThrow(() -> {
            try {
                OutputStream out = new FileOutputStream("resources/output/myout2.json");
                controller.run(10000, out);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Test
    void testComparisonRunCase2() {
        try {
            MovingTowardsFixedPoint mtfp = new MovingTowardsFixedPoint();
            physicsSimulator = new PhysicsSimulator(10000, mtfp);
            controller = new Controller(physicsSimulator, factory);
            InputStream in = new FileInputStream("resources/examples/ex1.2body.json");
            controller.loadBodies(in);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        assertDoesNotThrow(() -> {
            try {
                EpsilonEqualStates eps = new EpsilonEqualStates(0.1);
                OutputStream out = new FileOutputStream("resources/output/myout2.json");
                InputStream expectedOut = new FileInputStream("resources/output/out.2.json");
                controller.run(10000, expectedOut, out, eps);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Test
    void testComparisonRunCase3() {
        try {
            InputStream in = new FileInputStream("resources/examples/ex2.3body.json");
            controller.loadBodies(in);
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
        assertDoesNotThrow(() -> {
            try {
                EpsilonEqualStates eps = new EpsilonEqualStates(0.1);
                OutputStream out = new FileOutputStream("resources/output/myout3.json");
                InputStream expectedOut = new FileInputStream("resources/output/out.3.json");
                controller.run(10000, expectedOut, out, eps);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
        });
    }

    @Test
    void testInvalidStepsRun() {
        try {
            InputStream in = new FileInputStream("resources/examples/ex1.2body.json");
            controller.loadBodies(in);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
        Throwable exception = assertThrows(IllegalArgumentException.class,
                this::executeInvalidSteps);
        assertEquals("Can not execute negative number steps!", exception.getMessage());
    }

    private void executeInvalidSteps() {
        EpsilonEqualStates eps = new EpsilonEqualStates(0.1);
        try {
            OutputStream out = new FileOutputStream("resources/output/myout.json");
            InputStream expectedOut = new FileInputStream("resources/output/out.1.json");
            controller.run(-5, expectedOut, out, eps);
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }
}
