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

import de.norvos.account.SettingsService;
import de.norvos.log.Errors;
import de.norvos.persistence.tables.AccountDataTable;
import de.norvos.persistence.tables.IdentityKeyTable;

public class IdentityKeyStore implements org.whispersystems.libaxolotl.state.IdentityKeyStore {
	private static IdentityKeyStore instance;

	synchronized public static IdentityKeyStore getInstance() {
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
			final byte[] keyPairBytes = SettingsService.getIdentityKeyPair();
			if (keyPairBytes == null) {
				return null;
			}
			return new IdentityKeyPair(keyPairBytes);
		} catch (final InvalidKeyException e) {
			Errors.critical("databaseError");
			return null;
		}
	}

	@Override
	public int getLocalRegistrationId() {
		try {
			final String registrationIdString = AccountDataTable.getInstance().getString("localRegistrationId");
			if (registrationIdString == null) {
				return KeyHelper.generateRegistrationId(true);
			}
			return Integer.valueOf(registrationIdString);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
			return KeyHelper.generateRegistrationId(true);
		}
	}

	public void initialize() {
		final IdentityKeyPair identityKeyPair = KeyHelper.generateIdentityKeyPair();
		setIdentityKeyPair(identityKeyPair);
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
			System.err.println(e.getMessage());
			;
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
			throw new NullPointerException(e.getMessage());
		}
	}

	@SuppressWarnings("static-method")
	private void setIdentityKeyPair(final IdentityKeyPair keyPair) {
		SettingsService.setIdentityKeyPair(keyPair);
	}

	@SuppressWarnings("static-method")
	private void setLocalRegistrationId(final int id) {
		SettingsService.setLocalRegistrationId(id);
	}

}
