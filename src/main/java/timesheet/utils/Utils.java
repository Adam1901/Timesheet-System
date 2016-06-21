package timesheet.utils;

import org.joda.time.DateTime;

public class Utils {
	public static DateTime getFirstDateOfWeek(DateTime dateTime) {
		long firstDayOfWeekTimestamp = dateTime.withDayOfWeek(1).getMillis();
		DateTime first = new DateTime(firstDayOfWeekTimestamp);
		return first;
	}

	public static boolean isStringNullOrEmpty(String str) {
		return str == null || "".equals(str);
	}
}
