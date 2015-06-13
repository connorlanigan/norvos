package de.norvos.store;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.whispersystems.libaxolotl.IdentityKey;
import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.state.IdentityKeyStore;
import org.whispersystems.libaxolotl.util.KeyHelper;

import com.google.protobuf.ByteString;

import de.norvos.NorvosStorageProtos.IdentityKeyStoreStructure;
import de.norvos.NorvosStorageProtos.IdentityKeyStoreStructure.Builder;
import de.norvos.NorvosStorageProtos.IdentityKeyStoreStructure.IdentityStructure;

public class NorvosIdentityKeyStore implements IdentityKeyStore {

	private IdentityKeyPair keyPair;
	private int registrationId;
	private Map<String, IdentityKey> storedIdentities;

	
	public NorvosIdentityKeyStore(){
		keyPair = KeyHelper.generateIdentityKeyPair();
		registrationId = KeyHelper.generateRegistrationId(true);
		storedIdentities = new HashMap<>();
	}

	@Override
	public IdentityKeyPair getIdentityKeyPair() {
		return keyPair;
	}

	@Override
	public int getLocalRegistrationId() {
		return registrationId;
	}

	@Override
	synchronized public void saveIdentity(String name, IdentityKey identityKey) {
		if(identityKey == null || name == null){
			throw new NullPointerException();
		}
		storedIdentities.put(name, identityKey);
	}

	@Override
	synchronized public boolean isTrustedIdentity(String name, IdentityKey identityKey) {
		if (storedIdentities.containsKey(name) && !identityKey.equals(storedIdentities.get(name))) {
			return false;
		}
		return true;
	}

	public NorvosIdentityKeyStore(byte[] serialized) throws IOException{
		try{
			IdentityKeyStoreStructure struct = IdentityKeyStoreStructure.parseFrom(serialized);
			Map<String, IdentityKey> identities = new HashMap<>();


			for (IdentityStructure protoMessage : struct.getStoredIdentityList()) {
				String name = protoMessage.getName();
				IdentityKey key = new IdentityKey(protoMessage.getIdentitykey().toByteArray(), 0);
				identities.put(name, key);
			}

			this.keyPair = new IdentityKeyPair(struct.getIdentityKeyPair().toByteArray());
			this.registrationId = struct.getRegistrationId();
			this.storedIdentities = identities;
		}catch(InvalidKeyException e){
			throw new IOException(e);
		}
	}
	
	synchronized public byte[] serialize(){		
		Builder builder = IdentityKeyStoreStructure.newBuilder()
				 .setIdentityKeyPair(ByteString.copyFrom(keyPair.serialize()))
				 .setRegistrationId(registrationId);
		
		for(Entry<String, IdentityKey> entry : storedIdentities.entrySet()){
			IdentityStructure struct = IdentityStructure.newBuilder().setName(entry.getKey()).setIdentitykey(ByteString.copyFrom(entry.getValue().serialize())).build();
			builder.addStoredIdentity(struct);
		}
		
		return builder.build().toByteArray();
	}
}
