package de.norvos.contacts;

public class ContactData {
	private String displayName;
	private String draftMessage;

	private final String phoneNumber;

	public ContactData(final String phoneNumber, final String displayName, final String draftMessage) {
		this.phoneNumber = phoneNumber;
		this.displayName = displayName;
		this.draftMessage = draftMessage;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDraftMessage() {
		return draftMessage;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	public void setDraftMessage(final String draftMessage) {
		this.draftMessage = draftMessage;
	}
}
