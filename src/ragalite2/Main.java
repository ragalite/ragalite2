package ragalite2;

import static ragalite2.InteractiveMessage.*;

import java.util.function.*;
import java.io.*;
import java.util.*;

/**
 * Upload activities and categorize them
 */
public class Main {

	private static final Scanner stdin = new Scanner(System.in);

	/*private static final Map<ActivityType, List<Activity>> activities 
		= new HashMap<>();*/
	
	private static Set<Activity> activities;
	private static List<String> categories;

	private static final Map<String, Runnable> inputActions 
		= new HashMap<>();
	

	static {
		inputActions.put("add", () -> addActivity());
		inputActions.put("print", () -> printUserInfo());
		inputActions.put("filter", () -> filterByActivityType());
		inputActions.put("addcategory", () -> addCategory());
		inputActions.put("setcategory", () -> setActivityCategory());
	}

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
	 * Prompts the user to choose from a specified set of options.
	 *
	 * This function will attempt to match user input to an option. A
	 * *match* occurs when
	 *
	 * 1. the input is the same as the option, or 2. the option begins with
	 * the input (i.e., has it as one of its left substrings), *and* the
	 * input is not an empty string.
	 *
	 * Matching is case-insensitive.
	 *
	 * If multiple options match with the given substring, then any one of
	 * them may be returned.
	 *
	 * If no options match, then the user can be made to re-enter their
	 * choice until one does, or a default value can be returned.
	 *
	 * The user can always terminate this function by inputting the empty
	 * string.  This again will result in the default value being returned.
	 *
	 * Since the empty string is treated as a unique choice, it is
	 * discouraged for the provided options to actually contain the empty
	 * string. An empty string option will be ignored and the behavior will
	 * still execute.
	 *
	 * @param promptSuffix specifies what to choose. The prefix is "Choose "
	 * (including the space).  
	 *
	 * @param options the set of options to display to the user 
	 *
	 * @param reenterUntilSuccess forces the user to re-enter a choice until
	 *                            it matches an option (or the choice is an
	 *                            empty string) 
	 *
	 * @param defaultChoice what to return if the user refuses to provide
	 *                      an appropriate choice
	 *
	 * @return the user's choice
	 * 
         */
	public static String promptOption(String promptSuffix,
	                                  Set<String> options,
					  boolean reenterUntilSuccess,
					  String defaultChoice) {
		String choice;
		do {	
			System.out.printf("Choose %s (%s): ", promptSuffix, 
                                                              options);
			choice = stdin.nextLine().toLowerCase();
			if (choice.isEmpty()) { // all strings start with the
			                        // empty string so it's best
						// to just disallow it.
				break;
			}
			for (String option : options) {
				if (option.toLowerCase().startsWith(choice)) {
					return option;
				}
			}
		} while (reenterUntilSuccess);

		return defaultChoice;
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
	
	/**
	 * Looks up a member of an enum by its string representation.
	 *
	 * If the string does not correspond to a member of the given
	 * enum, null is returned isntead.
	 *
	 * @param enumType specifies the type of the members to search through
	 * @param toLookup the string representation of the desired enum
	 *                 member
	 *
	 * @return the enum member of enumType with string representation
	 *         toLookup, or null if such a member is nonexistent
	 *
	 */
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

	/**
	 * Serializes an object (writes it to a file).
	 *
	 */
	public static void serialize(Serializable toSerialize, String toFileDir) {
		// make sure the path to file exists!
		// we do getParentFile because we don't want to make the file
		// itself a directory.
		new File(toFileDir).getParentFile().mkdirs();

		try (FileOutputStream   fos = new FileOutputStream  (toFileDir);
		     ObjectOutputStream oos = new ObjectOutputStream(fos   )) {
			oos.writeObject(toSerialize);
		} 
		catch (IOException i) {
			 i.printStackTrace();
		}
	}
	
	
	
	public static Object deserialize(String fromFileDir) 
	                     throws IOException, ClassNotFoundException {
		
		try (FileInputStream   fis = new FileInputStream  (fromFileDir);
		     ObjectInputStream ois = new ObjectInputStream(fis   )) {
			return ois.readObject();
		} 
		catch (Exception e) {
			throw e;
		}
		/*
		catch (FileNotFoundException f) {
			return null;
		}
		catch (IOException i) {
			i.printStackTrace();
			return null;
		} 
		catch (ClassNotFoundException c) {
			System.out.println("Employee class not found");
			c.printStackTrace();
			return null;
		}*/
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T deserializeOrDefault(String fromFileDir, 
	                                         Supplier<T> defaultGenerator) {
		try {
			return (T) deserialize(fromFileDir);
				
		}
		catch (Exception e) {
			e.printStackTrace();
			return defaultGenerator.get();
		}
	}
	
	/**
	 * Loads the activities the user had previously registered under the
	 * specified username.
	 *
	 */
	public static void loadUser(String username) {
		activities = deserializeOrDefault(
		                     "USERS/" + username + "/activities", 
		                     HashSet::new);
		
		
		/*
		List<String> d = new ArrayList<String>() {{
			add("academic"); add("social"); add("physical");
		}}; serialize((Serializable) d, "DEFAULTS/categories");
		*/
		List<String> defaultCategories = deserializeOrDefault("DEFAULTS/categories", ArrayList::new);
		categories = deserializeOrDefault(
		                     "USERS/" + username + "/categories", 
		                     () -> defaultCategories);
	}

	public static void saveUser(String username) {
		serialize((Serializable) activities, "USERS/" + username + "/activities");
		serialize((Serializable) categories, "USERS/" + username + "/categories");
	}



	public static void main(String[] args) {
		// lets the user input their username
		String username = login();
		displayMsg(WELCOME_MSG, username);

		// load the user's data into memory
		loadUser(username);

		try {
			mainLoop();	
		}
		catch (NoSuchElementException e) {}
		finally {
			displayMsg(GOODBYE_MSG);
			saveUser(username);
		}
	}
	
	
	private static void mainLoop() {
		String input;
		mainloop:
		while (! (input = promptOption(ACTION_CHC, 
		                  inputActions.keySet(), true, "")
			 ).isEmpty()) {
			inputActions.get(input).run();
		}	
	}
	
	/**
	 * Prints all activities to the user.
	 *
	 *
	 */
	public static void printUserInfo() {
		
		displayMsg(CAT_MSG, categories);
		displayMsg(AC_MSG, activities);
	}

	public static void addActivity() {
		String name = prompt(AC_NAME_PMT);

		int from = Integer.valueOf(prompt(TIME_START_PMT));
		int to = Integer.valueOf(prompt(TIME_STOP_PMT));

		TimeInterval interval = new TimeInterval(from, to);

		Activity activity = new Activity(name, /*type,*/ interval);

		displayMsg(ADDING_AC_MSG, activity);

		activities.add(activity);

	}

	public static void addCategory() {
		categories.add(prompt(CAT_NAME_PMT));		
	}

	public static void setActivityCategory() {
		
	}

	
	/**
         *  
         *
         */
	public static void filterByActivityType() {

		ActivityType type = enumLookup(ActivityType.class,
                                               prompt(AC_TYPE_PMT));
		if (type == null) {
			displayMsg(AC_TYPE_NOT_FOUND_MSG);
			return;
		}
		
		/*if (! activities.containsKey(type)) {
			activities.put(type, new ArrayList<>());
		}*/
		displayMsg(type.getActivities().toString());
	}
}
