package de.norvos.gui.components;

import java.io.IOException;

import de.norvos.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

public class ContactListEntry extends Button {

	@FXML
	private Circle newMessageIndicator;

	@FXML
	private Text contactName;

	@FXML
	private Label lastMessage;

	public ContactListEntry() {
		FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource(Constants.FXML_LOCATION + "ContactListEntry.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	@FXML
    public void initialize(){
		newMessageIndicator.managedProperty().bind(newMessageIndicator.visibleProperty());
    }


	public String getName() {
		return contactName.getText();
	}

	public void setName(String value) {
		contactName.setText(value);
	}

	public String getNewMessage() {
		return String.valueOf(newMessageIndicator.isVisible());
	}

	public void setNewMessage(String value) {
		boolean newMessage = Boolean.valueOf(value);
		newMessageIndicator.setVisible(newMessage);
		if(newMessage){
			getStyleClass().add("newMessage");
		}else{
			getStyleClass().remove("newMessage");
		}
	}

	public String getLastMessage() {
		return lastMessage.getText();
	}

	public void setLastMessage(String value) {
		lastMessage.setText(value);
	}

}
