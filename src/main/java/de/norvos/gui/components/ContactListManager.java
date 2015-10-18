package de.norvos.gui.components;

import org.apache.commons.lang3.NotImplementedException;

import de.norvos.contacts.Contact;
import de.norvos.eventbus.events.MessageSentEvent;

public class ContactListManager {
	private static ContactListManager instance;

	public static ContactListManager getInstance() {
		if (instance == null) {
			instance = new ContactListManager();
		}
		return instance;
	}

	private ContactListEntry getContactListEntry(final Contact contact) {
		// TODO getContactListEntry
		throw new NotImplementedException("ContactListManager.getContactListEntry() not implemented");
	}

	public void handle(final MessageSentEvent event) {
		final ContactListEntry entry = getContactListEntry(event.getContact());
		entry.update(event);
	}
}
