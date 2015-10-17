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
package de.norvos.account;

import static de.norvos.i18n.Translations.translate;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.norvos.persistence.tables.AccountDataTable;
import de.norvos.persistence.tables.ContactsTable;
import de.norvos.utils.Errors;
import de.norvos.utils.UnreachableCodeException;

/**
 * Allows access to storing and reading raw settings values from and to the user
 * settings database.
 *
 * @author Connor Lanigan
 */
public class AccountDataStore {
	final static Logger LOGGER = LoggerFactory.getLogger(AccountDataStore.class);
	/**
	 * Get a binary value from the account data table. Do not use this method
	 * directly to access user settings, instead use the {@link SettingsService}
	 * methods.
	 *
	 * @param key
	 *            the table entry key
	 * @return the stored value
	 */
	static byte[] getBinaryValue(final String key) {
		try {
			return AccountDataTable.getInstance().getBinary(key);
		} catch (final SQLException e) {
			LOGGER.error("Binary value for key ["+key+"] could not be fetched to database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}

	/**
	 * Get a string value from the account data table. Do not use this method
	 * directly to access user settings, instead use the {@link SettingsService}
	 * methods.
	 *
	 * @param key
	 *            the table entry key
	 * @return the stored value
	 */
	static String getStringValue(final String key) {
		try {
			return AccountDataTable.getInstance().getString(key);
		} catch (final SQLException e) {
			LOGGER.error("Settings string ["+key+"] could not be fetched from database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}

	/**
	 * Store a binary value into the account data table. Do not use this method
	 * directly to access user settings, instead use the {@link SettingsService}
	 * methods.
	 *
	 * @param key
	 *            the table entry key
	 * @param value
	 *            the value to store
	 * @return <code>true</code> if and only if the storing was successful
	 */
	static boolean storeBinaryValue(final String key, final byte[] value) {
		try {
			AccountDataTable.getInstance().storeBinary(key, value);
			return true;
		} catch (final SQLException e) {
			LOGGER.error("Binary value for key ["+key+"] could not be stored to database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}

	/**
	 * Store a string value into the account data table. Do not use this method
	 * directly to access user settings, instead use the {@link SettingsService}
	 * methods.
	 *
	 * @param key
	 *            the table entry key
	 * @param value
	 *            the value to store
	 * @return <code>true</code> if and only if the storing was successful
	 */
	static boolean storeStringValue(final String key, final String value) {
		try {
			AccountDataTable.getInstance().storeString(key, value);
			return true;
		} catch (final SQLException e) {
			LOGGER.error("String value ["+value+"] for key ["+key+"] could not be stored to database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}
}
