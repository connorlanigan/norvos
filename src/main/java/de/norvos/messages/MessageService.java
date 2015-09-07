package de.norvos.messages;

import static de.norvos.i18n.Translations.T;

import java.util.Arrays;
import java.util.List;

import de.norvos.contacts.Contact;

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

	public List<DecryptedMessage> getMessages(final Contact user) {
		//TODO this is debug code
		final DecryptedMessage one = new DecryptedMessage(System.currentTimeMillis() - 300000, false, "Hallo Du",
				"+491234", "", true);
		final DecryptedMessage two = new DecryptedMessage(System.currentTimeMillis() - 100000, false,
				"Hallo " + user.getDisplayName(), "+491234", "", false);
		final DecryptedMessage three = new DecryptedMessage(System.currentTimeMillis() - 100000, false,
				T("databaseError"), "+491234", "", false);

		return Arrays.asList(one, two, three);
	}

}
