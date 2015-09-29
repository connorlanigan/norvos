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

/**
 * Contains the data of a contact.
 * 
 * @author Connor Lanigan
 */
public class ContactData {
	private String displayName;
	private String draftMessage;

	private final String phoneNumber;

	/**
	 * @param phoneNumber
	 *            the contact's phone number
	 * @param displayName
	 *            the contact's display (human readable) name
	 * @param draftMessage
	 *            the message stored as draft
	 */
	public ContactData(final String phoneNumber, final String displayName, final String draftMessage) {
		this.phoneNumber = phoneNumber;
		this.displayName = displayName;
		this.draftMessage = draftMessage;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDraftMessage() {
		return draftMessage;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	void setDraftMessage(final String draftMessage) {
		this.draftMessage = draftMessage;
	}
}
