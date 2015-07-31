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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.whispersystems.libaxolotl.AxolotlAddress;
import org.whispersystems.libaxolotl.IdentityKey;
import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.state.PreKeyRecord;
import org.whispersystems.libaxolotl.state.SessionRecord;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;
import org.whispersystems.libaxolotl.util.KeyHelper;

public class AxolotlStoreTest {

	int ANY_NUMBER;
	NorvosAxolotlStore secondStore;

	NorvosAxolotlStore store;

	@Test
	public void contains_storePreKey() {
		final PreKeyRecord preKey = KeyHelper.generatePreKeys(0, ANY_NUMBER).get(0);
		store.storePreKey(preKey.getId(), preKey);
		assertTrue(store.containsPreKey(preKey.getId()));
		assertFalse(store.containsPreKey(preKey.getId() + 1));
	}

	@Test
	public void getIdentityKeyPair() {
		assertEquals(store.getIdentityKeyPair(), store.getIdentityKeyPair());
		assertNotEquals(store.getIdentityKeyPair(), secondStore.getIdentityKeyPair());
	}

	@Test
	public void getLocalRegistrationId() {
		assertEquals(store.getLocalRegistrationId(), store.getLocalRegistrationId());
		assertNotEquals(store.getLocalRegistrationId(), secondStore.getLocalRegistrationId());
	}

	@Test
	public void isTrustedIdentity() {
		final IdentityKey key = KeyHelper.generateIdentityKeyPair().getPublicKey();
		final IdentityKey wrongKey = KeyHelper.generateIdentityKeyPair().getPublicKey();
		assertNotEquals(key, wrongKey);

		// Case: Correct Name and Key
		store.saveIdentity("TestName", key);
		assertTrue(store.isTrustedIdentity("TestName", key));

		// Case: Unknown Key (important: Trust on first use!)
		assertTrue(store.isTrustedIdentity("UnseenName", key));

		// Case: Wrong Key
		assertFalse(store.isTrustedIdentity("TestName", wrongKey));
	}

	@Test
	public void loadPreKey() throws InvalidKeyIdException {
		final PreKeyRecord preKey = KeyHelper.generatePreKeys(0, ANY_NUMBER).get(0);
		store.storePreKey(preKey.getId(), preKey);
		final PreKeyRecord newKey = store.loadPreKey(preKey.getId());
		assertEquals(preKey, newKey);
	}

	@Test(expected = InvalidKeyIdException.class)
	public void loadPreKeyInvalid() throws InvalidKeyIdException {
		final PreKeyRecord preKey = KeyHelper.generatePreKeys(0, ANY_NUMBER).get(0);
		store.storePreKey(42, preKey);
		store.loadPreKey(preKey.getId() + 1);
	}

	@Test
	public void removePreKey() {
		final PreKeyRecord preKey = KeyHelper.generatePreKeys(0, ANY_NUMBER).get(0);
		store.storePreKey(42, preKey);
		store.removePreKey(preKey.getId());
		assertFalse(store.containsPreKey(preKey.getId()));
	}

	@Test
	public void saveIdentity() {
		final IdentityKey key = KeyHelper.generateIdentityKeyPair().getPublicKey();
		store.saveIdentity("TestName", key);
		assertTrue(store.isTrustedIdentity("TestName", key));
	}

	@Test(expected = NullPointerException.class)
	public void saveIdentityBothNull() {
		store.saveIdentity(null, null);
	}

	@Test(expected = NullPointerException.class)
	public void saveIdentityIdentityNull() {
		store.saveIdentity("TestName", null);
	}

	@Test(expected = NullPointerException.class)
	public void saveIdentityNameNull() {
		store.saveIdentity(null, KeyHelper.generateIdentityKeyPair().getPublicKey());
	}

	@Test
	public void sessions() {
		final AxolotlAddress address = new AxolotlAddress("TestName", ANY_NUMBER);
		final AxolotlAddress wrongAddress = new AxolotlAddress("WrongName", ANY_NUMBER);
		final SessionRecord session = new SessionRecord();
		final SessionRecord wrongSession = new SessionRecord();
		store.storeSession(address, session);

		assertTrue(store.containsSession(address));
		assertFalse(store.containsSession(wrongAddress));

		assertNotEquals(session, store.loadSession(address)); // returns a copy
		assertNotEquals(wrongSession, store.loadSession(address));

		store.deleteSession(address);
		assertFalse(store.containsSession(address));

		store.storeSession(address, new SessionRecord());
		store.deleteAllSessions(address.getName());
		assertFalse(store.containsSession(address));
	}

	@Test
	public void sessionsLoadInvalid() {
		final AxolotlAddress address = new AxolotlAddress("TestName", ANY_NUMBER);
		assertFalse(store.containsSession(address));
		store.loadSession(address);
	}

	@Before
	public void setUp() throws Exception {
		store = new NorvosAxolotlStore();
		Thread.sleep(10);
		secondStore = new NorvosAxolotlStore();
		final Random r = new Random();
		ANY_NUMBER = r.nextInt(10) + 1;
	}

	@Test
	public void signedPreKey() throws InvalidKeyException, InvalidKeyIdException {
		final Random r = new Random();
		final IdentityKeyPair identityKey = KeyHelper.generateIdentityKeyPair();
		final SignedPreKeyRecord key = KeyHelper.generateSignedPreKey(identityKey, r.nextInt(Integer.MAX_VALUE));

		store.storeSignedPreKey(key.getId(), key);
		assertTrue(store.containsSignedPreKey(key.getId()));
		assertFalse(store.containsSignedPreKey(key.getId() + 1));

		assertEquals(key, store.loadSignedPreKey(key.getId()));
		store.removeSignedPreKey(key.getId());

		assertFalse(store.containsSignedPreKey(key.getId()));
	}
}
