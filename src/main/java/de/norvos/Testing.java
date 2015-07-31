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
package de.norvos;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;

import de.norvos.persistence.Database;
import de.norvos.persistence.tables.IdentityKeyTable;

public class Testing {

	public static void main(final String[] args) throws Exception {
		final String name = "connor";
		final byte[] bytes = new byte[] { 1, 1, 1, 1 };

		final String query = "MERGE INTO identity_keystore VALUES (?, ?)";

		try (final PreparedStatement stmt =
				Database.ensureTableExists(IdentityKeyTable.getInstance()).prepareStatement(query)) {

			stmt.setString(1, name);
			stmt.setBinaryStream(2, new ByteArrayInputStream(bytes));

			stmt.execute();
		}
	}
}
