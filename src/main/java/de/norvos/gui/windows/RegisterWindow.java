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

import de.norvos.account.Registrator;
import javafx.stage.Modality;

/**
 * The window containing the registration process.
 *
 * @author Connor Lanigan
 */
public class RegisterWindow extends Window {

	private static RegisterWindow instance = null;

	synchronized public static RegisterWindow getInstance() {
		return instance;
	}

	public RegisterWindow() {
		super("register/Register.fxml", "register/", false, 600, 400, null);
		instance = this;
		Registrator.initialize();
	}

}
