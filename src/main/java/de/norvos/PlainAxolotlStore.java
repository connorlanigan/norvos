package de.norvos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import com.google.protobuf.ByteString;

import de.norvos.PlainAxolotlStoreProtos.PlainAxolotlStoreStructure;
import de.norvos.PlainAxolotlStoreProtos.PlainAxolotlStoreStructure.Identity;
import de.norvos.PlainAxolotlStoreProtos.PlainAxolotlStoreStructure.Session;
import de.norvos.PlainAxolotlStoreProtos.PlainAxolotlStoreStructure.axolotlAddress;

public class PlainAxolotlStore implements AxolotlStore {

	private IdentityKeyPair identityKey;
	private List<PreKeyRecord> oneTimePreKeys = new ArrayList<>(); // LinkedList
	private PreKeyRecord lastResortKey;
	private SignedPreKeyRecord signedPreKeyRecord;

	private List<SignedPreKeyRecord> signedPreKeys = new ArrayList<>();

	private HashMap<String, IdentityKey> remoteIdentities = new HashMap<>();
	private HashMap<AxolotlAddress, SessionRecord> sessions = new HashMap<>();

	private int localRegistrationId;

	private static String axolotlStoreFileName = ".config/axolotlStore.ser";

	/**
	 * Generates new keys for a new store and uses a random localRegistrationId
	 * between 1 and 16380.
	 */
	public PlainAxolotlStore() {
		Random r = new Random();
		identityKey = KeyHelper.generateIdentityKeyPair();
		oneTimePreKeys = KeyHelper.generatePreKeys(2, 100);
		lastResortKey = KeyHelper.generateLastResortPreKey();
		//TODO ask moxie about key ids
		try {
			signedPreKeyRecord = KeyHelper.generateSignedPreKey(identityKey, r.nextInt(Integer.MAX_VALUE));
		} catch (InvalidKeyException e) {
			Main.handleCriticalError("Could not create the AxolotlStore.");
			System.exit(1);
		}

		int minimumId = 1;
		int maximumId = 16380;

		localRegistrationId = (new Random()).nextInt(maximumId - minimumId) + minimumId;
	}

