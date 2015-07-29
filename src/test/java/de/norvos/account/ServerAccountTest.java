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

import static org.junit.Assert.*;

import org.junit.Test;

import de.norvos.axolotl.NorvosTrustStore;
import de.norvos.utils.RandomUtils;

public class ServerAccountTest {
	
	private String username = "+4912345678";
	private String password = "somePassword";

	@Test
	public void test() {
		ServerAccount account = new ServerAccount(username, password, RandomUtils.randomAlphanumerical(52));
		assertEquals(account.getPassword(), password);
		assertEquals(account.getUsername(), username);
		
		assertTrue(account.getTrustStore() instanceof NorvosTrustStore);
	}

}
