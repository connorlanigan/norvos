/*******************************************************************************
 * Copyright (C) 2015 Connor Lanigan (email: dev@connorlanigan.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
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
