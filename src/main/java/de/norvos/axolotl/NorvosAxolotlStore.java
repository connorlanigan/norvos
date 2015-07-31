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

import java.io.IOException;
import java.util.List;

import org.whispersystems.libaxolotl.AxolotlAddress;
import org.whispersystems.libaxolotl.IdentityKey;
import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.AxolotlStore;
import org.whispersystems.libaxolotl.state.IdentityKeyStore;
import org.whispersystems.libaxolotl.state.PreKeyRecord;
import org.whispersystems.libaxolotl.state.PreKeyStore;
import org.whispersystems.libaxolotl.state.SessionRecord;
import org.whispersystems.libaxolotl.state.SessionStore;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;
import org.whispersystems.libaxolotl.state.SignedPreKeyStore;

import com.google.protobuf.ByteString;

import de.norvos.NorvosStorageProtos.AxolotlStoreStructure;
import de.norvos.NorvosStorageProtos.AxolotlStoreStructure.Builder;
import de.norvos.axolotl.old_substores.NorvosIdentityKeyStore;
import de.norvos.axolotl.old_substores.NorvosPreKeyStore;
import de.norvos.axolotl.old_substores.NorvosSessionStore;
import de.norvos.axolotl.old_substores.NorvosSignedPreKeyStore;
import de.norvos.log.Errors;
import de.norvos.observers.Notifiable;
import de.norvos.observers.NotificatorMap;
import de.norvos.observers.Observable;

/**
 * An implementation of the AxolotlStore interface. This class only bundles the
 * interfaces {@link IdentityKeyStore},{@link PreKeyStore}, {@link SessionStore}
 * and {@link SignedPreKeyStore}.
 *
 * @author Connor Lanigan {@literal <dev@connorlanigan.com>}
 *
 */
