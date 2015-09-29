package de.norvos.messages;

import static de.norvos.i18n.Translations.translate;

import java.util.Arrays;
import java.util.List;

import de.norvos.contacts.Contact;

/**
 * Provides access to stored messages.
 * @author Connor Lanigan
 */
public class MessageService {

	private static MessageService instance;

	synchronized public static MessageService getInstance() {
		if (instance == null) {
			instance = new MessageService();
		}
		return instance;
	}

	private MessageService() {
	}

	/**
	 * Return all messages stored for a given user.
	 * @param user the user for which to fetch stored messages
	 * @return List of all messages locally stored for that user
	 */
	public List<DecryptedMessage> getMessages(final Contact user) {
		// TODO this is debug code
		final DecryptedMessage one = new DecryptedMessage(System.currentTimeMillis() - 300000, false, "Hallo Du",
				"+491234", "", true);
		final DecryptedMessage two = new DecryptedMessage(System.currentTimeMillis() - 100000, false,
				"Hallo " + user.getDisplayName(), "+491234", "", false);
		final DecryptedMessage three = new DecryptedMessage(System.currentTimeMillis() - 100000, false,
				translate("databaseError"), "+491234", "", false);

		return Arrays.asList(one, two, three);
	}

}
