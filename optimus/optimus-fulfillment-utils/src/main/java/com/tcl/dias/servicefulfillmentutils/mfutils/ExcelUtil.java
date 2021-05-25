package com.tcl.dias.servicefulfillmentutils.mfutils;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

public class ExcelUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

	public static final String GETTER_PREFIX = "get";
	public static final String ERR_IN_EXCEL_CREATION = "error in generating excel sheet";
	
	
	/**
	 * @author KRUTSRIN A static class to initialize row and column count for excel
	 *         generation.
	 */
	public static class Context {
		Integer rowCount = 0;
		Integer columnCount = 0;
		Row row;
	}

	/**
	 * A Generic template to write an excel file.
	 *
	 * @author KRUTSRIN
	 * @param beanList
	 * @param sheetName
	 * @param excelFileName
	 * @param response
	 * @return byteArray
	 * @throws Exception
	 */
	public static <T> byte[] writeXLSXFile(List<T> beanList, String feasibilityType,String sheetName, String excelFileName, String message)
			throws TclCommonException, Exception {
		
		XSSFWorkbook wb = new XSSFWorkbook();
		XSSFSheet sheet = wb.createSheet(sheetName);

		// context class obj for initializing row and rowCount
		Context context = new Context();

		// Create a Font for styling header cells
		CellStyle headerCellStyle1 = beautifyHeader(wb,IndexedColors.LIGHT_GREEN.getIndex());

		if (beanList == null || beanList.isEmpty()) {
			context.row = sheet.createRow(1);
			Cell cell = context.row.createCell(1);
			cell.setCellValue(message);
			cell.setCellStyle(headerCellStyle1);
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 10));

		} else {
			
			if(feasibilityType.equals("RADWIN")) {

			context.row = sheet.createRow(context.rowCount);
			Cell cell1 = context.row.createCell(0);
			cell1.setCellValue("Information");
			cell1.setCellStyle(headerCellStyle1);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
			
			Cell cell2 = context.row.createCell(14);
	        sheet.addMergedRegion(new CellRangeAddress(0, 0, 14, 19));
			cell2.setCellValue("Commercials");
			CellStyle headerCellStyle2= beautifyHeader(wb,IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
			cell2.setCellStyle(headerCellStyle2);
			
			Cell cell3 = context.row.createCell(20);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 20, 40));
			cell3.setCellValue("RF Response");
			CellStyle headerCellStyle3= beautifyHeader(wb,IndexedColors.LIGHT_YELLOW.getIndex());
			cell3.setCellStyle(headerCellStyle3);
			
			
			Cell cell4 = context.row.createCell(41);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 41, 44));
			cell4.setCellValue("Bh Details");
			CellStyle headerCellStyle4= beautifyHeader(wb,IndexedColors.GREY_25_PERCENT.getIndex());
			cell4.setCellStyle(headerCellStyle4);
			}
			else if(feasibilityType.equals("MAN/VBL"))
			{
				context.row = sheet.createRow(context.rowCount);
				Cell cell1 = context.row.createCell(0);
				cell1.setCellValue("Information");
				cell1.setCellStyle(headerCellStyle1);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));
				
				Cell cell2 = context.row.createCell(13);
		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 13, 19));
				cell2.setCellValue("Commercials");
				CellStyle headerCellStyle2= beautifyHeader(wb,IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
				cell2.setCellStyle(headerCellStyle2);
				
				Cell cell3 = context.row.createCell(20);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 20, 25));
				cell3.setCellValue("Capex");
				CellStyle headerCellStyle3= beautifyHeader(wb,IndexedColors.LIGHT_YELLOW.getIndex());
				cell3.setCellStyle(headerCellStyle3);
				
				
				Cell cell4 = context.row.createCell(26);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 26, 32));
				cell4.setCellValue("Pop Details");
				CellStyle headerCellStyle4= beautifyHeader(wb,IndexedColors.GREY_25_PERCENT.getIndex());
				cell4.setCellStyle(headerCellStyle4);
				
			}else if(feasibilityType.equals("Offnet Wireline")){
				context.row = sheet.createRow(context.rowCount);
				Cell cell1 = context.row.createCell(0);
				cell1.setCellValue("Task Information");
				cell1.setCellStyle(headerCellStyle1);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
				
				Cell cell2 = context.row.createCell(4);
		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 22));
				cell2.setCellValue("Response Information");
				CellStyle headerCellStyle2= beautifyHeader(wb,IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
				cell2.setCellStyle(headerCellStyle2);
				
				Cell cell3 = context.row.createCell(23);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 23, 26));
				cell3.setCellValue("Commercials");
				CellStyle headerCellStyle3= beautifyHeader(wb,IndexedColors.LIGHT_YELLOW.getIndex());
				cell3.setCellStyle(headerCellStyle3);
			}else if(feasibilityType.equals("Offnet Wireless")){
				context.row = sheet.createRow(context.rowCount);
				Cell cell1 = context.row.createCell(0);
				cell1.setCellValue("Task Information");
				cell1.setCellStyle(headerCellStyle1);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
				
				Cell cell2 = context.row.createCell(4);
		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 24));
				cell2.setCellValue("Response Information");
				CellStyle headerCellStyle2= beautifyHeader(wb,IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
				cell2.setCellStyle(headerCellStyle2);
				
				Cell cell3 = context.row.createCell(25);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 25, 27));
				cell3.setCellValue("Commercials");
				CellStyle headerCellStyle3= beautifyHeader(wb,IndexedColors.LIGHT_YELLOW.getIndex());
				cell3.setCellStyle(headerCellStyle3);
			}else if(feasibilityType.equals("Offnet Wireline")){
				context.row = sheet.createRow(context.rowCount);
				Cell cell1 = context.row.createCell(0);
				cell1.setCellValue("Task Information");
				cell1.setCellStyle(headerCellStyle1);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
				
				Cell cell2 = context.row.createCell(4);
		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 4, 22));
				cell2.setCellValue("Response Information");
				CellStyle headerCellStyle2= beautifyHeader(wb,IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
				cell2.setCellStyle(headerCellStyle2);
				
				Cell cell3 = context.row.createCell(23);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 23, 26));
				cell3.setCellValue("Commercials");
				CellStyle headerCellStyle3= beautifyHeader(wb,IndexedColors.LIGHT_YELLOW.getIndex());
				cell3.setCellStyle(headerCellStyle3);
			}else if(feasibilityType.equals("UBR PMP/WiMax")){
				context.row = sheet.createRow(context.rowCount);
				Cell cell1 = context.row.createCell(0);
				cell1.setCellValue("Information");
				cell1.setCellStyle(headerCellStyle1);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 13));
				
				Cell cell2 = context.row.createCell(14);
		        sheet.addMergedRegion(new CellRangeAddress(0, 0, 14, 17));
				cell2.setCellValue("Commercials");
				CellStyle headerCellStyle2= beautifyHeader(wb,IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
				cell2.setCellStyle(headerCellStyle2);
				
				Cell cell3 = context.row.createCell(18);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 38));
				cell3.setCellValue("RF Response");
				CellStyle headerCellStyle3= beautifyHeader(wb,IndexedColors.LIGHT_YELLOW.getIndex());
				cell3.setCellStyle(headerCellStyle3);
			}
			
			
			Class<? extends Object> classz = beanList.get(0).getClass();
			List<List<String>> fieldNamesList = getFieldNamesForClass(classz);

			context.rowCount = context.rowCount+1;

			context.row = sheet.createRow(context.rowCount);

			fieldNamesList.get(0).forEach(fieldName -> {
				Cell cell = context.row.createCell(context.columnCount++);
				cell.setCellValue(fieldName);
				Font headerFont = wb.createFont();
				headerFont.setBold(true);
				headerFont.setFontHeightInPoints((short) 10);
				CellStyle headerCellStyle = wb.createCellStyle();
				headerCellStyle.setFont(headerFont);
				cell.setCellStyle(headerCellStyle);

			});

			beanList.stream().forEach(t -> {
				context.rowCount = context.rowCount+1;
				context.row = sheet.createRow(context.rowCount);
				context.columnCount = 0;
				fieldNamesList.get(1).stream().forEach(fieldName -> {
					Cell cell = context.row.createCell(context.columnCount);
					Method method = null;
					try {
						method = classz.getMethod(GETTER_PREFIX.concat(capitalize(fieldName)));
					} catch (NoSuchMethodException nme) {
						try {
							method = classz.getMethod(GETTER_PREFIX.concat(fieldName));
						} catch (NoSuchMethodException | SecurityException e) {
							LOGGER.error(ERR_IN_EXCEL_CREATION, ExceptionUtils.getStackTrace(e));
						}
					}
					Object value = null;
					try {
						value = method.invoke(t, (Object[]) null);
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						LOGGER.error(ERR_IN_EXCEL_CREATION, ExceptionUtils.getStackTrace(e));
					}
					if (value != null) {
						if (value instanceof String) {
							cell.setCellValue((String) value);
						} else if (value instanceof Long) {
							cell.setCellValue((Long) value);
						} else if (value instanceof Integer) {
							cell.setCellValue((Integer) value);
						} else if (value instanceof Double) {
							cell.setCellValue((Double) value);
						} else {
							cell.setCellValue(String.valueOf(value));
						}
					}
					context.columnCount++;
				});
			});
		}
		// Resize all columns to fit the content size
		IntStream.range(0, context.columnCount).forEach(colCount -> {
			sheet.autoSizeColumn(colCount);
		});

		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		wb.write(outByteStream);
		outArray = outByteStream.toByteArray();
		wb.close();
		outByteStream.flush();
		outByteStream.close();
		return outArray;
	}


	private static CellStyle beautifyHeader(XSSFWorkbook wb,short bg) {
		Font headerFont = wb.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 10);

		// Create a CellStyle with the font
		CellStyle headerCellStyle = wb.createCellStyle();
		headerCellStyle.setFont(headerFont);
		headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
		headerCellStyle.setFillForegroundColor(bg);
		headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		return headerCellStyle;
	}

	
	// retrieve field names from a POJO class
		private static List<List<String>> getFieldNamesForClass(Class<?> clazz) throws Exception {

			List<List<String>> fieldLists = new ArrayList<List<String>>();
			List<String> captitalizedFieldNames = new ArrayList<String>();
			List<String> fieldNames = new ArrayList<String>();
			Field[] fields = clazz.getDeclaredFields();
			IntStream.range(0, fields.length).forEach(field -> {
				fieldNames.add(fields[field].getName());
				captitalizedFieldNames.add(StringUtils
						.join(StringUtils.splitByCharacterTypeCamelCase(fields[field].getName()), ' ').toUpperCase());
			});
			fieldLists.add(captitalizedFieldNames);
			fieldLists.add(fieldNames);
			return fieldLists;
		}
		
		// capitalize the first letter of the field name for retrieving value of the
		// field later
		private static String capitalize(String s) {
			if (s.length() == 0)
				return s;
			return s.substring(0, 1).toUpperCase() + s.substring(1);
		}

}
