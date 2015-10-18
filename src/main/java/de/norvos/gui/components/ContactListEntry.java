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
package de.norvos.gui.components;

import static de.norvos.i18n.Translations.translate;

import java.io.IOException;
import java.net.URL;

import de.norvos.contacts.Contact;
import de.norvos.contacts.ContactService;
import de.norvos.eventbus.Event;
import de.norvos.eventbus.EventBus;
import de.norvos.eventbus.EventBusListener;
import de.norvos.eventbus.events.MessageReceivedEvent;
import de.norvos.eventbus.events.MessageSentEvent;
import de.norvos.gui.controller.OverviewController;
import de.norvos.messages.DecryptedMessage;
import de.norvos.messages.MessageService;
import de.norvos.utils.Constants;
import de.norvos.utils.ResourceUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * An entry in the GUI representing a contact in the contact list.
 *
 * @author Connor Lanigan
 */
public class ContactListEntry extends Button implements EventBusListener {

	private Contact contact;

	@FXML
	private Text contactName;

	@FXML
	private Label lastMessage;

	@FXML
	private Circle newMessageIndicator;

	private boolean sent;

	public ContactListEntry() {
		final FXMLLoader fxmlLoader = new FXMLLoader();

		final URL fxml = getClass().getResource(Constants.FXML_LOCATION + "ContactListEntry.fxml");
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.setResources(ResourceUtils.getLocalizedStringsBundle());

		try {
			fxmlLoader.load(fxml.openStream());
			EventBus.register(MessageReceivedEvent.class, this);
			EventBus.register(MessageSentEvent.class, this);
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public String getLastMessage() {
		return lastMessage.getText();
	}

	public String getNewMessage() {
		return String.valueOf(newMessageIndicator.isVisible());
	}

	public String getUser() {
		return contact.getPhoneNumber();
	}

	public String getDisplayName() {
		return contactName.getText();
	}

	public void handleClick(final ActionEvent event) {
		setNewMessage("false");
		OverviewController.getInstance().loadChat(contact);
	}

	public void setSent(boolean sent) {
		this.sent = sent;
	}

	@FXML
	public void initialize() {
		newMessageIndicator.managedProperty().bind(newMessageIndicator.visibleProperty());
	}

	private void loadLatestMessage(){
		DecryptedMessage lastMessage = MessageService.getInstance().getLastMessage(contact);
		if (lastMessage == null) {
			clearLastMessage();
		} else {
			setSent(lastMessage.isSent());
			setLastMessage(lastMessage.getBody());
		}
	}

	public void clearLastMessage() {
		lastMessage.setText("");
		lastMessage.getStyleClass().add("lastMessageEmpty");
		lastMessage.setText(translate("no_messages_yet_preview"));
	}

	public void setLastMessage(final String value) {
		String prefix = sent ? "↗ " : "↘ ";
		lastMessage.getStyleClass().remove("lastMessageEmpty");
		lastMessage.setText(prefix + value);
	}

	public void setNewMessage(final String value) {
		final boolean newMessage = Boolean.valueOf(value);
		newMessageIndicator.setVisible(newMessage);
		if (newMessage) {
			getStyleClass().add("newMessage");
		} else {
			getStyleClass().remove("newMessage");
		}
	}

	public void setUser(final String value) {
		contact = ContactService.getInstance().getByNumber(value);
		contactName.setText(contact.getDisplayName());
		loadLatestMessage();
	}

	public void test(final String name) {
		contactName.setText(name);
	}

	@Override
	public void update(final Event event) {
		if (event instanceof MessageReceivedEvent) {
			if (contact.equals(((MessageReceivedEvent) event).getMessage().getContact())) {
				setNewMessage("true");
				setSent(false);
				setLastMessage(((MessageSentEvent) event).getMessage());
			}
		} else if (event instanceof MessageSentEvent) {
			if (contact.equals(((MessageSentEvent) event).getContact())) {
				setNewMessage("false");
				setSent(true);
				setLastMessage(((MessageSentEvent) event).getMessage());
			}
		}
	}
}
