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
