package de.norvos.store;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import de.norvos.store.axolotl.NorvosAxolotlStore;

public class DurableStoreManager {

	
	public static boolean availableOnDisk(){
		return Locations.AXOLOTL_STORE.toFile().exists();
	}
	
	public static void removeFromDisk(){
		Locations.AXOLOTL_STORE.toFile().delete();
	}
	
	public static NorvosAxolotlStore loadFromDisk() throws IOException{
		byte[] bytes = Files.readAllBytes(Locations.AXOLOTL_STORE);
		return new NorvosAxolotlStore(bytes);
	}

	public static void storeToDisk(NorvosAxolotlStore store) throws IOException{
		File tempFile = File.createTempFile("norvos_store", ".data.tmp");
		
		FileOutputStream stream = new FileOutputStream(tempFile);
		try {
		    stream.write(store.serialize());
		} finally {
		    stream.close();
		}
		
		Files.move(tempFile.toPath(), Locations.AXOLOTL_STORE, StandardCopyOption.REPLACE_EXISTING);
	}
	
}
