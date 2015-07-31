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

	@Override
	public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
		try {
			return PreKeyTable.getInstance().getKey(preKeyId);
		} catch (SQLException e) {
			Errors.critical("databaseError");
			throw new InvalidKeyIdException(e);
		}
	}

	public PreKeyRecord getLastResortKey() {
		try {
			return new PreKeyRecord(AccountDataTable.getInstance().getBinary("lastResortKey"));
		} catch (IOException | SQLException e) {
			Errors.critical("databaseError");
			return null;
		}
	}

	private static void storeLastResortKey(PreKeyRecord record) {
		try {
			AccountDataTable.getInstance().storeBinary("lastResortKey", record.serialize());
		} catch (SQLException e) {
			Errors.critical("databaseError");
		}
	}

	public void initialize() {
		storeLastResortKey(KeyHelper.generateLastResortPreKey());

		int numberOfKeys = 100;
		final List<PreKeyRecord> list =
				KeyHelper.generatePreKeys((new Random()).nextInt(Medium.MAX_VALUE - numberOfKeys), numberOfKeys);
		for (final PreKeyRecord key : list) {
			storePreKey(key.getId(), key);
		}
	}

	@Override
	public void storePreKey(int preKeyId, PreKeyRecord record) {
		try {
			PreKeyTable.getInstance().storeKey(preKeyId, record);
		} catch (SQLException e) {
			Errors.critical("databaseError");
		}
	}

	@Override
	public boolean containsPreKey(int preKeyId) {
		try {
			return null == PreKeyTable.getInstance().getKey(preKeyId);
		} catch (SQLException e) {
			Errors.critical("databaseError");
			return false;
		}
	}

	@Override
	public void removePreKey(int preKeyId) {
		try {
			PreKeyTable.getInstance().deleteKey(preKeyId);
		} catch (SQLException e) {
			Errors.critical("databaseError");
		}
	}

}
