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
