package de.norvos.log;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import de.norvos.log.Errors.Message;
import de.norvos.store.Locations;

public class Logger {
	
	public enum Severity {
		INFO, WARNING, CRITICAL, DEBUG
	};

	private static void log(Severity s, String message) {
		String level;
		switch (s) {
		case DEBUG:
			level = "[DEBUG]";
			break;
		case INFO:
			level = "[INFO]";
			break;
		case WARNING:
			level = "[WARNING]";
			break;
		case CRITICAL:
			level = "[CRITICAL]";
			break;
		default:
			level = "[UNDEFINED]";
		}
		String fullMessage = level + " " + message;

		writeToFile(fullMessage);
	}

	private static void writeToFile(String fullMessage) {
		Locations.LOG.toFile().mkdirs();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(Locations.LOG.toFile());
			writer.println(fullMessage);
		} catch (FileNotFoundException e) {
			Errors.handleWarning(Message.logFileNotAccessible);
		} finally{
			if(writer != null){				
				writer.close();
			}
		}
		
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
