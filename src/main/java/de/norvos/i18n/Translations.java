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

public class Translations {

	public static String T(final String stringId, final Object... args) {
		final Locale locale = SettingsService.getLanguage().getLocale();
		ResourceBundle res;
		try {
			res = ResourceBundle.getBundle("de.norvos.i18n.strings", locale, new UTF8Control());
			final String patternString = res.getString(stringId);
			return MessageFormat.format(patternString, args, locale);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "{i18n-ERR}";

	}
}
