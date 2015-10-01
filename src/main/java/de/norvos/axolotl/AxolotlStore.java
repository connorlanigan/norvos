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
package de.norvos.axolotl;

import java.util.List;

import org.whispersystems.libaxolotl.AxolotlAddress;
import org.whispersystems.libaxolotl.IdentityKey;
import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.PreKeyRecord;
import org.whispersystems.libaxolotl.state.SessionRecord;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;

import de.norvos.axolotl.stores.IdentityKeyStore;
import de.norvos.axolotl.stores.PreKeyStore;
import de.norvos.axolotl.stores.SessionStore;
import de.norvos.axolotl.stores.SignedPreKeyStore;

/**
 * The store containing all data needed for the encryption and decryption
 * process in the TextSecure protocol.
 *
 * @author Connor Lanigan
 */
public class AxolotlStore implements org.whispersystems.libaxolotl.state.AxolotlStore {

	private static AxolotlStore instance;

	synchronized public static AxolotlStore getInstance() {
		if (instance == null) {
			instance = new AxolotlStore();
		}
		return instance;
	}

	private AxolotlStore() {
	}

	@Override
	public boolean containsPreKey(final int preKeyId) {
		return PreKeyStore.getInstance().containsPreKey(preKeyId);
	}

	@Override
	public boolean containsSession(final AxolotlAddress address) {
		return SessionStore.getInstance().containsSession(address);
	}

	@Override
	public boolean containsSignedPreKey(final int signedPreKeyId) {
		return SignedPreKeyStore.getInstance().containsSignedPreKey(signedPreKeyId);
	}

	@Override
	public void deleteAllSessions(final String name) {
		SessionStore.getInstance().deleteAllSessions(name);
	}

	@Override
	public void deleteSession(final AxolotlAddress address) {
		SessionStore.getInstance().deleteSession(address);
	}

	@Override
	public IdentityKeyPair getIdentityKeyPair() {
		return IdentityKeyStore.getInstance().getIdentityKeyPair();
	}

	@Override
	public int getLocalRegistrationId() {
		return IdentityKeyStore.getInstance().getLocalRegistrationId();
	}

	@Override
	public List<Integer> getSubDeviceSessions(final String name) {
		return SessionStore.getInstance().getSubDeviceSessions(name);
	}

	@Override
	public boolean isTrustedIdentity(final String name, final IdentityKey identityKey) {
		return IdentityKeyStore.getInstance().isTrustedIdentity(name, identityKey);
	}

	@Override
	public PreKeyRecord loadPreKey(final int preKeyId) throws InvalidKeyIdException {
		return PreKeyStore.getInstance().loadPreKey(preKeyId);
	}

	@Override
	public SessionRecord loadSession(final AxolotlAddress address) {
		return SessionStore.getInstance().loadSession(address);
	}

	@Override
	public SignedPreKeyRecord loadSignedPreKey(final int signedPreKeyId) throws InvalidKeyIdException {
		return SignedPreKeyStore.getInstance().loadSignedPreKey(signedPreKeyId);
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		return SignedPreKeyStore.getInstance().loadSignedPreKeys();
	}

	@Override
	public void removePreKey(final int preKeyId) {
		PreKeyStore.getInstance().removePreKey(preKeyId);
	}

	@Override
	public void removeSignedPreKey(final int signedPreKeyId) {
		SignedPreKeyStore.getInstance().removeSignedPreKey(signedPreKeyId);
	}

	@Override
	public void saveIdentity(final String name, final IdentityKey identityKey) {
		IdentityKeyStore.getInstance().saveIdentity(name, identityKey);
	}

	@Override
	public void storePreKey(final int preKeyId, final PreKeyRecord record) {
		PreKeyStore.getInstance().storePreKey(preKeyId, record);
	}

	@Override
	public void storeSession(final AxolotlAddress address, final SessionRecord record) {
		SessionStore.getInstance().storeSession(address, record);
	}

	@Override
	public void storeSignedPreKey(final int signedPreKeyId, final SignedPreKeyRecord record) {
		SignedPreKeyStore.getInstance().storeSignedPreKey(signedPreKeyId, record);
	}

}
