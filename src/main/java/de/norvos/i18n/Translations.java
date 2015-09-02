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

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import de.norvos.account.AccountDataStore;
import de.norvos.log.Errors;

public class Translations {

	/**
	 * Translates the message and returns a formatted string using the specified
	 * format string and arguments.
	 */
	public static String format(final String resourceBundle, final String stringId, final Object... args) {
		return String.format(getMessage(resourceBundle, stringId), args);
	}

	/**
	 * Translates the given message.
	 *
	 * @param originalMessage
	 *            message in English
	 * @return translated message
	 */
	private static String getMessage(final String resourceBundle, final String stringId) {
		String translated = "<I18N:" + resourceBundle + "." + stringId + ">";

		try {
			final String resourceBundlePath = "de.norvos.i18n.strings." + resourceBundle.toLowerCase() + "."
					+ resourceBundle;
			final Locale locale = Locale.forLanguageTag(AccountDataStore.getStringValue("locale"));
			final ResourceBundle res = ResourceBundle.getBundle(resourceBundlePath, locale);

			translated = res.getString(stringId);
		} catch (final NullPointerException e) {
			Errors.debug("Requested translation for a null-pointer.");
		} catch (final MissingResourceException e) {
			Errors.debug("Missing translation resource: " + resourceBundle + "#" + stringId);
		}

		return translated;
	}
}
