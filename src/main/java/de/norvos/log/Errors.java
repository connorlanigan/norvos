package de.norvos.log;

public class Errors {

	
	public static void handleCritical(Message message){
		Logger.critical(message.toString());
		// TODO get translation and inform user
	}
	
	public enum Message {
		axolotlCouldNotBeLoaded,
		axolotlCouldNotBeSaved,
		sessionCopyCouldNotBeCreated,	
	}
	
	
	
	static String axolotlCouldNotBeLoaded =
			"Your configuration file could not be read. Please make sure you have read-access rights on the file.";
	static String axolotlCouldNotBeSaved =
			"Your configuration file could not be written. Please make sure that you have write-access rights on the file.";
	static String sessionCopyCouldNotBeCreated =
			"While attempting to load the requested session, an IO-error occured.";
	

}
