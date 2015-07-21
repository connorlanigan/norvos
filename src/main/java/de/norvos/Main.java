package de.norvos;

import java.io.IOException;
import java.security.Security;
import java.util.Scanner;

import javax.swing.UIManager;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.whispersystems.libaxolotl.logging.AxolotlLogger;
import org.whispersystems.libaxolotl.logging.AxolotlLoggerProvider;
import org.whispersystems.textsecure.api.push.exceptions.AuthorizationFailedException;

import de.norvos.account.Registrator;
import de.norvos.account.ServerAccount;
import de.norvos.account.Settings;
import de.norvos.gui.RegistrationHandler;
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
				Registrator.register(new ServerAccount(number, randomPassword()), new Main());
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

	private static String randomPassword() {
		return "arsch34asdjhjfll";
	}

	@Override
	public String getCode() {
		System.out
				.println("Great! You will now get an SMS to your phone with a number. Please enter this number here:");
		String code = in.nextLine();

		return code.replaceAll("\\D+","");
	}
}