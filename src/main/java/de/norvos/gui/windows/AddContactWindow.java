package de.norvos.gui.windows;

import javafx.stage.Modality;

public class AddContactWindow extends Window {

	private static AddContactWindow instance = null;

	synchronized public static AddContactWindow getInstance() {
		return instance;
	}

	public AddContactWindow() {
		super("AddContact.fxml", "/", false, 400, 270, Modality.APPLICATION_MODAL);
		instance = this;
	}

}
