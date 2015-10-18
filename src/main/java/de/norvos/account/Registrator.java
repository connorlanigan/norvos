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
import de.norvos.utils.Constants;
import de.norvos.utils.RandomUtils;

/**
 * Provides methods to register a client with the server.
 *
 * @author Connor Lanigan
 */
public class Registrator {
	private static boolean initialized = false;
	final static private int initialSignedKeyID = 5;
	private static List<PreKeyRecord> oneTimePreKeys;
	final static private int passwordLength = 40;

	final static boolean REDPHONE_UNSUPPORTED = false;
	private static boolean requested = false;

	final static public String WHISPERSYSTEMS_REGISTRATION_ID = "312334754206";

	/**
	 * Initialize the Registrator. This will generate new keys, and a new
	 * installation ID.<br>
	 * <strong>Warning: All existing user data is possibly destroyed after
	 * calling this method.</strong>
	 */
	public static void initialize() {
		IdentityKeyStore.getInstance().initialize();
		oneTimePreKeys = PreKeyStore.getInstance().initialize();

		final IdentityKeyPair identityKeyPair = IdentityKeyStore.getInstance().getIdentityKeyPair();
		SignedPreKeyStore.getInstance().initialize(identityKeyPair, initialSignedKeyID);

		SettingsService.setPassword(RandomUtils.randomAlphanumerical(passwordLength));
		SettingsService.setSignalingKey(RandomUtils.randomAlphanumerical(52));
		SettingsService.setInstallID(RandomUtils.generateInstallId());

		initialized = true;
	}

	/**
	 * Requests a verification SMS code for this client from the server. The
	 * Registrator needs to be {@link Registrator#initialize() initialized}
	 * first.
	 *
	 * @throws IOException
	 *             if an error occurs during registering
	 * @throws IllegalStateException
	 *             if this Registrator has not been initialized
	 */
	public static void requestCode() throws IOException {
		if (!initialized) {
			throw new IllegalStateException("Registrator must be initialized by calling initialize() first.");
		}
		final String url = SettingsService.getURL();
		final String username = SettingsService.getUsername();
		final TrustStore trustStore = TrustStore.getInstance();
		final String password = SettingsService.getPassword();

		final TextSecureAccountManager accountManager = new TextSecureAccountManager(url, trustStore, username,
				password, Constants.USER_AGENT);

		try {
			accountManager.requestSmsVerificationCode();
		} catch (final Exception e) {
			throw new IOException(e);
		}

		requested = true;
	}

	/**
	 * Verifies the received SMS code.
	 *
	 * @param verificationCode
	 * @throws IOException
	 */
	public static void verify(final String verificationCode) throws IOException {
		if (!initialized) {
			throw new IllegalStateException("Registrator must be initialized by calling initialize() first.");
		}
		if (!requested) {
			throw new IllegalStateException("Registrator must request a code before verifying.");
		}

		final String url = SettingsService.getURL();
		final String username = SettingsService.getUsername();
		final TrustStore trustStore = TrustStore.getInstance();
		final String password = SettingsService.getPassword();

		final String signalingKey = SettingsService.getSignalingKey();
		final Integer installID = SettingsService.getInstallID();

		final TextSecureAccountManager accountManager = new TextSecureAccountManager(url, trustStore, username,
				password, Constants.USER_AGENT);

		accountManager.verifyAccountWithCode(verificationCode, signalingKey, installID, REDPHONE_UNSUPPORTED);

		accountManager.setGcmId(Optional.of("Norvos does not support GCM."));

		final IdentityKey identityKey = IdentityKeyStore.getInstance().getIdentityKeyPair().getPublicKey();
		final PreKeyRecord lastResortKey = PreKeyStore.getInstance().getLastResortKey();
		final SignedPreKeyRecord signedPreKey = SignedPreKeyStore.getInstance().loadSignedPreKeys().get(0);

		accountManager.setPreKeys(identityKey, lastResortKey, signedPreKey, oneTimePreKeys);

		SettingsService.setSetupFinished(true);
	}
}
