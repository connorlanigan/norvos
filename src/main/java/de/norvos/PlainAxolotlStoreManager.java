package de.norvos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Container for a single {@link PlainAxolotlStore}. Loads and saves the contained {@link PlainAxolotlStore} from/to disk.
 * 
 * @author Connor Lanigan {@literal <dev@connorlanigan.com>}
 */


public class PlainAxolotlStoreManager{

	private static PlainAxolotlStore axolotlStore;
	private static boolean loadedFromFile = false;

	/**
	 * This is the recommended place for saving application-specific configuration files in Linux.
	 * On Windows this file and folder will be as visible as any other file.
	 */
	private static String axolotlStoreFileName = ".config/axolotlStore.ser";

	private PlainAxolotlStoreManager() {
	}

	/**
	 * Load a PlainAxolotlStore from disk from the preconfigured location, or create a new one if a saved one was not found.
	 * @return true if an already existing store was loaded
	 * 
	 * @see #setFileName(String)
	 */
	public static boolean loadStore() {

		File axolotlStoreFile = getFile();
		if (axolotlStoreFile.exists()) {
			try {
				axolotlStore = PlainAxolotlStore.loadFromStream(new FileInputStream(axolotlStoreFile));
				loadedFromFile = true;
			} catch (ClassNotFoundException e) {
				Logger.critical("AxolotlStore exists in [" + axolotlStoreFile.getAbsolutePath()
						+ "], but could not be loaded, because the PlainAxolotlStore-class was not found in memory.");
				Main.handleCriticalError(ErrorMessages.axolotlCouldNotBeLoaded);

			} catch (FileNotFoundException e) {
				Logger.critical("AxolotlStore exists in [" + axolotlStoreFile.getAbsolutePath()
						+ "], but FileNotFoundException thrown. This is unexpected.");
				Main.handleCriticalError(ErrorMessages.axolotlCouldNotBeLoaded);

			} catch (IOException e) {
				Logger.critical("AxolotlStore exists in [" + axolotlStoreFile.getAbsolutePath()
						+ "], but an IOException occurred during loading.");
				Main.handleCriticalError(ErrorMessages.axolotlCouldNotBeLoaded);
			}
		} else {
			Logger.debug("File [" + axolotlStoreFile.getAbsolutePath() + "] not found, creating new store there.");
			axolotlStore = new PlainAxolotlStore();
			loadedFromFile = false;
		}
		return loadedFromFile;
	}

	/**
	 * Sets the filename used for loading and saving.
	 * @param newFileName
	 */
	public static void setFileName(String newFileName) {
		axolotlStoreFileName = newFileName;
	}

	/**
	 * Returns the loaded PlainAxolotlStore.
	 * @return the PlainAxolotlStore loaded by loadStore()
	 */
	public static PlainAxolotlStore getStore() {
		return axolotlStore;
	}

	/**
	 * Saves the contained PlainAxolotlStore in its current state to disk.
	 * @see #setFileName(String)
	 */
	public static void saveStore() {
		File outputFile = getFile();
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(new FileOutputStream(outputFile));
			os.writeObject(axolotlStore);
		} catch (IOException e) {
			Logger.critical("Could not save the AxolotlStore.");
			Main.handleCriticalError(ErrorMessages.axolotlCouldNotBeSaved);
		}
	}

	/**
	 * Returns the File for input/output.
	 * @return the File for the PlainAxolotlStore
	 */
	private static File getFile() {
		Path homePath = Paths.get(System.getProperty("user.home"));
		homePath = homePath.resolve(axolotlStoreFileName);
		return homePath.toFile();
	}

}
