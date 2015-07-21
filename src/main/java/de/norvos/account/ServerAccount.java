package de.norvos.account;


import de.norvos.axolotl.NorvosTrustStore;

public class ServerAccount {
	private final String URL = "https://textsecure-service.whispersystems.org";
	private final NorvosTrustStore TRUST_STORE = NorvosTrustStore.get();
	private final String USERNAME;	
	private final String PASSWORD;

	public ServerAccount(String username, String password){
		this.USERNAME = username;
		this.PASSWORD = password;
	}

	public String getURL() {
		return URL;
	}

	public NorvosTrustStore getTrustStore() {
		return TRUST_STORE;
	}

	public String getUsername() {
		return USERNAME;
	}

	public String getPassword() {
		return PASSWORD;
	}
}
