package de.norvos.gui;

import java.io.IOException;
import java.net.URL;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.whispersystems.libaxolotl.logging.AxolotlLoggerProvider;

import de.norvos.account.AccountDataStore;
import de.norvos.account.Registrator;
import de.norvos.utils.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindow extends Application {

	public static void main(final String[] args) {
		Security.addProvider(new BouncyCastleProvider());

		AxolotlLoggerProvider.setProvider(
				(priority, tag, message) -> System.err.println("Priority " + priority + ": [" + tag + "] " + message));
		launch(args);
	}

	private Stage primaryStage;

	private void initializeDB() {
		AccountDataStore.storeStringValue("url", "https://textsecure-service.whispersystems.org");
	}

	/**
	 * Loads an FXML file into the main window.
	 *
	 * @param fxml
	 *            the path to the FXML file
	 * @return the controller associated with the FXML
	 * @throws IOException
	 *             if an error occurs during loading
	 */
	public <T> T loadFXML(final String fxml) {
		final URL fxmlURL = getClass().getResource(Constants.FXML_LOCATION + fxml);
		final FXMLLoader loader = new FXMLLoader(fxmlURL);
		Parent parent;
		try {
			parent = loader.load();
			final T controller = loader.<T> getController();
			final Scene scene = new Scene(parent, 600, 400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			return controller;
		} catch (final IOException e) {
			// TODO logging
			System.err.println("FXML could not be loaded: [" + fxml + "]");
			System.exit(1);
			return null;
		}
	}

	@Override
	public void start(final Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle(Constants.WINDOW_TITLE);
		primaryStage.centerOnScreen();

		final boolean hasToRegister = !"true".equals(AccountDataStore.getStringValue("setupFinished"));
		if (hasToRegister) {
			initializeDB();
			Registrator.initialize();
			loadFXML("register/Register.fxml");
		} else {
			loadFXML("Overview.fxml");
		}
	}

}
