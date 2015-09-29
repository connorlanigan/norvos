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

import java.sql.SQLException;

import de.norvos.persistence.tables.AccountDataTable;

/**
 * Allows access to storing and reading raw settings values from and to the user
 * settings database.
 * 
 * @author Connor Lanigan
 */
public class AccountDataStore {
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
			return null;
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
			return null;
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
			return false;
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
			return false;
		}
	}
}
