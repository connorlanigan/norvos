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
	private static MessageService instance;

	private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

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
	 *
	 * @param user
	 *            the user for which to fetch stored messages
	 * @return List of all messages locally stored for that user
	 */
	public List<DecryptedMessage> getMessages(final Contact user) {
		return DecryptedMessageTable.getInstance().getMessages(user.getPhoneNumber());
	}

	public DecryptedMessage getLastMessage(final Contact user){
		return DecryptedMessageTable.getInstance().getLastMessage(user.getPhoneNumber());
	}

	public void sendMessage(final Contact contact, final String message) {
		try {
			MessageSender.sendTextMessage(contact, message);
		} catch (final UntrustedIdentityException e) {
			LOGGER.error("Untrusted Identity while sending message.", e);
		} catch (final IOException e) {
			LOGGER.error("IOException while sending message.", e);
		}
	}

	public void sendMessage(final Contact contact, final String message, final File attachment) {
		try {
			MessageSender.sendMediaMessage(contact, message, attachment);
		} catch (final UntrustedIdentityException e) {
			LOGGER.error("Untrusted Identity while sending message.", e);
		} catch (final IOException e) {
			LOGGER.error("IOException while sending message.", e);
		}

	}
	public void deleteMessage(long messageId){
		try {
			DecryptedMessageTable.getInstance().deleteMessage(messageId);
		} catch (final SQLException e) {
			LOGGER.error("Error while deleting message {} to database.", messageId);
		}
	}

	synchronized public void startReceiving() {
		new MessageListener().run();
		MessageDecrypter.start();
	}

	long storeMessage(final DecryptedMessage message) {
		try {
			return DecryptedMessageTable.getInstance().storeMessage(message);
		} catch (final SQLException e) {
			LOGGER.error("Error while saving message to database.", e);
			return 0;
		}
	}

}
