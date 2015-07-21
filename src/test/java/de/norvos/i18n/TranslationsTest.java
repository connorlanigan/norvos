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

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Test;

import de.norvos.account.Settings;

public class TranslationsTest {

	@Test
	public void translationAvailable() {
		Settings.getCurrent().setLocale(Locale.ENGLISH);
		String translated = Translations.format("errors", "registrationFailed", "TestReason");
		assertEquals("Registration failed. Reason: TestReason", translated);
	}
	
	@Test
	public void translationUnavailable() {
		Settings.getCurrent().setLocale(Locale.ENGLISH);
		String translated = Translations.format("this resource does", "not exist", "TestReason");
		assertEquals("<I18N:this resource does.not exist>", translated);
		
		translated = Translations.format("errors", "testEntry", "TestReason");
		assertEquals("<I18N:errors.testEntry>", translated);
	}

}