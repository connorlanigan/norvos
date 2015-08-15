package de.norvos.account;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AccountDataStoreTest {

	@Test
	public void test() {
		final String s = AccountDataStore.getStringValue("testKey");
		assertNull(s);

		final byte[] b = AccountDataStore.getBinaryValue("testKey");
		assertNull(b);

		AccountDataStore.storeStringValue("testKey", "testString");
		AccountDataStore.storeStringValue("secondTestKey", "secondTestString");

		assertEquals("testString", AccountDataStore.getStringValue("testKey"));
		assertEquals("secondTestString", AccountDataStore.getStringValue("secondTestKey"));

		AccountDataStore.storeStringValue("testKey", "overwrittenTestString");

		assertEquals("overwrittenTestString", AccountDataStore.getStringValue("testKey"));
		assertEquals("secondTestString", AccountDataStore.getStringValue("secondTestKey"));
	}
}
