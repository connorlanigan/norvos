package de.norvos.account;

import static org.junit.Assert.*;

import java.io.IOException;
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
		ServerAccount account = new ServerAccount("username", "password");
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
		settingsInstance.setServerAccount(new ServerAccount("username", "password"));
		Settings.load(settingsInstance.serialize());
		assertEquals(Locale.ENGLISH, Settings.getCurrent().getLocale());
		assertEquals("username", Settings.getCurrent().getServerAccount().getUsername());
		assertEquals("password", Settings.getCurrent().getServerAccount().getPassword());	
	}
}
