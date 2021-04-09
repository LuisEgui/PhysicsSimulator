package simulator.launcher;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONObject;

import simulator.control.Controller;
import simulator.control.StateComparator;
import simulator.factories.*;
import simulator.model.PhysicsSimulator;
import simulator.model.bodies.FluentBuilder.Body;
import simulator.model.forcelaws.ForceLaws;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

	// Default values for some parameters
	private static final Double DTIME_DEFAULT_VALUE = 2500.0;
	private static final String FORCE_LAWS_DEFAULT_VALUE = "nlug";
	private static final String STATE_COMPARATOR_DEFAULT_VALUE = "epseq";

	// Attributes to stores values corresponding to command-line parameters
	private static Double deltaTime = null;
	private static String inFile = null;
	private static String outFile = null;
	private static String expectedOutputFile = null;
	private static Integer steps = null;
	private static JSONObject forceLawsInfo = null;
	private static JSONObject stateComparatorInfo = null;

	// Factories
	private static Factory<? extends Body> bodyFactory;
	private static Factory<ForceLaws> forceLawsFactory;
	private static Factory<StateComparator> stateComparatorFactory;

	private static void init() {
		// Initialize the bodies factory
		List<Builder<? extends simulator.model.bodies.FluentBuilder.Body>> bodyBuilders = new ArrayList<>();
		bodyBuilders.add(new MassLossingBodyBuilder());
		bodyBuilders.add(new BasicBodyBuilder());
		bodyFactory = new BodyBasedFactory(bodyBuilders);

		// Initialize the force laws factory
		List<Builder<? extends ForceLaws>> forceLawBuilders = new ArrayList<>();
		forceLawBuilders.add(new NoForceBuilder());
		forceLawBuilders.add(new NewtonUniversalGravitationBuilder());
		forceLawBuilders.add(new MovingTowardsFixedPointBuilder());
		forceLawsFactory = new ForceLawBasedFactory(forceLawBuilders);

		// Initialize the state comparator
		List<Builder<? extends StateComparator>> stateComparatorBuilders = new ArrayList<>();
		stateComparatorBuilders.add(new EpsilonEqualStateBuilder());
		stateComparatorBuilders.add(new MassEqualStateBuilder());
		stateComparatorFactory = new EqualStateBasedFactory(stateComparatorBuilders);
	}

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);

			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutputFileOption(line);
			parseStepsOption(line);
			parseExpectedOutput(line);
			parseDeltaTimeOption(line);
			parseForceLawsOption(line);
			parseStateComparatorOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		// help
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message.").build());

		// input file
		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Bodies JSON input file.").build());

		// output file
		cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Bodies JSON output file.").build());

		// ExpectedOutput file
		cmdLineOptions.addOption(Option.builder("eo").longOpt("expected-output").hasArg().desc("Expected simulator JSON output file.").build());

		// Steps
		cmdLineOptions.addOption(Option.builder("s").longOpt("steps").hasArg().desc("Steps to advance.").build());

		// delta-time
		cmdLineOptions.addOption(Option.builder("dt").longOpt("delta-time").hasArg()
				.desc("A double representing actual time, in seconds, per simulation step. Default value: "
						+ DTIME_DEFAULT_VALUE + ".")
				.build());

		// force laws
		cmdLineOptions.addOption(Option.builder("fl").longOpt("force-laws").hasArg()
				.desc("Force laws to be used in the simulator. Possible values: "
						+ factoryPossibleValues(forceLawsFactory) + ". Default value: '" + FORCE_LAWS_DEFAULT_VALUE
						+ "'.")
				.build());

		// gravity laws
		cmdLineOptions.addOption(Option.builder("cmp").longOpt("comparator").hasArg()
				.desc("State comparator to be used when comparing states. Possible values: "
						+ factoryPossibleValues(stateComparatorFactory) + ". Default value: '"
						+ STATE_COMPARATOR_DEFAULT_VALUE + "'.")
				.build());

		return cmdLineOptions;
	}

	public static String factoryPossibleValues(Factory<?> factory) {
		if (factory == null)
			return "No values found (the factory is null)";

		String s = "";

		for (JSONObject fe : factory.getInfo()) {
			if (s.length() > 0) {
				s = s + ", ";
			}
			s = s + "'" + fe.getString("type") + "' (" + fe.getString("desc") + ")";
		}

		s = s + ". You can provide the 'data' json attaching :{...} to the tag, but without spaces.";
		return s;
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		inFile = line.getOptionValue("i");
		if (inFile == null) {
			throw new ParseException("In batch mode an input file of bodies is required");
		}
	}

	private static void parseOutputFileOption(CommandLine line) throws ParseException {
		if(line.hasOption("o"))
			outFile = line.getOptionValue("o");
	}

	private static void parseStepsOption(CommandLine line) {
		if(line.hasOption("s"))
			steps = Integer.parseInt(line.getOptionValue("s"));
	}

	private static void parseExpectedOutput(CommandLine line) {
		if(line.hasOption("eo"))
			expectedOutputFile = line.getOptionValue("eo");
	}

	private static void parseDeltaTimeOption(CommandLine line) throws ParseException {
		String dt = line.getOptionValue("dt", DTIME_DEFAULT_VALUE.toString());
		try {
			deltaTime = Double.parseDouble(dt);
			assert (deltaTime > 0);
		} catch (Exception e) {
			throw new ParseException("Invalid delta-time value: " + dt);
		}
	}

	private static JSONObject parseWRTFactory(String v, Factory<?> factory) {

		// the value of v is either a tag for the type, or a tag:data where data is a
		// JSON structure corresponding to the data of that type. We split this
		// information
		// into variables 'type' and 'data'
		//
		int i = v.indexOf(":");
		String type = null;
		String data = null;
		if (i != -1) {
			type = v.substring(0, i);
			data = v.substring(i + 1);
		} else {
			type = v;
			data = "{}";
		}

		// look if the type is supported by the factory
		boolean found = false;
		for (JSONObject fe : factory.getInfo()) {
			if (type.equals(fe.getString("type"))) {
				found = true;
				break;
			}
		}

		// build a corresponding JSON for that data, if found
		JSONObject jo = null;
		if (found) {
			jo = new JSONObject();
			jo.put("type", type);
			jo.put("data", new JSONObject(data));
		}
		return jo;

	}

	private static void parseForceLawsOption(CommandLine line) throws ParseException {
		String fl = line.getOptionValue("fl", FORCE_LAWS_DEFAULT_VALUE);
		forceLawsInfo = parseWRTFactory(fl, forceLawsFactory);
		if (forceLawsInfo == null) {
			throw new ParseException("Invalid force laws: " + fl);
		}
	}

	private static void parseStateComparatorOption(CommandLine line) throws ParseException {
		String scmp = line.getOptionValue("cmp", STATE_COMPARATOR_DEFAULT_VALUE);
		stateComparatorInfo = parseWRTFactory(scmp, stateComparatorFactory);
		if (stateComparatorInfo == null) {
			throw new ParseException("Invalid state comparator: " + scmp);
		}
	}

	private static void startBatchMode() {
		try {
			InputStream inputFile = new FileInputStream(inFile);
			OutputStream outputFile = outFile == null ? System.out : new FileOutputStream(outFile);
			ForceLaws forceLaws = forceLawsFactory.createInstance(forceLawsInfo);
			PhysicsSimulator physicsSimulator = new PhysicsSimulator(deltaTime, forceLaws);
			Controller controller = new Controller(physicsSimulator, bodyFactory);
			controller.loadBodies(inputFile);
			if(expectedOutputFile == null)
				controller.run(steps, outputFile);
			else {
				InputStream expectedOutput = new FileInputStream(expectedOutputFile);
				StateComparator cmp = stateComparatorFactory.createInstance(stateComparatorInfo);
				controller.run(steps, expectedOutput, outputFile, cmp);
			}
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
		}
	}

	private static void start(String[] args) {
		parseArgs(args);
		startBatchMode();
	}

	public static void main(String[] args) {
		try {
			init();
			start(args);
		} catch (Exception e) {
			System.err.println("Something went wrong ...");
			System.err.println();
			e.printStackTrace();
		}
	}
}
