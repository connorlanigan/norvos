package de.norvos.account;

import org.whispersystems.textsecure.api.push.TrustStore;

import de.norvos.axolotl.NorvosTrustStore;

public class ServerAccount {
	private String URL = "https://my.textsecure.server.com";
	private TrustStore TRUST_STORE = NorvosTrustStore.get();
	private String USERNAME;
	private String PASSWORD = "testPassword";
	
	public void setUrl(String url) {
		URL = url;
	}

	public void setTrustStore(TrustStore trustStore) {
		TRUST_STORE = trustStore;
	}

	public void setUsername(String username) {
		USERNAME = username;
	}

	public void setPassword(String password) {
		PASSWORD = password;
	}
	
	
	// TODO implement method stubs
	
	public String getURL(){
		return URL;
		
	}
	
	public TrustStore getTrustStore(){
		return TRUST_STORE;
		
	}
	
	public String getUsername(){
		return USERNAME;
		
	}
	
	public String getPassword(){
		return PASSWORD;
		
	}
}
