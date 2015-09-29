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
package de.norvos.utils;

import java.math.BigInteger;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Enumeration;

/**
 * Provides various utility methods related to random data.
 * @author Connor Lanigan
 */
public class RandomUtils {

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private static SecureRandom random = new SecureRandom();

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
	 * @throws SocketException
	 *             if an I/O-error occurs while getting the system's MAC address
	 * @throws NoSuchAlgorithmException
	 *             if the SHA-1 hash algorithm cannot be found
	 */
	public static int generateInstallId() {
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
	 * Generates a random alphanumerical String of the given length. The used
	 * characters are:<br>
	 * <br>
	 * 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ
	 *
	 * @param len
	 *            length of the String to generate
	 * @return the generated random String
	 */
	public static String randomAlphanumerical(final int len) {
		final StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			sb.append(AB.charAt(random.nextInt(AB.length())));
		}
		return sb.toString();
	}

	/**
	 * Generates a random String containing ASCII characters. Every ASCII
	 * character can appear in the String, even non-printable ones and all
	 * others.
	 *
	 * @param size
	 *            number of ASCII characters to generate
	 * @return the generated random String
	 */
	public static String randomASCII(final int size) {
		final byte[] array = new byte[size];

		random.nextBytes(array);

		return new String(array, StandardCharsets.US_ASCII);
	}
}
