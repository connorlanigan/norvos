package de.norvos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PlainAxolotlStoreContainer {

	private static PlainAxolotlStore axolotlStore;
	private static boolean loadedFromFile = false;
	
	private PlainAxolotlStoreContainer(){}
	
	
	public static boolean loadStore(){
		Path axolotlStoreFileName = Paths.get(".config/axolotlStore.ser");		
		Path homePath = Paths.get(System.getProperty("user.home"));
		homePath = homePath.resolve(axolotlStoreFileName);
		File axolotlStoreFile = homePath.toFile();
		
		if (axolotlStoreFile.exists()) {
			try {
				axolotlStore = PlainAxolotlStore.loadFromStream(new FileInputStream(axolotlStoreFile));
				loadedFromFile = true;
			} catch (ClassNotFoundException e) {
				Logger.critical("AxolotlStore exists in ["+axolotlStoreFile.getAbsolutePath()+"], but could not be loaded, because the PlainAxolotlStore-class was not found in memory.");
				Main.handleCriticalError(ErrorMessages.axolotlCouldNotBeLoaded);

			} catch (FileNotFoundException e) {
				Logger.critical("AxolotlStore exists in ["+axolotlStoreFile.getAbsolutePath()+"], but FileNotFoundException thrown. This is unexpected.");
				Main.handleCriticalError(ErrorMessages.axolotlCouldNotBeLoaded);

			} catch (IOException e) {
				Logger.critical("AxolotlStore exists in ["+axolotlStoreFile.getAbsolutePath()+"], but an IOException occurred during loading.");
				Main.handleCriticalError(ErrorMessages.axolotlCouldNotBeLoaded);
			}
		}else{
			Logger.debug("File ["+axolotlStoreFile.getAbsolutePath()+"] not found, creating new store there.");
			axolotlStore = new PlainAxolotlStore();
			loadedFromFile = false;
		}
		return loadedFromFile;		
	}
	
	public static PlainAxolotlStore getStore(){
		return axolotlStore;
	}
	
	public static void saveStore(){
		// TODO save store to disk
	}
	
	
}
