package de.norvos.gui.controller;

import java.util.Optional;

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

public class OverviewController extends Controller {

	private static OverviewController instance;

	@FXML
	private TextArea messageInput;
	@FXML
	private VBox contactList;

	@FXML
	private Button quitButton;
	@FXML
	private TextField searchInput;
	@FXML
	private Button searchClearButton;

	public OverviewController() {
		instance = this;
	}

	public static OverviewController getInstance() {
		return instance;
	}

	@FXML
    public void initialize(){
		searchClearButton.setManaged(false);
    }

	public void clearSearchBar(ActionEvent event) {
		searchInput.setText("");
		searchClearButton.setDisable(true);
		searchClearButton.setManaged(false);
		searchInput.requestFocus();
		// reset search result
	}

	public void searchInputKeyReleased(KeyEvent event) {
		if (searchInput.getLength() > 0) {
			searchClearButton.setDisable(false);
			searchClearButton.setManaged(true);
		} else {
			searchClearButton.setDisable(true);
			searchClearButton.setManaged(false);
		}
		// start search
	}

	public void handleQuitButton(ActionEvent event) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.initStyle(StageStyle.UTILITY);
			alert.setTitle("Quit " + Constants.APPLICATON_NAME);
			alert.setHeaderText("Quit " + Constants.APPLICATON_NAME + "?");
			alert.setContentText("You will no longer receive messages.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				Platform.exit();
			}
		});
	}
}
