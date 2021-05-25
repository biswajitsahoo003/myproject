package com.tcl.dias.servicehandover.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.tcl.dias.servicehandover.constants.Constants;
/**
 * TimeStampUtil - for formatting TimeStamp as per Geneva's requirement
 * 
 * @author yomagesh
 *
 */
public class TimeStampUtil {
	public static SimpleDateFormat formatWithTimeStamp() {
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.YYYY_MM_DD_HH_MM_SS);
		return formatter;

	}

	public static SimpleDateFormat formatWithoutTimeStamp() {

		SimpleDateFormat formatter1 = new SimpleDateFormat(Constants.DD_MMM_YYYY);
		return formatter1;
	}

	public static String poDateFormat(String attribute) throws ParseException {
		Date date = new SimpleDateFormat("dd-MM-yyyy").parse(attribute);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		return formatter.format(date).toUpperCase();
	}
	
	public static String poDateFormatCpe(String attribute) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(attribute);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
		return formatter.format(date).toUpperCase();
	}
	
	public static String formatWithTimeStampForComm(String commDate){
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.YYYY_MM_DD_HH_MM_SS);
		return formatter.format(date);

	}

	public static String formatWithTimeStampForCommMinusDays(String commDate, Integer days) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).minusDays(days);
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.YYYY_MM_DD_HH_MM_SS);
		return formatter.format(out);
	}
	public static String formatWithoutTimeStamp(String commDate) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);

	}

	public static String formatWithTimeStampForCommPlusDays(String commDate, Integer days) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusDays(days);
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.YYYY_MM_DD_HH_MM_SS);
		return formatter.format(out);

	}

	public static String formatWithTimeStampForCommMinusDaysForLR(String commDate,Integer days) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).minusDays(days);
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(out);

	}
	
	public static SimpleDateFormat formatWithoutTimeStampForLR() {

		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		return formatter1;
	}
	
	public static String formatTerminationDateForLR(String terminationDate){

		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(terminationDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(date);
		
	}
	
	public static String formatWithTimeStampForCommPlusDaysLR(String commDate,Integer days) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).plusDays(days);
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter.format(out);

	}
	
	public static LocalDateTime flowableISOTime(String terminationDate) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(terminationDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	
	}
	
	public static String optimusDateFormat(String attribute){
		Date date=null;
		try {
			date = new SimpleDateFormat(Constants.YYYY_MM_DD_HH_MM_SS).parse(attribute);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date).toUpperCase();
	}
	
	public static String commDateforAttributeString(String commDate) {
		Date date=null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DD_MMM_YYYY);
		return formatter.format(date);

	}
	
	public static String convertWithTimeStampToWithoutTimeStamp(String attribute) {
		Date date = null;
		try {
			date = new SimpleDateFormat(Constants.YYYY_MM_DD_HH_MM_SS).parse(attribute);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat formatter = new SimpleDateFormat(Constants.DD_MMM_YYYY);
		return formatter.format(date);
	}
	
	public static XMLGregorianCalendar xMLGregorianCalendar() {
		XMLGregorianCalendar xmlCal = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date());
		try {
			xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(date);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return xmlCal;
	}

	public static Timestamp formatStringToDate(String inpValue) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date formattedAttributeValue = sdf.parse(inpValue);
			return new Timestamp(formattedAttributeValue.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
}