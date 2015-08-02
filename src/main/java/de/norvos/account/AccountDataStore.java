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

public class AccountDataStore {
	public static byte[] getBinaryValue(final String key) {
		try {
			return AccountDataTable.getInstance().getBinary(key);
		} catch (final SQLException e) {
			return null;
		}
	}

	public static String getStringValue(final String key) {
		try {
			return AccountDataTable.getInstance().getString(key);
		} catch (final SQLException e) {
			return null;
		}
	}

	public static boolean storeBinaryValue(final String key, final byte[] value) {
		try {
			AccountDataTable.getInstance().storeBinary(key, value);
			return true;
		} catch (final SQLException e) {
			return false;
		}
	}

	public static boolean storeStringValue(final String key, final String value) {
		try {
			AccountDataTable.getInstance().storeString(key, value);
			return true;
		} catch (final SQLException e) {
			return false;
		}
	}
}
