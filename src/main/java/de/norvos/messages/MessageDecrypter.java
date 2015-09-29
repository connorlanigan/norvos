package de.norvos.messages;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

import org.whispersystems.libaxolotl.DuplicateMessageException;
import org.whispersystems.libaxolotl.InvalidKeyException;
import org.whispersystems.libaxolotl.InvalidKeyIdException;
import org.whispersystems.libaxolotl.InvalidMessageException;
import org.whispersystems.libaxolotl.InvalidVersionException;
import org.whispersystems.libaxolotl.LegacyMessageException;
import org.whispersystems.libaxolotl.NoSessionException;
import org.whispersystems.libaxolotl.UntrustedIdentityException;
import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.crypto.TextSecureCipher;
import org.whispersystems.textsecure.api.messages.TextSecureContent;
import org.whispersystems.textsecure.api.messages.TextSecureDataMessage;
import org.whispersystems.textsecure.api.messages.TextSecureEnvelope;

import de.norvos.axolotl.AxolotlStore;
import de.norvos.eventbus.EventBus;
import de.norvos.eventbus.events.MessageReceivedEvent;
import de.norvos.persistence.tables.DecryptedMessageTable;
import javafx.concurrent.Task;

/**
 * Provides methods for decrypting received messages.
 * @author Connor Lanigan
 */
public class MessageDecrypter {

	private static BlockingQueue<TextSecureEnvelope> queue;
	private static Task<Void> task;
	private static Thread thread;

	/**
	 * Adds an encrypted message to the queue. It will be automatically decrypted.
	 * @param envelope the encrypted message envelope
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
	 * Starts the Decrypter. It will automatically decrypt messages from the queue and fire a {@link MessageReceivedEvent} after decrypting each one.
	 */
	synchronized public static void start() {
		if (thread != null && thread.isAlive()) {
			throw new IllegalStateException("MessageDecrypter is already running!");
		}

		task = new Task<Void>() {
			@Override
			protected Void call() {
				while (!isCancelled()) {
					try {
						final TextSecureEnvelope envelope = queue.take();
						final TextSecureCipher cipher = new TextSecureCipher(envelope.getSourceAddress(),
								AxolotlStore.getInstance());
						final TextSecureContent content = cipher.decrypt(envelope);
						final Optional<TextSecureDataMessage> dataMessage = content.getDataMessage();
						if (dataMessage.isPresent()) {
							final TextSecureDataMessage dataMessageContent = dataMessage.get();
							final DecryptedMessage message = new DecryptedMessage(System.currentTimeMillis(), false,
									dataMessageContent.getBody().get(), envelope.getSource(), "", false);
							try {
								DecryptedMessageTable.getInstance().storeMessage(message);
							} catch (final SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							EventBus.sendEvent(new MessageReceivedEvent(message));
						} else {
							content.getSyncMessage();
							// TODO log synchronize-message
						}
					} catch (final InterruptedException e) {
						continue;
					} catch (InvalidVersionException | InvalidMessageException | InvalidKeyException
							| DuplicateMessageException | InvalidKeyIdException | LegacyMessageException e) {
						// TODO log this incident
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

		};
		thread = new Thread(task);
		thread.start();
	}

	/**
	 * Stops the Decrypter.
	 */
	synchronized public static void stop() {
		task.cancel();
	}

}
