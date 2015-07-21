package de.norvos.account;

import java.io.IOException;
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Enumeration;

import org.whispersystems.textsecure.api.TextSecureAccountManager;
import org.whispersystems.textsecure.api.push.exceptions.AuthorizationFailedException;

import de.norvos.axolotl.NorvosAxolotlStore;
import de.norvos.axolotl.NorvosTrustStore;
import de.norvos.gui.RegistrationHandler;

public class Registrator {

	final static SecureRandom random = new SecureRandom();
	final static boolean SMS_UNSUPPORTED = false;

	/**
	 * Registers a ServerAccount with its server. The input of the verification
	 * code is handled in the RegistrationHandler.
	 * 
	 * @param account
	 *            the account that should get registered
	 * @param handler
	 *            the verification code input handler
	 * @return <code>true</code> if and only if the registration was successful
	 * @throws IOException
	 */
	public static void register(ServerAccount account, RegistrationHandler handler) throws IOException,
			AuthorizationFailedException,
			Exception {
		TextSecureAccountManager accountManager =
				new TextSecureAccountManager(account.getURL(), NorvosTrustStore.get(), account.getUsername(),
						account.getPassword());

		accountManager.requestSmsVerificationCode();

		String receivedVerificationCode = handler.getCode();

		accountManager.verifyAccount(receivedVerificationCode, generateRandomSignalingKey(), SMS_UNSUPPORTED,
				generateRandomInstallId());

		NorvosAxolotlStore axolotlStore = Settings.getCurrent().getAxolotlStore();
		accountManager.setPreKeys(axolotlStore.getIdentityKeyPair().getPublicKey(), axolotlStore.getLastResortKey(),
				axolotlStore.getSignedPreKey(), axolotlStore.getOneTimePreKeys());

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
	public static String generateRandomSignalingKey() {
		int size = 52;
		byte[] array = new byte[size];

		random.nextBytes(array);

		return new String(array, StandardCharsets.US_ASCII);
	}

	/**
	 * Generates an install ID with a maximum of 14bit. The number is based on
	 * the SHA-1 hash of the first MAC adress in the system. If no such adress
	 * is found, a random number is chosen.<br><br>
	 * Taken from
	 * {@link org.whispersystems.textsecure.api.TextSecureAccountManager#verifyAccount
	 * TextSecureAccountManager.verifyAccount()}, which uses this value:<br>
	 * "A random 14-bit number that identifies this TextSecure install. This
	 * value should remain consistent across registrations for the same install,
	 * but probabilistically differ across registrations for separate installs."
	 * 
	 * @return a random number with a maximum of 14 bit;
	 * @throws UnknownHostException
	 * @throws SocketException
	 * @throws NoSuchAlgorithmException
	 */
	public static int generateRandomInstallId() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			byte[] hashedMac = digest.digest(getFirstMACAdress());
			return limitInt(new BigInteger(hashedMac).intValue(), 14);

		} catch (Exception e) {
			int size = (int) Math.pow(2, 14 + 1);
			return random.nextInt(size);
		}
	}

	/**
	 * Returns the first <code>bitlength</code> bits of the given int.
	 * @param value the int to shorten
	 * @param bitlength the amount of bits to return
	 * @return the <code>bitlength</code> most significant bits of the value
	 */
	private static int limitInt(int value, int bitlength) {
		return Integer.parseInt(Integer.toBinaryString(value).substring(0, bitlength), 2);
	}

	/**
	 * Returns the first MAC adress that is found in the system.
	 * @return MAC adress
	 * @throws SocketException if an I/O error occurs.
	 */
	private static byte[] getFirstMACAdress() throws SocketException {
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface network = networkInterfaces.nextElement();

			byte[] bmac = network.getHardwareAddress();
			if (bmac != null) {
				return bmac;
			}
		}
		throw new SocketException("No network interfaces with a MAC adress found.");
	}
}
