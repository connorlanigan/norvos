package de.norvos.messages;

/**
 * Contains the data of a decrypted message.
 * @author Connor Lanigan
 */
public class DecryptedMessage {

	private final String address;
	private final String body;

	private final String mismatchedIdentities;

	private final boolean read;

	private final boolean sent;

	private final long timestamp;

	public DecryptedMessage(final long timestamp, final boolean read, final String body, final String address,
			final String mismatchedIdentities, final boolean sent) {
		this.read = read;
		this.body = body;
		this.address = address;
		this.mismatchedIdentities = mismatchedIdentities;
		this.timestamp = timestamp;
		this.sent = sent;
	}

	public String getAddress() {
		return address;
	}

	public String getBody() {
		return body;
	}

	public String getMismatchedIdentities() {
		return mismatchedIdentities;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public boolean isRead() {
		return read;
	}

	public boolean isSent() {
		return sent;
	}

}
