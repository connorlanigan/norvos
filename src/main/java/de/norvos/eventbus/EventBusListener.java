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

/**
 * An interface of all listeners that can be registered with the
 * {@link de.norvos.eventbus.EventBus EventBus}.
 * @author Connor Lanigan
 */
public interface EventBusListener {

	/**
	 * Handles the notification of a new event.<br>
	 * <br>
	 * This method blocks the EventBus. Thus, if an implementation of this
	 * method needs some time to execute, consider running it in a separate
	 * thread.
	 *
	 * @param event
	 *            the event that has occured
	 */
	public void update(Event event);

}