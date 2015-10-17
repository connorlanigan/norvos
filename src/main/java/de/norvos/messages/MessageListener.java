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

import java.util.concurrent.TimeUnit;

import org.whispersystems.textsecure.api.TextSecureMessagePipe;
import org.whispersystems.textsecure.api.TextSecureMessageReceiver;
import org.whispersystems.textsecure.api.messages.TextSecureEnvelope;

import de.norvos.account.SettingsService;
import de.norvos.axolotl.TrustStore;
import de.norvos.utils.Constants;
import javafx.concurrent.Task;

/**
 * Listens for messages from the server and sends them for decryption to the
 * {@link MessageDecrypter}.
 *
 * @author Connor Lanigan
 */
class MessageListener extends Task<Void> {
	static private MessageListener instance;
	final private String password;
	final private String signalingKey;
	final private TrustStore trustStore;
	final private String url;

	final private String username;

	public MessageListener() {
		url = SettingsService.getURL();
		username = SettingsService.getUsername();
		trustStore = TrustStore.getInstance();
		password = SettingsService.getPassword();
		signalingKey = SettingsService.getSignalingKey();
	}

	@Override
	protected Void call() {
		if (instance != null && instance != this) {
			throw new RuntimeException("Can't run two MessageListeners at the same time.");
		}
		instance = this;
		final TextSecureMessageReceiver messageReceiver = new TextSecureMessageReceiver(url, trustStore, username,
				password, signalingKey, Constants.USER_AGENT);
		final TextSecureMessagePipe messagePipe = messageReceiver.createMessagePipe();

		while (!isCancelled()) {
			try {
				final TextSecureEnvelope envelope = messagePipe.read(30, TimeUnit.SECONDS);
				MessageDecrypter.pushEncryptedMessage(envelope);
			} catch (final Exception e) {
			}
		}

		return null;
	}
}
