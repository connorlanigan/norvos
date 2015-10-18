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
package de.norvos.messages;

import java.io.File;

import de.norvos.contacts.Contact;
import de.norvos.contacts.ContactService;
import de.norvos.persistence.AttachmentStore;

/**
 * Contains the data of a decrypted message.
 *
 * @author Connor Lanigan
 */
public class DecryptedMessage {

	private final String address;
	private final long attachmentId;

	private final String body;

	private final long messageId;

	private final String mismatchedIdentities;

	private final boolean read;
	private final boolean sent;
	private final long timestamp;

	public DecryptedMessage(final long timestamp, final boolean read, final String body, final String address,
			final String mismatchedIdentities, final boolean sent, final long attachmentId, final long messageId) {
		this.read = read;
		this.body = body;
		this.address = address;
		this.mismatchedIdentities = mismatchedIdentities;
		this.timestamp = timestamp;
		this.sent = sent;
		this.attachmentId = attachmentId;
		this.messageId = messageId;
	}

	public String getAddress() {
		return address;
	}

	public File getAttachment() {
		return AttachmentStore.getAttachment(attachmentId);
	}

	public String getBody() {
		return body;
	}

	public Contact getContact() {
		return ContactService.getInstance().getByNumber(getAddress());
	}

	public long getMessageId() {
		return messageId;
	}

	public String getMismatchedIdentities() {
		return mismatchedIdentities;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public boolean isRead() {
		return read;
	}

	public boolean isSent() {
		return sent;
	}

}
