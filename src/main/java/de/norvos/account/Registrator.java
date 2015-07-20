package de.norvos.account;

import java.io.IOException;
import java.security.SecureRandom;

import org.whispersystems.textsecure.api.TextSecureAccountManager;

import de.norvos.axolotl.NorvosAxolotlStore;
import de.norvos.axolotl.NorvosTrustStore;
import de.norvos.gui.RegistrationHandler;
import de.norvos.log.Errors;
import de.norvos.i18n.Translations;

public class Registrator {

	final static SecureRandom random = new SecureRandom();
	final static boolean SMS_UNSUPPORTED = false;

	/**
	 * Registers a ServerAccount with its server. The input of the verification code is handled in the RegistrationHandler.
	 * @param account the account that should get registered
	 * @param handler the verification code input handler
	 * @return <code>true</code> if and only if the registration was successful
	 */
	public static boolean register(ServerAccount account, RegistrationHandler handler) {
		try {
			TextSecureAccountManager accountManager =
					new TextSecureAccountManager(account.getURL(), NorvosTrustStore.get(),
							account.getUsername(), account.getPassword());

			accountManager.requestSmsVerificationCode();

			String receivedVerificationCode = handler.getCode();

			accountManager.verifyAccount(receivedVerificationCode, generateRandomSignalingKey(), SMS_UNSUPPORTED,
					generateRandomInstallId());

			NorvosAxolotlStore axolotlStore = Settings.getCurrent().getAxolotlStore();
			accountManager.setPreKeys(axolotlStore.getIdentityKeyPair().getPublicKey(),
					axolotlStore.getLastResortKey(), axolotlStore.getSignedPreKey(), axolotlStore.getOneTimePreKeys());

		} catch (IOException e) {
			handler.registrationFailed(e.getMessage());
			Errors.warning(Translations.format("errors", "registrationFailed", e.getMessage()));
			return false;
		}
		return true;

	}

	/**
	 * Generates a random array of 52 bytes. <br>
	 * <br>
	 * Taken from
	 * {@link org.whispersystems.textsecure.api.TextSecureAccountManager#verifyAccount
	 * TextSecureAccountManager.verifyAccount()}, which uses this value:<br>
	 * "52 random bytes. A 32 byte AES key and a 20 byte Hmac256 key, concatenated."
	 * 
	 * @return a random 52-byte array.
	 */
	private static String generateRandomSignalingKey() {
		int size = 52;
		byte[] array = new byte[size];

		random.nextBytes(array);

		return new String(array);
	}

	/**
	 * Generates a random number with a maximum length of 14bit. <br>
	 * <br>
	 * Taken from
	 * {@link org.whispersystems.textsecure.api.TextSecureAccountManager#verifyAccount
	 * TextSecureAccountManager.verifyAccount()}, which uses this value:<br>
	 * "A random 14-bit number that identifies this TextSecure install. This
	 * value should remain consistent across registrations for the same install,
	 * but probabilistically differ across registrations for separate installs."
	 * 
	 * @return a random number with a maximum of 14 bit;
	 */
	private static int generateRandomInstallId() {
		int size = (int) Math.pow(2, 14 + 1);

		return random.nextInt(size);
	}
}
