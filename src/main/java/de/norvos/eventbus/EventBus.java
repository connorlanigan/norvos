/*******************************************************************************
 * Copyright (C) 2015 Connor Lanigan (email: dev@connorlanigan.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.norvos.eventbus;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Distributes events to listeners that have previously registered to be
 * notified.
 * 
 * @author Connor Lanigan
 */
public class EventBus {
	// TODO use weak references
	static private HashMap<Class<? extends Event>, List<EventBusListener>> map = new HashMap<>();

	private static List<EventBusListener> getListForEvent(final Class<? extends Event> event) {
		List<EventBusListener> list = map.get(event);
		if (list == null) {
			list = new LinkedList<EventBusListener>();
			map.put(event, list);
		}
		return list;
	}

	/**
	 * Registers a new listener to be notified (usually <b>this</b>).
	 *
	 * @param event
	 *            the type of event to listen for
	 * @param listener
	 *            the listener (usually <b>this</b>)
	 */
	synchronized public static void register(final Class<? extends Event> event, final EventBusListener listener) {
		getListForEvent(event).add(listener);
	}

	/**
	 * Sends an event and distributes it to all registered listeners. The
	 * listeners are notified in the order in which they were registered.
	 *
	 * @param event
	 *            the event to distribute
	 */
	synchronized public static void sendEvent(final Event event) {
		for (final EventBusListener listener : getListForEvent(event.getClass())) {
			listener.update(event);
		}
	}

	/**
	 * Removes a listener from all events.
	 *
	 * @param listener
	 *            the listener to remove
	 */
	synchronized public static void unregister(final EventBusListener listener) {
		for (final List<EventBusListener> list : map.values()) {
			list.remove(listener);
		}
	}

	/**
	 * Removes all listener of the specified class from all events.
	 *
	 * @param listener
	 *            the listener class to remove
	 */
	synchronized public static void unregisterAll(final Class<? extends EventBusListener> listenerClass) {
		for (final List<EventBusListener> list : map.values()) {
			for (final EventBusListener listener : list) {
				if (listenerClass.isAssignableFrom(listener.getClass())) {
					list.remove(listener);
				}
			}
		}
	}

	private EventBus() {
	}

}
