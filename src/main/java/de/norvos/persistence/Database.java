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
package de.norvos.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import de.norvos.persistence.tables.Table;
import de.norvos.utils.ResourceUtils;

/**
 * Provides access to the database.
 * @author Connor Lanigan
 */
public class Database {
	private static Connection connection;

	/**
	 * Closes the database connection.
	 * @throws SQLException
	 */
	public static void close() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	/**
	 * Call this method before interacting with the database. This ensures that
	 * your table does exist before you interact with it. If your table does not
	 * yet exist, it will be created.
	 *
	 * @param table
	 *            the table to ensure existence for
	 * @return a connection to the database (to allow for method chaining)
	 * @throws SQLException if an error occurs during interaction with the database
	 */
	public static Connection ensureTableExists(final Table table) throws SQLException {
		establishConnection();
		try (final Statement stmt = connection.createStatement()) {
			stmt.execute(table.getCreationStatement());
			return connection;
		}
	}

	private static void establishConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			try {
				Class.forName("org.h2.Driver");
				connection = DriverManager.getConnection("jdbc:h2:" + ResourceUtils.getDatabaseDirectory());
			} catch (final ClassNotFoundException e) {
				System.err.println("The database driver is not available!");
				throw new SQLException(e);
			}
		}
	}
}
