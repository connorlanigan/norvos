package de.norvos;

public class Logger {

	public enum Severity { INFO, WARNING, CRITICAL, DEBUG };
	
	
	private static void log(Severity s, String message){
		// TODO implement logging
	}
	public static void debug(String message){
		log(Severity.DEBUG, message);
	}
	public static void critical(String message){
		log(Severity.CRITICAL, message);
	}
	public static void warning(String message){
		log(Severity.WARNING, message);
	}
}
