package de.norvos.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class MessageListController {

	@FXML
	private FlowPane messageList;

	public void addMessage(final String message) {
		messageList.getChildren().add(new Label(message));
	}

	@FXML
	public void initialize() {
		addMessage("welcome to swaggoland");
	}

}
