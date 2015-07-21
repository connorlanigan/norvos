package de.norvos.account;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class RegistratorTest {

	@Test
	public void test() throws Exception {
		int id = Registrator.generateRandomInstallId();
		assertTrue(id < Math.pow(2, 15) && id > 0);
		
		assertEquals(52, Registrator.generateRandomSignalingKey().getBytes(StandardCharsets.US_ASCII).length);
	}

}
