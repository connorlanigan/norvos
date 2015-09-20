package de.norvos.gui.windows;

import static de.norvos.utils.DebugProvider.debug;

import java.io.IOException;
import java.net.URL;

import de.norvos.account.SettingsService;
import de.norvos.utils.Constants;
import de.norvos.utils.ResourceUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * An abstract class providing functionality for loading a Window containing an
 * FXML file.
 * 
 * @author Connor Lanigan
 */
public abstract class Window extends Application {

	private final URL FXML;
	private final double initialHeight;
	private final double initialWidth;
	private final URL LOCATION;
	private final boolean minimizeOnClose;
	private Stage primaryStage;

	/**
	 * Creates a Window which loads the given FXML file. It is important to
	 * provide the <code>includeLocation</code>, which must contain all custom
	 * components that are used in the given FXML file.
	 *
	 * @param fxml
	 *            the FXML file to load
	 * @param includeLocation
	 *            the location from which to load custom components (must end
	 *            with a slash: "/")
	 * @param minimizeOnClose
	 *            if the window should be minimized instead of closed if it is
	 *            requested to close
	 */
	protected Window(final String fxml, final String includeLocation, final boolean minimizeOnClose,
			final double initialWidth, final double initialHeight) {

		if (fxml == null || includeLocation == null) {
			throw new NullPointerException(
					"A Window needs both an FXML and a location to include its containted components from.");
		}
		this.initialHeight = initialHeight;
		this.initialWidth = initialWidth;
		FXML = getClass().getResource(Constants.FXML_LOCATION + fxml);
		LOCATION = getClass().getResource(Constants.FXML_LOCATION + includeLocation);
		this.minimizeOnClose = minimizeOnClose;
	}

	/**
	 * Sets the focus to the application window. This will on most platforms
	 * remove the focus from the currently focused input element.
	 */
	public void focusWindow() {
		primaryStage.requestFocus();
	}

	private void initWindow() {
		primaryStage.setTitle(Constants.WINDOW_TITLE);
		primaryStage.centerOnScreen();
		if (minimizeOnClose) {
			primaryStage.setOnCloseRequest(event -> {
				primaryStage.setIconified(true);
				event.consume();
			});
		}
	}

	private void loadWindowContent() {
		final FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LOCATION);
		loader.setResources(ResourceUtils.getLocalizedStringsBundle());
		Parent parent;
		try {
			debug("Loading [%s] in location [%s] with language [%s].", FXML, LOCATION, SettingsService.getLanguage());
			parent = loader.load(FXML.openStream());
			final Scene scene = new Scene(parent, initialWidth, initialHeight);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (final IOException e) {
			// TODO logging
			System.err.println("FXML could not be loaded: [" + FXML + "]");
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void start(final Stage primaryStage) {
		this.primaryStage = primaryStage;
		initWindow();
		loadWindowContent();
	}

	/**
	 * Brings the window to the front.
	 */
	public void toFront() {
		primaryStage.toFront();
	}
}
