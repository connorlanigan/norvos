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

import org.whispersystems.libaxolotl.IdentityKey;
import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.util.KeyHelper;

import de.norvos.log.Errors;
import de.norvos.persistence.tables.AccountDataTable;
import de.norvos.persistence.tables.IdentityKeyTable;

public class IdentityKeyStore implements org.whispersystems.libaxolotl.state.IdentityKeyStore {
	private static IdentityKeyStore instance;

	public static IdentityKeyStore getInstance() {
		if (instance == null) {
			instance = new IdentityKeyStore();
		}
		return instance;
	}

	private IdentityKeyStore() {
	}

	@Override
	public IdentityKeyPair getIdentityKeyPair() {
		try {
			return new IdentityKeyPair(AccountDataTable.getInstance().getBinary("identityKeyPair"));
		} catch (InvalidKeyException | SQLException e) {
			Errors.critical("databaseError");
			return null;
		}
	}

	@Override
	public int getLocalRegistrationId() {
		try {
			return Integer.valueOf(AccountDataTable.getInstance().getString("localRegistrationId"));
		} catch (final SQLException e) {
			Errors.critical("databaseError");
			return KeyHelper.generateRegistrationId(true);
		}
	}

	public void initialize() throws SQLException {
		setIdentityKeyPair(KeyHelper.generateIdentityKeyPair());
		setLocalRegistrationId(KeyHelper.generateRegistrationId(true));
	}

	@Override
	public boolean isTrustedIdentity(final String name, final IdentityKey identityKey) {
		IdentityKey storedKey;
		try {
			storedKey = IdentityKeyTable.getInstance().getIdentity(name);

			if (storedKey != null) {
				return storedKey.equals(identityKey);
			}
			return true;

		} catch (final SQLException e) {
			Errors.critical("databaseError");
			return true;
		}
	}

	@Override
	public void saveIdentity(final String name, final IdentityKey identityKey) {
		try {
			IdentityKeyTable.getInstance().storeIdentity(name, identityKey);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
		}
	}

	private void setIdentityKeyPair(final IdentityKeyPair keyPair) throws SQLException {
		AccountDataTable.getInstance().storeBinary("identityKeyPair", keyPair.serialize());
	}

	private void setLocalRegistrationId(final int id) throws SQLException {
		AccountDataTable.getInstance().storeString("localRegistrationId", String.valueOf(id));
	}

}