	PlainAxolotlStore(IdentityKeyPair identityKey, List<PreKeyRecord> oneTimePreKeys,
			PreKeyRecord lastResortKey, SignedPreKeyRecord signedPreKeyRecord, List<SignedPreKeyRecord> signedPreKeys,
			HashMap<String, IdentityKey> remoteIdentities, HashMap<AxolotlAddress, SessionRecord> sessions,
			int localRegistrationId) {
		this.identityKey = identityKey;
		this.oneTimePreKeys = oneTimePreKeys;
		this.lastResortKey = lastResortKey;
		this.signedPreKeyRecord = signedPreKeyRecord;
		this.signedPreKeys = signedPreKeys;
		this.remoteIdentities = remoteIdentities;
		this.sessions = sessions;
		this.localRegistrationId = localRegistrationId;
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
	public void saveIdentity(String pName, IdentityKey pIdentityKey) throws NullPointerException{
		if(pName == null || pIdentityKey == null){
			Logger.debug("Error while trying to save an invalid identity. Name: ["+pName+"] IdentityKey: ["+pIdentityKey+"]");
			throw new NullPointerException("Invalid identity to save.");
		}
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
	public void storePreKey(int pId, PreKeyRecord pPreKeyRecord) {
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
			if (original == null) {
				Logger.debug("loadSession(" + address.toString() + ") found a null-mapping.");
				throw new NullPointerException("Requested session could not be loaded from the store.");
			}
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
		throw new InvalidKeyIdException("There is no corresponding key record in the store.");
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		return signedPreKeys;
	}

	@Override
	public void removeSignedPreKey(int pId) {
		SignedPreKeyRecord foundRecord = null;
		for (SignedPreKeyRecord record : signedPreKeys) {
			if (record.getId() == pId) {
				foundRecord = record;
			}
		}
		signedPreKeys.remove(foundRecord);
	}

	@Override
	public void storeSignedPreKey(int pId, SignedPreKeyRecord pSignedRecord) {
		signedPreKeys.add(pSignedRecord);
	}

	public void save() {

		PlainAxolotlStoreStructure.Builder builder =
				PlainAxolotlStoreStructure.newBuilder().setIdentityKey(ByteString.copyFrom(identityKey.serialize()))
						.setLastResortKey(ByteString.copyFrom(lastResortKey.serialize()))
						.setLocalRegistrationId(localRegistrationId)
						.setSignedPreKeyRecord(ByteString.copyFrom(signedPreKeyRecord.serialize()));
		for (PreKeyRecord record : oneTimePreKeys) {
			builder.addOneTimePreKeys(ByteString.copyFrom(record.serialize()));
		}
		for (SignedPreKeyRecord record : signedPreKeys) {
			builder.addSignedPreKeys(ByteString.copyFrom(record.serialize()));
		}
		for (HashMap.Entry<String, IdentityKey> entry : remoteIdentities.entrySet()) {
			Identity protoEntry =
					Identity.newBuilder().setName(entry.getKey())
							.setIdentityKey(ByteString.copyFrom(entry.getValue().serialize())).build();
			builder.addRemoteIdentities(protoEntry);
		}
		for (HashMap.Entry<AxolotlAddress, SessionRecord> entry : sessions.entrySet()) {
			axolotlAddress address =
					axolotlAddress.newBuilder().setName(entry.getKey().getName())
							.setDeviceId(entry.getKey().getDeviceId()).build();

			Session protoEntry =
					Session.newBuilder().setAddress(address)
							.setSessionRecord(ByteString.copyFrom(entry.getValue().serialize())).build();

			builder.addSessions(protoEntry);
		}
		try {
			builder.build().writeTo(getOutputStream());
		} catch (IOException e) {
			Logger.critical(ErrorMessages.axolotlCouldNotBeSaved);
		}

	}

	private static OutputStream getOutputStream() throws FileNotFoundException, IOException {
		File outputFile = getFile();
		return new FileOutputStream(outputFile);
	}

	private static InputStream getInputStream() throws FileNotFoundException {
		File inputFile = getFile();
		return new FileInputStream(inputFile);
	}

	/**
	 * Returns the File for input/output.
	 * 
	 * @return the File for the PlainAxolotlStore
	 */
	private static File getFile() {
		Path homePath = Paths.get(System.getProperty("user.home"));
		homePath = homePath.resolve(axolotlStoreFileName);
		return homePath.toFile();
	}

	public static PlainAxolotlStore load() throws FileNotFoundException, IOException, InvalidKeyException {
		PlainAxolotlStoreStructure struct = PlainAxolotlStoreStructure.parseFrom(getInputStream());
		
		List<PreKeyRecord> oneTimePreKeys = new ArrayList<>();
		for (ByteString entry : struct.getOneTimePreKeysList()) {
			oneTimePreKeys.add(new PreKeyRecord(entry.toByteArray()));
		}

		List<SignedPreKeyRecord> signedPreKeys = new ArrayList<>();
		for (ByteString entry : struct.getSignedPreKeysList()) {
			signedPreKeys.add(new SignedPreKeyRecord(entry.toByteArray()));
		}

		HashMap<String, IdentityKey> remoteIdentities = new HashMap<>();
		for (Identity entry : struct.getRemoteIdentitiesList()) {
			try {
				remoteIdentities.put(entry.getName(), new IdentityKey(entry.getIdentityKey().toByteArray(), 0));
			} catch (InvalidKeyException theException) {
				Logger.critical("Error in parsing serialized IdentityKey.");
				throw theException;
			}
		}
		HashMap<AxolotlAddress, SessionRecord> sessions = new HashMap<>();
		for (Session entry : struct.getSessionsList()) {
			sessions.put(new AxolotlAddress(entry.getAddress().getName(), entry.getAddress().getDeviceId()),
					new SessionRecord(entry.getSessionRecord().toByteArray()));

		}
		
		
		/*
		 * PlainAxolotlStore(
		 * IdentityKeyPair identityKey,
		 *  List<PreKeyRecord> oneTimePreKeys, 
		 *  PreKeyRecord lastResortKey, 
		 *  SignedPreKeyRecord signedPreKeyRecord,
		 *  List<SignedPreKeyRecord> signedPreKeys,
		 *  
		 * HashMap<String, IdentityKey> remoteIdentities,
		 * HashMap<AxolotlAddress, SessionRecord> sessions,
		 *  int	localRegistrationId){
		 */
		
		PlainAxolotlStore newStore;
		try {
			 newStore = new PlainAxolotlStore(
					new IdentityKeyPair(struct.getIdentityKey().toByteArray()),
					oneTimePreKeys, 
					new PreKeyRecord(struct.getLastResortKey().toByteArray()),
					new SignedPreKeyRecord(struct.getSignedPreKeyRecord().toByteArray()),
					signedPreKeys,
					remoteIdentities,
					sessions,
					struct.getLocalRegistrationId());
		} catch (InvalidKeyException theException) {
			Logger.critical("Error in parsing serialized IdentityKeyPair.");
			throw theException;
		}

		return newStore;
	}
}
