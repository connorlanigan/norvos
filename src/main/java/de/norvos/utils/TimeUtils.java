package de.norvos.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {


	static public String formatDate(long timestamp){
		Calendar now = Calendar.getInstance();
		Date date = new Date(timestamp);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		SimpleDateFormat dateFormat = getFormatForTimestamp(now, cal);

		return dateFormat.format(date);
	}

	static private SimpleDateFormat getFormatForTimestamp(Calendar now, Calendar timestamp){
		if(now.get(Calendar.YEAR) != timestamp.get(Calendar.YEAR)){
			return new SimpleDateFormat("dd.MM.yyyy\nHH:mm");
		} if(now.get(Calendar.DAY_OF_YEAR) != timestamp.get(Calendar.DAY_OF_YEAR)){
			return new SimpleDateFormat("dd.MM.\nHH:mm");
		}
		return new SimpleDateFormat("HH:mm");
	}

}
