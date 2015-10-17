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

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.libaxolotl.logging.AxolotlLoggerProvider;
import org.whispersystems.textsecure.api.push.exceptions.AuthorizationFailedException;
import org.whispersystems.textsecure.api.push.exceptions.RateLimitException;

import de.norvos.account.Registrator;
import de.norvos.account.SettingsService;
import de.norvos.gui.controller.register.RegisterController;
import de.norvos.gui.windows.MainWindow;
import de.norvos.gui.windows.RegisterWindow;
import de.norvos.gui.windows.Window;
import de.norvos.i18n.AvailableLanguage;
import de.norvos.utils.ApplicationSingleton;
import de.norvos.utils.ArgumentsHandler;
import de.norvos.utils.AxolotlLoggerImpl;
import de.norvos.utils.DataManipulationUtils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;

/**
 * The main application and entry point of the application.
 *
 * @author Connor Lanigan
 */
public class MainApplication extends Application {
	final static Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

	private static boolean hasToRegister() {
		return !SettingsService.isSetupFinished() && !ArgumentsHandler.INSTANCE.getSkipRegistration();
	}

	/**
	 * Sets the default values for a new user.
	 */
	private static void initializeWithDefaultSettings() {
		final String defaultURL = "https://textsecure-service.whispersystems.org";
		SettingsService.setURL(defaultURL);
		LOGGER.debug("Setting server URL to [{}].", defaultURL);
		SettingsService.setLanguage(AvailableLanguage.getDefaultLanguage());
	}

	/**
	 * Initializes all libraries.
	 */
	private static void initLibraries() {
		Security.addProvider(new BouncyCastleProvider());
		AxolotlLoggerProvider.setProvider(new AxolotlLoggerImpl());
	}

	public static void main(final String[] args) {
		ApplicationSingleton.checkAndLock();
		LOGGER.info("Starting application.");

		ArgumentsHandler.INSTANCE.init(args);

		initLibraries();

		Application.launch(MainApplication.class, args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		final Task<Void> task = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				try {
					if (hasToRegister()) {
						initializeWithDefaultSettings();

						Window registerWindow = new RegisterWindow();
						Platform.runLater(() -> registerWindow.start(primaryStage));
						registerWindow.waitForClose();

						// completely end the program if the user has not
						// completed
						// the
						// registration
						if (!SettingsService.isSetupFinished()) {
							LOGGER.info("Registration has been aborted.");
							System.exit(0);
						}
					}

					Window mainWindow = new MainWindow();
					Platform.runLater(() -> mainWindow.start(new Stage()));
				} catch (Throwable e) {
					LOGGER.debug(e.getMessage(), e);
				}
				return (Void) null;
			}
		};
		new Thread(task).start();

	}
}
