/*******************************************************************************
 * Copyright (C) 2015 Connor Lanigan (email: dev@connorlanigan.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.norvos.gui.windows;

import static de.norvos.i18n.Translations.translate;

import java.io.IOException;
import java.net.URL;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.norvos.account.SettingsService;
import de.norvos.utils.Constants;
import de.norvos.utils.Errors;
import de.norvos.utils.ResourceUtils;
import de.norvos.utils.UnreachableCodeException;
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
public abstract class Window {

	private final static Logger LOGGER = LoggerFactory.getLogger(Window.class);
	private final URL FXML;
	private Boolean hasQuit;
	private final Object hasQuitLock = new Object();
	private final double initialHeight;
	private final double initialWidth;
	private final URL LOCATION;
	private final boolean minimizeOnClose;
	private Stage stage;

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
		hasQuit = false;
	}

	/**
	 * Sets the focus to the application window. This will on most platforms
	 * remove the focus from the currently focused input element.
	 */
	public void focusWindow() {
		stage.requestFocus();
	}

	private void initWindow() {
		stage.setTitle(Constants.WINDOW_TITLE);
		stage.centerOnScreen();
		stage.setOnCloseRequest(event -> {
			if (minimizeOnClose) {
				stage.setIconified(true);
				event.consume();
			} else {
				releaseWindowQuitLock();
			}
		});

	}

	private void loadWindowContent() {
		final FXMLLoader loader = new FXMLLoader();
		loader.setLocation(LOCATION);
		loader.setResources(ResourceUtils.getLocalizedStringsBundle());
		Parent parent;
		try {
			LOGGER.debug("Loading [{}] in location [{}[ with language [{}].", FXML, LOCATION,
					SettingsService.getLanguage().name());
			parent = loader.load(FXML.openStream());
			final Scene scene = new Scene(parent, initialWidth, initialHeight);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			stage.setScene(scene);
			stage.show();
		} catch (final IOException e) {
			LOGGER.error("FXML could not be loaded.", e);
			JOptionPane.showMessageDialog(null, translate("unexpected_quit"), "Norvos – Error",
					JOptionPane.WARNING_MESSAGE);
			Errors.stopApplication();
			throw new UnreachableCodeException();
		}
	}

	public void closeWindow() {
		stage.close();
	}

	public void releaseWindowQuitLock() {
		synchronized (hasQuitLock) {
			hasQuit = true;
			hasQuitLock.notifyAll();
		}
	}

	public void start(final Stage primaryStage) {
		stage = primaryStage;
		initWindow();
		loadWindowContent();
	}

	/**
	 * Brings the window to the front.
	 */
	public void toFront() {
		stage.toFront();
	}

	public void waitForClose() {
		if (minimizeOnClose) {
			throw new IllegalStateException("Can't wait for close on a window with \"minimizeOnClose\" enabled");
		}
		synchronized (hasQuitLock) {
			while (!hasQuit) {
				try {
					hasQuitLock.wait();
				} catch (final InterruptedException e) {
				}
			}
		}
	}
}
