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

import static de.norvos.i18n.Translations.translate;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.norvos.account.SettingsService;
import de.norvos.utils.Constants;

public class TranslationsTest {

	@Test
	public void translationAvailable() {
		SettingsService.setLanguage(AvailableLanguage.ENGLISH);
		final String translated = translate("registrationFailed", "TestReason");
		assertEquals("Registration failed. Reason: TestReason", translated);
	}

	@Test
	public void translationUnavailable() {
		SettingsService.setLanguage(AvailableLanguage.ENGLISH);
		final String translated = translate("This does not exist", "TestReason");
		assertEquals(Constants.I18N_ERROR, translated);
	}

}
