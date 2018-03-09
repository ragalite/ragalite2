import java.util.Set;
import java.util.Collections;
import java.util.HashSet;

public enum ActivityType {
	WORKOUT,
	SOCIAL,
	ACADEMIC;
	
	private final Set<Activity> activities;

	private ActivityType() {
		activities = new HashSet<>();
	}

	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	public Set<Activity> getActivities() {
		return Collections.unmodifiableSet(activities);
	}
		
}
