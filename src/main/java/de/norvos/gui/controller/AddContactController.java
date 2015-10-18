package de.norvos.gui.controller;

import javafx.event.ActionEvent;
import de.norvos.contacts.Contact;
import de.norvos.contacts.ContactData;
import de.norvos.contacts.ContactData.ContactState;
import de.norvos.contacts.ContactService;
import de.norvos.gui.windows.AddContactWindow;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class AddContactController {
	private static AddContactController instance;

	@FXML
	private TextField contactName;
	
	@FXML
	private TextField contactNumber;
	
	@FXML
	private Button cancelButton;
	
	public AddContactController() {
		instance = this;
	}
	
	synchronized public static AddContactController getInstance() {
		return instance;
	}
	
	public void handleCancelButton(final ActionEvent event) {
		Platform.runLater(() -> {
			AddContactWindow.getInstance().closeWindow();
		});
	}
	
	public void handleAddContactButton(final ActionEvent event) {
		ContactService.getInstance().setContactData(new ContactData(contactNumber.getText(), contactName.getText(), "", ContactState.KNOWN_USER));
		OverviewController.getInstance().refreshContactsList();
	}
}
