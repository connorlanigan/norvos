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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import de.norvos.i18n.Translations;
import de.norvos.log.Errors;
import de.norvos.log.Logger;

import org.whispersystems.libaxolotl.AxolotlAddress;
import org.whispersystems.libaxolotl.state.SessionRecord;
import org.whispersystems.libaxolotl.state.SessionStore;

import com.google.protobuf.ByteString;

import de.norvos.NorvosStorageProtos.SessionStoreStructure;
import de.norvos.NorvosStorageProtos.SessionStoreStructure.Builder;
import de.norvos.NorvosStorageProtos.SessionStoreStructure.SessionStructure;

public class NorvosSessionStore implements SessionStore {

	private HashMap<AxolotlAddress, SessionRecord> sessions;

	public NorvosSessionStore() {
		sessions = new HashMap<>();
	}

	@Override
	synchronized public SessionRecord loadSession(AxolotlAddress address) throws NullPointerException{
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
				Logger.debug("The requested ession copy could not be created. Reason: "+e.getMessage());
				Errors.critical(Translations.format("errors", "sessionCopyCouldNotBeCreated"));
				return new SessionRecord();
			}
		} else {
			return new SessionRecord();
		}
	}

	@Override
	synchronized public List<Integer> getSubDeviceSessions(String pName) {
		List<Integer> result = new ArrayList<Integer>();
		for (AxolotlAddress address : sessions.keySet()) {
			if (address.getName() == pName) {
				result.add(address.getDeviceId());
			}
		}
		return result;
	}

	@Override
	synchronized public void storeSession(AxolotlAddress pAddress, SessionRecord pSessionRecord) {
		sessions.put(pAddress, pSessionRecord);
	}

	@Override
	synchronized public boolean containsSession(AxolotlAddress address) {
		return sessions.containsKey(address);
	}

	@Override
	synchronized public void deleteSession(AxolotlAddress address) {
		sessions.remove(address);
	}

	@Override
	synchronized public void deleteAllSessions(String pName) {
		for (AxolotlAddress address : sessions.keySet()) {
			if (address.getName() == pName) {
				sessions.remove(address);
			}
		}
	}

	public NorvosSessionStore(byte[] serialized) throws IOException {
		SessionStoreStructure struct = SessionStoreStructure.parseFrom(serialized);
		sessions = new HashMap<>();

		for (SessionStructure protoMessage : struct.getSessionList()) {
			SessionRecord record = new SessionRecord(protoMessage.getSessionRecord().toByteArray());
			AxolotlAddress address =
					new AxolotlAddress(protoMessage.getAxolotlAddressName(), protoMessage.getAxolotlAddressId());
			sessions.put(address, record);
		}

	}

	synchronized public byte[] serialize() {
		Builder builder = SessionStoreStructure.newBuilder();
		for (Entry<AxolotlAddress, SessionRecord> entry : sessions.entrySet()) {
			SessionStructure struct =
					SessionStructure.newBuilder().setSessionRecord(ByteString.copyFrom(entry.getValue().serialize()))
							.setAxolotlAddressName(entry.getKey().getName())
							.setAxolotlAddressId(entry.getKey().getDeviceId()).build();
			builder.addSession(struct);
		}

		return builder.build().toByteArray();
	}
}
