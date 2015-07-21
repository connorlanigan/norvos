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
		Settings.getCurrent().setServerAccount(new ServerAccount("username", "password", ServerAccount.generateRandomBytes(52)));
		Settings.getCurrent().setLocale(Locale.ENGLISH);
		Settings.getCurrent().setSetupFinished(true);
		
		
		DiskPersistence.save("settings", Settings.getCurrent().serialize());
		
		Settings.getCurrent().setLocale(Locale.CHINESE);
		
		assertArrayEquals(Settings.getCurrent().serialize(), DiskPersistence.load("settings"));
	}
}
