package de.norvos.store;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Locations {
	private static final Path basePath = FileSystems.getDefault().getPath(System.getProperty("user.home"), "Norvos");
	
	public static final Path AXOLOTL_STORE = basePath.resolve("axolotl_store.novax");
	public static final Path LOG = basePath.resolve("errors.log");
	public static final Path TRUST_STORE = basePath.resolve("keystore.jks");

}
