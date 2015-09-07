package de.norvos.gui.components;

import java.io.IOException;
import java.util.List;

import de.norvos.contacts.Contact;
import de.norvos.messages.DecryptedMessage;
import de.norvos.messages.MessageService;
import de.norvos.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MessageList extends BorderPane {

	private Contact contact;
	@FXML
	private TextArea messageInput;

	@FXML
	private VBox messageList;
	private String user;
	@FXML
	private Text usernameDisplay;
	@FXML
	private CheckBox verified;

	public MessageList() {
		final FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource(Constants.FXML_LOCATION + "MessageList.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public String getUser() {
		return user;
	}

	public void keyReleased(final KeyEvent event) {
		contact.setDraftMessage(messageInput.getText());
	}

	public void setUser(final String user) {
		this.user = user;
		contact = new Contact(user);
		usernameDisplay.setText(contact.getDisplayName());
		messageInput.setText(contact.getDraftMessage());
		final List<DecryptedMessage> list = MessageService.getInstance().getMessages(contact);
		for (final DecryptedMessage message : list) {
			final SingleMessage singleMessage = new SingleMessage();
			singleMessage.setMessage(message.getBody());
			singleMessage.setSent(String.valueOf(message.isSent()));
			singleMessage.setTime(message.getTimestamp());
			messageList.getChildren().add(singleMessage);
		}
	}

}
