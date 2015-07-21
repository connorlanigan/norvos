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
package de.norvos.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.norvos.account.Settings;
import de.norvos.log.Logger;

public class Translations {

	/**
	 * Translates the message and returns a formatted string using the specified
	 * format string and arguments.
	 */
	public static String format(String resourceBundle, String stringId, Object... args) {
		return String.format(getMessage(resourceBundle, stringId), args);
	}

	/**
	 * Translates the given message.
	 * 
	 * @param originalMessage
	 *            message in English
	 * @return translated message
	 */
	private static String getMessage(String resourceBundle, String stringId) {
		String translated = "<I18N:" + resourceBundle + "." +stringId + ">";

		try {
			String resourceBundlePath = "de.norvos.i18n.strings." + resourceBundle.toLowerCase() + "." + resourceBundle;
			ResourceBundle res = ResourceBundle.getBundle(resourceBundlePath, Settings.getCurrent().getLocale());

			translated = res.getString(stringId);
		} catch (NullPointerException e) {
			Logger.critical("Requested translation for a null-pointer.");
		} catch (MissingResourceException e) {
			Logger.critical("Missing translation resource: " + resourceBundle + "#" + stringId);
		}

		return translated;
	}
}
