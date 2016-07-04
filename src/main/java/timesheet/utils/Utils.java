package timesheet.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFormattedTextField.AbstractFormatter;

import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
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
	
	
	public static UtilDateModel createDateModel(DateTime date) {
		UtilDateModel utilDateModel = new UtilDateModel();
		int year = date.getYear();
		int monthOfYear = date.getMonthOfYear() - 1; // Stupid java 0 based
														// month /facepalm
		int dayOfMonth = date.getDayOfMonth();
		utilDateModel.setDate(year, monthOfYear, dayOfMonth);
		utilDateModel.setSelected(true);
		return utilDateModel;
	}

	public static DateTime getDateTime(JDatePickerImpl date) {
		Date value = (Date) date.getModel().getValue();
		return new DateTime(value.getTime());
	}

	public class DateLabelFormatter extends AbstractFormatter {
		private static final long serialVersionUID = 1L;
		private String datePattern = "dd/MM/yyyy";
		private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

		@Override
		public Object stringToValue(String text) throws ParseException {
			return dateFormatter.parseObject(text);
		}

		@Override
		public String valueToString(Object value) throws ParseException {
			if (value != null) {
				Calendar cal = (Calendar) value;
				return dateFormatter.format(cal.getTime());
			}

			return "";
		}

	}
}
