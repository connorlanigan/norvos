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
package de.norvos.communication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.AbstractMap;

import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.TextSecureMessageSender;
import org.whispersystems.textsecure.api.crypto.UntrustedIdentityException;
import org.whispersystems.textsecure.api.messages.TextSecureAttachment;
import org.whispersystems.textsecure.api.messages.TextSecureDataMessage;
import org.whispersystems.textsecure.api.push.TextSecureAddress;

import de.norvos.account.AccountDataStore;
import de.norvos.axolotl.AxolotlStore;
import de.norvos.axolotl.TrustStore;
import de.norvos.observers.Notifiable;
import de.norvos.observers.NotificatorMap;
import de.norvos.observers.Observable;

public class MessageSender implements Observable {

	protected static NotificatorMap notifiables = new NotificatorMap();

	private static TextSecureAttachment createAttachment(final File attachmentFile) throws FileNotFoundException {
		final FileInputStream attachmentStream = new FileInputStream(attachmentFile);
		return TextSecureAttachment.newStreamBuilder().withStream(attachmentStream)
				.withContentType(getMimeType(attachmentFile)).withLength(attachmentFile.length()).build();
	}

	private static TextSecureMessageSender getMessageSender() {
		final String url = AccountDataStore.getStringValue("url");
		final String username = AccountDataStore.getStringValue("username");
		final TrustStore trustStore = TrustStore.getInstance();
		final String password = AccountDataStore.getStringValue("password");

		return new TextSecureMessageSender(url, trustStore, username, password, AxolotlStore.getInstance(),
				Optional.absent());
	}

	private static String getMimeType(final File file) {
		// TODO Communicator: use mime-type library
		return "application/octet-stream";
	}

	public static void sendMediaMessage(final String recipientId, final String message, final File attachment) throws UntrustedIdentityException,
	IOException {

		final TextSecureDataMessage messageBody =
				TextSecureDataMessage.newBuilder().withBody(message).withAttachment(createAttachment(attachment))
				.build();
		getMessageSender().sendMessage(new TextSecureAddress(recipientId), messageBody);
		notifiables.notify("messageSent", new AbstractMap.SimpleEntry<String, TextSecureDataMessage>(recipientId,
				messageBody));
	}

	public static void sendTextMessage(final String recipientId, final String message) throws UntrustedIdentityException,
	IOException {
		System.err.println("About to send message: " + message);
		final TextSecureDataMessage messageBody = TextSecureDataMessage.newBuilder().withBody(message).build();
		getMessageSender().sendMessage(new TextSecureAddress(recipientId), messageBody);
		notifiables.notify("messageSent", new AbstractMap.SimpleEntry<String, TextSecureDataMessage>(recipientId,
				messageBody));
	}

	@Override
	public void register(final Notifiable n, final String event) {
		notifiables.register(event, n);
	}

	@Override
	public void unregister(final Notifiable n) {
		notifiables.unregister(n);
	}

}
