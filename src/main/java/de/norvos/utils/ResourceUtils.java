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
 * @author Connor Lanigan
 */
public class ResourceUtils {

	/**
	 * Returns the path to the database directory.
	 * @return the path
	 */
	public static Path getDatabaseDirectory() {
		return getDataDirectory().resolve("configuration");
	}

	/**
	 * Returns the path to the data directory. If it does not yet exist on the file system, it is created.
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
	 * Provides a ResourceBundle containing the string for the currently chosen language.
	 * @return the ResourceBundle
	 */
	public static ResourceBundle getLocalizedStringsBundle() {
		final ResourceBundle bundle = ResourceBundle.getBundle("de.norvos.i18n.strings",
				SettingsService.getLanguage().getLocale(), new UTF8Control());
		return bundle;
	}

	/**
	 * Returns the path of the application log file.
	 * @return
	 */
	public static Path getLogfile() {
		return getDataDirectory().resolve("application.log");
	}

	private ResourceUtils() {
	}
}
