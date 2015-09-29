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

import java.io.IOException;

import de.norvos.account.Registrator;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.text.Text;

/**
 * @author Connor Lanigan
 */
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
					final RegisterController controller = RegisterController.getInstance();
					Platform.runLater(() -> {
						controller.setProgress(0.7F);
						controller.loadRegisterPage("RegisterValidation.fxml");
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
