package de.norvos.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import de.norvos.log.Errors;
import de.norvos.log.Errors.Message;
import de.norvos.store.axolotl.NorvosAxolotlStore;

public class DurableStoreManager {

	private static NorvosAxolotlStore store = null;

	public static boolean availableOnDisk() {
		return Locations.AXOLOTL_STORE.toFile().exists();
	}

	public static NorvosAxolotlStore createNewAxolotlStore() {
		store = new NorvosAxolotlStore();
		return store;
	}

	public static NorvosAxolotlStore getAxolotlStore() {
		if (store == null) {
			try {
				loadAxolotlStoreFromDisk();
			} catch (IOException e) {
				Errors.handleCritical(Message.axolotlCouldNotBeLoaded);
				// Program stops here.
			}
		}
		return store;
	}

	private static void loadAxolotlStoreFromDisk() throws IOException {
		byte[] bytes = Files.readAllBytes(Locations.AXOLOTL_STORE);
		store = new NorvosAxolotlStore(bytes);
	}

	public static void saveAll() throws IOException {
		saveAxolotlStoreToDisk();
		saveConfigStoreToDisk();
	}

	private static void saveAxolotlStoreToDisk() throws IOException {
		File tempFile = File.createTempFile("norvos_store", ".data.tmp");

		FileOutputStream stream = new FileOutputStream(tempFile);
		try {
			stream.write(store.serialize());
		} finally {
			stream.close();
		}

		Files.move(tempFile.toPath(), Locations.AXOLOTL_STORE, StandardCopyOption.REPLACE_EXISTING);
	}

	private static void saveConfigStoreToDisk() {
		// TODO save Configstore to disk
	}

}
