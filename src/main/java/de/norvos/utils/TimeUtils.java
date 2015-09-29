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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Provides various utility methods related to time and date.
 *
 * @author Connor Lanigan
 */
public class TimeUtils {

	/**
	 * Formats a timestamp as a human readable date. The returned value is
	 * <strong>not</strong> usable for calculation or storage.
	 *
	 * @param timestamp
	 *            the timestamp to format
	 * @return the date in human readable form
	 */
	public static String formatDate(final long timestamp) {
		final Calendar now = Calendar.getInstance();
		final Date date = new Date(timestamp);
		final Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		final SimpleDateFormat dateFormat = getFormatForTimestamp(now, cal);

		return dateFormat.format(date);
	}

	private static SimpleDateFormat getFormatForTimestamp(final Calendar now, final Calendar timestamp) {
		if (now.get(Calendar.YEAR) != timestamp.get(Calendar.YEAR)) {
			return new SimpleDateFormat("yyyy-MM-dd\nHH:mm");
		}
		if (now.get(Calendar.DAY_OF_YEAR) != timestamp.get(Calendar.DAY_OF_YEAR)) {
			return new SimpleDateFormat("MM-yy\nHH:mm");
		}
		return new SimpleDateFormat("HH:mm");
	}

}
