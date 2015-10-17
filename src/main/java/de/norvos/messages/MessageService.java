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
package de.norvos.messages;

import static de.norvos.i18n.Translations.translate;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.textsecure.api.crypto.UntrustedIdentityException;

import de.norvos.contacts.Contact;
import de.norvos.persistence.tables.DecryptedMessageTable;

/**
 * Provides access to stored messages.
 *
 * @author Connor Lanigan
 */
public class MessageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

	private static MessageService instance;

	synchronized public static MessageService getInstance() {
		if (instance == null) {
			instance = new MessageService();
		}
		return instance;
	}

	private MessageService() {
	}

	synchronized public void startReceiving(){
		new MessageListener().run();
		MessageDecrypter.start();
	}

	/**
	 * Return all messages stored for a given user.
	 *
	 * @param user
	 *            the user for which to fetch stored messages
	 * @return List of all messages locally stored for that user
	 */
	public List<DecryptedMessage> getMessages(final Contact user) {
		// TODO this is debug code
		final DecryptedMessage one = new DecryptedMessage(System.currentTimeMillis() - 300000, false, "Hi there!",
				"+491234", "", true);
		final DecryptedMessage two = new DecryptedMessage(System.currentTimeMillis() - 100000, false,
				"Hello " + user.getDisplayName() + "!", "+491234", "", false);
		final DecryptedMessage three = new DecryptedMessage(System.currentTimeMillis() - 100000, false,
				translate("databaseError"), "+491234", "", false);

		return Arrays.asList(one, two, three);
	}

	public void storeMessage(final DecryptedMessage message) {
		try {
			DecryptedMessageTable.getInstance().storeMessage(message);
		} catch (final SQLException e) {
			LOGGER.error("Untrusted Identity while sending message.", e);
		}
	}

	public void sendMessage(final Contact contact, final String message, final File attachment) {
		try {
			MessageSender.sendMediaMessage(contact, message, attachment);
		} catch (UntrustedIdentityException e) {
			LOGGER.error("Untrusted Identity while sending message.", e);
		} catch (IOException e) {
			LOGGER.error("IOException while sending message.", e);
		}

	}

	public void sendMessage(final Contact contact, final String message) {
		try {
			MessageSender.sendTextMessage(contact, message);
		} catch (UntrustedIdentityException e) {
			LOGGER.error("Untrusted Identity while sending message.", e);
		} catch (IOException e) {
			LOGGER.error("IOException while sending message.", e);
		}
	}

}
