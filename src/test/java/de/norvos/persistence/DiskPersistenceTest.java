package de.norvos.persistence;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Locale;

import org.junit.Test;

import de.norvos.account.ServerAccount;
import de.norvos.account.Settings;


public class DiskPersistenceTest {

	@Test
	public void test() throws IOException {
		byte[] bytes = "This file can safely be deleted.".getBytes();
		DiskPersistence.save("testFile.tmp", bytes);
		assertArrayEquals(bytes, DiskPersistence.load("testFile.tmp"));
	}
	
	@Test
	public void notification() throws IOException{
		Settings.getCurrent().setServerAccount(new ServerAccount("username", "password"));
		Settings.getCurrent().setLocale(Locale.ENGLISH);
		Settings.getCurrent().setSetupFinished(true);
		
		
		DiskPersistence.save("settings", Settings.getCurrent().serialize());
		
		Settings.getCurrent().setLocale(Locale.CHINESE);
		
		assertArrayEquals(Settings.getCurrent().serialize(), DiskPersistence.load("settings"));
	}
}
