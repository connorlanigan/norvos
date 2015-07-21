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
package de.norvos.axolotl.substores;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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

	public NorvosSignedPreKeyStore(IdentityKeyPair identityKey) throws InvalidKeyException {
		initialSignedPreKeyRecord = KeyHelper.generateSignedPreKey(identityKey, 5);
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
		return builder.setInitialSignedPreKey(ByteString.copyFrom(initialSignedPreKeyRecord.serialize())).build().toByteArray();
	}

}
