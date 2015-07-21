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
