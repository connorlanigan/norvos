package de.norvos;

import java.io.IOException;

import de.norvos.log.Errors;
import de.norvos.log.Errors.Message;
import de.norvos.store.DurableStoreManager;
import de.norvos.store.axolotl.NorvosAxolotlStore;

public class Main {

	public static NorvosAxolotlStore store;

	public static void main(String[] args) {
		if (!DurableStoreManager.availableOnDisk()) {
			store = new NorvosAxolotlStore();
			registerThisClient();
		} else {
			try {
				store = DurableStoreManager.loadFromDisk();
			} catch (IOException e) {
				Errors.handleCritical(Message.axolotlCouldNotBeLoaded);
			}
		}
	}

	private static void registerThisClient() {

		/*
		final String URL = "https://my.textsecure.server.com";
		final TrustStore TRUST_STORE = new NorvosTrustStore();
		final String USERNAME = "+14151231234";
		final String PASSWORD = generateRandomPassword();

		TextSecureAccountManager accountManager = new TextSecureAccountManager(URL, TRUST_STORE, USERNAME, PASSWORD);

		accountManager.requestSmsVerificationCode();
		accountManager.verifyAccount(receivedSmsVerificationCode, generateRandomSignalingKey(), false,
				generateRandomInstallId());
		accountManager.setGcmId(Optional.of(GoogleCloudMessaging.getInstance(this).register(REGISTRATION_ID)));
		accountManager.setPreKeys(identityKey.getPublic(), lastResortKey, signedPreKey, oneTimePreKeys);
		
		*/
	}
}
