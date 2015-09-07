package de.norvos.contacts;

import de.norvos.persistence.tables.Contacts;

public class Contact {
	private final String phoneNumber;

	public Contact(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getDisplayName() {
		return Contacts.getInstance().getContactData(phoneNumber).getDisplayName();
	}

	public String getDraftMessage() {
		return Contacts.getInstance().getContactData(phoneNumber).getDraftMessage();
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setDraftMessage(final String draftMessage) {
		final ContactData contactData = Contacts.getInstance().getContactData(phoneNumber);
		contactData.setDraftMessage(draftMessage);
		Contacts.getInstance().storeContactData(contactData);
	}

}
