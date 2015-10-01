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

import static de.norvos.i18n.Translations.translate;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;
import org.whispersystems.libaxolotl.util.KeyHelper;

import de.norvos.persistence.tables.SignedPreKeyTable;
import de.norvos.utils.Errors;
import de.norvos.utils.UnreachableCodeException;

/**
 * Contains the signed-prekey-related data for the TextSecure protocol.
 *
 * @author Connor Lanigan
 */
public class SignedPreKeyStore implements org.whispersystems.libaxolotl.state.SignedPreKeyStore {
	private static SignedPreKeyStore instance;

	final static Logger LOGGER = LoggerFactory.getLogger(SignedPreKeyStore.class);

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
			LOGGER.error("Signed pre key could not be fetched from database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}

	public void initialize(final IdentityKeyPair identityKeyPair, final int signedPreKeyId) {
		try {
			storeSignedPreKey(signedPreKeyId, KeyHelper.generateSignedPreKey(identityKeyPair, signedPreKeyId));
		} catch (final InvalidKeyException e) {
			LOGGER.error("Identity key pair for storage in SignedPreKeyStore was invalid.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}

	@Override
	public SignedPreKeyRecord loadSignedPreKey(final int signedPreKeyId) throws InvalidKeyIdException {
		try {
			final SignedPreKeyRecord record = SignedPreKeyTable.getInstance().loadKey(signedPreKeyId);
			if (record == null) {
				LOGGER.debug("Tried to fetch SignedPreKeyRecord for the invalid key ID [{}].", signedPreKeyId);
				throw new InvalidKeyIdException("Key id " + signedPreKeyId + " has no associated PreKeyRecord.");
			}
			return SignedPreKeyTable.getInstance().loadKey(signedPreKeyId);
		} catch (final SQLException e) {
			LOGGER.error("Signed pre key could not be fetched from database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		try {
			return SignedPreKeyTable.getInstance().loadAll();
		} catch (final SQLException e) {
			LOGGER.error("Signed pre keys could not be fetched from database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}

	@Override
	public void removeSignedPreKey(final int signedPreKeyId) {
		try {
			SignedPreKeyTable.getInstance().deleteKey(signedPreKeyId);
		} catch (final SQLException e) {
			LOGGER.error("Signed pre key could not be removed from database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}

	@Override
	public void storeSignedPreKey(final int signedPreKeyId, final SignedPreKeyRecord record) {
		try {
			SignedPreKeyTable.getInstance().storeKey(signedPreKeyId, record);
		} catch (final SQLException e) {
			LOGGER.error("Signed pre key could not be stored to database.", e);
			Errors.showError(translate("unexpected_quit"));
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}
}
