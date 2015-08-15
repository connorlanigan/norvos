package de.norvos.gui.controller.register;

import java.io.IOException;
import java.net.URL;

import de.norvos.gui.controller.Controller;
import de.norvos.utils.Constants;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

public class RegisterController extends Controller {
	@FXML
	private BorderPane containerPane;

	@FXML
	private ProgressBar registrationProgress;

	public void loadFXML(final String fxml) {
		final URL fxmlURL = getClass().getResource(Constants.FXML_LOCATION + fxml);
		final FXMLLoader loader = new FXMLLoader(fxmlURL);
		Parent parent;
		try {
			parent = loader.load();
			containerPane.getChildren().clear();
			containerPane.setCenter(parent);
		} catch (final IOException e) {
			// TODO logging
			System.err.println("FXML could not be loaded: [" + fxmlURL + "]");
			e.printStackTrace();
		}
	}

	public void setProgress(final float value) {
		registrationProgress.setProgress(value);
	}

}
