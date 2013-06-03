package com.source3g.hermes.utils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.source3g.hermes.constants.Constants;

public class FormateUtils {

	public static String changeEncode(String str, String fromEncode, String toEncode) throws UnsupportedEncodingException {
		if (StringUtils.isEmpty(str)) {
			return StringUtils.EMPTY;
		} else {
			return new String(str.getBytes(fromEncode), toEncode);
		}
	}

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

	/**
	 * 获取从开始日期到结束日期的时间字符串，以天为单位，格式形如 yyyy-MM-dd
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static List<String> getDays(Date startTime, Date endTime) {
		List<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");// yyyy-
		Calendar cStart = Calendar.getInstance();
		Calendar cEnd = Calendar.getInstance();
		cStart.setTime(startTime);
		cEnd.setTime(endTime);
		result.add(sdf.format(startTime));
		while (cStart.get(Calendar.DAY_OF_YEAR) != cEnd.get(Calendar.DAY_OF_YEAR) || cStart.get(Calendar.MONTH) != cEnd.get(Calendar.MONTH)) {
			cStart.add(Calendar.DAY_OF_YEAR, 1);
			result.add(sdf.format(cStart.getTime()));
		}
		return result;
	}

	/**
	 * 获取某天的0点0分0秒的时间
	 * 
	 * @param time
	 * @return
	 */
	public static Date getStartDateOfDay(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(time);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return calendar.getTime();
	}

	/**
	 * 获取某天的23点59分59秒的时间
	 * 
	 * @param time
	 * @return
	 */
	public static Date getEndDateOfDay(Date time) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 58);
		return calendar.getTime();
	}

	/**
	 * 计算endTime,取0点0分0秒
	 */

	public static Date calEndTime(Date startTime, int advancedDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		calendar.add(Calendar.DAY_OF_MONTH, advancedDays);
		Date endTime = FormateUtils.getStartDateOfDay(calendar.getTime());
		return endTime;
	}

	public static String getTimeStampStr() {
		Date date = new Date();
		return String.valueOf(date.getTime());
	}

	public static String getDirByDay() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FILE_PATH_DATE_FORMAT);
		String result = sdf.format(new Date());
		return result;
	}

	public static String getDirByMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.FILE_PATH_MONTH_FORMAT);
		String result = sdf.format(new Date());
		return result;
	}
}
