package timesheet.utils;

import java.text.DecimalFormat;

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

	public static String doubleValueOf(double d) {
		DecimalFormat df = new DecimalFormat("0.0");
		String format = df.format(d);
		return format;
	}
}
