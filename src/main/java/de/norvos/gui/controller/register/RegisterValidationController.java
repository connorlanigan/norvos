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
package de.norvos.gui.controller.register;

import org.whispersystems.textsecure.api.push.exceptions.AuthorizationFailedException;
import org.whispersystems.textsecure.api.push.exceptions.RateLimitException;

import de.norvos.account.Registrator;
import de.norvos.utils.DataManipulationUtils;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

/**
 * @author Connor Lanigan
 */
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

					final String verificationCode = DataManipulationUtils
							.extractDigitsFromString(verificationCodeInput.getText());
					if (!verificationCode.isEmpty()) {

						Registrator.verify(verificationCode);

						final RegisterController controller = RegisterController.getInstance();
						Platform.runLater(() -> controller.loadRegisterPage("RegisterComplete.fxml"));

					} else {
						errorEmptyCode.setVisible(true);

						button.setVisible(true);
						spinningWheel.setVisible(false);
					}

				} catch (final Exception exception) {
					if (exception instanceof RateLimitException) {
						errorRateLimit.setVisible(true);
					} else if (exception instanceof AuthorizationFailedException) {
						errorAuthorization.setVisible(true);
					} else {
						exception.printStackTrace();
					}

					button.setVisible(true);
					spinningWheel.setVisible(false);
				}

				return (Void) null;
			}
		};
		new Thread(task).start();
	}
}
