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
import java.security.Security;
import java.util.Scanner;

import javax.swing.UIManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.whispersystems.libaxolotl.logging.AxolotlLogger;
import org.whispersystems.libaxolotl.logging.AxolotlLoggerProvider;
import org.whispersystems.textsecure.api.push.exceptions.AuthorizationFailedException;

import de.norvos.account.RegistrationHandler;
import de.norvos.account.Registrator;
import de.norvos.account.ServerAccount;
import de.norvos.account.Settings;
import de.norvos.persistence.DiskPersistence;

public class Main implements RegistrationHandler{
	static Scanner in = new Scanner(System.in);

	public static void main(String[] args) throws Exception{
		AxolotlLoggerProvider.setProvider(new AxolotlLogger() {

			@Override
			public void log(int priority, String tag, String message) {
				System.err.println("Priority "+priority + ": [" + tag + "] " + message);
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
			try {
				Registrator.register(new ServerAccount(number, ServerAccount.generateRandomBytes(128), ServerAccount.generateRandomBytes(52)), new Main());
				System.out.println("The registration was succesful!");
			} catch (AuthorizationFailedException e) {
				System.out.println("Unfortunately your code was wrong.");
			} catch (IOException e) {
				System.out.println("There was an error in communicating with the server. Please try again later.");
			}


		} else {
			System.out.println("Welcome back!");
		}
	}

	@Override
	public String getCode() {
		System.out
				.println("Great! You will now get an SMS to your phone with a number. Please enter this number here:");
		String code = in.nextLine();

		return code.replaceAll("\\D+","");
	}
}