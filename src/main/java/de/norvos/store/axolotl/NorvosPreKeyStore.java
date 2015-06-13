package de.norvos.store.axolotl;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import de.norvos.log.Logger;
import de.norvos.store.CircularBuffer;

import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.PreKeyRecord;
import org.whispersystems.libaxolotl.state.PreKeyStore;
import org.whispersystems.libaxolotl.util.KeyHelper;
import org.whispersystems.libaxolotl.util.Medium;

import com.google.protobuf.ByteString;

import de.norvos.NorvosStorageProtos.PreKeyStoreStructure;
import de.norvos.NorvosStorageProtos.PreKeyStoreStructure.Builder;
import de.norvos.NorvosStorageProtos.PreKeyStoreStructure.PreKeyStructure;

public class NorvosPreKeyStore implements PreKeyStore {

	private CircularBuffer<PreKeyRecord> oneTimePreKeys;

	private PreKeyRecord lastResortKey;

	public PreKeyRecord getLastResortKey() {
		return lastResortKey;
	}

	public NorvosPreKeyStore() {
		lastResortKey = KeyHelper.generateLastResortPreKey();
		oneTimePreKeys = new CircularBuffer<PreKeyRecord>(Medium.MAX_VALUE);
		Random r = new Random();
		List<PreKeyRecord> list = KeyHelper.generatePreKeys(r.nextInt(Medium.MAX_VALUE), 100);
		for (PreKeyRecord key : list) {
			oneTimePreKeys.add(key.getId(), key);
		}
	}

	@Override
	synchronized public PreKeyRecord loadPreKey(int pId) throws InvalidKeyIdException {
		PreKeyRecord record = oneTimePreKeys.get(pId);
		if (record != null) {
			return record;
		} else {
			Logger.debug("loadPreKey(" + pId + ") could not find the requested key.");
			throw new InvalidKeyIdException("There is no corresponding key record in the store.");
		}
	}

	@Override
	synchronized public void storePreKey(int preKeyId, PreKeyRecord pPreKeyRecord) {
		oneTimePreKeys.add(preKeyId, pPreKeyRecord);
	}

	@Override
	synchronized public boolean containsPreKey(int preKeyId) {
		return oneTimePreKeys.get(preKeyId) != null;
	}

	@Override
	synchronized public void removePreKey(int preKeyId) {
		oneTimePreKeys.remove(preKeyId);
	}

	public NorvosPreKeyStore(byte[] serialized) throws IOException {
		PreKeyStoreStructure struct = PreKeyStoreStructure.parseFrom(serialized);
		oneTimePreKeys = new CircularBuffer<PreKeyRecord>(Medium.MAX_VALUE);

		for (PreKeyStructure protoMessage : struct.getOneTimePreKeyList()) {
			PreKeyRecord key = new PreKeyRecord(protoMessage.getPreKeyRecord().toByteArray());
			oneTimePreKeys.add(protoMessage.getKeyId(), key);
		}
	}

	synchronized public byte[] serialize() {
		Builder builder = PreKeyStoreStructure.newBuilder();
		for (Entry<Integer, PreKeyRecord> entry : oneTimePreKeys.getAll()) {
			PreKeyStructure struct =
					PreKeyStructure.newBuilder().setKeyId(entry.getKey())
							.setPreKeyRecord(ByteString.copyFrom(entry.getValue().serialize())).build();
			builder.addOneTimePreKey(struct);
		}

		return builder.build().toByteArray();
	}
}
