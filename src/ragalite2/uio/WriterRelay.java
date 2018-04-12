package ragalite2.uio;

import java.io.Writer;
import java.io.PrintStream;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;

/**
 * One-to-many relay for writing character streams.
 *
 * In particular, this class supports relaying to 
 * {@link java.io.PrintStream PrintStreams}
 * (System.out and System.err included) and 
 * {@link java.util.function.Consumer String Consumers} (for custom callbacks).
 *
 * The order of receivers in which a character stream is relayed to is
 * undefined.
 *
 * A stream or callback may be attached to a relay multiple times and thus be
 * relayed a message however many times it was added.
 * 
 * public static void displayAlert(String alertMessage) {
 *	alertLabel.setText(alertMessage):
 *	
 *	// show the alert
 * }
 *
 *
 * WriterRelay relay = new WriterRelay();
 * relay.addReceiver(System.out);
 *
 * relay.addReceive(
 * );
 *
 * relay.relay(); // message printed to System.out
 */
public final class WriterRelay extends Writer {

	private final Collection<Consumer<String>> receivingCallbacks;
	private final Collection<PrintStream> receivingStreams;

	// TODO: Consider dependency injection for the container implementation.
	
	
	/**
	 * Supplier of new WriterRelay objects with caller-specified defaults.
	 *
	 * This class is intended to faciliate dependency injection. It is
	 * instantiated in {@link WriterRelay#makeSupplier} to simplify {@code
	 * makeSupplier}.
	 */
	private static class WriterRelaySupplier implements Supplier<WriterRelay> {


		/*
		Supplier of the collection storing the receivers of a
		relay supplied by this supplier.
		*/
		private final Supplier<Collection> storageObjectSupplier;

		/*
		Starting streams to relay input to from a relay supplied by this
		supplier.
		*/
		private final Collection<PrintStream> defaultReceivingStreams;

		/*
		Starting callbacks to send input to from a relay supplied by
		this supplier.
		*/
		private final Collection<Consumer<String>> defaultReceivingCallbacks;


		/**
		 * Creates a new supplier with the specified defaults for
		 * the supplied relays.
		 *
		 * The collections given may be mutated by the caller, i.e., the caller
		 * need not be careful that adding/subtracting from the collections will
		 * have side effects in the returned supplier. However, the
		 * objects in the collections at the time of this construction
		 * should be immutable.
		 *
		 * @param storageObjectSupplier supplier of the collections
		 * storing the receivers
		 * @param defaultReceivingStreams Default streams to relay into
		 * to 
		 * @param defaultReceivingCallbacks Default callbacks to send
		 * input to
		 */
		public WriterRelaySupplier(
			Supplier<Collection> storageObjectSupplier,
			Collection<PrintStream> defaultReceivingStreams,
			Collection<Consumer<String>> defaultReceivingCallbacks) {

			this.defaultReceivingStreams = storageObjectSupplier.get();
			this.defaultReceivingCallbacks = storageObjectSupplier.get();

			// Make defensive copies of the stuff in the
			// collections.
			for (PrintStream stream : this.defaultReceivingStreams) {
				defaultReceivingStreams.add(stream);
			}

			for (Consumer<String> callback : defaultReceivingCallbacks) {
				defaultReceivingCallbacks.add(callback);
			}

		}

		/**
		 * Generates a new WriterRelay with the specified defaults.
		 *
		 * @return a new WriterRelay with the specified defaults
		 */
		public @Override WriterRelay get() {
			WriterRelay relay = new WriterRelay(storageObjectSupplier);
			for (PrintStream stream : this.defaultReceivingStreams) {
				relay.addReceiver(stream);
			}

			for (Consumer<String> callback : defaultReceivingCallbacks) {
				relay.addReceiver(callback);
			}
			return relay;
		}
	}


	/**
	 * Creates a supplier of WriterRelay objects with specific defaults.
	 *
	 *
	 * @param storageObjectSupplier supplier of the collections
	 * storing the receivers
	 * @param defaultReceivingStreams Default streams to relay into
	 * to 
	 * @param defaultReceivingCallbacks Default callbacks to send
	 * input to
	 */
	public static Supplier<WriterRelay> makeSupplier(
		Supplier<Collection> storageObjectSupplier,
		Collection<PrintStream> defaultReceivingStreams,
		Collection<Consumer<String>> defaultReceivingCallbacks) {

		// Delegate to DI class
		return new WriterRelaySupplier(storageObjectSupplier,
		                               defaultReceivingStreams,
					       defaultReceivingCallbacks);
	}

	/**
	 * {@inheritDoc}
	 *
	 * After construction, this relay will immediately be able to add 
	 * (and otherwise manipulate its) receivers.
	 *
	 */
	private WriterRelay(Supplier<Collection> storageObjectSupplier) {
		receivingCallbacks = storageObjectSupplier.get();
		receivingStreams = new LinkedList<>();
	}
	
	/**
	 * Adds a receiving stream to this relay. 
	 *
	 * After its addition, any message sent to this relay will be sent to
	 * the stream as well.
	 *
	 * @param receivingStream Stream to relay messages to
	 *
	 */
	public void addReceiver(final PrintStream receivingStream) {
		receivingStreams.add(receivingStream); // Decorator
	}

	/**
	 * Adds a callback to this relay. 
	 *
	 * After its addition, any message sent to this relay will be sent as
	 * a parameter to the callback.
	 *
	 * @param receivingStream Callback to relay messages to
	 *
	 */
	public void addReceiver(final Consumer<String> receivingCallback) {
		receivingCallbacks.add(receivingCallback); // Decorator
	}

	/**
	 * Relays a message to each receiver.
	 *
	 * @param message Message to send to each receiver
	 *
	 */
	public void relay(final String message) {
		// Relay to callbacks...
		for (Consumer<String> callback : receivingCallbacks) {
			callback.accept(message);
		}
		// ... then streams. (Order's arbitrary, actually.)
		for (PrintStream stream : receivingStreams) {
			stream.print(message);
		}
	}

	/**
	 * Relays a portion of an array of characters.
	 *
	 * @param cbuf Array to relay characters from
	 * @param off  Offset from which to start relaying characters
	 * @param len  Number of characters to relay
	 */
	public @Override void write(char[] cbuf, int off, int len) {
		// Make a string starting at *off* that's *len* long.
		// Then relay it as usual.
		String substring = new String(cbuf, off, len);
		relay(substring);
	}

	/**
	 * Flushes all receiving streams.
	 * 
	 * Receiving callbacks are not affected by this operation (unless they
	 * are tied to a receiving stream, which is outside the scope of this
	 * containing class.)
	 */
	public @Override void flush() {
		for (PrintStream stream : receivingStreams) {
			stream.flush();
		}
	}
	
	/**
	 * Closes all receiving streams.
	 * 
	 * Receiving callbacks are not affected by this operation (unless they
	 * are tied to a receiving stream).
	 *
	 * This relay will still attempt to relay messages to its receiving
	 * streams even after they're closed, which will most likely result
	 * in an {@link java.io.IOException} being thrown.
	 */
	public @Override void close() {
		for (PrintStream stream : receivingStreams) {
			stream.close();
		}
	}
}
