package de.norvos.account;

import java.util.Locale;

import org.whispersystems.libaxolotl.IdentityKeyPair;

import de.norvos.i18n.Language;

public class SettingsService {

	private enum Setting {
		LOCALE("locale"),
		SERVERURL("server_url"),
		PASSWORD("password"),
		INSTALLID("install_id"),
		USERNAME("username"),
		SETUPFINISHED("setup_finished"),
		LOCALREGISTRATIONID("local_registration_id"),
		IDENTITYKEYPAIR("identity_keypair"),
		SIGNALINGKEY("signaling_key");

		Setting(String key) {
			this.key = key;
		}

		private String key;

		@Override
		public String toString() {
			return key;
		}
	}

	public static void setLanguage(Language language){
		setSetting(Setting.LOCALE, language.getLocale().toLanguageTag());
	}

	public static Language getLanguage(){
		String languageTag = getStringSetting(Setting.LOCALE);
		return Language.forLocale(Locale.forLanguageTag(languageTag));
	}

	public static void setURL(String url) {
		setSetting(Setting.SERVERURL, url);
	}

	public static String getURL() {
		return getStringSetting(Setting.SERVERURL);
	}

	public static void setLocalRegistrationId(int id) {
		setSetting(Setting.LOCALREGISTRATIONID, String.valueOf(id));
	}

	public static int getLocalRegistrationId() {
		return Integer.valueOf(getStringSetting(Setting.LOCALREGISTRATIONID));
	}

	public static void setIdentityKeyPair(IdentityKeyPair keyPair) {
		setSetting(Setting.IDENTITYKEYPAIR, keyPair.serialize());
	}

	public static byte[] getIdentityKeyPair() {
		return getBinarySetting(Setting.IDENTITYKEYPAIR);
	}


	public static void setUsername(String username) {
		setSetting(Setting.USERNAME, username);
	}

	public static String getUsername() {
		return getStringSetting(Setting.USERNAME);
	}


	public static void setPassword(String password) {
		setSetting(Setting.PASSWORD, password);
	}

	public static String getPassword() {
		return getStringSetting(Setting.PASSWORD);
	}

	public static void setSignalingKey(String signalingKey) {
		setSetting(Setting.SIGNALINGKEY, signalingKey);
	}

	public static String getSignalingKey() {
		return getStringSetting(Setting.SIGNALINGKEY);
	}

	public static void setInstallID(int installID) {
		setSetting(Setting.INSTALLID, String.valueOf(installID));
	}

	public static int getInstallID() {
		return Integer.valueOf(getStringSetting(Setting.INSTALLID));
	}

	public static void setSetupFinished(boolean finished) {
		setSetting(Setting.SETUPFINISHED, String.valueOf(finished));
	}

	public static boolean isSetupFinished() {
		return Boolean.valueOf(getStringSetting(Setting.SETUPFINISHED));
	}


	synchronized private static void setSetting(Setting setting, String value) {
		AccountDataStore.storeStringValue(setting.toString(), value);
	}

	synchronized private static String getStringSetting(Setting setting) {
		return AccountDataStore.getStringValue(setting.toString());
	}

	synchronized private static byte[] getBinarySetting(Setting setting) {
		return AccountDataStore.getBinaryValue(setting.toString());
	}

	synchronized private static void setSetting(Setting setting, byte[] value) {
		AccountDataStore.storeBinaryValue(setting.toString(), value);
	}
}
