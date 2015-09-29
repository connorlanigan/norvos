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

import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;
import org.whispersystems.libaxolotl.util.KeyHelper;

import de.norvos.log.Errors;
import de.norvos.persistence.tables.SignedPreKeyTable;

/**
 * Contains the signed-prekey-related data for the TextSecure protocol.
 * @author Connor Lanigan
 */
public class SignedPreKeyStore implements org.whispersystems.libaxolotl.state.SignedPreKeyStore {

	private static SignedPreKeyStore instance;

	synchronized public static SignedPreKeyStore getInstance() {
		if (instance == null) {
			instance = new SignedPreKeyStore();
		}
		return instance;
	}

	private SignedPreKeyStore() {
	}

	@Override
	public boolean containsSignedPreKey(final int signedPreKeyId) {
		try {
			return null != SignedPreKeyTable.getInstance().loadKey(signedPreKeyId);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
			return false;
		}
	}

	public void initialize(final IdentityKeyPair identityKeyPair, final int signedPreKeyId) {
		try {
			storeSignedPreKey(signedPreKeyId, KeyHelper.generateSignedPreKey(identityKeyPair, signedPreKeyId));
		} catch (final InvalidKeyException e) {
			Errors.critical("cannotCreateKey");
		}
	}

	@Override
	public SignedPreKeyRecord loadSignedPreKey(final int signedPreKeyId) throws InvalidKeyIdException {
		try {
			final SignedPreKeyRecord record = SignedPreKeyTable.getInstance().loadKey(signedPreKeyId);
			if (record == null) {
				throw new InvalidKeyIdException("Key id " + signedPreKeyId + " has no associated PreKeyRecord.");
			}
			return SignedPreKeyTable.getInstance().loadKey(signedPreKeyId);
		} catch (final SQLException e) {
			throw new InvalidKeyIdException(e);
		}
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		try {
			return SignedPreKeyTable.getInstance().loadAll();
		} catch (final SQLException e) {
			Errors.critical("databaseError");
			return new LinkedList<SignedPreKeyRecord>();
		}
	}

	@Override
	public void removeSignedPreKey(final int signedPreKeyId) {
		try {
			SignedPreKeyTable.getInstance().deleteKey(signedPreKeyId);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
		}
	}

	@Override
	public void storeSignedPreKey(final int signedPreKeyId, final SignedPreKeyRecord record) {
		try {
			SignedPreKeyTable.getInstance().storeKey(signedPreKeyId, record);
		} catch (final SQLException e) {
			Errors.critical("databaseError");
		}
	}
}
