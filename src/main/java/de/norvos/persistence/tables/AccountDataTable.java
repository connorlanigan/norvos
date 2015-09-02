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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.norvos.persistence.Database;

public class AccountDataTable implements Table {
	private static AccountDataTable instance;

	public static AccountDataTable getInstance() {
		if (instance == null) {
			instance = new AccountDataTable();
		}
		return instance;
	}

	private AccountDataTable() {
	}

	public byte[] getBinary(final String key) throws SQLException {
		final String query = "SELECT binary_value FROM account_data WHERE key = ?";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setString(1, key);
			final ResultSet result = stmt.executeQuery();
			if (result.first()) {
				return result.getBytes(1);
			} else {
				return null;
			}
		}
	}

	@Override
	public String getCreationStatement() {
		return "CREATE TABLE IF NOT EXISTS account_data ( key VARCHAR PRIMARY KEY, binary_value BINARY, string_value VARCHAR, CHECK ((binary_value is not null) != (string_value is not null)))";
	}

	public String getString(final String key) throws SQLException {
		final String query = "SELECT string_value FROM account_data WHERE key = ?";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setString(1, key);
			final ResultSet result = stmt.executeQuery();
			if (result.first()) {
				return result.getString(1);
			} else {
				return null;
			}
		}
	}

	public void storeBinary(final String key, final byte[] value) throws SQLException {
		final String query = "MERGE INTO account_data VALUES (?, ?, null)";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setString(1, key);
			stmt.setBytes(2, value);

			stmt.execute();
		}
	}

	public void storeString(final String key, final String value) throws SQLException {
		final String query = "MERGE INTO account_data VALUES (?, null, ?)";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setString(1, key);
			stmt.setString(2, value);

			stmt.execute();
		}
	}

}
