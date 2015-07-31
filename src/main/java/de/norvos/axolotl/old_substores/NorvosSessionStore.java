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
package de.norvos.axolotl.old_substores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.whispersystems.libaxolotl.AxolotlAddress;
import org.whispersystems.libaxolotl.state.SessionRecord;
import org.whispersystems.libaxolotl.state.SessionStore;

import com.google.protobuf.ByteString;

import de.norvos.NorvosStorageProtos.SessionStoreStructure;
import de.norvos.NorvosStorageProtos.SessionStoreStructure.Builder;
import de.norvos.NorvosStorageProtos.SessionStoreStructure.SessionStructure;
import de.norvos.i18n.Translations;
import de.norvos.log.Errors;

public class NorvosSessionStore implements SessionStore {

	private final HashMap<AxolotlAddress, SessionRecord> sessions;

	public NorvosSessionStore() {
		sessions = new HashMap<>();
	}

	public NorvosSessionStore(final byte[] serialized) throws IOException {
		final SessionStoreStructure struct = SessionStoreStructure.parseFrom(serialized);
		sessions = new HashMap<>();

		for (final SessionStructure protoMessage : struct.getSessionList()) {
			final SessionRecord record = new SessionRecord(protoMessage.getSessionRecord().toByteArray());
			final AxolotlAddress address =
					new AxolotlAddress(protoMessage.getAxolotlAddressName(), protoMessage.getAxolotlAddressId());
			sessions.put(address, record);
		}

	}

	@Override
	synchronized public boolean containsSession(final AxolotlAddress address) {
		return sessions.containsKey(address);
	}

	@Override
	synchronized public void deleteAllSessions(final String pName) {
		for (final AxolotlAddress address : sessions.keySet()) {
			if (address.getName() == pName) {
				sessions.remove(address);
			}
		}
	}

	@Override
	synchronized public void deleteSession(final AxolotlAddress address) {
		sessions.remove(address);
	}

	@Override
	synchronized public List<Integer> getSubDeviceSessions(final String pName) {
		final List<Integer> result = new ArrayList<Integer>();
		for (final AxolotlAddress address : sessions.keySet()) {
			if (address.getName() == pName) {
				result.add(address.getDeviceId());
			}
		}
		return result;
	}

	@Override
	synchronized public SessionRecord loadSession(final AxolotlAddress address) throws NullPointerException {
		if (sessions.containsKey(address)) {
			final SessionRecord original = sessions.get(address);
			if (original == null) {
				Logger.debug("loadSession(" + address.toString() + ") found a null-mapping.");
				throw new NullPointerException("Requested session could not be loaded from the store.");
			}
			try {
				final SessionRecord copy = new SessionRecord(original.serialize());
				return copy;
			} catch (final IOException e) {
				Logger.debug("The requested ession copy could not be created. Reason: " + e.getMessage());
				Errors.critical(Translations.format("errors", "sessionCopyCouldNotBeCreated"));
				return new SessionRecord();
			}
		} else {
			return new SessionRecord();
		}
	}

	synchronized public byte[] serialize() {
		final Builder builder = SessionStoreStructure.newBuilder();
		for (final Entry<AxolotlAddress, SessionRecord> entry : sessions.entrySet()) {
			final SessionStructure struct =
					SessionStructure.newBuilder().setSessionRecord(ByteString.copyFrom(entry.getValue().serialize()))
					.setAxolotlAddressName(entry.getKey().getName())
					.setAxolotlAddressId(entry.getKey().getDeviceId()).build();
			builder.addSession(struct);
		}

		return builder.build().toByteArray();
	}

	@Override
	synchronized public void storeSession(final AxolotlAddress pAddress, final SessionRecord pSessionRecord) {
		sessions.put(pAddress, pSessionRecord);
	}
}
