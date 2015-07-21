package de.norvos.observers;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class NotificatorMap{

	private final HashMap<String, List<Notifiable>> map = new HashMap<>();
		
	/**
	 * Registers a Notifiable for a given event.
	 * @param event the event
	 * @param value the object to notify of this event
	 */
	public void register(String event, Notifiable value){
		if(!map.containsKey(event)){
			map.put(event, new LinkedList<Notifiable>());
		}
		List<Notifiable> list = map.get(event);
		if(!list.contains(value)){
			list.add(value);
		}
	}
	
	/**
	 * Returns all Notifiables associated with the given event.
	 * @param event from which the Notifiables are returned
	 * @return List of associated Notifiables
	 */
	private List<Notifiable> get(String event){
		if(!map.containsKey(event)){
			map.put(event, new LinkedList<Notifiable>());
		}
		return Collections.unmodifiableList(map.get(event));
	}
	
	/**
	 * Unregisters the Notifiable from all events in this map.
	 * @param value the value to remove.
	 */
	public void unregister(Notifiable value){
		for(List<Notifiable> entry : map.values()){
			while(entry.remove(value));
		}
	}
	
	/**
	 * Notifies all objects that have registered for the given event.
	 * @param event the event
	 * @param notificationData Data that is supplied to the Notifiables
	 */
	public void notify(String event, Object notificationData){
		for(Notifiable n : get(event)){
			n.notify(event, notificationData);
		}
	}
	
}
