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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.AbstractMap;
import java.util.List;
import java.util.Scanner;

import javax.swing.UIManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.whispersystems.libaxolotl.logging.AxolotlLogger;
import org.whispersystems.libaxolotl.logging.AxolotlLoggerProvider;
import org.whispersystems.textsecure.api.messages.TextSecureDataMessage;
import org.whispersystems.textsecure.api.push.exceptions.AuthorizationFailedException;

import de.norvos.account.RegistrationHandler;
import de.norvos.account.Registrator;
import de.norvos.account.ServerAccount;
import de.norvos.account.Settings;
import de.norvos.communication.MessageListener;
import de.norvos.communication.MessageSender;
import de.norvos.observers.Notifiable;
import de.norvos.persistence.DiskPersistence;
import de.norvos.utils.OSCustomizations;
import de.norvos.utils.PersistentSet;
import de.norvos.utils.PersistentSet.PersistentBytes;
import de.norvos.utils.RandomUtils;

public class Main implements RegistrationHandler, Notifiable {
	private static Scanner in = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		OSCustomizations.initialize();
		OSCustomizations.setLookAndFeel();

		AxolotlLoggerProvider.setProvider(new AxolotlLogger() {

			@Override
			public void log(int priority, String tag, String message) {
				System.err.println("Priority " + priority + ": [" + tag + "] " + message);
			}

		});
		Security.addProvider(new BouncyCastleProvider());
		try {
			// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Settings.load(DiskPersistence.load("settings"));
		} catch (IOException e) {
		}

		if (!Settings.getCurrent().isSetupFinished()) {
			System.out.println("Hello, this is the first time you're using Norvos.\n"
					+ "You will now be registered for this service. Please enter your mobile phone number here:");

			String number = in.nextLine();
			number = "+4915788471709";
			Settings.getCurrent().setServerAccount(
					new ServerAccount(number, RandomUtils.randomAlphanumerical(128), RandomUtils.randomAlphanumerical(52)));
			try {
				Registrator.register(Settings.getCurrent().getServerAccount(), new Main());
				System.out.println("The registration was succesful!");
				Settings.getCurrent().setSetupFinished(true);
				System.out.println("Settings saved.");
			} catch (AuthorizationFailedException e) {
				System.out.println("Unfortunately your code was wrong.");
				System.exit(1);
			} catch (IOException e) {
				System.out.println("There was an error in communicating with the server. Please try again later.");
				System.exit(1);
			}

		} else {
			System.out.println("Welcome back!");
		}
		
		
		Thread messageListener = new Thread(new MessageListener());
		messageListener.start();
		
		(new MessageSender()).register(new Main(), "messageSent");

		System.out.println("-- Write your messages here:");
		String input = "";
		input = in.nextLine();

		while (!input.equals("/exit")) {
			MessageSender.sendTextMessage("+491788174362", input);
			input = in.nextLine();
		}
		System.out.println("-- Thanks for using Norvos. Bye!");
	}


	@Override
	public String getCode() {
		System.out
				.println("Great! You will now get an SMS to your phone with a number. Please enter this number here:");
		String code = in.nextLine();

		return code.replaceAll("\\D+", "");
	}

	@Override
	public void notify(String event, Object notificationData) {
		if (event.equals("messageSent")) {
			AbstractMap.SimpleEntry<String, TextSecureDataMessage> entry =
					(AbstractMap.SimpleEntry<String, TextSecureDataMessage>) notificationData;
			System.err.println("Message sent: " + entry.getValue().getBody().get());
		}
	}

}