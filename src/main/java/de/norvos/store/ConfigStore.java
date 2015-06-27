package de.norvos.store;

import org.whispersystems.textsecure.api.push.TrustStore;

public class ConfigStore {
	private static String URL = "https://my.textsecure.server.com";
	private static TrustStore TRUST_STORE = new NorvosTrustStore();
	private static String USERNAME;
	private static String PASSWORD = "testPassword";
	
	public static void setUrl(String url) {
		URL = url;
	}

	public static void setTrustStore(TrustStore trustStore) {
		TRUST_STORE = trustStore;
	}

	public static void setUsername(String username) {
		USERNAME = username;
	}

	public static void setPassword(String password) {
		PASSWORD = password;
	}
	
	
	// TODO implement method stubs
	
	static public String getURL(){
		return URL;
		
	}
	
	static public TrustStore getTrustStore(){
		return TRUST_STORE;
		
	}
	
	static public String getUsername(){
		return USERNAME;
		
	}
	
	static public String getPassword(){
		return PASSWORD;
		
	}
}
