package de.norvos.account;

import static org.junit.Assert.*;

import org.junit.Test;

import de.norvos.axolotl.NorvosTrustStore;

public class ServerAccountTest {
	
	private String username = "+4912345678";
	private String password = "somePassword";

	@Test
	public void test() {
		ServerAccount account = new ServerAccount(username, password);
		assertEquals(account.getPassword(), password);
		assertEquals(account.getUsername(), username);
		
		assertTrue(account.getTrustStore() instanceof NorvosTrustStore);
	}

}
