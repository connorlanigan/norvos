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
import java.util.LinkedList;
import java.util.List;

import de.norvos.messages.DecryptedMessage;
import de.norvos.persistence.Database;

public class DecryptedMessageTable implements Table {
	private static DecryptedMessageTable instance;

	synchronized public static DecryptedMessageTable getInstance() {
		if (instance == null) {
			instance = new DecryptedMessageTable();
		}
		return instance;
	}

	private DecryptedMessageTable() {
	}

	@Override
	public String getCreationStatement() {
		return "CREATE TABLE IF NOT EXISTS decrypted_messages ("
				+ "id VARCHAR PRIMARY KEY auto_increment, time_received LONG, thread_id INTEGER,"
				+ "read BOOLEAN, body VARCHAR, address VARCHAR, mismatched_identities VARCHAR, sent BOOLEAN)";
	}

	public List<DecryptedMessage> getMessages(final String address) {
		final List<DecryptedMessage> list = new LinkedList<>();
		final String query = "SELECT * FROM decrypted_messages WHERE address = ?";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setString(1, address);
			final ResultSet result = stmt.executeQuery();

			while (result.next()) {
				final long timestamp = result.getLong("timestamp");
				final boolean read = result.getBoolean("read");
				final String body = result.getString("body");
				final String mismatchedIdentities = result.getString("mismatched_identities");
				final boolean sent = result.getBoolean("sent");

				final DecryptedMessage message = new DecryptedMessage(timestamp, read, body, address,
						mismatchedIdentities, sent);
				list.add(message);
			}
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;

	}

	public void storeMessage(final DecryptedMessage message) throws SQLException {
		final String query = "INSERT INTO decrypted_messages VALUES"
				+ "time_received, read, body, address, mismatched_identities, sent" + "(?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setDouble(1, message.getTimestamp());
			stmt.setBoolean(2, message.isRead());
			stmt.setString(3, message.getBody());
			stmt.setString(4, message.getAddress());
			stmt.setString(5, message.getMismatchedIdentities());
			stmt.setBoolean(6, message.isSent());

			stmt.execute();
		}
	}

}
