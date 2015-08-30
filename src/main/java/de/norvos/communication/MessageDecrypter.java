package de.norvos.communication;

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
import javafx.concurrent.Task;

public class MessageDecrypter {

	private static BlockingQueue<TextSecureEnvelope> queue;
	private static boolean running;

	public static void addEncryptedMessage(final TextSecureEnvelope envelope) {
		while (true) {
			try {
				queue.put(envelope);
				return;
			} catch (final InterruptedException e) {
			}
		}
	}

	synchronized public static void start() {
		if (running) {
			throw new IllegalStateException("MessageDecrypter is already running!");
		}

		final Task<Void> task = new Task<Void>() {
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
							EventBus.sendEvent(new MessageReceivedEvent(envelope, dataMessage.get()));
						} else {
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
		new Thread(task).start();
		running = true;
	}

}
