package de.norvos.gui.controller.register;

import java.io.IOException;

import de.norvos.account.Registrator;
import de.norvos.gui.controller.Controller;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

public class RegisterRequestErrorController {

	@FXML
	private Button button;

	@FXML
	private Text errorMsg;
	@FXML
	private Text errorTitle;

	@FXML
	private ProgressIndicator spinningWheel;

	private void disableSpinningWheel() {
		spinningWheel.setVisible(false);
		errorTitle.setVisible(true);
		errorMsg.setVisible(true);
	}

	private void enableSpinningWheel() {
		spinningWheel.setVisible(true);
		errorTitle.setVisible(false);
		errorMsg.setVisible(false);
	}

	public void handleButtonClicked(final ActionEvent event) {
		button.setDisable(true);
		enableSpinningWheel();

		final Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				try {
					try {
						Thread.sleep(500);
					} catch (final InterruptedException e) {
					}
					Registrator.requestCode();
					final RegisterController controller = (RegisterController) Controller.getInstance();
					Platform.runLater(() -> {
						controller.setProgress(0.7F);
						controller.loadFXML("register/RegisterValidation.fxml");
					});
				} catch (final IOException e) {
					disableSpinningWheel();
					button.setDisable(false);
				}
				return null;
			}
		};
		new Thread(task).start();
	}

}
