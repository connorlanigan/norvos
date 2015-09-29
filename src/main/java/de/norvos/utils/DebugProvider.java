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
