package simulator.control;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import simulator.factories.Factory;
import simulator.model.PhysicsSimulator;
import simulator.model.bodies.FluentBuilder.Body;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.Objects;

public class Controller {

    private PhysicsSimulator physicsSimulator;
    private Factory<? extends Body> factory;

    public Controller(PhysicsSimulator physicsSimulator, Factory<? extends Body> factory) {
        Objects.requireNonNull(physicsSimulator); Objects.requireNonNull(factory);
        this.physicsSimulator = physicsSimulator;
        this.factory = factory;
    }

    public void loadBodies(InputStream in) {
        Objects.requireNonNull(in);
        JSONObject jsonInput = new JSONObject(new JSONTokener(in));
        JSONArray bodies = jsonInput.getJSONArray("bodies");
        for(int i = 0; i < bodies.length(); i++) {
            JSONObject jBody = bodies.getJSONObject(i);
            physicsSimulator.addBody(factory.createInstance(jBody));
        }
    }

    public void run(int steps, OutputStream output) {
        if(steps < 0)
            throw new IllegalArgumentException("Can not execute negative number steps!");
        Objects.requireNonNull(output);
        PrintStream printStream = new PrintStream(output);
        printStream.println("{");
        printStream.println("\"states\": [");
        for(int i = 1; i < steps; i++) {
            printStream.println(physicsSimulator.getState().toString());
            physicsSimulator.advance();
            if(i < steps - 1)
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
        JSONArray jsonExpectedOut = jsonOut.getJSONArray("states");
        for(int i = 0; i < steps; i++) {
            if(!comparator.compare(physicsSimulator.getState(), jsonExpectedOut.getJSONObject(i)))
                throw new InputMismatchException("Comparison failed");
            printStream.println(physicsSimulator.getState().toString());
            physicsSimulator.advance();
            if(i < steps - 1)
                printStream.println(",");
        }
        printStream.println("]");
        printStream.println("}");
    }
}
