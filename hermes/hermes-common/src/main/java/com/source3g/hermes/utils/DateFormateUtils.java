package com.source3g.hermes.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormateUtils {

	public static Date getDate(String unformatedDate) {
		SimpleDateFormat formatterLong = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatterShort = new SimpleDateFormat("yyyy-MM-dd");
		Date retVal = null;
		try {
			if (unformatedDate.length() == 19) {
				retVal = formatterLong.parse(unformatedDate);
			} else if (unformatedDate.length() == 10) {
				retVal = formatterShort.parse(unformatedDate);
			}
		} catch (Exception e) {
			return null;
		}
		return retVal;
	}

}
