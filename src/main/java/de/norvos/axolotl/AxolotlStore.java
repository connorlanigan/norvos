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

import java.util.List;

import org.whispersystems.libaxolotl.AxolotlAddress;
import org.whispersystems.libaxolotl.IdentityKey;
import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.PreKeyRecord;
import org.whispersystems.libaxolotl.state.SessionRecord;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;

public class AxolotlStore implements org.whispersystems.libaxolotl.state.AxolotlStore {

	@Override
	public boolean containsPreKey(final int preKeyId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsSession(final AxolotlAddress address) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsSignedPreKey(final int signedPreKeyId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteAllSessions(final String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSession(final AxolotlAddress address) {
		// TODO Auto-generated method stub

	}

	@Override
	public IdentityKeyPair getIdentityKeyPair() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLocalRegistrationId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Integer> getSubDeviceSessions(final String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTrustedIdentity(final String name, final IdentityKey identityKey) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PreKeyRecord loadPreKey(final int preKeyId) throws InvalidKeyIdException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionRecord loadSession(final AxolotlAddress address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SignedPreKeyRecord loadSignedPreKey(final int signedPreKeyId) throws InvalidKeyIdException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<SignedPreKeyRecord> loadSignedPreKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removePreKey(final int preKeyId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeSignedPreKey(final int signedPreKeyId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveIdentity(final String name, final IdentityKey identityKey) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storePreKey(final int preKeyId, final PreKeyRecord record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeSession(final AxolotlAddress address, final SessionRecord record) {
		// TODO Auto-generated method stub

	}

	@Override
	public void storeSignedPreKey(final int signedPreKeyId, final SignedPreKeyRecord record) {
		// TODO Auto-generated method stub

	}

}
