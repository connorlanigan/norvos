package de.norvos.gui.components;

import java.io.IOException;

import de.norvos.utils.Constants;
import de.norvos.utils.TimeUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class SingleMessage extends BorderPane {

	private final static String baseClass="message";

	@FXML
	private Label message;

	@FXML
	private Label time;

	@FXML
	private BorderPane messageBox;

	private Long timestamp;
	private boolean sent;

	public String getSent() {
		return String.valueOf(sent);
	}

	public void setSent(String sent) {
		boolean pSent = Boolean.valueOf(sent);
		this.sent = pSent;

		String cssClass;
		if(pSent){
			cssClass = "sentMessage";
		}else{
			cssClass = "receivedMessage";
		}
		messageBox.getStyleClass().setAll(cssClass, baseClass);
	}

	public SingleMessage() {
		final FXMLLoader fxmlLoader = new FXMLLoader(
				getClass().getResource(Constants.FXML_LOCATION + "SingleMessage.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
			setSent(getSent());
		} catch (final IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public String getTime() {
		return String.valueOf(timestamp);
	}

	public void setTime(String timestamp) {
		this.timestamp = Long.valueOf(timestamp);
		String string = TimeUtils.formatDate(this.timestamp);
		time.setText(string);
	}

	public String getMessage() {
		return message.getText();
	}

	public void setMessage(String message) {
		this.message.setText(message);
	}

}
