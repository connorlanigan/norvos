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
import de.norvos.persistence.tables.ContactsTable;
import de.norvos.utils.Constants;
import de.norvos.utils.DebugProvider;
import javafx.application.Application;

/**
 * The main application and entry point of the application.
 *
 * @author Connor Lanigan
 */
public class MainApplication {

	public static List<String> arguments;

	/**
	 * Sets data and configuration that is used for debug mode.
	 */
	private static void enableDebugMode() {
		System.out.println("## Warning! Debug mode enabled! The displayed data"
				+ "may be modified and behaviour might be altered!");
		ContactsTable.getInstance().storeContactData(new ContactData("+491788174362", "Connor", ""));
		ContactsTable.getInstance().storeContactData(new ContactData("1", "Léanne", ""));
		ContactsTable.getInstance().storeContactData(new ContactData("2", "Björn", ""));
	}

	private static boolean hasRegistered() {
		return SettingsService.isSetupFinished();
	}

	/**
	 * Sets the default values for a new user.
	 */
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

	/**
	 * Has to be executed before opening a new window. This will set the
	 * window's display language.
	 */
	private static void preWindowLaunch() {
		if (arguments.contains("testTranslation")) {
			SettingsService.setLanguage(AvailableLanguage.TEST);
		} else {
			setDefaultLanguage();
		}
	}

	/**
	 * This automatically sets the language that is probably best suited for a
	 * new user. It uses the systemLocale, if available, or else the Default
	 * Language of this application.
	 */
	private static void setDefaultLanguage() {
		final Locale systemLocale = Locale.getDefault();
		AvailableLanguage applicationLanguage = AvailableLanguage.forLocaleLanguage(systemLocale);

		if (applicationLanguage == null) {
			applicationLanguage = Constants.DEFAULT_LANGUAGE;
		}

		SettingsService.setLanguage(applicationLanguage);
	}
}
