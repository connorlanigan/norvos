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

public interface Observable {

	/**
	 * Register a Notifiable object for a specific event.
	 * 
	 * @param n
	 *            the object to be notified
	 * @param event
	 *            the event to register for
	 */
	public void register(Notifiable n, String event);

	/**
	 * Removes a Notifiable object from all events.
	 * 
	 * @param n
	 *            the notifiable object
	 */
	public void unregister(Notifiable n);
}
