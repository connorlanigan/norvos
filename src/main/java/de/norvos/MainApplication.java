package de.norvos;

import static de.norvos.utils.DebugProvider.debug;

import java.security.Security;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.whispersystems.libaxolotl.logging.AxolotlLoggerProvider;

import de.norvos.account.Registrator;
import de.norvos.account.SettingsService;
import de.norvos.contacts.ContactData;
import de.norvos.gui.windows.MainWindow;
import de.norvos.gui.windows.RegisterWindow;
import de.norvos.i18n.AvailableLanguage;
import de.norvos.persistence.tables.Contacts;
import de.norvos.utils.Constants;
import de.norvos.utils.DebugProvider;
import javafx.application.Application;

public class MainApplication {

	public static List<String> arguments;

	private static void enableDebugMode() {
		System.out.println("## Warning! Debug mode enabled! The displayed data"
				+ "may be modified and behaviour might be altered!");
		Contacts.getInstance().storeContactData(new ContactData("+491788174362", "Connor", ""));
		Contacts.getInstance().storeContactData(new ContactData("1", "Léanne", ""));
		Contacts.getInstance().storeContactData(new ContactData("2", "Björn", ""));
	}

	private static boolean hasRegistered() {
		return SettingsService.isSetupFinished();
	}

	private static void initializeWithDefaultSettings() {
		SettingsService.setURL("https://textsecure-service.whispersystems.org");
		debug("URL: %s", SettingsService.getURL());
		setDefaultLanguage();
	}

	public static void main(final String[] args) {
		arguments = Collections.unmodifiableList(Arrays.asList(args));

		Security.addProvider(new BouncyCastleProvider());

		AxolotlLoggerProvider.setProvider(new DebugProvider());

		final boolean skipRegistration = arguments.contains("skipRegistration");

		if (arguments.contains("debug")) {
			enableDebugMode();
		}

		if (!hasRegistered() && !skipRegistration) {
			initializeWithDefaultSettings();
			Registrator.initialize();

			preWindowLaunch();
			Application.launch(RegisterWindow.class, args);

			if (!hasRegistered()) {
				System.exit(0);
			}
		}
		preWindowLaunch();
		Application.launch(MainWindow.class, args);
	}

	private static void preWindowLaunch() {
		if (arguments.contains("testTranslation")) {
			SettingsService.setLanguage(AvailableLanguage.TEST);
		}else{
			setDefaultLanguage();
		}
	}

	private static void setDefaultLanguage() {
		final Locale systemLocale = Locale.getDefault();
		AvailableLanguage applicationLanguage = AvailableLanguage.forLocaleLanguage(systemLocale);

		if (applicationLanguage == null) {
			applicationLanguage = Constants.DEFAULT_LANGUAGE;
		}

		SettingsService.setLanguage(applicationLanguage);
	}
}
