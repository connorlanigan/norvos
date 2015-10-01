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
package de.norvos.persistence.tables;

import static de.norvos.i18n.Translations.translate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.norvos.contacts.ContactData;
import de.norvos.persistence.Database;
import de.norvos.utils.Errors;
import de.norvos.utils.UnreachableCodeException;

public class ContactsTable implements Table {
	private static ContactsTable instance;

	final static Logger LOGGER = LoggerFactory.getLogger(ContactsTable.class);

	synchronized public static ContactsTable getInstance() {
		if (instance == null) {
			instance = new ContactsTable();
		}
		return instance;
	}

	private ContactsTable() {
	}

	public ContactData getContactData(final String phoneNumber) {
		final String query = "SELECT * FROM contacts WHERE phone_number = ?";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setString(1, phoneNumber);
			final ResultSet result = stmt.executeQuery();

			if (result.first()) {
				final String displayName = result.getString("display_name");
				final String draftMessage = result.getString("draft_message");

				return new ContactData(phoneNumber, displayName, draftMessage, ContactData.ContactState.KNOWN_USER);
			} else {
				return null;
			}
		} catch (final SQLException e) {
			LOGGER.error("Contact data could not be fetched from database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}

	}

	@Override
	public String getCreationStatement() {
		return "CREATE TABLE IF NOT EXISTS contacts (phone_number VARCHAR PRIMARY KEY, display_name VARCHAR, draft_message VARCHAR)";
	}

	public void storeContactData(final ContactData contact) {
		final String query = "MERGE INTO contacts VALUES (?, ?, ?)";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {
			stmt.setString(1, contact.getPhoneNumber());
			stmt.setString(2, contact.getDisplayName());
			stmt.setString(3, contact.getDraftMessage());

			stmt.execute();
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
