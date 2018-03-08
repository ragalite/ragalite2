import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/**
 * Upload activities and then categorizes them. 
 */
public class Main {

	private static final Scanner stdin = new Scanner(System.in);

	private static final Map<ActivityType, List<Activity>> activities 
		= new HashMap<>();
	
	private static final Map<String, Runnable> inputActions 
		= new HashMap<>();
	

	static {
		inputActions.put("add", () -> addActivity());
		inputActions.put("print", () -> printActivities());
	}
	/* INTERACTIVE MESSAGES */
	/* PMT = PROMPT, MSG = MESSAGE */
	/* (AC)TIVITY */

	/* LOGIN */
	public static final String LOGIN_PMT =
		"Login: ";
	public static final String WELCOME_MSG = 
		"Welcome %s%n";
	public static final String AC_NAME_PMT = 
		"Enter an activity name: ";
	
	/* mainLoop */
	public static final String ACTION_PMT =
		"Enter an action (%s): ";

	/* printActivities */
	public static final String AC_MSG =
		"Current activities: %s%n";
	
	/* addActivity */
	public static final String AC_TYPE_PMT =
		"What kind of activity is it? ";
	public static final String ADDING_AC_MSG = 
		"Adding activity: %s%n";
	public static final String AC_TYPE_NOT_FOUND_MSG = 
		"Couldn't find activity type!%n";
	public static final String TIME_START_PMT = 
		"Enter the start time: ";
	public static final String TIME_STOP_PMT = 
		"Enter the stop time: ";

	/**
	 * Prompts the user for input.
	 *
	 * @param promptMsg the prompt to display to the user
	 * @param fmtParams the format parameters for the prompt
	 *
	 * @return the user's response to promptMsg
	 */
	public static String prompt(String promptMsg, Object... fmtParams) {
		System.out.printf(promptMsg, fmtParams);
		return stdin.nextLine();
	}

	/**
	 * Displays a message to the user.
	 *
	 * @param msg the message to display
	 * @param fmtParams the format parameters for the message
	 *
	 */
	public static void displayMsg(String msg, Object... fmtParams) {
		System.out.printf(msg, fmtParams);
	}
	
	/**
	 * Prompts the user to log in.
	 *
	 * @return the username used to log in
	 */
	public static String login() {
		String username = prompt(LOGIN_PMT); 
		return username;
	}

	public static <E extends Enum<E>> E enumLookup(Class<E> enumType,
	                                               String toLookup) {
		try {
			toLookup = toLookup.toUpperCase();
			E value = Enum.valueOf(enumType, toLookup);
			return value;
		} catch (IllegalArgumentException iae) {
			return null;
		}
	}

	public static void main(String[] args) {
		displayMsg(WELCOME_MSG, login());
		mainLoop();	
	}
	
	
	private static void mainLoop() {
		String input;

		mainloop:
		while (! (input = prompt(ACTION_PMT, inputActions.keySet()))
			  .isEmpty()) {
			inputActions.get(input).run();
		}	
	}

	public static void printActivities() {
		displayMsg(AC_MSG, activities);
	}

	public static void addActivity() {
		String name = prompt(AC_NAME_PMT);
		
		ActivityType type = enumLookup(ActivityType.class, 
			                               prompt(AC_TYPE_PMT));
		if (type == null) {
			displayMsg(AC_TYPE_NOT_FOUND_MSG);
			return;	
		}

		int from = Integer.valueOf(prompt(TIME_START_PMT));
		int to = Integer.valueOf(prompt(TIME_STOP_PMT));

		TimeInterval interval = new TimeInterval(from, to);

		Activity activity = new Activity(name, type, interval);

		displayMsg(ADDING_AC_MSG, activity);

		if (! activities.containsKey(type)) {
			activities.put(type, new ArrayList<>());
		}
		activities.get(type).add(activity);



	}
}
