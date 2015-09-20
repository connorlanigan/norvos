package de.norvos.utils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

import de.norvos.account.SettingsService;
import de.norvos.i18n.UTF8Control;

public class ResourceUtils {

	public static Path getDatabaseDirectory() {
		return getDataDirectory().resolve("configuration");
	}

	public static Path getDataDirectory() {
		final Path directory = FileSystems.getDefault().getPath(System.getProperty("user.home"), ".norvos");
		directory.toFile().mkdirs();
		try {
			Files.setAttribute(directory, "dos:hidden", true);
		} catch (final IOException e) {
		}
		return directory;
	}

	public static ResourceBundle getLocalizedStringsBundle() {
		final ResourceBundle bundle = ResourceBundle.getBundle("de.norvos.i18n.strings",
				SettingsService.getLanguage().getLocale(), new UTF8Control());
		return bundle;
	}

	public static Path getLogfile() {
		return getDataDirectory().resolve("application.log");
	}

	private ResourceUtils() {
	}
}
