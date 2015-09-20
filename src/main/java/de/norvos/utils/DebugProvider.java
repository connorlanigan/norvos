package de.norvos.utils;

import org.whispersystems.libaxolotl.logging.AxolotlLogger;

import de.norvos.MainApplication;

public class DebugProvider implements AxolotlLogger {

	public static void debug(final String string, final Object... args) {
		if (MainApplication.arguments.contains("debugMessages")) {
			if (args.length == 1) {
				System.out.println(String.format(string, args[0]));
			} else {
				System.out.println(String.format(string, args));
			}
		}
	}

	@Override
	public void log(final int priority, final String tag, final String message) {
		debug("%d [%s]: %s", priority, tag, message);
	}
}
