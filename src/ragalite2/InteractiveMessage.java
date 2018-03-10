package ragalite2;

public enum InteractiveMessage {
	/* INTERACTIVE MESSAGES */
	/* PMT = PROMPT, MSG = MESSAGE, CHC = CHOICE */
	/* (AC)TIVITY */

	/* LOGIN */
	LOGIN_PMT("Login: "),
	WELCOME_MSG( "Welcome %s%n"),
	AC_NAME_PMT( "Enter an activity name: "),
		

	/* mainLoop */
	ACTION_CHC( "an action"),

	/* printUserInfo */
	AC_MSG("Current activities: %s%n"),
		
	CAT_MSG("Current categories: %s%n"),
		

	/* addActivity */
	AC_TYPE_PMT("What kind of activity is it? "),
		
	ADDING_AC_MSG( "Adding activity: %s%n"),
		
	AC_TYPE_NOT_FOUND_MSG( "Couldn't find activity type!%n"),
		
	TIME_START_PMT( "Enter the start time: "),
		
	TIME_STOP_PMT( "Enter the stop time: "),
		

	CAT_NAME_PMT( "Enter the category to create: "),
		

	/* Exiting */
	GOODBYE_MSG("Goodbye!%n");

	private String msg;
	private InteractiveMessage(String msg) {
		this.msg = msg;
	}

	public @Override String toString() {
		return msg;
	}
}
