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
import java.util.List;

import de.norvos.contacts.Contact;
import de.norvos.contacts.ContactService;
import de.norvos.messages.DecryptedMessage;
import de.norvos.messages.MessagePersistenceService;
import de.norvos.utils.Constants;
import de.norvos.utils.ResourceUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The GUI area containg the messages for a contact.
 *
 * @author Connor Lanigan
 */
public class MessageList extends BorderPane {

	private Contact contact;
	@FXML
	private TextArea messageInput;

	@FXML
	private VBox messageList;
	private String user;
	@FXML
	private Text usernameDisplay;
	@FXML
	private CheckBox verified;

	public MessageList() {
		final FXMLLoader fxmlLoader = new FXMLLoader();

		final URL fxml = getClass().getResource(Constants.FXML_LOCATION + "MessageList.fxml");
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.setResources(ResourceUtils.getLocalizedStringsBundle());

		try {
			fxmlLoader.load(fxml.openStream());
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public String getUser() {
		return user;
	}

	public void initialize() {
		// TODO get verified status
		setVerified(false);
	}

	public void keyReleased(final KeyEvent event) {
		contact.setDraftMessage(messageInput.getText());
	}

	public void setUser(final String user) {
		this.user = user;
		contact = ContactService.getInstance().getByNumber(user);
		usernameDisplay.setText(contact.getDisplayName());
		messageInput.setText(contact.getDraftMessage());
		final List<DecryptedMessage> list = MessagePersistenceService.getInstance().getMessages(contact);
		for (final DecryptedMessage message : list) {
			final SingleMessage singleMessage = new SingleMessage();
			singleMessage.setMessage(message.getBody());
			singleMessage.setSent(String.valueOf(message.isSent()));
			singleMessage.setTime(message.getTimestamp());
			messageList.getChildren().add(singleMessage);
		}
	}

	private void setVerified(final boolean value) {
		verified.setSelected(value);

		// TODO change color
		if (value) {
			verified.setText(translate("verified_label"));
		} else {
			verified.setText(translate("not_verified_label"));
		}
	}

}
