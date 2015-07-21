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
