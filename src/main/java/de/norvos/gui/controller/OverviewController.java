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
package de.norvos.gui.controller;

import static de.norvos.i18n.Translations.translate;

import java.util.Optional;

import de.norvos.contacts.Contact;
import de.norvos.eventbus.EventBus;
import de.norvos.eventbus.events.ApplicationQuitEvent;
import de.norvos.gui.components.MessageList;
import de.norvos.utils.Constants;
import edu.stanford.ejalbert.BrowserLauncher;
import edu.stanford.ejalbert.exception.BrowserLaunchingInitializingException;
import edu.stanford.ejalbert.exception.UnsupportedOperatingSystemException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

/**
 * @author Connor Lanigan
 */
public class OverviewController {

	private static OverviewController instance;

	synchronized public static OverviewController getInstance() {
		return instance;
	}

	@FXML
	private VBox contactList;

	@FXML
	private BorderPane contentPane;
	@FXML
	private TextArea messageInput;
	@FXML
	private Button quitButton;

	@FXML
	private Button searchClearButton;

	@FXML
	private TextField searchInput;

	public OverviewController() {
		instance = this;
	}

	public void clearSearchBar(final ActionEvent event) {
		searchInput.setText("");
		searchClearButton.setDisable(true);
		searchClearButton.setManaged(false);
		searchInput.requestFocus();
		// reset search result
	}

	public void handleHelpButton(final ActionEvent event) {
		BrowserLauncher launcher;
		try {
			launcher = new BrowserLauncher();
			launcher.openURLinBrowser(Constants.HELP_URL);
		} catch (BrowserLaunchingInitializingException | UnsupportedOperatingSystemException e1) {
			Platform.runLater(() -> {
				final Alert alert = new Alert(AlertType.INFORMATION);
				alert.initStyle(StageStyle.UTILITY);
				alert.setTitle(translate("get_help_title"));
				alert.setHeaderText(translate("get_help_header_text"));
				alert.setContentText(translate("supportMessage", "\n" + Constants.HELP_URL));

				alert.show();
			});
		}

	}

	public void handleQuitButton(final ActionEvent event) {
		Platform.runLater(() -> {
			final Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UTILITY);
			alert.setTitle(translate("quit_title",Constants.APPLICATON_NAME));
			alert.setHeaderText(translate("quit_header", Constants.APPLICATON_NAME));
			alert.setContentText(translate("quit_warning_message"));

			final Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				EventBus.sendEvent(new ApplicationQuitEvent());
				Platform.exit();
			}
		});
	}

	@FXML
	public void initialize() {
		searchClearButton.setManaged(false);
	}

	public void loadChat(final Contact contact) {
		final MessageList messageList = new MessageList();
		messageList.setUser(contact.getPhoneNumber());
		contentPane.setCenter(messageList);
	}

	public void searchInputKeyReleased(final KeyEvent event) {
		if (searchInput.getLength() > 0) {
			searchClearButton.setDisable(false);
			searchClearButton.setManaged(true);
		} else {
			searchClearButton.setDisable(true);
			searchClearButton.setManaged(false);
		}
		// TODO start search
	}
}
