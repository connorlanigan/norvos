package de.norvos.messages;

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
		final DecryptedMessage one = new DecryptedMessage(System.currentTimeMillis() - 300000, false, "Hallo Du",
				"+491234", "", true);
		final DecryptedMessage two = new DecryptedMessage(System.currentTimeMillis() - 100000, false,
				"Hallo " + user.getDisplayName(), "+491234", "", false);
		return Arrays.asList(one, two);
	}

}
