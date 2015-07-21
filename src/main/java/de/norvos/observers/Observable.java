package de.norvos.observers;

public interface Observable {
	
	/**
	 * Register a Notifiable object for a specific event.
	 * @param n the object to be notified
	 * @param event the event to register for
	 */
	public void register(Notifiable n, String event);
	
	/**
	 * Removes a Notifiable object from all events.
	 * @param n the notifiable object
	 */
	public void unregister(Notifiable n);
}
