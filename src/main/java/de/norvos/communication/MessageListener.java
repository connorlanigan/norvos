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

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.whispersystems.libaxolotl.DuplicateMessageException;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.InvalidMessageException;
import org.whispersystems.libaxolotl.InvalidVersionException;
import org.whispersystems.libaxolotl.LegacyMessageException;
import org.whispersystems.libaxolotl.NoSessionException;
import org.whispersystems.libaxolotl.UntrustedIdentityException;
import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.TextSecureMessagePipe;
import org.whispersystems.textsecure.api.TextSecureMessageReceiver;
import org.whispersystems.textsecure.api.crypto.TextSecureCipher;
import org.whispersystems.textsecure.api.messages.TextSecureContent;
import org.whispersystems.textsecure.api.messages.TextSecureDataMessage;
import org.whispersystems.textsecure.api.messages.TextSecureEnvelope;

import de.norvos.account.AccountDataStore;
import de.norvos.axolotl.AxolotlStore;
import de.norvos.axolotl.TrustStore;
import de.norvos.log.Errors;
import javafx.concurrent.Task;

public class MessageListener extends Task<Void> {
	final private String password;
	final private String signalingKey;
	final private TrustStore trustStore;
	final private String url;
	final private String username;

	public MessageListener() {
		url = AccountDataStore.getStringValue("url");
		username = AccountDataStore.getStringValue("username");
		trustStore = TrustStore.getInstance();
		password = AccountDataStore.getStringValue("password");
		signalingKey = AccountDataStore.getStringValue("signalingKey");
	}

	@Override
	protected Void call() {
		final TextSecureMessageReceiver messageReceiver = new TextSecureMessageReceiver(url, trustStore, username,
				password, signalingKey);
		final TextSecureMessagePipe messagePipe = messageReceiver.createMessagePipe();

		while (!isCancelled()) {
			final TextSecureEnvelope envelope;
			try {
				envelope = messagePipe.read(30, TimeUnit.SECONDS);
			} catch (final Exception e) {
				continue;
			}
			final TextSecureCipher cipher = new TextSecureCipher(envelope.getSourceAddress(),
					AxolotlStore.getInstance());
			TextSecureContent content;
			try {
				content = cipher.decrypt(envelope);
				content.getDataMessage();

			} catch (InvalidVersionException | InvalidMessageException | InvalidKeyException | DuplicateMessageException
					| InvalidKeyIdException | LegacyMessageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final UntrustedIdentityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final NoSessionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return null;
	}

	@Override
	public void run() {
		// This part needs restructuring.
		/*
		Errors.debug("##### Listening for messages!");

		final String url = AccountDataStore.getStringValue("url");
		final String username = AccountDataStore.getStringValue("username");
		final TrustStore trustStore = TrustStore.getInstance();
		final String password = AccountDataStore.getStringValue("password");
		final String signalingKey = AccountDataStore.getStringValue("signalingKey");

		final TextSecureMessageReceiver messageReceiver = new TextSecureMessageReceiver(url, trustStore, username,
				password, signalingKey);

		TextSecureMessagePipe messagePipe = null;

		try {
			messagePipe = messageReceiver.createMessagePipe();

			while (listeningForMessages) {
				final TextSecureEnvelope envelope = messagePipe.read(1000, TimeUnit.DAYS);
				final TextSecureCipher cipher = new TextSecureCipher(envelope.getSourceAddress(),
						AxolotlStore.getInstance());
				final TextSecureContent content = cipher.decrypt(envelope);

				final Optional<TextSecureDataMessage> dataMessage = content.getDataMessage();

				System.out.println("####### Received message!!");

				// We currently drop the synchronization messages, because they
				// seem not to be officially published as of now.
				//
				// Optional<TextSecureSyncMessage> syncMessage =
				// content.getSyncMessage();
				//

				if (dataMessage.isPresent()) {
					final TextSecureDataMessage message = dataMessage.get();
					final String sender = envelope.getSource();
					if (message.isEndSession()) {
						System.out.println("Session ended by " + sender + ".");
					} else {
						System.out.println("Received message: " + message.getBody().get());
					}
				}

			}

		} catch (InvalidVersionException | IOException | TimeoutException | InvalidMessageException
				| InvalidKeyException | DuplicateMessageException | InvalidKeyIdException | UntrustedIdentityException
				| LegacyMessageException | NoSessionException e) {
			// TODO Reorganize try into the while-loop and handles message
			// errors in a sane way
			// callback.messageListeningException(e);
		} finally {
			if (messagePipe != null) {
				messagePipe.shutdown();
			}
		} */

	}

}
