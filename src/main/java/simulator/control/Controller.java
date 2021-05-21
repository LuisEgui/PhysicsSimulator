package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.Factory;
import simulator.model.PhysicsSimulator;
import simulator.model.SimulatorObserver;
import simulator.model.bodies.FluentBuilder.Body;
import simulator.model.forcelaws.ForceLaws;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;

public class Controller {

    private PhysicsSimulator physicsSimulator;
    private Factory<? extends Body> bodyFactory;
    private Factory<ForceLaws> forceLawsFactory;

    public Controller(PhysicsSimulator physicsSimulator, Factory<? extends Body> bodyFactory, Factory<ForceLaws> forceLawsFactory) {
        Objects.requireNonNull(physicsSimulator); Objects.requireNonNull(bodyFactory);
        this.physicsSimulator = physicsSimulator;
        this.bodyFactory = bodyFactory;
        this.forceLawsFactory = forceLawsFactory;
    }

    public List<JSONObject> getForceLawInfo() {
        return forceLawsFactory.getInfo();
    }

    public void setForceLaws(JSONObject info) {
        Objects.requireNonNull(info);
        physicsSimulator.setForceLaw(forceLawsFactory.createInstance(info));
    }

    public void setDeltaTime(double deltaTime) {
        physicsSimulator.setRealTimePerStep(deltaTime);
    }

    public void addObserver(SimulatorObserver observer) {
        physicsSimulator.addObserver(observer);
    }

    public void loadBodies(InputStream in) {
        Objects.requireNonNull(in);
        JSONObject jsonInput = new JSONObject(new JSONTokener(in));
        JSONArray bodies = jsonInput.getJSONArray("bodies");
        for(int i = 0; i < bodies.length(); i++) {
            JSONObject jBody = bodies.getJSONObject(i);
            physicsSimulator.addBody(bodyFactory.createInstance(jBody));
        }
    }

    public void reset() {
        physicsSimulator.reset();
    }

    public void run(int steps) {
        if(steps < 0)
            throw new IllegalArgumentException("Can not execute negative number steps!");
        for(int i = 0; i < steps; i++)
            physicsSimulator.advance();
    }

    public void run(int steps, OutputStream output) {
        if(steps < 0)
            throw new IllegalArgumentException("Can not execute negative number steps!");
        Objects.requireNonNull(output);
        PrintStream printStream = new PrintStream(output);
        printStream.println("{");
        printStream.println("\"states\": [");
        printStream.println(physicsSimulator.getState().toString());
        printStream.println(",");
        for(int i = 1; i <= steps; i++) {
            physicsSimulator.advance();
            printStream.println(physicsSimulator.getState().toString());
            if(i < steps)
                printStream.println(",");
        }
        printStream.println("]");
        printStream.println("}");
        printStream.close();
    }

    public void run(int steps, InputStream expectedOutput, OutputStream output, StateComparator comparator) {
        if(steps < 0)
            throw new IllegalArgumentException("Can not execute negative number steps!");
        Objects.requireNonNull(output); Objects.requireNonNull(comparator); Objects.requireNonNull(expectedOutput);
        JSONObject jsonOut = new JSONObject(new JSONTokener(expectedOutput));
        PrintStream printStream = new PrintStream(output);
        printStream.println("{");
        printStream.println("\"states\": [");
        printStream.println(physicsSimulator.getState().toString());
        printStream.println(",");
        JSONArray jsonExpectedOut = jsonOut.getJSONArray("states");
        for(int i = 1; i <= steps; i++) {
            physicsSimulator.advance();
            if(!comparator.compare(physicsSimulator.getState(), jsonExpectedOut.getJSONObject(i)))
                throw new InputMismatchException("Comparison failed");
            printStream.println(physicsSimulator.getState().toString());
            if(i < steps)
                printStream.println(",");
        }
        printStream.println("]");
        printStream.println("}");
    }
}
