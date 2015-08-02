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

import org.whispersystems.libaxolotl.AxolotlAddress;
import org.whispersystems.libaxolotl.state.SessionRecord;

import de.norvos.persistence.Database;

public class SessionTable implements Table {

	private static SessionTable instance;

	public static SessionTable getInstance() {
		if (instance == null) {
			instance = new SessionTable();
		}
		return instance;
	}

	private SessionTable() {
	}

	public void deleteSession(final AxolotlAddress address) throws SQLException {
		final String query = "DELETE FROM session_store WHERE name = ? AND device_id = ?";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {
			stmt.setString(1, address.getName());
			stmt.setInt(2, address.getDeviceId());

			stmt.execute();
		}
	}

	public void deleteSessions(final String name) throws SQLException {
		final String query = "DELETE FROM session_store WHERE name = ?";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {
			stmt.setString(1, name);

			stmt.execute();
		}
	}

	@Override
	public String getCreationStatement() {
		return "CREATE TABLE IF NOT EXISTS session_store ( name VARCHAR NOT NULL, device_id INTEGER NOT NULL, session_record BINARY NOT NULL, PRIMARY KEY (name, device_id))";
	}

	public SessionRecord getSession(final AxolotlAddress address) throws SQLException {
		final String query = "SELECT session_record FROM session_store WHERE name = ? AND device_id = ?";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {
			stmt.setString(1, address.getName());
			stmt.setInt(2, address.getDeviceId());
			final ResultSet result = stmt.executeQuery();
			if (result.first()) {
				return new SessionRecord(result.getBytes(1));
			}
			return null;
		} catch (final IOException e) {
			throw new SQLException("SessionTable: Value of session_record for address [" + address.toString()
					+ "] is invalid.", e);
		}
	}

	public List<Integer> getSessions(final String name) throws SQLException {
		final String query = "SELECT device_id FROM session_store WHERE name = ?";
		final List<Integer> list = new LinkedList<Integer>();

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {
			stmt.setString(1, name);
			final ResultSet result = stmt.executeQuery();

			while (result.next()) {
				list.add(result.getInt(1));
			}
			return list;
		}
	}

	public void storeSession(final AxolotlAddress address, final SessionRecord record) throws SQLException {
		final String query = "MERGE INTO session_store VALUES (?, ?, ?)";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {
			stmt.setString(1, address.getName());
			stmt.setInt(2, address.getDeviceId());
			stmt.setBytes(3, record.serialize());

			stmt.execute();
		}
	}

}
