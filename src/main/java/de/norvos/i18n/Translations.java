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

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import de.norvos.account.SettingsService;
import de.norvos.utils.Constants;
import de.norvos.utils.ResourceUtils;

public class Translations {

	private static String format(final String patternString, final Object[] args) {
		final Locale locale = SettingsService.getLanguage().getLocale();
		if (args.length == 1) {
			return MessageFormat.format(patternString, args[0], locale);
		} else {
			return MessageFormat.format(patternString, args, locale);
		}
	}

	private static String translate(final String stringId) {
		final ResourceBundle res = ResourceUtils.getLocalizedStringsBundle();
		return res.getString(stringId);
	}

	public static String translate(final String stringId, final Object... args) {
		try {
			final String translated = translate(stringId);
			final String formatted = format(translated, args);

			return formatted;
		} catch (final Exception e) {
			e.printStackTrace();
			return Constants.I18N_ERROR;
		}

	}
}
