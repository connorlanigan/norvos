package de.norvos.contacts;

import de.norvos.persistence.tables.Contacts;

/**
 * Represents a contact.
 *
 * @author Connor Lanigan
 */
public class Contact {
	private final String phoneNumber;

	/**
	 * Creates a contact identified by this phone number. The phone number is
	 * not checked for validity.
	 *
	 * @param phoneNumber
	 *            the contact's phone number
	 */
	public Contact(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Returns the name of the contact that should be displayed for interaction
	 * with the user.
	 *
	 * @return the contact's human readable name
	 */
	public String getDisplayName() {
		return Contacts.getInstance().getContactData(phoneNumber).getDisplayName();
	}

	/**
	 * Returns the stored draft message (a message that was typed but not yet sent) for this contact.
	 * @return the stored draft message
	 */
	public String getDraftMessage() {
		return Contacts.getInstance().getContactData(phoneNumber).getDraftMessage();
	}

	/**
	 * Returns the phone number of this contact.
	 * @return the contact's phone number
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Store a message as the contact's draft message.
	 * @param draftMessage the message to store
	 */
	synchronized public void setDraftMessage(final String draftMessage) {
		final ContactData contactData = Contacts.getInstance().getContactData(phoneNumber);
		contactData.setDraftMessage(draftMessage);
		Contacts.getInstance().storeContactData(contactData);
	}

}
