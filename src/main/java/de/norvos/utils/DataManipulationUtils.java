package de.norvos.utils;

public class DataManipulationUtils {

	public static String extractDigitsFromString(String input) {
		return input.replaceAll("\\D", "");
	}

	private DataManipulationUtils() {
	}

}
