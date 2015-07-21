package de.norvos.persistence;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import de.norvos.account.Settings;
import de.norvos.axolotl.NorvosAxolotlStore;
import de.norvos.i18n.Translations;
import de.norvos.log.Errors;
import de.norvos.observers.Notifiable;

public class DiskPersistence implements Notifiable {
	private static final Path basePath = FileSystems.getDefault().getPath(System.getProperty("user.home"), "Norvos");

	public static void save(String objectId, byte[] obj) throws IOException {
		File writeFile = basePath.resolve(objectId).toFile();
		if (!writeFile.exists()) {
			writeFile.getParentFile().mkdirs();
		}

		File tempFile = File.createTempFile("norvos_" + objectId, ".data.tmp");

		FileOutputStream stream = new FileOutputStream(tempFile);
		try {
			stream.write(obj);
			stream.flush();
		} finally {
			stream.close();
		}

		Files.move(tempFile.toPath(), basePath.resolve(objectId), StandardCopyOption.REPLACE_EXISTING);
	}

	public static void append(String objectId, byte[] obj) throws IOException {
		File writeFile = basePath.resolve(objectId).toFile();
		if (!writeFile.exists()) {
			writeFile.getParentFile().mkdirs();
		}
		if (!writeFile.canWrite()) {
			return;
		}

		FileOutputStream stream = new FileOutputStream(writeFile, true);
		try {
			stream.write(obj);
			stream.flush();
		} finally {
			stream.close();
		}
	}

	public static byte[] load(String objectId) throws IOException {
		File readFile = basePath.resolve(objectId).toFile();

		byte[] data = new byte[(int) readFile.length()];
		DataInputStream dataStream = new DataInputStream(new FileInputStream(readFile));
		dataStream.readFully(data);

		dataStream.close();

		return data;
	}

	@Override
	public void notify(String event, Object notificationData) {
		if (notificationData instanceof NorvosAxolotlStore && event.equals("axolotlStoreChange")) {
			try {
				save("axolotlStore", ((NorvosAxolotlStore) notificationData).serialize());
			} catch (IOException e) {
				Errors.critical(Translations.format("errors", "axolotlCouldNotBeSaved"));
			}
		} else if (notificationData instanceof Settings && event.equals("settingsChange")) {
			try {
				save("settings", ((Settings) notificationData).serialize());
			} catch (IOException e) {
				Errors.critical(Translations.format("errors", "settingsCouldNotBeSaved"));
			}
		}

	}
}
