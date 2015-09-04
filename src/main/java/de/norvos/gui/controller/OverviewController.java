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

import java.util.Optional;

import de.norvos.eventbus.EventBus;
import de.norvos.eventbus.events.ApplicationQuitEvent;
import de.norvos.utils.Constants;
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
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

public class OverviewController{

	private static OverviewController instance;

	public static OverviewController getInstance() {
		return instance;
	}

	@FXML
	private VBox contactList;

	@FXML
	private TextArea messageInput;
	@FXML
	private Button quitButton;
	@FXML
	private Button searchClearButton;

	@FXML
	private TextField searchInput;


	public void clearSearchBar(final ActionEvent event) {
		searchInput.setText("");
		searchClearButton.setDisable(true);
		searchClearButton.setManaged(false);
		searchInput.requestFocus();
		// reset search result
	}

	public void handleQuitButton(final ActionEvent event) {
		Platform.runLater(() -> {
			final Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UTILITY);
			alert.setTitle("Quit " + Constants.APPLICATON_NAME);
			alert.setHeaderText("Quit " + Constants.APPLICATON_NAME + "?");
			alert.setContentText("You will no longer receive messages.");

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

	public void searchInputKeyReleased(final KeyEvent event) {
		if (searchInput.getLength() > 0) {
			searchClearButton.setDisable(false);
			searchClearButton.setManaged(true);
		} else {
			searchClearButton.setDisable(true);
			searchClearButton.setManaged(false);
		}
		// start search
	}
}
