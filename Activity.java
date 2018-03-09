import java.io.Serializable;
/**
 *
 *
 */
public class Activity implements Serializable {
	

	// Immutable attributes.
	public final String name;
	public final TimeInterval interval;
	
	public Activity(String name, TimeInterval interval) {
		this.name = name;
		//this.type = type;
		this.interval = interval;
	}

	public String toString() {
		return String.format("Name: %s Interval: %s", 
		                     name, /*type,*/ interval);
	}
}
