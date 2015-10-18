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
package de.norvos.eventbus.events;

import java.io.File;

import de.norvos.contacts.Contact;
import de.norvos.eventbus.Event;

/**
 * An event indicating that a message has been sent.
 *
 * @author Connor Lanigan
 */
public class MessageSentEvent implements Event {

	private final File attachment;
	private final Contact contact;
	private final String message;
	private final long timestamp;

	public MessageSentEvent(final Contact contact, final String message, final long timestamp, final File attachment) {
		this.contact = contact;
		this.message = message;
		this.timestamp = timestamp;
		this.attachment = attachment;
	}

	public File getAttachment() {
		return attachment;
	}

	public Contact getContact() {
		return contact;
	}

	public String getMessage() {
		return message;
	}

	public long getTimestamp() {
		return timestamp;
	}

}
