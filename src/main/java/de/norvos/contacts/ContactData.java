package de.norvos.contacts;

/**
 * Contains the data of a contact.
 * @author Connor Lanigan
 */
public class ContactData {
	private String displayName;
	private String draftMessage;

	private final String phoneNumber;

	/**
	 * @param phoneNumber the contact's phone number
	 * @param displayName the contact's display (human readable) name
	 * @param draftMessage the message stored as draft
	 */
	public ContactData(final String phoneNumber, final String displayName, final String draftMessage) {
		this.phoneNumber = phoneNumber;
		this.displayName = displayName;
		this.draftMessage = draftMessage;
	}

	String getDisplayName() {
		return displayName;
	}

	String getDraftMessage() {
		return draftMessage;
	}

	String getPhoneNumber() {
		return phoneNumber;
	}

	void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	void setDraftMessage(final String draftMessage) {
		this.draftMessage = draftMessage;
	}
}
