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

import de.norvos.account.ServerAccount;
import de.norvos.account.Settings;
import de.norvos.observers.Notifiable;
import de.norvos.observers.NotificatorMap;
import de.norvos.observers.Observable;

public class MessageSender implements Observable{
	

	protected static NotificatorMap notifiables = new NotificatorMap();

	public static void sendTextMessage(String recipientId, String message) throws UntrustedIdentityException, IOException {
		TextSecureDataMessage messageBody = TextSecureDataMessage.newBuilder().withBody(message).build();
		getMessageSender().sendMessage(new TextSecureAddress(recipientId),
				messageBody);
		notifiables.notify("messageSent", new AbstractMap.SimpleEntry<String, TextSecureDataMessage>(recipientId, messageBody));
	}

	public static void sendMediaMessage(String recipientId, String message, File attachment) throws UntrustedIdentityException,
			IOException {

		TextSecureDataMessage messageBody = TextSecureDataMessage.newBuilder().withBody(message).withAttachment(createAttachment(attachment)).build();
		getMessageSender().sendMessage(new TextSecureAddress(recipientId), messageBody);
		notifiables.notify("messageSent", new AbstractMap.SimpleEntry<String, TextSecureDataMessage>(recipientId, messageBody));
	}

	private static TextSecureMessageSender getMessageSender() {
		ServerAccount serverConfiguration = Settings.getCurrent().getServerAccount();
		return new TextSecureMessageSender(serverConfiguration.getURL(), serverConfiguration.getTrustStore(),
				serverConfiguration.getUsername(), serverConfiguration.getPassword(), Settings.getCurrent().getAxolotlStore(),
				Optional.absent());
	}
	
	private static TextSecureAttachment createAttachment(File attachmentFile) throws FileNotFoundException{
		FileInputStream attachmentStream = new FileInputStream(attachmentFile);
		return TextSecureAttachment.newStreamBuilder().withStream(attachmentStream).withContentType(getMimeType(attachmentFile))
				.withLength(attachmentFile.length()).build();
	}
	
	private static String getMimeType(File file){
		// TODO Communicator: use mime-type library
		return "application/octet-stream";
	}
	
	@Override
	public void register(Notifiable n, String event){
		notifiables.register(event, n);
	}
	
	@Override
	public void unregister(Notifiable n){
		notifiables.unregister(n);
	}

}
