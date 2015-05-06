package de.norvos;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.whispersystems.libaxolotl.AxolotlAddress;
import org.whispersystems.libaxolotl.IdentityKey;
import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.AxolotlStore;
import org.whispersystems.libaxolotl.state.PreKeyRecord;
import org.whispersystems.libaxolotl.state.SessionRecord;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;
import org.whispersystems.libaxolotl.util.KeyHelper;

public class PlainAxolotlStore implements AxolotlStore {

	IdentityKeyPair identityKey;
	List<PreKeyRecord> oneTimePreKeys;
	PreKeyRecord lastResortKey;
	SignedPreKeyRecord signedPreKeyRecord;

	List<SignedPreKeyRecord> signedPreKeys;

	HashMap<String, IdentityKey> remoteIdentities;
	HashMap<AxolotlAddress, SessionRecord> sessions;

	int localRegistrationId;

	/**
	 * Generates new keys for a new store and uses a random localRegistrationId between 1 and 16380.
	 */	
	public PlainAxolotlStore() {
		identityKey = KeyHelper.generateIdentityKeyPair();
		oneTimePreKeys = KeyHelper.generatePreKeys(2, 100);
		lastResortKey = KeyHelper.generateLastResortPreKey();
		try {
			signedPreKeyRecord = KeyHelper.generateSignedPreKey(identityKey, 1);
		} catch (InvalidKeyException e) {
			Main.handleCriticalError("Could not create the AxolotlStore.");
			System.exit(1);
		}

		int minimumId = 1;
		int maximumId = 16380;

		localRegistrationId = (new Random()).nextInt(maximumId - minimumId) + minimumId;
	}

	@Override
	public IdentityKeyPair getIdentityKeyPair() {
		return identityKey;
	}

	@Override
	public int getLocalRegistrationId() {
		return localRegistrationId;
	}

	@Override
	public boolean isTrustedIdentity(String pName, IdentityKey pIdentityKey) {
		if (remoteIdentities.containsKey(pName)) {
			IdentityKey storedIdentityKey = remoteIdentities.get(pName);
			return storedIdentityKey.equals(pIdentityKey);
		} else {
			return true;
		}
	}

	@Override
	public void saveIdentity(String pName, IdentityKey pIdentityKey) {
		remoteIdentities.put(pName, pIdentityKey);
	}

	@Override
	public boolean containsPreKey(int pId) {
		for (PreKeyRecord record : oneTimePreKeys) {
			if (record.getId() == pId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void removePreKey(int pId) {
		for (PreKeyRecord record : oneTimePreKeys) {
			if (record.getId() == pId) {
				oneTimePreKeys.remove(record);
			}
		}
	}

	@Override
	public PreKeyRecord loadPreKey(int pId) throws InvalidKeyIdException {
		for (PreKeyRecord record : oneTimePreKeys) {
			if (record.getId() == pId) {
				return record;
			}
		}
		Logger.debug("loadPreKey(" + pId + ") could not find the requested key.");
		throw new InvalidKeyIdException("Connor: There is no corresponding key record in the store.");
	}

	@Override
	public void storePreKey(int UNUSED, PreKeyRecord pPreKeyRecord) {
		oneTimePreKeys.add(pPreKeyRecord);
	}

	@Override
	public boolean containsSession(AxolotlAddress address) {
		return sessions.containsKey(address);
	}

	@Override
	public void deleteAllSessions(String pName) {
		for (AxolotlAddress address : sessions.keySet()) {
			if (address.getName() == pName) {
				sessions.remove(address);
			}
		}
	}

	@Override
	public void deleteSession(AxolotlAddress address) {
		sessions.remove(address);
	}

	@Override
	public List<Integer> getSubDeviceSessions(String pName) {
		List<Integer> result = new ArrayList<Integer>();
		for (AxolotlAddress address : sessions.keySet()) {
			if (address.getName() == pName) {
				result.add(address.getDeviceId());
			}
		}
		return result;
	}

	@Override
	public SessionRecord loadSession(AxolotlAddress address) {
		if (sessions.containsKey(address)) {
			SessionRecord original = sessions.get(address);
			try {
				SessionRecord copy = new SessionRecord(original.serialize());
				return copy;
			} catch (IOException e) {
				Main.handleCriticalError("IOException while creating copy of session.");
				System.exit(1);
				return null;
			}
		} else {
			return new SessionRecord();
		}
	}

	@Override
	public void storeSession(AxolotlAddress pAddress, SessionRecord pSessionRecord) {
		sessions.put(pAddress, pSessionRecord);
	}

	@Override
	public boolean containsSignedPreKey(int pId) {
		for (SignedPreKeyRecord record : signedPreKeys) {
			if (record.getId() == pId) {
				return true;
			}
		}
		return false;
	}

	@Override
	public SignedPreKeyRecord loadSignedPreKey(int pId) throws InvalidKeyIdException {
		for (SignedPreKeyRecord record : signedPreKeys) {
			if (record.getId() == pId) {
				return record;
			}
		}
		Logger.debug("loadSignedPreKey(" + pId + ") could not find the requested key.");
		throw new InvalidKeyIdException("Connor: There is no corresponding key record in the store.");
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		return signedPreKeys;
	}

	@Override
	public void removeSignedPreKey(int pId) {
		for (SignedPreKeyRecord record : signedPreKeys) {
			if (record.getId() == pId) {
				signedPreKeys.remove(record);
			}
		}
	}

	@Override
	public void storeSignedPreKey(int pId, SignedPreKeyRecord pSignedRecord) {
		signedPreKeys.add(pSignedRecord);
	}

	/**
	 * Creates a PlainAxolotlStore from a stream.
	 * 
	 * @param stream
	 *            the stream supplying the serialized PlainAxolotlStore data.
	 * @return a saved instance of PlainAxolotlStore
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static PlainAxolotlStore loadFromStream(InputStream stream) throws IOException, ClassNotFoundException {
		ObjectInputStream os = new ObjectInputStream(stream);
		PlainAxolotlStore store = (PlainAxolotlStore) os.readObject();
		return store;
	}

}
