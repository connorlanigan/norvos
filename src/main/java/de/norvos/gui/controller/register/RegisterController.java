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
package de.norvos.gui.controller.register;

import java.io.IOException;
import java.net.URL;

import de.norvos.utils.Constants;
import de.norvos.utils.ResourceUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;

public class RegisterController {

	private static RegisterController instance;

	synchronized public static RegisterController getInstance() {
		return instance;
	}

	@FXML
	private BorderPane containerPane;

	@FXML
	private ProgressBar registrationProgress;

	public RegisterController() {
		instance = this;
	}

	public void loadRegisterPage(final String fxml) {
		final URL fxmlURL = getClass().getResource(Constants.FXML_LOCATION + "register/" + fxml);
		final FXMLLoader loader = new FXMLLoader();

		loader.setResources(ResourceUtils.getLocalizedStringsBundle());
		Parent parent;
		try {
			parent = loader.load(fxmlURL.openStream());
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
