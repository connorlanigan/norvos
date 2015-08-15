package de.norvos.gui.controller.register;

import java.io.IOException;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import de.norvos.account.AccountDataStore;
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
					AccountDataStore.storeStringValue("username", phoneNumber);
					final RegisterController controller = (RegisterController) Controller.getInstance();

					try {
						Registrator.requestCode();
						Platform.runLater(() -> {
							controller.setProgress(0.7F);
							controller.loadFXML("register/RegisterValidation.fxml");
						});
					} catch (final IOException e) {
						Platform.runLater(() -> controller.loadFXML("register/RegisterRequestError.fxml"));
					}
				} catch (final NumberParseException e) {
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
