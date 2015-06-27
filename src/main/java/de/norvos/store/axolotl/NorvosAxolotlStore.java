package de.norvos.store.axolotl;

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

/**
 * An implementation of the AxolotlStore interface. This class only bundles the
 * interfaces {@link IdentityKeyStore},{@link PreKeyStore}, {@link SessionStore}
 * and {@link SignedPreKeyStore}.
 * 
 * @author Connor Lanigan {@literal <dev@connorlanigan.com>}
 *
 */
public class NorvosAxolotlStore implements AxolotlStore, PreKeyStore, IdentityKeyStore, SessionStore, SignedPreKeyStore {

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
		signedPreKeyStore = new NorvosSignedPreKeyStore(identityKeyStore.getIdentityKeyPair());
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
	}

	@Override
	public boolean containsPreKey(int preKeyId) {
		return preKeyStore.containsPreKey(preKeyId);
	}

	@Override
	public void removePreKey(int preKeyId) {
		preKeyStore.removePreKey(preKeyId);
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
	}

	@Override
	public boolean containsSession(AxolotlAddress address) {
		return sessionStore.containsSession(address);
	}

	@Override
	public void deleteSession(AxolotlAddress address) {
		sessionStore.deleteSession(address);
	}

	@Override
	public void deleteAllSessions(String name) {
		sessionStore.deleteAllSessions(name);
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
	}

	@Override
	public boolean containsSignedPreKey(int signedPreKeyId) {
		return signedPreKeyStore.containsSignedPreKey(signedPreKeyId);
	}

	@Override
	public void removeSignedPreKey(int signedPreKeyId) {
		signedPreKeyStore.removeSignedPreKey(signedPreKeyId);
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
