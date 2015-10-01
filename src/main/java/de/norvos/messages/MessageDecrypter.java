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

import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.whispersystems.libaxolotl.DuplicateMessageException;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.InvalidMessageException;
import org.whispersystems.libaxolotl.InvalidVersionException;
import org.whispersystems.libaxolotl.LegacyMessageException;
import org.whispersystems.libaxolotl.NoSessionException;
import org.whispersystems.libaxolotl.UntrustedIdentityException;
import org.whispersystems.textsecure.api.crypto.TextSecureCipher;
import org.whispersystems.textsecure.api.messages.TextSecureContent;
import org.whispersystems.textsecure.api.messages.TextSecureDataMessage;
import org.whispersystems.textsecure.api.messages.TextSecureEnvelope;
import org.whispersystems.textsecure.api.messages.multidevice.TextSecureSyncMessage;

import de.norvos.axolotl.AxolotlStore;
import de.norvos.eventbus.EventBus;
import de.norvos.eventbus.events.MessageReceivedEvent;
import javafx.concurrent.Task;

/**
 * Provides methods for decrypting received messages.
 *
 * @author Connor Lanigan
 */
public class MessageDecrypter {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageDecrypter.class);
	private static BlockingQueue<TextSecureEnvelope> queue;
	private static Task<Void> task;
	private static Thread thread;

	private static void handleEndSessionMessage(final TextSecureEnvelope envelope,
			final TextSecureDataMessage message) {
		LOGGER.warn("End-session-message received. Will be ignored.");
	}

	private static void handleGroupMessage(final TextSecureEnvelope envelope, final TextSecureDataMessage message) {
		LOGGER.warn("Group message received. Will be ignored.");
	}

	protected static void handleInvalidMessage(final TextSecureEnvelope envelope) {
		LOGGER.warn("Invalid message received. Will be ignored.");
	}

	private static void handleMediaMessage(final TextSecureEnvelope envelope, final TextSecureDataMessage message) {
		LOGGER.warn("Media message received. Will be ignored.");
	}

	private static void handleSyncMessage(final TextSecureSyncMessage syncMessage) {
		LOGGER.warn("Synchronization message received. Will be ignored.");
	}

	private static void handleTextMessage(final TextSecureEnvelope envelope, final TextSecureDataMessage message) {
		final DecryptedMessage decryptedMessage = new DecryptedMessage(System.currentTimeMillis(), false,
				message.getBody().get(), envelope.getSource(), "", false);
		MessagePersistenceService.getInstance().storeMessage(decryptedMessage);
		EventBus.sendEvent(new MessageReceivedEvent(decryptedMessage));
	}

	protected static void handleUntrustedMessage(final TextSecureEnvelope envelope) {
		LOGGER.warn("Untrusted message received. Will be ignored.");
	}

	/**
	 * Adds an encrypted message to the queue. It will be automatically
	 * decrypted.
	 *
	 * @param envelope
	 *            the encrypted message envelope
	 */
	public static void pushEncryptedMessage(final TextSecureEnvelope envelope) {
		while (true) {
			try {
				queue.put(envelope);
				return;
			} catch (final InterruptedException e) {
			}
		}
	}

	/**
	 * Starts the Decrypter. It will automatically decrypt messages from the
	 * queue and fire a {@link MessageReceivedEvent} after decrypting each one.
	 */
	synchronized public static void start() {
		if (thread != null && thread.isAlive()) {
			throw new IllegalStateException("MessageDecrypter is already running!");
		}

		task = new Task<Void>() {
			@Override
			protected Void call() {
				while (!isCancelled()) {
					TextSecureEnvelope envelope = null;
					try {
						envelope = queue.take();
						final TextSecureCipher cipher = new TextSecureCipher(envelope.getSourceAddress(),
								AxolotlStore.getInstance());
						final TextSecureContent content = cipher.decrypt(envelope);

						if (content.getDataMessage().isPresent()) {
							final TextSecureDataMessage message = content.getDataMessage().get();

							if (message.isEndSession()) {
								handleEndSessionMessage(envelope, message);
							} else if (message.isGroupUpdate()) {
								handleGroupMessage(envelope, message);
							} else if (message.getAttachments().isPresent()) {
								handleMediaMessage(envelope, message);
							} else {
								handleTextMessage(envelope, message);
							}
						} else {
							handleSyncMessage(content.getSyncMessage().get());
						}
					} catch (final InterruptedException e) {
						continue;
					} catch (InvalidVersionException | InvalidMessageException | InvalidKeyException
							| DuplicateMessageException | InvalidKeyIdException | NoSessionException
							| LegacyMessageException e) {
						handleInvalidMessage(envelope);
					} catch (final UntrustedIdentityException e) {
						handleUntrustedMessage(envelope);
					}
				}
				return null;
			}

		};
		thread = new Thread(task);
		thread.start();
	}

	/**
	 * Stops the Decrypter.
	 */
	synchronized public static void stop() {
		if (task != null) {
			task.cancel();
		}
	}
}
