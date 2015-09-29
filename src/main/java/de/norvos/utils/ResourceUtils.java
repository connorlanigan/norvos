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

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import de.norvos.account.SettingsService;
import de.norvos.i18n.UTF8Control;

/**
 * Provides various utility methods related to resources.
 * 
 * @author Connor Lanigan
 */
public class ResourceUtils {

	/**
	 * Returns the path to the database directory.
	 * 
	 * @return the path
	 */
	public static Path getDatabaseDirectory() {
		return getDataDirectory().resolve("configuration");
	}

	/**
	 * Returns the path to the data directory. If it does not yet exist on the
	 * file system, it is created.
	 * 
	 * @return the path
	 */
	public static Path getDataDirectory() {
		final Path directory = FileSystems.getDefault().getPath(System.getProperty("user.home"), ".norvos");
		directory.toFile().mkdirs();
		try {
			Files.setAttribute(directory, "dos:hidden", true);
		} catch (final IOException e) {
		}
		return directory;
	}

	/**
	 * Provides a ResourceBundle containing the string for the currently chosen
	 * language.
	 * 
	 * @return the ResourceBundle
	 */
	public static ResourceBundle getLocalizedStringsBundle() {
		final ResourceBundle bundle = ResourceBundle.getBundle("de.norvos.i18n.strings",
				SettingsService.getLanguage().getLocale(), new UTF8Control());
		return bundle;
	}

	/**
	 * Returns the path of the application log file.
	 * 
	 * @return
	 */
	public static Path getLogfile() {
		return getDataDirectory().resolve("application.log");
	}

	private ResourceUtils() {
	}
}
