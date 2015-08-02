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
package de.norvos.observers;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class NotificatorMap {

	private final HashMap<String, List<Notifiable>> map = new HashMap<>();

	/**
	 * Returns all Notifiables associated with the given event.
	 *
	 * @param event
	 *            from which the Notifiables are returned
	 * @return List of associated Notifiables
	 */
	private List<Notifiable> get(final String event) {
		if (!map.containsKey(event)) {
			map.put(event, new LinkedList<Notifiable>());
		}
		return Collections.unmodifiableList(map.get(event));
	}

	/**
	 * Notifies all objects that have registered for the given event.
	 *
	 * @param event
	 *            the event
	 * @param notificationData
	 *            Data that is supplied to the Notifiables
	 */
	public void notify(final String event, final Object notificationData) {
		for (final Notifiable n : get(event)) {
			n.notify(event, notificationData);
		}
	}

	/**
	 * Registers a Notifiable for a given event.
	 *
	 * @param event
	 *            the event
	 * @param value
	 *            the object to notify of this event
	 */
	public void register(final String event, final Notifiable value) {
		if (!map.containsKey(event)) {
			map.put(event, new LinkedList<Notifiable>());
		}
		final List<Notifiable> list = map.get(event);
		if (!list.contains(value)) {
			list.add(value);
		}
	}

	/**
	 * Unregisters the Notifiable from all events in this map.
	 *
	 * @param value
	 *            the value to remove.
	 */
	public void unregister(final Notifiable value) {
		for (final List<Notifiable> entry : map.values()) {
			while (entry.remove(value)) {
			}
		}
	}

}
