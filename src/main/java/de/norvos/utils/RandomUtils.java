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

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class RandomUtils {

	private static SecureRandom random = new SecureRandom();

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	
	public static String randomAlphanumerical(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(random.nextInt(AB.length())));
		return sb.toString();
	}
	
	public static String randomASCII(int size){
		byte[] array = new byte[size];

		random.nextBytes(array);

		return new String(array, StandardCharsets.US_ASCII);
	}
	
	

	

}
