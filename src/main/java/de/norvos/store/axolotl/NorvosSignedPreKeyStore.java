package de.norvos.store.axolotl;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.norvos.log.Errors;
import de.norvos.log.Logger;

import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;
import org.whispersystems.libaxolotl.state.SignedPreKeyStore;
import org.whispersystems.libaxolotl.util.KeyHelper;

import com.google.protobuf.ByteString;

import de.norvos.NorvosStorageProtos.SignedPreKeyStoreStructure;
import de.norvos.NorvosStorageProtos.SignedPreKeyStoreStructure.Builder;

public class NorvosSignedPreKeyStore implements SignedPreKeyStore {

	private List<SignedPreKeyRecord> signedPreKeys = new LinkedList<>();

	private SignedPreKeyRecord initialSignedPreKeyRecord;

	public SignedPreKeyRecord getInitialSignedPreKey() {
		return initialSignedPreKeyRecord;
	}

	public NorvosSignedPreKeyStore(IdentityKeyPair identityKey){
		for(int i = 0; i < 100; i++){
			try{
				initialSignedPreKeyRecord = KeyHelper.generateSignedPreKey(identityKey, 5);
				return;
			}catch(Exception e){}
		}
		Errors.handleCritical(null);
	}

	@Override
	synchronized public SignedPreKeyRecord loadSignedPreKey(int pId) throws InvalidKeyIdException {
		for (SignedPreKeyRecord record : signedPreKeys) {
			if (record.getId() == pId) {
				return record;
			}
		}
		Logger.debug("loadSignedPreKey(" + pId + ") could not find the requested key.");
		throw new InvalidKeyIdException("There is no corresponding key record in the store.");
	}

	@Override
	synchronized public List<SignedPreKeyRecord> loadSignedPreKeys() {
		return new LinkedList<SignedPreKeyRecord>(signedPreKeys);
	}

	@Override
	synchronized public void storeSignedPreKey(int pId, SignedPreKeyRecord pSignedRecord) {
		signedPreKeys.add(pSignedRecord);
	}

	@Override
	synchronized public boolean containsSignedPreKey(int pId) {
		for (SignedPreKeyRecord record : signedPreKeys) {
			if (record.getId() == pId) {
				return true;
			}
		}
		return false;
	}

	@Override
	synchronized public void removeSignedPreKey(int pId) {
		SignedPreKeyRecord foundRecord = null;
		for (SignedPreKeyRecord record : signedPreKeys) {
			if (record.getId() == pId) {
				foundRecord = record;
			}
		}
		signedPreKeys.remove(foundRecord);
	}

	public NorvosSignedPreKeyStore(byte[] serialized) throws IOException {
		SignedPreKeyStoreStructure struct = SignedPreKeyStoreStructure.parseFrom(serialized);
		for (ByteString key : struct.getSignedPreKeyList()) {
			signedPreKeys.add(new SignedPreKeyRecord(key.toByteArray()));
		}
		initialSignedPreKeyRecord = new SignedPreKeyRecord(struct.getInitialSignedPreKey().toByteArray());
	}

	synchronized public byte[] serialize() {
		Builder builder = SignedPreKeyStoreStructure.newBuilder();
		for (SignedPreKeyRecord entry : signedPreKeys) {
			builder.addSignedPreKey(ByteString.copyFrom(entry.serialize()));
		}
		return builder.build().toByteArray();
	}

}
