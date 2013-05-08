package com.source3g.hermes.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class DateFormatUtilsTest {
	@Test
	public void testGetDays() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = sdf.parse("2012-05-01");
		Date end = sdf.parse("2012-12-05");
		List<String> result = FormateUtils.getDays(start, end);
		for (String s : result) {
			System.out.println(s);
		}
		System.out.println(result.size());
		Assert.assertEquals(result.size(), 219);
	}
	
	@Test
	public void testGetDayTime(){
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String timeFormat=sdf.format(calendar.getTime());
		System.out.println(timeFormat);
		System.out.println(calendar.getTime().getTime());
	    Assert.assertEquals( true,timeFormat.contains("00:00:00"));
	}
}
