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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.TextSecureMessageSender;
import org.whispersystems.textsecure.api.crypto.UntrustedIdentityException;
import org.whispersystems.textsecure.api.messages.TextSecureAttachment;
import org.whispersystems.textsecure.api.messages.TextSecureDataMessage;

import de.norvos.account.SettingsService;
import de.norvos.axolotl.AxolotlStore;
import de.norvos.axolotl.TrustStore;
import de.norvos.contacts.Contact;
import de.norvos.eventbus.EventBus;
import de.norvos.eventbus.events.MessageSentEvent;

/**
 * Provides methods for sending messages to other users.
 *
 * @author Connor Lanigan
 */
public class MessageSender {
	final static Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);

	private static TextSecureAttachment createAttachment(final File attachmentFile) throws FileNotFoundException {
		final FileInputStream attachmentStream = new FileInputStream(attachmentFile);
		return TextSecureAttachment.newStreamBuilder().withStream(attachmentStream)
				.withContentType(getMimeType(attachmentFile)).withLength(attachmentFile.length()).build();
	}

	private static TextSecureMessageSender getMessageSender() {
		final String url = SettingsService.getURL();
		final String username = SettingsService.getUsername();
		final TrustStore trustStore = TrustStore.getInstance();
		final String password = SettingsService.getPassword();

		return new TextSecureMessageSender(url, trustStore, username, password, AxolotlStore.getInstance(),
				Optional.absent());
	}

	private static String getMimeType(final File file) {
		// TODO use mime-type library
		return "application/octet-stream";
	}

	public static void sendMediaMessage(final Contact contact, final String message, final File attachment)
			throws UntrustedIdentityException, IOException {

		final TextSecureDataMessage messageBody = TextSecureDataMessage.newBuilder().withBody(message)
				.withAttachment(createAttachment(attachment)).build();
		getMessageSender().sendMessage(contact.toTSAddress(), messageBody);
		EventBus.sendEvent(new MessageSentEvent(contact, message, System.currentTimeMillis(), attachment));
	}

	public static void sendTextMessage(final Contact contact, final String message)
			throws UntrustedIdentityException, IOException {
		LOGGER.debug("About to send message: [{}]", message);
		final TextSecureDataMessage messageBody = TextSecureDataMessage.newBuilder().withBody(message).build();
		getMessageSender().sendMessage(contact.toTSAddress(), messageBody);
		EventBus.sendEvent(new MessageSentEvent(contact, message, System.currentTimeMillis(), null));
	}

}
