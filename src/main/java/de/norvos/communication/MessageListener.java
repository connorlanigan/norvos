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

import java.util.concurrent.TimeUnit;

import org.whispersystems.textsecure.api.TextSecureMessagePipe;
import org.whispersystems.textsecure.api.TextSecureMessageReceiver;
import org.whispersystems.textsecure.api.crypto.TextSecureCipher;
import org.whispersystems.textsecure.api.messages.TextSecureDataMessage;
import org.whispersystems.textsecure.api.messages.TextSecureEnvelope;

import de.norvos.account.ServerAccount;
import de.norvos.account.Settings;
import de.norvos.axolotl.NorvosTrustStore;

public class MessageListener implements Runnable {

	private boolean listeningForMessages = true;

	@Override
	public void run() {
		ServerAccount account = Settings.getCurrent().getServerAccount();
		TextSecureMessageReceiver messageReceiver =
				new TextSecureMessageReceiver(account.getURL(),
						NorvosTrustStore.get(), account.getUsername(), account.getPassword(), account.getSignalingKey());
		
		TextSecureMessagePipe messagePipe = null;

		try {
			messagePipe = messageReceiver.createMessagePipe();

			while (listeningForMessages) {
				TextSecureEnvelope envelope = messagePipe.read(1000, TimeUnit.DAYS);
				TextSecureCipher cipher =
						new TextSecureCipher(envelope.getSourceAddress(), Settings.getCurrent().getAxolotlStore());
				TextSecureDataMessage message = cipher.decrypt(envelope);

				System.out.println("Received message: " + message.getBody().get());
			}

		} finally {
			if (messagePipe != null)
				messagePipe.shutdown();
		}

	}

}
