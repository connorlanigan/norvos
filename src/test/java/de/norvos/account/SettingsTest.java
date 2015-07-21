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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.Test;

import de.norvos.axolotl.NorvosAxolotlStore;

public class SettingsTest {

	Settings settingsInstance = Settings.getCurrent();

	@Test
	public void locale() {
		settingsInstance.setLocale(Locale.CHINESE);
		assertEquals(Locale.CHINESE, Settings.getCurrent().getLocale());
	}

	@Test
	public void axolotlStore() {
		NorvosAxolotlStore store = new NorvosAxolotlStore();
		settingsInstance.setAxolotlStore(store);
		assertSame(store, Settings.getCurrent().getAxolotlStore());
	}

	@Test
	public void serverAccount() {
		ServerAccount account = new ServerAccount("username", "password", ServerAccount.generateRandomBytes(52));
		settingsInstance.setServerAccount(account);
		assertEquals(account, Settings.getCurrent().getServerAccount());
	}

	@Test
	public void singleton() {
		Settings secondSettings = Settings.getCurrent();
		assertSame(settingsInstance, secondSettings);
	}

	@Test
	public void serialization() throws IOException {
		settingsInstance.setLocale(Locale.ENGLISH);
		settingsInstance.setServerAccount(new ServerAccount("username", "password", ServerAccount.generateRandomBytes(52)));
		settingsInstance.setAxolotlStore(new NorvosAxolotlStore());
		Settings.load(settingsInstance.serialize());
		
		assertEquals(Locale.ENGLISH, Settings.getCurrent().getLocale());
		assertEquals("username", Settings.getCurrent().getServerAccount().getUsername());
		assertEquals("password", Settings.getCurrent().getServerAccount().getPassword());	
	}
	
	@Test
	public void generateRandomBytes(){
		String bytes = ServerAccount.generateRandomBytes(52);
		assertEquals(52, bytes.getBytes(StandardCharsets.US_ASCII).length);
		
		bytes = ServerAccount.generateRandomBytes(128);
		assertEquals(128, bytes.getBytes(StandardCharsets.US_ASCII).length);
	}
}
