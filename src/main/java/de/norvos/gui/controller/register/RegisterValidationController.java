package de.norvos.gui.controller.register;

import java.io.IOException;

import org.whispersystems.textsecure.api.push.exceptions.AuthorizationFailedException;
import org.whispersystems.textsecure.api.push.exceptions.RateLimitException;

import de.norvos.account.Registrator;
import de.norvos.gui.controller.Controller;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class RegisterValidationController {

	@FXML
	private Button button;

	@FXML
	private Text errorAuthorization;

	@FXML
	private Text errorEmptyCode;

	@FXML
	private Text errorRateLimit;

	@FXML
	private ProgressIndicator spinningWheel;

	@FXML
	private TextField verificationCodeInput;

	public void handleButtonClicked(final ActionEvent event) {
		button.setVisible(false);
		spinningWheel.setVisible(true);
		errorRateLimit.setVisible(false);
		errorAuthorization.setVisible(false);
		errorEmptyCode.setVisible(false);
		final Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				try {
					try {
						Thread.sleep(500);
					} catch (final InterruptedException e) {
					}

					final String verificationCode = verificationCodeInput.getText();
					if (verificationCode.matches(".*\\w.*")) {

						Registrator.verify(verificationCode);

						final RegisterController controller = (RegisterController) Controller.getInstance();
						Platform.runLater(() -> controller.loadFXML("register/RegisterComplete.fxml"));

					} else {
						errorEmptyCode.setVisible(true);

						button.setVisible(true);
						spinningWheel.setVisible(false);
					}

				} catch (final IOException exception) {
					if (exception instanceof RateLimitException) {
						errorRateLimit.setVisible(true);
					} else if (exception instanceof AuthorizationFailedException) {
						errorAuthorization.setVisible(true);
					} else {
						System.err.println("other exception: " + exception.getClass().toString());
					}

					button.setVisible(true);
					spinningWheel.setVisible(false);
				}

				return null;
			}
		};
		new Thread(task).start();
	}
}
