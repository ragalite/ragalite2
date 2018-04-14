package ragalite2;

public final class InteractiveMessage {
	/* INTERACTIVE MESSAGES */
	/* PMT = PROMPT, MSG = MESSAGE, CHC = CHOICE */
	/* (AC)TIVITY */

	/* LOGIN */
	public static final String LOGIN_PMT
		= "Login: ";
	public static final String WELCOME_MSG
		=  "Welcome %s%n";
	public static final String AC_NAME_PMT
		=  "Enter an activity name: ";


	/* mainLoop */
	public static final String ACTION_CHC
		=  "an action";

	/* printUserInfo */
	public static final String AC_MSG
		= "Current activities: %s%n";

	public static final String CAT_MSG
		= "Current categories: %s%n";


	/* addActivity */
	public static final String AC_TYPE_PMT
		= "What kind of activity is it? ";

	public static final String ADDING_AC_MSG
		=  "Adding activity: %s%n";

	public static final String AC_TYPE_NOT_FOUND_MSG
		=  "Couldn't find activity type!%n";

	public static final String TIME_START_PMT
		=  "Enter the start time: ";

	public static final String TIME_STOP_PMT
		=  "Enter the stop time: ";


	public static final String CAT_NAME_PMT
		=  "Enter the category to create: ";

	public static final String NO_SUCH_AC_MSG
		= "No such activity: %s%n";

	/* Exiting */
	public static final String GOODBYE_MSG
		= "Goodbye!%n";


}
