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


import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import de.norvos.axolotl.NorvosTrustStore;

public class ServerAccount {
	

	private final static SecureRandom random = new SecureRandom();
	
	private final String URL = "https://textsecure-service.whispersystems.org";
	private final NorvosTrustStore TRUST_STORE = NorvosTrustStore.get();
	private final String USERNAME;	
	private final String PASSWORD;
	private final String SIGNALING_KEY;

	public ServerAccount(String username, String password, String signalingKey){
		this.USERNAME = username;
		this.PASSWORD = password;
		this.SIGNALING_KEY = signalingKey;
	}

	public String getURL() {
		return URL;
	}

	public NorvosTrustStore getTrustStore() {
		return TRUST_STORE;
	}

	public String getUsername() {
		return USERNAME;
	}

	public String getPassword() {
		return PASSWORD;
	}
	
	public String getSignalingKey(){
		return SIGNALING_KEY;
	}
	

	/**
	 * Generates a random array of n bytes.
	 * 
	 * @return a random array of the given size
	 */
	public static String generateRandomBytes(int size) {
		byte[] array = new byte[size];

		random.nextBytes(array);

		return new String(array, StandardCharsets.US_ASCII);
	}
}
