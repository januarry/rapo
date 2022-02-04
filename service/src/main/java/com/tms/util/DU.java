package com.tms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DU {

    public static String format(Date date) {
		return format(date, "dd/MM/yyyy-HH:mm:ss");
	}

	public static String formatStr(Date date) {
		return format(date, "dd-MMM-yyyy");
	}

	public static String format(Date date, String format) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = null;
		sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	// This gives date in format which client wants
	public static Date parse(String dateString) {
		return parse(dateString, "dd/MM/yyyy-HH:mm:ss");
	}

	public static Date parse(String dateString, String format) {
		SimpleDateFormat sdf = null;
		sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date parse(long timeInSecs) {
		Date date = new Date();
		date.setTime(timeInSecs * 1000);
		return date;
	}

}
