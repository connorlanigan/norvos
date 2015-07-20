package de.norvos.axolotl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.whispersystems.textsecure.api.push.TrustStore;

import de.norvos.i18n.Translations;
import de.norvos.log.Errors;
import de.norvos.persistence.DiskPersistence;

public class NorvosTrustStore implements TrustStore {
	
	/**
	 * Singleton instance
	 */
	private static NorvosTrustStore store = null;
	
	/**
	 * The default password of the KeyStore shipped with the TextSecure android client is "whisper".
	 */
	private static String password = "whisper";
	
	/**
	 * Singleton constructor
	 */
	private NorvosTrustStore(){}
	
	/**
	 * Returns the only instance of the NorvosTrustStore.
	 * @return the NorvosTrustStore
	 */
	public static NorvosTrustStore get(){
		if(store == null){
			store = new NorvosTrustStore();
		}
		return store;
	}


	/**
	 * Returns an InputStream to the Java KeyStore.
	 */
	@Override
	public InputStream getKeyStoreInputStream() {
		try {
			return new ByteArrayInputStream(DiskPersistence.load("truststore"));
		} catch (IOException e) {
			Errors.critical(Translations.format("errors", "trustStoreNotFound"));
			System.exit(1);
			return null;
		}
	}

	/**
	 * Returns the password for the KeyStore.
	 */
	@Override
	public String getKeyStorePassword() {
		return password;
	}
}
