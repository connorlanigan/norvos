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
package de.norvos.account;

import java.io.IOException;
import java.util.List;

import org.whispersystems.libaxolotl.IdentityKey;
import org.whispersystems.libaxolotl.IdentityKeyPair;
import org.whispersystems.libaxolotl.state.PreKeyRecord;
import org.whispersystems.libaxolotl.state.SignedPreKeyRecord;
import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.TextSecureAccountManager;

import de.norvos.axolotl.TrustStore;
import de.norvos.axolotl.stores.IdentityKeyStore;
import de.norvos.axolotl.stores.PreKeyStore;
import de.norvos.axolotl.stores.SignedPreKeyStore;
import de.norvos.utils.RandomUtils;

public class Registrator {
	final static private int initialSignedKeyID = 5;
	final static private int passwordLength = 40;
	final static boolean SMS_UNSUPPORTED = false;
	final static public String WHISPERSYSTEMS_REGISTRATION_ID = "312334754206";

	private boolean initialized = false;

	private List<PreKeyRecord> oneTimePreKeys;

	public void initialize() {
		final IdentityKeyPair identityKeyPair = IdentityKeyStore.getInstance().initialize();
		oneTimePreKeys = PreKeyStore.getInstance().initialize();
		SignedPreKeyStore.getInstance().initialize(identityKeyPair, initialSignedKeyID);

		AccountDataStore.storeStringValue("password", RandomUtils.randomAlphanumerical(passwordLength));
		AccountDataStore.storeStringValue("signalingKey", RandomUtils.randomAlphanumerical(52));
		AccountDataStore.storeStringValue("installID", String.valueOf(RandomUtils.generateInstallId()));

		initialized = true;
	}

	/**
	 * Registers this client with its server. The input of the verification code
	 * is handled in the RegistrationHandler.
	 *
	 * @param account
	 *            the account that should get registered
	 * @param handler
	 *            the verification code input handler
	 * @return <code>true</code> if and only if the registration was successful
	 * @throws IOException
	 *             if an error occurs during registering
	 * @throws IllegalStateException
	 *             if this Registrator has not been initialized
	 */
	public void register(final RegistrationCodeHandler handler) throws IOException {
		if (!initialized) {
			throw new IllegalStateException("Registrator must be initialized by calling initialize() first.");
		}
		final String url = AccountDataStore.getStringValue("url");
		final String username = AccountDataStore.getStringValue("username");
		final TrustStore trustStore = TrustStore.getInstance();
		final String password = AccountDataStore.getStringValue("password");

		final String signalingKey = AccountDataStore.getStringValue("signalingKey");
		final Integer installID = Integer.valueOf(AccountDataStore.getStringValue("installID"));

		final TextSecureAccountManager accountManager =
				new TextSecureAccountManager(url, trustStore, username, password);

		accountManager.requestSmsVerificationCode();

		accountManager.verifyAccount(handler.getCode(), signalingKey, SMS_UNSUPPORTED, installID);

		accountManager.setGcmId(Optional.of("Not using GCM."));

		final IdentityKey identityKey = IdentityKeyStore.getInstance().getIdentityKeyPair().getPublicKey();
		final PreKeyRecord lastResortKey = PreKeyStore.getInstance().getLastResortKey();
		final SignedPreKeyRecord signedPreKey = SignedPreKeyStore.getInstance().loadSignedPreKeys().get(0);

		accountManager.setPreKeys(identityKey, lastResortKey, signedPreKey, oneTimePreKeys);

		AccountDataStore.storeStringValue("setupFinished", "true");
	}
}
