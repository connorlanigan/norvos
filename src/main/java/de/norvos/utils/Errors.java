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

import static de.norvos.i18n.Translations.translate;

import javax.swing.JOptionPane;

import javafx.application.Platform;

//TODO replace self-built logging with Java file rotation logger. Also, error message dialogs should not be automatically shown. See de.norvos.utils.DebugProvider
/**
 * Provides methods for logging errors, warnings and debug messages.
 *
 * @author Connor Lanigan
 */
public class Errors {
	/**
	 * Shows an error message box to the user. Returns only after being
	 * confirmed by the user.
	 *
	 * @param messageText
	 *            the error message
	 */
	public static void showError(final String messageText) {
		JOptionPane.showMessageDialog(null, messageText, translate("genericErrorMessageTitle"),
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Shows a warning message box to the user. Returns only after being
	 * confirmed by the user.
	 *
	 * @param messageText
	 *            the error message
	 */
	public static void showWarning(final String messageText) {
		JOptionPane.showMessageDialog(null, messageText, translate("genericWarningMessageTitle"),
				JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Stops the application and indicates an error to the operating system.
	 */
	public static void stopApplication() {
		Platform.exit();
		System.exit(1);
	}

}