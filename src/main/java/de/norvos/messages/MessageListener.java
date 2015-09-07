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

import de.norvos.account.AccountDataStore;
import de.norvos.axolotl.TrustStore;
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
			try {
				final TextSecureEnvelope envelope = messagePipe.read(30, TimeUnit.SECONDS);
				MessageDecrypter.pushEncryptedMessage(envelope);
			} catch (final Exception e) {
			}
		}

		return null;
	}
}
