package com.kekeinfo.web.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class DateConverter implements Converter<String, Date> {
	private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
	public Date convert(String text) {
		if (StringUtils.hasText(text)) {
			try {
				if (text.indexOf(":") == -1 && text.length() == 10) {
					return dateFormat.parse(text);
				} else if (text.indexOf(":") > 0 && text.length() == 19) {
					return datetimeFormat.parse(text);
				}else{
					throw new IllegalArgumentException("Could not parse date, date format is error ");
				}
			} catch (ParseException ex) {
				IllegalArgumentException iae = new IllegalArgumentException("Could not parse date: " + ex.getMessage());
				iae.initCause(ex);
				throw iae;
			}
		} else {
			return null;
		}
	}
	
	public String dateFormate(Date date){
		if (date == null)
			return null;
		
		return datetimeFormat.format(date);
	}
	
	public BigDecimal sub(Date date1,Date date2) throws ParseException {
		 Calendar cal = Calendar.getInstance();  
		 String d1=dateFormat.format(date1);
		 String d2=dateFormat.format(date2);
		 cal.setTime(dateFormat.parse(d1));
		 long time1 =cal.getTimeInMillis();
		 cal.setTime(dateFormat.parse(d2));
		long time2 = cal.getTimeInMillis();
		BigDecimal time = new BigDecimal(time1-time2);
		BigDecimal p = new BigDecimal(1000*60*60);
		time = time.divide(p);
		//time.setScale(2, BigDecimal.ROUND_HALF_UP);
		return time;
		
	}
	
	public boolean DateEq(Date date1,Date date2) throws ParseException{
		
		 Calendar cal = Calendar.getInstance();  
		 String d1=dateFormat.format(date1);
		 String d2=dateFormat.format(date2);
		 cal.setTime(dateFormat.parse(d1));
		 long time1 =cal.getTimeInMillis();
		 cal.setTime(dateFormat.parse(d2));
		long time2 = cal.getTimeInMillis();
		if(time1==time2){
			return true;
		}else{
			return false;
		}
		
	}
}
