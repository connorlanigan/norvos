package de.norvos.communication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.whispersystems.libaxolotl.util.guava.Optional;
import org.whispersystems.textsecure.api.TextSecureMessageSender;
import org.whispersystems.textsecure.api.crypto.UntrustedIdentityException;
import org.whispersystems.textsecure.api.messages.TextSecureAttachment;
import org.whispersystems.textsecure.api.messages.TextSecureMessage;
import org.whispersystems.textsecure.api.push.TextSecureAddress;

import de.norvos.account.ServerAccount;
import de.norvos.account.Settings;

public class Communicator {

	public void sendTextMessage(String recipientId, String message) throws UntrustedIdentityException, IOException {
		
		getMessageSender().sendMessage(new TextSecureAddress(recipientId),
				TextSecureMessage.newBuilder().withBody(message).build());
	}

	public void sendMediaMessage(String recipientId, String message, File attachment) throws UntrustedIdentityException,
			IOException {

		
		getMessageSender().sendMessage(new TextSecureAddress(recipientId),
				TextSecureMessage.newBuilder().withBody(message).withAttachment(createAttachment(attachment)).build());
	}

	private TextSecureMessageSender getMessageSender() {
		ServerAccount serverConfiguration = Settings.getCurrent().getServerConfiguration();
		return new TextSecureMessageSender(serverConfiguration.getURL(), serverConfiguration.getTrustStore(),
				serverConfiguration.getUsername(), serverConfiguration.getPassword(), Settings.getCurrent().getAxolotlStore(),
				Optional.absent());
	}
	
	private TextSecureAttachment createAttachment(File attachmentFile) throws FileNotFoundException{
		FileInputStream attachmentStream = new FileInputStream(attachmentFile);
		return TextSecureAttachment.newStreamBuilder().withStream(attachmentStream).withContentType(getMimeType(attachmentFile))
				.withLength(attachmentFile.length()).build();
	}
	
	private String getMimeType(File file){
		// TODO use mime-type library
		return null;
	}

}