public class NorvosAxolotlStore implements AxolotlStore, PreKeyStore, IdentityKeyStore, SessionStore,
		SignedPreKeyStore, Observable {

	private final NorvosIdentityKeyStore identityKeyStore;

	NotificatorMap notifiables = new NotificatorMap();

	private final NorvosPreKeyStore preKeyStore;

	private final NorvosSessionStore sessionStore;
	private NorvosSignedPreKeyStore signedPreKeyStore;

	/**
	 * Initializes all substores with their default constructor.
	 *
	 * @throws InvalidKeyException
	 *             when the generated IdentityKeyPair is invalid
	 */
	public NorvosAxolotlStore() {
		identityKeyStore = new NorvosIdentityKeyStore();
		preKeyStore = new NorvosPreKeyStore();
		sessionStore = new NorvosSessionStore();
		try {
			signedPreKeyStore = new NorvosSignedPreKeyStore(identityKeyStore.getIdentityKeyPair());
		} catch (final InvalidKeyException e) {
			Errors.critical("Creation of the SignedPreKeyStore failed. Reason: " + e.getMessage());
		}
		register(new DiskPersistence(), "axolotlStoreChange");
	}

	/**
	 * Reconstructs a NorvosAxolotlStore from a given byte-array. The byte-array
	 * needs to have been created by calling serialize() on a
	 * NorvosAxolotlStore.
	 *
	 * @param serialized
	 *            the byte-array containing the store
	 * @throws IOException
	 *             if the byte-array does not represent a valid
	 *             NorvosAxolotlStore
	 */
	public NorvosAxolotlStore(final byte[] serialized) throws IOException {
		final AxolotlStoreStructure struct = AxolotlStoreStructure.parseFrom(serialized);
		identityKeyStore = new NorvosIdentityKeyStore(struct.getIdentityKeyStore().toByteArray());
		preKeyStore = new NorvosPreKeyStore(struct.getPreKeyStore().toByteArray());
		sessionStore = new NorvosSessionStore(struct.getSessionStore().toByteArray());
		signedPreKeyStore = new NorvosSignedPreKeyStore(struct.getSignedPreKeyStore().toByteArray());
	}

	@Override
	public boolean containsPreKey(final int preKeyId) {
		return preKeyStore.containsPreKey(preKeyId);
	}

	@Override
	public boolean containsSession(final AxolotlAddress address) {
		return sessionStore.containsSession(address);
	}

	@Override
	public boolean containsSignedPreKey(final int signedPreKeyId) {
		return signedPreKeyStore.containsSignedPreKey(signedPreKeyId);
	}

	@Override
	public void deleteAllSessions(final String name) {
		sessionStore.deleteAllSessions(name);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public void deleteSession(final AxolotlAddress address) {
		sessionStore.deleteSession(address);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public IdentityKeyPair getIdentityKeyPair() {
		return identityKeyStore.getIdentityKeyPair();
	}

	public PreKeyRecord getLastResortKey() {
		return preKeyStore.getLastResortKey();
	}

	@Override
	public int getLocalRegistrationId() {
		return identityKeyStore.getLocalRegistrationId();
	}

	public List<PreKeyRecord> getOneTimePreKeys() {
		return preKeyStore.getPreKeys();
	}

	public SignedPreKeyRecord getSignedPreKey() {
		return signedPreKeyStore.getInitialSignedPreKey();
	}

	@Override
	public List<Integer> getSubDeviceSessions(final String name) {
		return sessionStore.getSubDeviceSessions(name);
	}

	@Override
	public boolean isTrustedIdentity(final String name, final IdentityKey identityKey) {
		return identityKeyStore.isTrustedIdentity(name, identityKey);
	}

	@Override
	public PreKeyRecord loadPreKey(final int preKeyId) throws InvalidKeyIdException {
		return preKeyStore.loadPreKey(preKeyId);
	}

	@Override
	public SessionRecord loadSession(final AxolotlAddress address) {
		return sessionStore.loadSession(address);
	}

	@Override
	public SignedPreKeyRecord loadSignedPreKey(final int signedPreKeyId) throws InvalidKeyIdException {
		return signedPreKeyStore.loadSignedPreKey(signedPreKeyId);
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		return signedPreKeyStore.loadSignedPreKeys();
	}

	@Override
	public void register(final Notifiable n, final String event) {
		notifiables.register(event, n);
	}

	@Override
	public void removePreKey(final int preKeyId) {
		preKeyStore.removePreKey(preKeyId);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public void removeSignedPreKey(final int signedPreKeyId) {
		signedPreKeyStore.removeSignedPreKey(signedPreKeyId);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public void saveIdentity(final String name, final IdentityKey identityKey) {
		identityKeyStore.saveIdentity(name, identityKey);
		notifiables.notify("axolotlStoreChange", this);
	}

	/**
	 * Serializes this store to a byte-array. It can later be reconstructed by
	 * using the constructor(byte[]).
	 *
	 * @return Bytearray representing this store
	 */
	public byte[] serialize() {
		final Builder builder = AxolotlStoreStructure.newBuilder();
		builder.setIdentityKeyStore(ByteString.copyFrom(identityKeyStore.serialize()));
		builder.setPreKeyStore(ByteString.copyFrom(preKeyStore.serialize()));
		builder.setSessionStore(ByteString.copyFrom(sessionStore.serialize()));
		builder.setSignedPreKeyStore(ByteString.copyFrom(signedPreKeyStore.serialize()));
		return builder.build().toByteArray();
	}

	@Override
	public void storePreKey(final int preKeyId, final PreKeyRecord record) {
		preKeyStore.storePreKey(preKeyId, record);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public void storeSession(final AxolotlAddress address, final SessionRecord record) {
		sessionStore.storeSession(address, record);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public void storeSignedPreKey(final int signedPreKeyId, final SignedPreKeyRecord record) {
		signedPreKeyStore.storeSignedPreKey(signedPreKeyId, record);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public void unregister(final Notifiable n) {
		notifiables.unregister(n);
	}

}
