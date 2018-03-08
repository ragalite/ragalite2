public class Activity {
	

	/** Immutable attributes. */
	public final String name;
	public final ActivityType type;
	public final TimeInterval interval;
	
	public Activity(String name, ActivityType type, TimeInterval interval) {
		this.name = name;
		this.type = type;
		this.interval = interval;
	}

	public String toString() {
		return String.format("Name: %s Type: %s Interval: %s", 
		                     name, type, interval);
	}
}
