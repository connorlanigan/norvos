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

import java.io.IOException;

import de.norvos.eventbus.Event;
import de.norvos.eventbus.EventBus;
import de.norvos.eventbus.EventBusListener;
import de.norvos.eventbus.events.MessageReceivedEvent;
import de.norvos.eventbus.events.MessageSentEvent;
import de.norvos.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class ContactListEntry extends Button implements EventBusListener {

	@FXML
	private Text contactName;

	@FXML
	private Label lastMessage;

	@FXML
	private Circle newMessageIndicator;

	public ContactListEntry() {
		final FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource(Constants.FXML_LOCATION + "ContactListEntry.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
			EventBus.register(MessageReceivedEvent.class, this);
			EventBus.register(MessageSentEvent.class, this);
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public String getLastMessage() {
		return lastMessage.getText();
	}

	public String getName() {
		return contactName.getText();
	}

	public String getNewMessage() {
		return String.valueOf(newMessageIndicator.isVisible());
	}

	@FXML
	public void initialize() {
		newMessageIndicator.managedProperty().bind(newMessageIndicator.visibleProperty());
	}

	public void setLastMessage(final String value) {
		lastMessage.setText(value);
	}

	public void setName(final String value) {
		contactName.setText(value);
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

	@Override
	public void update(final Event event) {
		// TODO Auto-generated method stub
		if (event instanceof MessageReceivedEvent) {

		} else if (event instanceof MessageSentEvent) {
		}

	}

}
