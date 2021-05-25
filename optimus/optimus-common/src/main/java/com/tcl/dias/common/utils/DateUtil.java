package com.tcl.dias.common.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This file contains the DateUtil.java class. used for date conversion
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class DateUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

	static final String HYPHEN_TYPE = "yyyy-MM-dd";
	static final String SLASH_TYPE = "dd/MM/yyyy";
	static final String HYPEN_TYPE_IN_ORDER = "dd-MM-yyyy";
	static final String DD_MMM_YYYY = "dd-MMM-yyyy";
	static final String TIME_STAMP="yyyy-MM-dd HH:mm:ss zzz";
	static final String YY_MM_DD_MINUTUES = "yyyymmddhhmmss";
	static final String YY_MM_DD_ = "yyyymmdd";
	static final String DD_MM_YY = "dd-MM-yy";

	public static String convertDateToString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(HYPHEN_TYPE);
		return df.format(date);

	}

	public static String convertDateToStringWithTime(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(YY_MM_DD_MINUTUES);
		return df.format(date);

	}
	
	

	public static String convertDateWithoutHypen(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(HYPHEN_TYPE);
		return df.format(date).replaceAll("-", "");

	}

	public static String convertDateToSlashString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(SLASH_TYPE);
		return df.format(date);

	}

	public static String convertDateToMMMString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(DD_MMM_YYYY);
		return df.format(date);

	}
	
	public static String convertDateToTimeStamp(Date date) {
		SimpleDateFormat df = new SimpleDateFormat(TIME_STAMP);
		return df.format(date);

	}

	public static String generateUidForSap(int length, String code) {

		StringBuilder builder = new StringBuilder();
		builder.append("OPTIMUS_");
		builder.append(code);
		builder.append(RandomStringUtils.random(length, true, true).toUpperCase());
		String time = convertDateToStringWithTime(new Date());
		time.replaceAll("-", "").replaceAll(":", "");
		builder.append(time);
		return builder.toString().toUpperCase();
	}

	public static Date convertStringToDate(String date) {
		Date formattedDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat(HYPEN_TYPE_IN_ORDER);
		try {
			formattedDate = df.parse(df.format(date));
		} catch (ParseException e) {
			LOGGER.error("Error in parsing date", e);
		}
		return formattedDate;
	}
	
	public static Date convertStringToDateYYMMDD(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			return df.parse(date);
		} catch (ParseException e) {
			LOGGER.error("Error in parsing date", e);
		}
		return null;
	}
	
	public static Timestamp convertStringToTimeStampYYMMDD(String date) {
		if(date==null) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
		try {
			Date actualDate =  df.parse(date);
			return new Timestamp(actualDate.getTime());
		} catch (ParseException e) {
			LOGGER.error("Error in parsing date", e);
		}
		return null;
	}
	
	public static Date convertStringToDateYYMMDDIfNUllReturnDate(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (date != null) {
				return df.parse(date);
			} else {
				return new Date();
			}
		} catch (ParseException e) {
			LOGGER.error("Error in parsing date", e);
		}
		return new Date();
	}
	


	
	public static String formatWithoutTimeStamp(Date date) {

		SimpleDateFormat formatter = new SimpleDateFormat(DD_MM_YY);
		return formatter.format(date);
	}

	public static Timestamp convertStringToTimestamp(String date) {
		Timestamp timeStampDate =null;
		try {
			if (date != null) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				timeStampDate = new Timestamp(formatter.parse(date).getTime());
			} 
		} catch (ParseException e) {
			LOGGER.error("Error in convertStringToTimestamp", e);
		}
		return timeStampDate;
	}
	
	
	public static String convertTimestampToDate(Timestamp inputTimestamp) {
		String outputDate=null;
		if (inputTimestamp != null) {
			Date date = inputTimestamp;
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			outputDate = formatter.format(date);
		} 
		return outputDate;
	}
	
	public static Timestamp convertDateStringDDMMMYYYYToIncTimestampByMonth(String inputDate,Integer inputMonth) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
		LocalDate localDate = LocalDate.parse(inputDate, formatter).plus(inputMonth, ChronoUnit.MONTHS);
		return Timestamp.valueOf(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localDate.atStartOfDay()));
	}
}
