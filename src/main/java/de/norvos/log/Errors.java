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
package de.norvos.log;

import static de.norvos.i18n.Translations.translate;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import de.norvos.utils.ResourceUtils;

//TODO replace self-built logging with Java file rotation logger
public class Errors {
	private static void critical(final String message) {
		final String logMessage = "[CRITICAL] " + message;
		System.err.println(logMessage);
		writeToFile(logMessage);
	}

	/**
	 * Displays a localized error message to the user and logs the message to
	 * disk.
	 *
	 * @param stringId
	 *            the ID of the error message
	 * @param args
	 *            optional arguments for the error message
	 */
	public static void critical(final String stringId, final Object... args) {
		final String messageText = translate(stringId, args);
		critical(messageText);
		JOptionPane.showMessageDialog(null, messageText, "Norvos – Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Logs a debug message to System.out
	 *
	 * @param message
	 *            the message to log
	 */
	public static void debug(final String message) {
		final String logMessage = "[DEBUG] " + message;
		System.err.println(logMessage);
	}

	private static void warning(final String message) {
		final String logMessage = "[WARNING] " + message;
		System.err.println(logMessage);
		writeToFile(logMessage);
	}

	/**
	 * Displays a localized warning message to the user and logs the message to
	 * disk.
	 *
	 * @param stringId
	 *            the ID of the warning message
	 * @param args
	 *            optional arguments for the warning message
	 */
	public static void warning(final String stringId, final Object... args) {
		final String messageText = translate(stringId, args);
		warning(messageText);
		JOptionPane.showMessageDialog(null, messageText, "Norvos – Warning", JOptionPane.WARNING_MESSAGE);
	}

	private static void writeToFile(final String fullMessage) {
		try (PrintWriter writer = new PrintWriter(ResourceUtils.getLogfile().toFile())) {
			writer.println(fullMessage);
		} catch (final FileNotFoundException e) {
		}
	}

}