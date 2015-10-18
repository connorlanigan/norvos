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
import java.sql.Statement;
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

	public void deleteMessage(final long messageId) throws SQLException {
		if (messageId == -1) {
			return;
		}
		final String query = "DELETE FROM decrypted_messages WHERE id = ?";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			System.out.println(messageId);
			stmt.setLong(1, messageId);

			stmt.execute();
		}
	}

	@Override
	public String getCreationStatement() {
		return "CREATE TABLE IF NOT EXISTS decrypted_messages ("
				+ "id BIGINT PRIMARY KEY auto_increment, timestamp LONG, thread_id INTEGER,"
				+ "read BOOLEAN, body VARCHAR, address VARCHAR, mismatched_identities VARCHAR, sent BOOLEAN)";
	}

	public DecryptedMessage getLastMessage(final String address) {
		final String query = "SELECT * FROM decrypted_messages WHERE address = ? ORDER BY id DESC LIMIT 1";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query)) {

			stmt.setString(1, address);
			final ResultSet result = stmt.executeQuery();

			if (result.first()) {
				final long timestamp = result.getLong("timestamp");
				final boolean read = result.getBoolean("read");
				final String body = result.getString("body");
				final String mismatchedIdentities = result.getString("mismatched_identities");
				final boolean sent = result.getBoolean("sent");
				final long messageId = result.getLong("id");

				// TODO store and read attachments
				final DecryptedMessage message = new DecryptedMessage(timestamp, read, body, address,
						mismatchedIdentities, sent, -1, messageId);
				return message;
			}
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
				final long messageId = result.getLong("id");

				// TODO store and read attachments
				final DecryptedMessage message = new DecryptedMessage(timestamp, read, body, address,
						mismatchedIdentities, sent, -1, messageId);
				list.add(message);
			}
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public long storeMessage(final DecryptedMessage message) throws SQLException {
		final String query = "INSERT INTO decrypted_messages "
				+ "(timestamp, read, body, address, mismatched_identities, sent) VALUES (?, ?, ?, ?, ?, ?)";

		try (PreparedStatement stmt = Database.ensureTableExists(this).prepareStatement(query,
				Statement.RETURN_GENERATED_KEYS)) {

			stmt.setDouble(1, message.getTimestamp());
			stmt.setBoolean(2, message.isRead());
			stmt.setString(3, message.getBody());
			stmt.setString(4, message.getAddress());
			stmt.setString(5, message.getMismatchedIdentities());
			stmt.setBoolean(6, message.isSent());

			stmt.execute();

			final ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			return rs.getLong(1);
		}
	}

}
