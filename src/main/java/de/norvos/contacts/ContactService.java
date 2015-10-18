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

import static de.norvos.i18n.Translations.translate;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.norvos.persistence.tables.ContactsTable;

/**
 * Provides access to managing contacts.
 *
 * @author Connor Lanigan
 */
public class ContactService {
	private static ContactService instance;

	final static Logger LOGGER = LoggerFactory.getLogger(ContactService.class);

	synchronized public static ContactService getInstance() {
		if (instance == null) {
			instance = new ContactService();
		}
		return instance;
	}

	private ContactService() {
	}

	public List<Contact> getAllContacts() {
		return ContactsTable.getInstance().getAllContacts();
	}

	public Contact getByNumber(final String number) {
		return new Contact(number);
	}

	ContactData getContactData(final Contact contact) {
		final ContactData data = ContactsTable.getInstance().getContactData(contact.getPhoneNumber());
		if (data != null) {
			return data;
		} else {
			LOGGER.debug("Requested contact data for [{}] not found in contact list.", contact.getPhoneNumber());
			return unknownUser(contact.getPhoneNumber());
		}
	}

	public void setContactData(final ContactData contactData) {
		ContactsTable.getInstance().storeContactData(contactData);
	}

	private ContactData unknownUser(final String phoneNumber) {
		return new ContactData(phoneNumber, translate("unknown_user"), "", ContactData.ContactState.UNKNOWN_USER);
	}
}
