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
package de.norvos.account;

import java.util.Locale;

import org.whispersystems.libaxolotl.IdentityKeyPair;

import de.norvos.i18n.AvailableLanguage;

/**
 * Allows access to user settings.
 *
 * @author Connor Lanigan
 */
public class SettingsService {

	private enum Setting {
		IDENTITYKEYPAIR("identity_keypair"),
		INSTALLID("install_id"),
		LOCALE("locale"),
		LOCALREGISTRATIONID("local_registration_id"),
		PASSWORD("password"),
		SERVERURL("server_url"),
		SETUPFINISHED("setup_finished"),
		SIGNALINGKEY("signaling_key"),
		USERNAME("username");

		private final String key;

		Setting(final String key) {
			this.key = key;
		}

		@Override
		public String toString() {
			return key;
		}
	}

	synchronized private static byte[] getBinarySetting(final Setting setting) {
		return AccountDataStore.getBinaryValue(setting.toString());
	}

	public static byte[] getIdentityKeyPair() {
		return getBinarySetting(Setting.IDENTITYKEYPAIR);
	}

	public static int getInstallID() {
		return Integer.valueOf(getStringSetting(Setting.INSTALLID));
	}

	public static AvailableLanguage getLanguage() {
		
		try {
			final String languageTag = getStringSetting(Setting.LOCALE);
			return AvailableLanguage.forLocaleLanguage(Locale.forLanguageTag(languageTag));
		} catch (Exception e) {
			return null;
		}
	}

	public static int getLocalRegistrationId() {
		return Integer.valueOf(getStringSetting(Setting.LOCALREGISTRATIONID));
	}

	public static String getPassword() {
		return getStringSetting(Setting.PASSWORD);
	}

	public static String getSignalingKey() {
		return getStringSetting(Setting.SIGNALINGKEY);
	}

	synchronized private static String getStringSetting(final Setting setting) {
		return AccountDataStore.getStringValue(setting.toString());
	}

	public static String getURL() {
		return getStringSetting(Setting.SERVERURL);
	}

	public static String getUsername() {
		return getStringSetting(Setting.USERNAME);
	}

	public static boolean isSetupFinished() {
		return Boolean.valueOf(getStringSetting(Setting.SETUPFINISHED));
	}

	public static void setIdentityKeyPair(final IdentityKeyPair keyPair) {
		setSetting(Setting.IDENTITYKEYPAIR, keyPair.serialize());
	}

	public static void setInstallID(final int installID) {
		setSetting(Setting.INSTALLID, String.valueOf(installID));
	}

	public static void setLanguage(final AvailableLanguage language) {
		setSetting(Setting.LOCALE, language.getLocale().toLanguageTag());
	}

	public static void setLocalRegistrationId(final int id) {
		setSetting(Setting.LOCALREGISTRATIONID, String.valueOf(id));
	}

	public static void setPassword(final String password) {
		setSetting(Setting.PASSWORD, password);
	}

	synchronized private static void setSetting(final Setting setting, final byte[] value) {
		AccountDataStore.storeBinaryValue(setting.toString(), value);
	}

	synchronized private static void setSetting(final Setting setting, final String value) {
		AccountDataStore.storeStringValue(setting.toString(), value);
	}

	public static void setSetupFinished(final boolean finished) {
		setSetting(Setting.SETUPFINISHED, String.valueOf(finished));
	}

	public static void setSignalingKey(final String signalingKey) {
		setSetting(Setting.SIGNALINGKEY, signalingKey);
	}

	public static void setURL(final String url) {
		setSetting(Setting.SERVERURL, url);
	}

	public static void setUsername(final String username) {
		setSetting(Setting.USERNAME, username);
	}
}
