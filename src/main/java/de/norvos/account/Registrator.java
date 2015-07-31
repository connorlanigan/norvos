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
import java.math.BigInteger;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Enumeration;

import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.TextSecureAccountManager;
import org.whispersystems.textsecure.api.push.exceptions.AuthorizationFailedException;

import de.norvos.axolotl.NorvosAxolotlStore;
import de.norvos.axolotl.NorvosTrustStore;

public class Registrator {

	final static SecureRandom random = new SecureRandom();
	final static boolean SMS_UNSUPPORTED = false;
	public static final String WHISPERSYSTEMS_REGISTRATION_ID = "312334754206";

	/**
	 * Generates an install ID with a maximum of 14bit. The number is based on
	 * the SHA-1 hash of the first MAC adress in the system. If no such adress
	 * is found, a random number is chosen.<br>
	 * <br>
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
			final MessageDigest digest = MessageDigest.getInstance("SHA-1");
			final byte[] hashedMac = digest.digest(getFirstMACAdress());
			return limitInt(new BigInteger(hashedMac).intValue(), 14);

		} catch (final Exception e) {
			final int size = (int) Math.pow(2, 14 + 1);
			return random.nextInt(size);
		}
	}

	/**
	 * Returns the first MAC adress that is found in the system.
	 *
	 * @return MAC adress
	 * @throws SocketException
	 *             if an I/O error occurs.
	 */
	private static byte[] getFirstMACAdress() throws SocketException {
		final Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

		while (networkInterfaces.hasMoreElements()) {
			final NetworkInterface network = networkInterfaces.nextElement();

			final byte[] bmac = network.getHardwareAddress();
			if (bmac != null) {
				return bmac;
			}
		}
		throw new SocketException("No network interfaces with a MAC adress found.");
	}

	/**
	 * Returns the first <code>bitlength</code> bits of the given int.
	 *
	 * @param value
	 *            the int to shorten
	 * @param bitlength
	 *            the amount of bits to return
	 * @return the <code>bitlength</code> most significant bits of the value
	 */
	private static int limitInt(final int value, final int bitlength) {
		return Integer.parseInt(Integer.toBinaryString(value).substring(0, bitlength), 2);
	}

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
	public static void register(final ServerAccount account, final RegistrationHandler handler) throws IOException,
	AuthorizationFailedException,
	Exception {
		final TextSecureAccountManager accountManager =
				new TextSecureAccountManager(account.getURL(), NorvosTrustStore.get(), account.getUsername(),
						account.getPassword());

		accountManager.requestSmsVerificationCode();
		final String receivedVerificationCode = handler.getCode();

		accountManager.verifyAccount(receivedVerificationCode, account.getSignalingKey(), SMS_UNSUPPORTED,
				generateRandomInstallId());

		accountManager.setGcmId(Optional.of("This is an invalid push ID. That doesn't harm anything, as the"
				+ "application pulls the messages manually from the server."));

		final NorvosAxolotlStore axolotlStore = Settings.getCurrent().getAxolotlStore();
		accountManager.setPreKeys(axolotlStore.getIdentityKeyPair().getPublicKey(), axolotlStore.getLastResortKey(),
				axolotlStore.getSignedPreKey(), axolotlStore.getOneTimePreKeys());

	}
}
