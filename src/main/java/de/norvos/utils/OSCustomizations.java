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
package de.norvos.utils;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * This class deals with customizing the application for the current operating
 * system. On first use, the method
 * {@link de.norvos.os_customizing#initialize() initialize()} has to be called
 * to determine which operating system is used.
 * 
 * @author Connor Lanigan {@literal <dev@connorlanigan.com>}
 *
 */
public class OSCustomizations {

	/**
	 * Represents an operating system type.
	 */
	private enum OSType {
		WINDOWS, MAC, OTHER
	}

	private static OSType os = OSType.OTHER;

	/**
	 * Call this method at the beginning of your application.
	 * <br><br>
	 * Determines the
	 * operating system type and saves the value for later use throughout the
	 * customizing.
	 */
	public static void initialize() {
		String osname = System.getProperty("os.name").toLowerCase();

		if (osname.indexOf("mac") >= 0) {
			os = OSType.MAC;
			return;
		} else if (osname.indexOf("win") >= 0) {
			os = OSType.WINDOWS;
			return;
		}
	}

	/**
	 * Sets the appropriate Look And Feel for the current platform. On Windows
	 * and Mac, the System Look And Feel is used. On other platforms, the
	 * SeaGlass Look And Feel is used.
	 */
	public static void setLookAndFeel() {
		try {
			if (os == OSType.WINDOWS || os == OSType.MAC) {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} else {
				UIManager.setLookAndFeel("com.seaglasslookandfeel.SeaGlassLookAndFeel");
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}
	}

}
