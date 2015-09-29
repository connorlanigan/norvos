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

/**
 * Provides various utility methods related to data manipulation.
 * 
 * @author Connor Lanigan
 */
public class DataManipulationUtils {

	/**
	 * Removes all non-digit characters from a string.
	 * 
	 * @param input
	 *            the string to extract the digits from
	 * @return the string with all non-digit characters removed
	 */
	public static String extractDigitsFromString(final String input) {
		return input.replaceAll("\\D", "");
	}

	private DataManipulationUtils() {
	}

}
