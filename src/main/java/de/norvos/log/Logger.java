package de.norvos.log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import de.norvos.i18n.Translations;
import de.norvos.persistence.DiskPersistence;

public class Logger {

	public static void debug(String message) {
		log("[DEBUG] "+message);
	}

	public static void warning(String message) {
		log("[WARNING] "+message);
	}
	
	public static void critical(String message) {
		log("[CRITICAL] "+message);
	}
	
	private static void log(String message) {
		System.out.println(message);
		writeToFile(message);
	}

	private static void writeToFile(String fullMessage) {
		try {
			DiskPersistence.append("application.log", fullMessage.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			Errors.warning(Translations.format("errors", "logFileNotWritable"));
		}		
	}
	
}
