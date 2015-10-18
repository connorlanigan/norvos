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
package de.norvos.contacts;

import org.whispersystems.textsecure.api.push.TextSecureAddress;

/**
 * Represents a contact.
 *
 * @author Connor Lanigan
 */
public class Contact {
	private final String phoneNumber;

	/**
	 * Creates a contact identified by this phone number. The phone number is
	 * not checked for validity.
	 *
	 * @param phoneNumber
	 *            the contact's phone number
	 */
	Contact(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Returns the name of the contact that should be displayed for interaction
	 * with the user.
	 *
	 * @return the contact's human readable name
	 */
	public String getDisplayName() {
		return ContactService.getInstance().getContactData(this).getDisplayName();
	}

	/**
	 * Returns the stored draft message (a message that was typed but not yet
	 * sent) for this contact.
	 *
	 * @return the stored draft message
	 */
	public String getDraftMessage() {
		return ContactService.getInstance().getContactData(this).getDraftMessage();
	}

	/**
	 * Returns the phone number of this contact.
	 *
	 * @return the contact's phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Store a message as the contact's draft message.
	 *
	 * @param draftMessage
	 *            the message to store
	 */
	synchronized public void setDraftMessage(final String draftMessage) {
		final ContactData contactData = ContactService.getInstance().getContactData(this);
		contactData.setDraftMessage(draftMessage);
		ContactService.getInstance().setContactData(contactData);
	}

	public TextSecureAddress toTSAddress() {
		return new TextSecureAddress(phoneNumber);
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof Contact) {
			Contact other = (Contact) o;
			if (other.phoneNumber.equals(this.phoneNumber)) {
				return true;
			}
		}
		return false;
	}

}
