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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.swing.JOptionPane;

import de.norvos.i18n.Translations;

public class Errors {
	private static void critical(final String message) {
		log("[CRITICAL] " + message);
	}

	public static void critical(final String stringId, final Object... args) {
		final String messageText = Translations.format("errors", stringId, args);
		critical(messageText);
		JOptionPane.showMessageDialog(null, messageText, "Norvos – Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void debug(final String message) {
		log("[DEBUG] " + message);
	}

	private static void log(final String message) {
		System.out.println(message);
		writeToFile(message);
	}

	private static void warning(final String message) {
		log("[WARNING] " + message);
	}

	public static void warning(final String stringId, final Object... args) {
		final String messageText = Translations.format("errors", stringId, args);
		warning(messageText);
		JOptionPane.showMessageDialog(null, messageText, "Norvos – Warning", JOptionPane.WARNING_MESSAGE);
	}

	private static void writeToFile(final String fullMessage) {
		try {
			DiskPersistence.append("application.log", fullMessage.getBytes(StandardCharsets.UTF_8));
		} catch (final IOException e) {
		}
	}

}