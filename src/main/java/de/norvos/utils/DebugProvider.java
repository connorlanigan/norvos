package de.norvos.utils;

import org.whispersystems.libaxolotl.logging.AxolotlLogger;

import de.norvos.MainApplication;

/**
 * Provides debugging methods for the Axolotl protocol.
 *
 * @author Connor Lanigan
 */
public class DebugProvider implements AxolotlLogger {

	/**
	 * Logs a debug message. The given string will be formatted using
	 * {@link String#format(String, Object...) String.format()}.
	 *
	 * @param string
	 *            the string
	 * @param args
	 *            the parameters for formatting
	 */
	public static void debug(final String string, final Object... args) {
		if (MainApplication.arguments.contains("debugMessages")) {
			if (args.length == 1) {
				System.out.println(String.format(string, args[0]));
			} else {
				System.out.println(String.format(string, args));
			}
		}
	}

	/**
	 * Logs a message.
	 */
	@Override
	public void log(final int priority, final String tag, final String message) {
		debug("%d [%s]: %s", priority, tag, message);
	}
}
