package com.yunjia.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public  class DateUtil {
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat dateSlantingFormat = new SimpleDateFormat("yyyy/MM/dd");
	public static SimpleDateFormat dateCondenseFormat = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateTimeCondenseFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	
	public static LocalDateTime toLocalDateTime(Date date){
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
	}
	public static LocalDate toLocalDate(Date date){
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	public static LocalTime toLocalTime(Date date){
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
	}
	
	public static Date toDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	public static String minusWeeks(Date date, String pattern){
		return toLocalDateTime(date).minusWeeks(3).format(DateTimeFormatter.ofPattern(pattern));
	}
	
	public static Date parseDate(String d) throws ParseException {
		return dateFormat.parse(d);
	}

	public static Date parseDateTime(String d) throws ParseException {
		return dateTimeFormat.parse(d);
	}

	/**
	 * @param date
	 * @return "" if null, else yyyy-MM-dd string
	 */
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * @param date
	 * @return "" if null, else yyyy/MM/dd string
	 */
	public static String formatSlantingDate(Date date) {
		return dateSlantingFormat.format(date);
	}

	/**
	 * @param date
	 * @return "" if null, else yyyyMMdd string
	 */
	public static String formatCondenseDate(Date date) {
		return dateCondenseFormat.format(date);
	}

	/**
	 * @param date
	 * @return "" if null, else yyyyMMddHHmmssS string
	 */
	public static String formatCondenseDateTime(Date date) {
		return dateTimeCondenseFormat.format(date);
	}

	public static String formatDateTime(Date date) {
		return dateTimeFormat.format(date);
	}
	
	
	public static String getPlainFormatDatetime() {
		LocalDateTime now = LocalDateTime.now();
		StringBuilder ret = new StringBuilder();
		ret.append("D").append(now.getYear());
		if(now.getMonthValue()<10) {
			ret.append(0);
		}
		ret.append(now.getMonthValue());
		
		if(now.getDayOfMonth()<10) {
			ret.append(0);
		}
		ret.append(now.getDayOfMonth());
		
		ret.append("T");
		if(now.getHour()<10) {
			ret.append(0);
		}
		ret.append(now.getHour());
		
		if(now.getMinute()<10) {
			ret.append(0);
		}
		ret.append(now.getMinute());
		
		if(now.getSecond()<10) {
			ret.append(0);
		}
		ret.append(now.getSecond());
		return ret.toString();
	}

	public static void main(String[] args) {
		System.out.println(getPlainFormatDatetime());
		System.out.println(getPlainFormatDatetime().length());
	}
}
