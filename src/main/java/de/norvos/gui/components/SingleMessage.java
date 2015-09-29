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
import java.net.URL;

import de.norvos.utils.Constants;
import de.norvos.utils.ResourceUtils;
import de.norvos.utils.TimeUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * A GUI element representing a single message.
 * 
 * @author Connor Lanigan
 */
public class SingleMessage extends BorderPane {

	private final static String baseClass = "message";

	@FXML
	private Label message;

	@FXML
	private BorderPane messageBox;

	private boolean sent;

	@FXML
	private Label time;
	private Long timestamp;

	public SingleMessage() {
		final FXMLLoader fxmlLoader = new FXMLLoader();

		final URL fxml = getClass().getResource(Constants.FXML_LOCATION + "SingleMessage.fxml");
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);
		fxmlLoader.setResources(ResourceUtils.getLocalizedStringsBundle());

		try {
			fxmlLoader.load(fxml.openStream());
			setSent(getSent());
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public String getMessage() {
		return message.getText();
	}

	public String getSent() {
		return String.valueOf(sent);
	}

	public String getTime() {
		return String.valueOf(timestamp);
	}

	public void setMessage(final String message) {
		this.message.setText(message);
	}

	public void setSent(final String sent) {
		final boolean pSent = Boolean.valueOf(sent);
		this.sent = pSent;

		String cssClass;
		if (pSent) {
			cssClass = "sentMessage";
		} else {
			cssClass = "receivedMessage";
		}
		messageBox.getStyleClass().setAll(cssClass, baseClass);
	}

	public void setTime(final long timestamp) {
		this.timestamp = timestamp;
		final String string = TimeUtils.formatDate(this.timestamp);
		time.setText(string);
	}

	public void setTime(final String timestamp) {
		this.timestamp = Long.valueOf(timestamp);
		final String string = TimeUtils.formatDate(this.timestamp);
		time.setText(string);
	}

}
