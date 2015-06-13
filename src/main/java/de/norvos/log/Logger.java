package de.norvos.log;

public class Logger {

	public enum Severity {
		INFO, WARNING, CRITICAL, DEBUG
	};

	private static void log(Severity s, String message) {
		String prepend;
		switch (s) {
		case DEBUG:
			prepend = "[DEBUG]";
			break;
		case INFO:
			prepend = "[INFO]";
			break;
		case WARNING:
			prepend = "[WARNING]";
			break;
		case CRITICAL:
			prepend = "[CRITICAL]";
			break;
		default:
			prepend = "[UNDEFINED]";
		}
		// TODO implement file logging
		System.out.println(prepend + " " + message);
	}

	public static void debug(String message) {
		log(Severity.DEBUG, message);
	}

	public static void critical(String message) {
		log(Severity.CRITICAL, message);
	}

	public static void warning(String message) {
		log(Severity.WARNING, message);
	}
}
