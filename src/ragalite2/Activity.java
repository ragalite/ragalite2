package ragalite2;

import java.io.Serializable;
/**
 *
 *
 */
public class Activity implements Serializable {


	// Immutable attributes.
	public final String name;
	public final TimeInterval interval;
	private String category;

	public Activity(String name, TimeInterval interval) {
		this.name = name;
		//this.type = type;
		this.interval = interval;
		category = "None";
	}

	public @Override String toString() {
		return String.format("Name: %s Interval: %s | Category: %s",
		                     name, interval, category);
	}

	public @Override boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (! (obj instanceof Activity)) return false;

		Activity other = (Activity) obj;
		return this.name.equals(other.name);
	}

	public String getCategory() {return category;}
	public void setCategory(String category) {this.category = category;}
}
