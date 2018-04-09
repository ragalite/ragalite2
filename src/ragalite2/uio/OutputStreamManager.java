package ragalite.uio;

import java.util.function.Consumer;
import java.util.Set;
import java.util.HashSet;

/**
 * 
 */
public final class OutputStream {
	
	private static OutputStreamManager managerInstance;
	
	private final Set<Consumer<String>> callbacksOnOutput;

	private OutputStreamManager() {
		callbacksOnOutput = new HashSet<>();	
	}

	public static OutputStreamManager getInstance() {
		if (managerInstance == null) {
			managerInstance = new OutputStreamManager();
		}
		return managerInstance;
	}

	public void send(String message) {
		for (Consumer<String> callback : callbacksOnOutput) {
			callback.accept(message);
		}
	}

	public void addCallback(Consumer<String> callback) {
		callbacksOnOutput.add(callback);
	}
}
