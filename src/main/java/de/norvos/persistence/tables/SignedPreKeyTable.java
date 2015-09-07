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

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;

import de.norvos.persistence.Database;

public class SignedPreKeyTable implements Table {
	private static SignedPreKeyTable instance;

	synchronized public static SignedPreKeyTable getInstance() {
		if (instance == null) {
			instance = new SignedPreKeyTable();
		}
		return instance;
	}

	private SignedPreKeyTable() {
	}

	public void deleteKey(final int id) throws SQLException {
		final String query = "DELETE FROM signed_prekeystore WHERE id = ?";
		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {
			stmt.setInt(1, id);

			stmt.execute();
		}
	}

	@Override
	public String getCreationStatement() {
		return "CREATE TABLE IF NOT EXISTS signed_prekeystore ( id INTEGER PRIMARY KEY, signed_prekey BINARY NOT NULL)";
	}

	public List<SignedPreKeyRecord> loadAll() throws SQLException {
		final String query = "SELECT signed_prekey, id FROM signed_prekeystore";
		final List<SignedPreKeyRecord> list = new LinkedList<SignedPreKeyRecord>();

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			final ResultSet result = stmt.executeQuery();

			while (result.next()) {
				try {
					list.add(new SignedPreKeyRecord(result.getBytes(1)));
				} catch (final IOException e) {
					throw new SQLException(
							"SignedPreKeyTable: Value of signed_prekey for id [" + result.getInt(2) + "] is invalid.",
							e);
				}
			}
			return list;
		}
	}

	public SignedPreKeyRecord loadKey(final int id) throws SQLException {
		final String query = "SELECT signed_prekey FROM signed_prekeystore WHERE id = ?";
		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {
			stmt.setInt(1, id);

			final ResultSet result = stmt.executeQuery();

			if (result.first()) {
				try {
					return new SignedPreKeyRecord(result.getBytes(1));
				} catch (final IOException e) {
					throw new SQLException("SignedPreKeyTable: Value of signed_prekey for id [" + id + "] is invalid.",
							e);
				}
			} else {
				return null;
			}
		}
	}

	public void storeKey(final int id, final SignedPreKeyRecord record) throws SQLException {
		final String query = "MERGE INTO signed_prekeystore VALUES (?, ?)";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {
			stmt.setInt(1, id);
			stmt.setBytes(2, record.serialize());

			stmt.execute();
		}
	}
}
