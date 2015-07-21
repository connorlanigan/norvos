package de.norvos.observers;

public interface Notifiable {
	
	/**
	 * Notifys the object of an event it has registered for.
	 * @param event the event
	 */
	public void notify(String event, Object notificationData);
}
