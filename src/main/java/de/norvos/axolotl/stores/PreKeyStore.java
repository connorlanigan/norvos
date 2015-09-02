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

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.PreKeyRecord;
import org.whispersystems.libaxolotl.util.KeyHelper;
import org.whispersystems.libaxolotl.util.Medium;

import de.norvos.log.Errors;
import de.norvos.persistence.tables.AccountDataTable;
import de.norvos.persistence.tables.PreKeyTable;

public class PreKeyStore implements org.whispersystems.libaxolotl.state.PreKeyStore {

	private static PreKeyStore instance;

	public static PreKeyStore getInstance() {
		if (instance == null) {
			instance = new PreKeyStore();
		}
		return instance;
	}

	private PreKeyStore() {
	}

	@Override
	public boolean containsPreKey(final int preKeyId) {
		try {
			return PreKeyTable.getInstance().getKey(preKeyId) != null;
		} catch (final SQLException e) {
			System.err.println(e.getMessage());
			Errors.critical("databaseError");
			return false;
		}
	}

	public int getLastIndex() {
		return PreKeyTable.getInstance().getLastIndex();
	}

	public PreKeyRecord getLastResortKey() {
		try {
			final byte[] record = AccountDataTable.getInstance().getBinary("lastResortKey");
			if (record == null) {
				return null;
			}
			return new PreKeyRecord(record);
		} catch (IOException | SQLException e) {
			Errors.critical("databaseError");
			return null;
		}
	}

	public List<PreKeyRecord> initialize() {

		final int numberOfKeys = 100;
		final int startingId = (new Random()).nextInt(Medium.MAX_VALUE - numberOfKeys);

		storeLastResortKey(KeyHelper.generateLastResortPreKey());
		final List<PreKeyRecord> list = KeyHelper.generatePreKeys(startingId, numberOfKeys);

		for (final PreKeyRecord key : list) {
			storePreKey(key.getId(), key);
		}

		return list;
	}

	@Override
	public PreKeyRecord loadPreKey(final int preKeyId) throws InvalidKeyIdException {
		try {
			final PreKeyRecord record = PreKeyTable.getInstance().getKey(preKeyId);
			if (record == null) {
				throw new InvalidKeyIdException("Key id " + preKeyId + " has no associated PreKeyRecord.");
			}
			return PreKeyTable.getInstance().getKey(preKeyId);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
			throw new InvalidKeyIdException(e);
		}
	}

	@Override
	public void removePreKey(final int preKeyId) {
		try {
			PreKeyTable.getInstance().deleteKey(preKeyId);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
		}
	}

	@SuppressWarnings("static-method")
	private void storeLastResortKey(final PreKeyRecord record) {
		try {
			AccountDataTable.getInstance().storeBinary("lastResortKey", record.serialize());
		} catch (final SQLException e) {
			Errors.critical("databaseError");
		}
	}

	@Override
	public void storePreKey(final int preKeyId, final PreKeyRecord record) {
		try {
			PreKeyTable.getInstance().storeKey(preKeyId, record);
		} catch (final SQLException e) {
			System.err.println(e.getMessage());
			Errors.critical("databaseError");
		}
	}
}
