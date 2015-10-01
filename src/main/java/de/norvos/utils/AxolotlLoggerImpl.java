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
package de.norvos.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.whispersystems.libaxolotl.logging.AxolotlLogger;

/**
 * Provides debugging methods for the Axolotl protocol.
 *
 * @author Connor Lanigan
 */
public class AxolotlLoggerImpl implements AxolotlLogger {

	final Logger LOGGER = LoggerFactory.getLogger(AxolotlLoggerImpl.class);

	/**
	 * Logs a message.
	 */
	@Override
	public void log(final int priority, final String tag, final String message) {
		final Marker marker = MarkerFactory.getMarker(tag);
		switch (priority) {

		case AxolotlLogger.VERBOSE:
		case AxolotlLogger.DEBUG:
			LOGGER.debug(marker, message);
			break;
		case AxolotlLogger.INFO:
			LOGGER.info(marker, message);
			break;
		case AxolotlLogger.WARN:
			LOGGER.warn(marker, message);
			break;
		case AxolotlLogger.ERROR:
			LOGGER.error(marker, message);
			break;
		case AxolotlLogger.ASSERT:
			LOGGER.trace(marker, message);
			break;
		default:
			LOGGER.error(marker, "## Unknown Loglevel Message: ##" + message);
		}
	}
}
