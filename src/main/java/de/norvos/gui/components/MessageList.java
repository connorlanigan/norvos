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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.norvos.contacts.Contact;
import de.norvos.contacts.ContactService;
import de.norvos.eventbus.Event;
import de.norvos.eventbus.EventBusListener;
import de.norvos.eventbus.events.MessageReceivedEvent;
import de.norvos.eventbus.events.MessageSentEvent;
import de.norvos.messages.DecryptedMessage;
import de.norvos.messages.MessageService;
import de.norvos.utils.Constants;
import de.norvos.utils.ResourceUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.input.KeyCode;

/**
 * The GUI area containg the messages for a contact.
 *
 * @author Connor Lanigan
 */
public class MessageList extends BorderPane implements EventBusListener {

	final static Logger LOGGER = LoggerFactory.getLogger(MessageList.class);
	private static MessageList activeInstance;

	public static MessageList getActiveInstance() {
		return activeInstance;
	}

	private Contact contact;
	@FXML
	private TextArea messageInput;
	@FXML
	private VBox messageList;

	@FXML
	private Text usernameDisplay;

	@FXML
	private CheckBox verified;

	private static final String verifiedColor = "#06f50a";
	private static final String unverifiedColor = "#FF0000";

	private boolean shiftHeld;

	public MessageList() {
		shiftHeld = false;
		final FXMLLoader fxmlLoader = new FXMLLoader();

		final URL fxml = getClass().getResource(Constants.FXML_LOCATION + "MessageList.fxml");
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.setResources(ResourceUtils.getLocalizedStringsBundle());

		try {
			fxmlLoader.load(fxml.openStream());
			activeInstance = this;
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	private void addMessage(final String message, final long timestamp, final File attachment, final boolean sent) {
		final SingleMessage singleMessage = new SingleMessage();
		singleMessage.setMessage(message);
		singleMessage.setSent(String.valueOf(sent));
		singleMessage.setTime(timestamp);
		messageList.getChildren().add(singleMessage);

	}

	public Contact getContact() {
		return contact;
	}

	public String getUser() {
		return contact.getPhoneNumber();
	}

	public void initialize() {
		// TODO get verified status
		setVerified(false);
	}

	public void keyReleased(final KeyEvent event) {
		if(event.getCode() == KeyCode.SHIFT) {
			LOGGER.debug("shift released");
			shiftHeld = false;
		} else
		contact.setDraftMessage(messageInput.getText());
	}

	public void keyPressed(final KeyEvent event) {
		if(event.getCode() == KeyCode.SHIFT) {
			LOGGER.debug("shift held");
			shiftHeld = true;
		} else if(event.getCode() == KeyCode.ENTER) {
			if(shiftHeld) {
				messageInput.setText(messageInput.getText() + "\n");
				messageInput.positionCaret(messageInput.getText().length());
				LOGGER.debug("New text length: {}", messageInput.getText().length()-1);
			} else {
				sendMessage();
				messageInput.setText("");
			}
			contact.setDraftMessage(messageInput.getText());
		}


	}

	public void setUser(final Contact user) {
		contact = user;
		usernameDisplay.setText(contact.getDisplayName());
		messageInput.setText(contact.getDraftMessage());
		final List<DecryptedMessage> list = MessageService.getInstance().getMessages(contact);
		for (final DecryptedMessage message : list) {
			addMessage(message.getBody(), message.getTimestamp(), message.getAttachment(), message.isSent());
		}
	}

	public void setUser(final String user) {
		setUser(ContactService.getInstance().getByNumber(user));
	}

	private void setVerified(final boolean value) {
		verified.setSelected(value);

		// TODO change color
		if (value) {
			verified.setText(translate("verified_label"));
			verified.setTextFill(Paint.valueOf(verifiedColor));
		} else {
			verified.setText(translate("not_verified_label"));
			verified.setTextFill(Paint.valueOf(unverifiedColor));
		}
	}

	@Override
	public void update(final Event event) {
		if (event instanceof MessageSentEvent) {
			final MessageSentEvent messageSentEvent = (MessageSentEvent) event;
			if (getContact().equals(messageSentEvent.getContact())) {
				addMessage(messageSentEvent.getMessage(), messageSentEvent.getTimestamp(),
						messageSentEvent.getAttachment(), true);
			}
		} else if (event instanceof MessageReceivedEvent) {
			final MessageReceivedEvent messageReceivedEvent = (MessageReceivedEvent) event;
			final DecryptedMessage message = messageReceivedEvent.getMessage();
			if (getContact().equals(message.getContact())) {
				addMessage(message.getBody(), message.getTimestamp(), message.getAttachment(), false);
			}
		}
	}

	public void sendMessage() {
		MessageService.getInstance().sendMessage(ContactService.getInstance().getByNumber("+491788174362"), messageInput.getText().trim());
	}
}
