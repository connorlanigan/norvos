package de.norvos.i18n;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum AvailableLanguage {
	ENGLISH("English", Locale.ENGLISH),
	GERMAN("Deutsch", Locale.GERMAN),
	SIMPLIFIED_CHINESE("简体中文", Locale.SIMPLIFIED_CHINESE),
	/**
	 * Represents a language used only for testing. Every translated key will be
	 * translated as "XXX", so that it can be seen in the running application
	 * where strings are hardcoded. Do not use this language in the production
	 * version.
	 */
	TEST("Test", new Locale("test"));

	/**
	 * Returns an {@link AvailableLanguage} for the given {@link Locale}. Only
	 * the language part of the {@link Locale} is compared, and the region part
	 * is ignored.
	 *
	 * @param locale
	 *            the {@link Locale} for which to get the
	 *            {@link AvailableLanguage}
	 * @return the corresponding {@link AvailableLanguage} or, if no
	 *         corresponding {@link AvailableLanguage} was found,
	 *         <code>null</code>.
	 */
	public static AvailableLanguage forLocaleLanguage(final Locale locale) {
		final String localeLanguage = locale.getLanguage();
		for (final AvailableLanguage language : AvailableLanguage.values()) {
			if (language.getLocale().getLanguage().equals(localeLanguage)) {
				return language;
			}
		}
		return null;
	}

	private Locale code;

	private String displayName;

	AvailableLanguage(final String name, final Locale locale) {
		displayName = name;
		code = locale;
	}

	public List<AvailableLanguage> getAll() {
		final List<AvailableLanguage> list = Arrays.asList(values());
		list.remove(AvailableLanguage.TEST);
		return list;
	}

	/**
	 * @return the Locale representing the language
	 */
	public Locale getLocale() {
		return code;
	}

	/**
	 * Returns the name of the language.
	 *
	 * @return the human-readable language name
	 */
	@Override
	public String toString() {
		return displayName;
	}
}
