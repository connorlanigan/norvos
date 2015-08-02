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
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.junit.Test;

import com.squareup.okhttp.internal.spdy.Settings;

import de.norvos.utils.RandomUtils;

public class SettingsTest {

	Settings settingsInstance = Settings.getCurrent();

	@Test
	public void axolotlStore() {
		final NorvosAxolotlStore store = new NorvosAxolotlStore();
		settingsInstance.setAxolotlStore(store);
		assertSame(store, Settings.getCurrent().getAxolotlStore());
	}

	@Test
	public void generateRandomBytes() {
		String bytes = RandomUtils.randomAlphanumerical(52);
		assertEquals(52, bytes.getBytes(StandardCharsets.US_ASCII).length);

		bytes = RandomUtils.randomAlphanumerical(128);
		assertEquals(128, bytes.getBytes(StandardCharsets.US_ASCII).length);
	}

	@Test
	public void locale() {
		settingsInstance.setLocale(Locale.CHINESE);
		assertEquals(Locale.CHINESE, Settings.getCurrent().getLocale());
	}

	@Test
	public void serialization() throws IOException {
		settingsInstance.setLocale(Locale.ENGLISH);
		settingsInstance.setServerAccount(new ServerAccount("username", "password", RandomUtils
				.randomAlphanumerical(52)));
		settingsInstance.setAxolotlStore(new NorvosAxolotlStore());
		Settings.load(settingsInstance.serialize());

		assertEquals(Locale.ENGLISH, Settings.getCurrent().getLocale());
		assertEquals("username", Settings.getCurrent().getServerAccount().getUsername());
		assertEquals("password", Settings.getCurrent().getServerAccount().getPassword());
	}

	@Test
	public void serverAccount() {
		final ServerAccount account = new ServerAccount("username", "password", RandomUtils.randomAlphanumerical(52));
		settingsInstance.setServerAccount(account);
		assertEquals(account, Settings.getCurrent().getServerAccount());
	}

	@Test
	public void singleton() {
		final Settings secondSettings = Settings.getCurrent();
		assertSame(settingsInstance, secondSettings);
	}
}
