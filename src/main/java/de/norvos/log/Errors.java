package de.norvos.log;

import i18n.Translatable;

import javax.swing.JOptionPane;

public class Errors {

	public static void handleWarning(Message message) {
		if (message != Message.logFileNotAccessible) {
			Logger.warning(message.toString());
		}
		JOptionPane.showMessageDialog(null, message, "Warning", JOptionPane.WARNING_MESSAGE);
	}

	public static void handleCritical(Message message) {
		if (message != Message.logFileNotAccessible) {
			Logger.critical(message.toString());
		}
		JOptionPane.showMessageDialog(null, message, "Critical Error", JOptionPane.ERROR_MESSAGE);
		System.exit(1);
	}

	public enum Message implements Translatable{
		axolotlCouldNotBeLoaded ("Your configuration file could not be read. Please make sure you have read-access rights on the file."),
		axolotlCouldNotBeSaved ("Your configuration file could not be written. Please make sure that you have write-access rights on the file."),
		sessionCopyCouldNotBeCreated ("While attempting to load the requested session, an IO-error occured."),
		logFileNotAccessible ("The logfile cannot be accessed. No logs will be created. Check if you have write permissions on Norvos/errors.log in your home folder."),
		cannotCreateKey ("The needed keyfiles could not be generated. This means something on your system is working REALLY bad. Please notify me at norvos@connorlanigan.com."),		
		trustStoreNotFound ("The local certificate of the TextSecure-server was removed. Without the certificate, you cannot communicate securely. Please reinstall the certificate by reinstalling Norvos.");
		
		private final String text;
		private Message(String pText){
			text = pText;
		}
		public String toString(){
			return text;
		}
	}
}
