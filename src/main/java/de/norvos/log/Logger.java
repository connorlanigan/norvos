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
package de.norvos.log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import de.norvos.i18n.Translations;
import de.norvos.persistence.DiskPersistence;

public class Logger {

	public static void debug(String message) {
		log("[DEBUG] "+message);
	}

	public static void warning(String message) {
		log("[WARNING] "+message);
	}
	
	public static void critical(String message) {
		log("[CRITICAL] "+message);
	}
	
	private static void log(String message) {
		System.out.println(message);
		writeToFile(message);
	}

	private static void writeToFile(String fullMessage) {
		try {
			DiskPersistence.append("application.log", fullMessage.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			Errors.warning(Translations.format("errors", "logFileNotWritable"));
		}		
	}
	
}
