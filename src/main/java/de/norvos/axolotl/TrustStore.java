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

import java.io.InputStream;

public class TrustStore implements org.whispersystems.textsecure.api.push.TrustStore {

	/**
	 * The default password of the KeyStore shipped with the TextSecure android
	 * client is "whisper".
	 */
	private static String password = "whisper";

	/**
	 * Singleton instance
	 */
	private static TrustStore store = null;

	/**
	 * Returns the only instance of the NorvosTrustStore.
	 *
	 * @return the NorvosTrustStore
	 */
	public static TrustStore getInstance() {
		if (store == null) {
			store = new TrustStore();
		}
		return store;
	}

	/**
	 * Singleton constructor
	 */
	private TrustStore() {
	}

	/**
	 * Returns an InputStream to the Java KeyStore.
	 */
	@Override
	public InputStream getKeyStoreInputStream() {
		final InputStream in = ClassLoader.getSystemResourceAsStream("whisper.store");
		return in;
	}

	/**
	 * Returns the password for the KeyStore.
	 */
	@Override
	public String getKeyStorePassword() {
		return password;
	}
}
