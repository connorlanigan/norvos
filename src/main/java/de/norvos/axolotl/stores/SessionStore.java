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
package de.norvos.axolotl.stores;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.whispersystems.libaxolotl.AxolotlAddress;
import org.whispersystems.libaxolotl.state.SessionRecord;

import de.norvos.log.Errors;
import de.norvos.persistence.tables.SessionTable;

public class SessionStore implements org.whispersystems.libaxolotl.state.SessionStore {
	private static SessionStore instance;

	synchronized public static SessionStore getInstance() {
		if (instance == null) {
			instance = new SessionStore();
		}
		return instance;
	}

	private SessionStore() {
	}

	@Override
	public boolean containsSession(final AxolotlAddress address) {
		try {
			return null != SessionTable.getInstance().getSession(address);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
			return false;
		}
	}

	@Override
	public void deleteAllSessions(final String name) {
		try {
			SessionTable.getInstance().deleteSessions(name);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
		}
	}

	@Override
	public void deleteSession(final AxolotlAddress address) {
		try {
			SessionTable.getInstance().deleteSession(address);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
		}
	}

	@Override
	public List<Integer> getSubDeviceSessions(final String name) {
		try {
			return SessionTable.getInstance().getSessions(name);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
			return new LinkedList<Integer>();
		}
	}

	@Override
	public SessionRecord loadSession(final AxolotlAddress address) {
		try {
			SessionRecord record = SessionTable.getInstance().getSession(address);
			if (record == null) {
				record = new SessionRecord();
			}
			return record;
		} catch (final SQLException e) {
			Errors.critical("databaseError");
			return new SessionRecord();
		}
	}

	@Override
	public void storeSession(final AxolotlAddress address, final SessionRecord record) {
		try {
			SessionTable.getInstance().storeSession(address, record);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
		}
	}

}
