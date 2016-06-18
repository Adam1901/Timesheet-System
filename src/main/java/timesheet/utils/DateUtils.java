package timesheet.utils;

import org.joda.time.DateTime;

public class DateUtils {
	public static DateTime getFirstDateOfWeek(DateTime dateTime) {
		long firstDayOfWeekTimestamp = dateTime.withDayOfWeek(1).getMillis();
		DateTime first = new DateTime(firstDayOfWeekTimestamp);
		return first;
	}
}
