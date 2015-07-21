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

import org.whispersystems.textsecure.api.push.TrustStore;

import de.norvos.i18n.Translations;
import de.norvos.log.Errors;
import de.norvos.log.Logger;

public class NorvosTrustStore implements TrustStore {
	
	/**
	 * Singleton instance
	 */
	private static NorvosTrustStore store = null;
	
	/**
	 * The default password of the KeyStore shipped with the TextSecure android client is "whisper".
	 */
	private static String password = "whisper";
	
	/**
	 * Singleton constructor
	 */
	private NorvosTrustStore(){}
	
	/**
	 * Returns the only instance of the NorvosTrustStore.
	 * @return the NorvosTrustStore
	 */
	public static NorvosTrustStore get(){
		if(store == null){
			store = new NorvosTrustStore();
		}
		return store;
	}


	/**
	 * Returns an InputStream to the Java KeyStore.
	 */
	@Override
	public InputStream getKeyStoreInputStream() {
		InputStream in = ClassLoader.getSystemResourceAsStream("whisper.store");
		if(in == null){
			Logger.critical("TLS certificate missing.");
			Errors.critical(Translations.format("errors", "trustStoreNotFound"));
		}
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
