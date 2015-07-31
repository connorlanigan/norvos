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

import org.whispersystems.libaxolotl.IdentityKey;
import org.whispersystems.libaxolotl.InvalidKeyException;

import de.norvos.persistence.Database;

public class IdentityKeyTable implements Table {
	private static IdentityKeyTable instance;

	public static IdentityKeyTable getInstance() {
		if (instance == null) {
			instance = new IdentityKeyTable();
		}
		return instance;
	}

	private IdentityKeyTable() {
	}

	@Override
	public String getCreationStatement() {
		return "CREATE TABLE IF NOT EXISTS identity_keystore ( user_id VARCHAR PRIMARY KEY, identity_key BINARY NOT NULL)";
	}

	public IdentityKey getIdentity(final String name) throws SQLException {
		final String query = "SELECT identity_key FROM identity_keystore WHERE name = ?";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setString(1, name);
			final ResultSet result = stmt.executeQuery();
			if (result.first()) {
				try {
					return new IdentityKey(result.getBytes(1), 0);
				} catch (final InvalidKeyException e) {
					throw new SQLException("Value of identity_key for name [" + name + "] is invalid.", e);
				}
			} else {
				return null;
			}
		}
	}

	public void storeIdentity(final String name, final IdentityKey identityKey) throws SQLException {
		final String query = "MERGE INTO identity_keystore VALUES (?, ?)";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setString(1, name);
			stmt.setBytes(2, identityKey.serialize());

			stmt.execute();
		}
	}

}
