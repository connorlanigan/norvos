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
import de.norvos.axolotl.substores.NorvosIdentityKeyStore;
import de.norvos.axolotl.substores.NorvosPreKeyStore;
import de.norvos.axolotl.substores.NorvosSessionStore;
import de.norvos.axolotl.substores.NorvosSignedPreKeyStore;
import de.norvos.log.Logger;
import de.norvos.observers.Notifiable;
import de.norvos.observers.NotificatorMap;
import de.norvos.observers.Observable;
import de.norvos.persistence.DiskPersistence;

/**
 * An implementation of the AxolotlStore interface. This class only bundles the
 * interfaces {@link IdentityKeyStore},{@link PreKeyStore}, {@link SessionStore}
 * and {@link SignedPreKeyStore}.
 * 
 * @author Connor Lanigan {@literal <dev@connorlanigan.com>}
 *
 */
public class NorvosAxolotlStore implements AxolotlStore, PreKeyStore, IdentityKeyStore, SessionStore, SignedPreKeyStore, Observable{

	NotificatorMap notifiables = new NotificatorMap();
	
	@Override
	public void register(Notifiable n, String event) {
		notifiables.register(event, n);
	}

	@Override
	public void unregister(Notifiable n) {
		notifiables.unregister(n);
	}
	
	
	private NorvosIdentityKeyStore identityKeyStore;
	private NorvosPreKeyStore preKeyStore;
	private NorvosSessionStore sessionStore;
	private NorvosSignedPreKeyStore signedPreKeyStore;

	/**
	 * Initializes all substores with their default constructor.
	 * 
	 * @throws InvalidKeyException
	 *             when the generated IdentityKeyPair is invalid
	 */
	public NorvosAxolotlStore(){
		identityKeyStore = new NorvosIdentityKeyStore();
		preKeyStore = new NorvosPreKeyStore();
		sessionStore = new NorvosSessionStore();
		try {
			signedPreKeyStore = new NorvosSignedPreKeyStore(identityKeyStore.getIdentityKeyPair());
		} catch (InvalidKeyException e) {
			Logger.critical("Creation of the SignedPreKeyStore failed. Reason: "+e.getMessage());
		}
		register(new DiskPersistence(), "axolotlStoreChange");
	}
	
	public PreKeyRecord getLastResortKey() {
		return preKeyStore.getLastResortKey();
	}
	public SignedPreKeyRecord getSignedPreKey(){
		return signedPreKeyStore.getInitialSignedPreKey();
	}
	public List<PreKeyRecord> getOneTimePreKeys(){
		return preKeyStore.getPreKeys();
	}

	@Override
	public IdentityKeyPair getIdentityKeyPair() {
		return identityKeyStore.getIdentityKeyPair();
	}

	@Override
	public int getLocalRegistrationId() {
		return identityKeyStore.getLocalRegistrationId();
	}

	@Override
	public void saveIdentity(String name, IdentityKey identityKey) {
		identityKeyStore.saveIdentity(name, identityKey);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public boolean isTrustedIdentity(String name, IdentityKey identityKey) {
		return identityKeyStore.isTrustedIdentity(name, identityKey);
	}

	@Override
	public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
		return preKeyStore.loadPreKey(preKeyId);
	}

	@Override
	public void storePreKey(int preKeyId, PreKeyRecord record) {
		preKeyStore.storePreKey(preKeyId, record);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public boolean containsPreKey(int preKeyId) {
		return preKeyStore.containsPreKey(preKeyId);
	}

	@Override
	public void removePreKey(int preKeyId) {
		preKeyStore.removePreKey(preKeyId);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public SessionRecord loadSession(AxolotlAddress address) {
		return sessionStore.loadSession(address);
	}

	@Override
	public List<Integer> getSubDeviceSessions(String name) {
		return sessionStore.getSubDeviceSessions(name);
	}

	@Override
	public void storeSession(AxolotlAddress address, SessionRecord record) {
		sessionStore.storeSession(address, record);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public boolean containsSession(AxolotlAddress address) {
		return sessionStore.containsSession(address);
	}

	@Override
	public void deleteSession(AxolotlAddress address) {
		sessionStore.deleteSession(address);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public void deleteAllSessions(String name) {
		sessionStore.deleteAllSessions(name);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
		return signedPreKeyStore.loadSignedPreKey(signedPreKeyId);
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		return signedPreKeyStore.loadSignedPreKeys();
	}

	@Override
	public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
		signedPreKeyStore.storeSignedPreKey(signedPreKeyId, record);
		notifiables.notify("axolotlStoreChange", this);
	}

	@Override
	public boolean containsSignedPreKey(int signedPreKeyId) {
		return signedPreKeyStore.containsSignedPreKey(signedPreKeyId);
	}

	@Override
	public void removeSignedPreKey(int signedPreKeyId) {
		signedPreKeyStore.removeSignedPreKey(signedPreKeyId);
		notifiables.notify("axolotlStoreChange", this);
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
	public NorvosAxolotlStore(byte[] serialized) throws IOException {
		AxolotlStoreStructure struct = AxolotlStoreStructure.parseFrom(serialized);
		identityKeyStore = new NorvosIdentityKeyStore(struct.getIdentityKeyStore().toByteArray());
		preKeyStore = new NorvosPreKeyStore(struct.getPreKeyStore().toByteArray());
		sessionStore = new NorvosSessionStore(struct.getSessionStore().toByteArray());
		signedPreKeyStore = new NorvosSignedPreKeyStore(struct.getSignedPreKeyStore().toByteArray());
	}

	/**
	 * Serializes this store to a byte-array. It can later be reconstructed by
	 * using the constructor(byte[]).
	 * 
	 * @return Bytearray representing this store
	 */
	public byte[] serialize() {
		Builder builder = AxolotlStoreStructure.newBuilder();
		builder.setIdentityKeyStore(ByteString.copyFrom(identityKeyStore.serialize()));
		builder.setPreKeyStore(ByteString.copyFrom(preKeyStore.serialize()));
		builder.setSessionStore(ByteString.copyFrom(sessionStore.serialize()));
		builder.setSignedPreKeyStore(ByteString.copyFrom(signedPreKeyStore.serialize()));
		return builder.build().toByteArray();
	}

}
