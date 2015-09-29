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

import static de.norvos.utils.DebugProvider.debug;

import java.io.IOException;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import de.norvos.account.Registrator;
import de.norvos.account.SettingsService;
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
public class RegisterPhoneNumberController {

	@FXML
	private Button button;

	@FXML
	private Text errorMessage;

	@FXML
	private TextField mobileNumberInput;

	@FXML
	private ProgressIndicator spinningWheel;

	private String formatPhoneNumber(final String phoneNumber) throws NumberParseException {
		final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

		final PhoneNumber number = phoneUtil.parse(phoneNumber, "DE");
		if (!phoneUtil.isValidNumber(number)) {
			throw new NumberParseException(NumberParseException.ErrorType.INVALID_COUNTRY_CODE,
					"Number [" + phoneNumber + "] is invalid.");
		}
		return phoneUtil.format(number, PhoneNumberFormat.E164);
	}

	public void handleButtonClicked(final ActionEvent event) {
		button.setVisible(false);
		spinningWheel.setVisible(true);
		errorMessage.setVisible(false);

		final Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				String phoneNumber = mobileNumberInput.getText();

				try {
					phoneNumber = formatPhoneNumber(phoneNumber);
					SettingsService.setUsername(phoneNumber);
					final RegisterController controller = RegisterController.getInstance();

					try {
						Registrator.requestCode();
						Platform.runLater(() -> {
							controller.setProgress(0.7F);
							controller.loadRegisterPage("RegisterValidation.fxml");
						});
					} catch (final IOException e) {
						debug(e.getMessage());
						Platform.runLater(() -> controller.loadRegisterPage("RegisterRequestError.fxml"));
					}
				} catch (final NumberParseException e) {
					try {
						Thread.sleep(300);
					} catch (final InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Platform.runLater(() -> {
						button.setVisible(true);
						spinningWheel.setVisible(false);
						errorMessage.setVisible(true);
					});
				}
				return null;
			}
		};
		new Thread(task).start();

	}

}
