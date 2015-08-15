package de.norvos.gui.controller.register;

import de.norvos.gui.controller.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class RegisterCompleteController {

	@FXML
	private Button button;

	public void handleButtonClicked(final ActionEvent event) {
		final RegisterController controller = (RegisterController) Controller.getInstance();
		Platform.runLater(() -> controller.loadFXML("register/Overview.fxml"));
	}
}
