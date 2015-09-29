package de.norvos.utils;

/**
 * Provides various utility methods related to data manipulation.
 * @author Connor Lanigan
 */
public class DataManipulationUtils {

	/**
	 * Removes all non-digit characters from a string.
	 * @param input the string to extract the digits from
	 * @return the string with all non-digit characters removed
	 */
	public static String extractDigitsFromString(final String input) {
		return input.replaceAll("\\D", "");
	}

	private DataManipulationUtils() {
	}

}
