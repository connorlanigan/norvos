package de.norvos.store;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.whispersystems.textsecure.api.push.TrustStore;

import de.norvos.log.Errors;
import de.norvos.log.Errors.Message;

public class NorvosTrustStore implements TrustStore {

	private String password = "As far as I know, this password is only used for integrity checking.";

	@Override
	public InputStream getKeyStoreInputStream() {
		// TODO This stream has to point to a store containing the server's TLS certificate.
		try {
			return new FileInputStream(Locations.TRUST_STORE.toFile());
		} catch (FileNotFoundException e) {
			Errors.handleCritical(Message.trustStoreNotFound);
			// The software will be stopped here.
			return null;
		}
	}

	@Override
	public String getKeyStorePassword() {
		return password;
	}
}
