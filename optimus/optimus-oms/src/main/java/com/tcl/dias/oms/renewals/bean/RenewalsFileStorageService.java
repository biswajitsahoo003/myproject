package com.tcl.dias.oms.renewals.bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class RenewalsFileStorageService {
	private static final Logger LOGGER = LoggerFactory.getLogger(RenewalsFileStorageService.class);


	
	public void createExcelTemplate(HttpServletResponse response, String productName) throws TclCommonException, IOException {
		XSSFWorkbook workbook = null ;
		String fileName ="";
		try {
		
		if(productName.equalsIgnoreCase("IAS")) {
			workbook=  downloadIasTemplate(response);
			fileName = "ias-template.xlsx";
		}else if(productName.equalsIgnoreCase("GVPN")) {
			workbook =downloadGvpnTemplate(response);
			fileName = "gvpn-template.xlsx";
		}else if(productName.equalsIgnoreCase("NPL")) {
			workbook =downloadNplTemplate(response);
			fileName = "npl-template.xlsx";
		}else {
			workbook =downloadNdeTemplate(response);
			fileName = "nde-template.xlsx";
		}
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);

		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		
		response.reset();
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {

		}

		outByteStream.flush();
		outByteStream.close();
		}catch (Exception e) {
			LOGGER.warn("Error in processing downloadBulkUploadUserTemplate {}", ExceptionUtils.getStackTrace(e));
			 throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e,
		     ResponseResource.R_CODE_ERROR);
		} finally {
			workbook.close();
		}
		
	}
	

	
	public XSSFWorkbook downloadIasTemplate(HttpServletResponse response) throws TclCommonException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			Sheet sheet = workbook.createSheet("With Commercial");

			CellStyle stylerowHeading = getStyle1(workbook);
			CellStyle stylerowHeading1 = getStyle2(workbook);
			Row row1 = sheet.createRow(0);
			Row row2 = sheet.createRow(1);
			Row row3 = sheet.createRow(2);

			row1.createCell(0).setCellValue("Service Id");
			row1.getCell(0).setCellStyle(stylerowHeading);


			row1.createCell(1).setCellValue("PO Number");
			row1.getCell(1).setCellStyle(stylerowHeading);
			

			row1.createCell(2).setCellValue("PO Date (dd-mm-yyyy)");
			row1.getCell(2).setCellStyle(stylerowHeading);
			
			row1.createCell(3).setCellValue("PRIMARY");
			row1.getCell(3).setCellStyle(stylerowHeading);

			row2.createCell(3).setCellValue("Internet Port");
			row2.getCell(3).setCellStyle(stylerowHeading1);

			row3.createCell(3).setCellValue("MRC");
			row3.createCell(4).setCellValue("NRC");
			row3.createCell(5).setCellValue("ARC");

			row2.createCell(6).setCellValue("CPE");
			row2.getCell(6).setCellStyle(stylerowHeading1);

			row3.createCell(6).setCellValue("MRC");
			row3.createCell(7).setCellValue("NRC");
			row3.createCell(8).setCellValue("ARC");

			row2.createCell(9).setCellValue("Last mile");
			row2.getCell(9).setCellStyle(stylerowHeading1);

			row3.createCell(9).setCellValue("MRC");
			row3.createCell(10).setCellValue("NRC");
			row3.createCell(11).setCellValue("ARC");

			row2.createCell(12).setCellValue("Addon");
			row2.getCell(12).setCellStyle(stylerowHeading1);

			row3.createCell(12).setCellValue("MRC");
			/* row3.createCell(13).setCellValue("NRC"); */
			row3.createCell(13).setCellValue("ARC");

			row2.createCell(14).setCellValue("Additional IPs");
			row2.getCell(14).setCellStyle(stylerowHeading1);

			row3.createCell(14).setCellValue("MRC");
//			row3.createCell(15).setCellValue("NRC");
			row3.createCell(15).setCellValue("ARC");

			row2.createCell(16).setCellValue("Mast Cost");
			row2.getCell(16).setCellStyle(stylerowHeading1);

			/*
			 * row3.createCell(17).setCellValue("MRC");
			 * row3.createCell(18).setCellValue("ARC");
			 */
			row3.createCell(16).setCellValue("NRC");  //euc

			row2.createCell(17).setCellValue("Burstable Bandwidth");
			row2.getCell(17).setCellStyle(stylerowHeading1);

			row3.createCell(17).setCellValue("NRC"); //EUC

			// sheet.addMergedRegion(new CellRangeAddress(1, 1, 18, 20));

			row1.createCell(18).setCellValue("SECONDARY");
			row1.getCell(18).setCellStyle(stylerowHeading);

			row2.createCell(18).setCellValue("Internet Port");
			row2.getCell(18).setCellStyle(stylerowHeading1);

			row3.createCell(18).setCellValue("MRC");
			row3.createCell(19).setCellValue("NRC");
			row3.createCell(20).setCellValue("ARC");

			row2.createCell(21).setCellValue("CPE");
			row2.getCell(21).setCellStyle(stylerowHeading1);

			row3.createCell(21).setCellValue("MRC");
			row3.createCell(22).setCellValue("NRC");
			row3.createCell(23).setCellValue("ARC");

			row2.createCell(24).setCellValue("Last mile");
			row2.getCell(24).setCellStyle(stylerowHeading1);

			row3.createCell(24).setCellValue("MRC");
			row3.createCell(25).setCellValue("NRC");
			row3.createCell(26).setCellValue("ARC");

			row2.createCell(27).setCellValue("Addon");
			row2.getCell(27).setCellStyle(stylerowHeading1);

			row3.createCell(27).setCellValue("MRC");
			/* row3.createCell(31).setCellValue("NRC"); */
			row3.createCell(28).setCellValue("ARC");

			row2.createCell(29).setCellValue("Additional IPs");
			row2.getCell(29).setCellStyle(stylerowHeading1);

			row3.createCell(29).setCellValue("MRC");
			row3.createCell(30).setCellValue("ARC");

			row2.createCell(31).setCellValue("Mast Cost");
			row2.getCell(31).setCellStyle(stylerowHeading1);

			/*
			 * row3.createCell(33).setCellValue("MRC");
			 * row3.createCell(34).setCellValue("ARC");
			 */
			row3.createCell(31).setCellValue("NRC"); //EUC

			row2.createCell(32).setCellValue("Burstable Bandwidth");
			row2.getCell(32).setCellStyle(stylerowHeading1);

			row3.createCell(32).setCellValue("NRC");
			
			row1.createCell(33).setCellValue("GST NUMBER");
			row1.getCell(33).setCellStyle(stylerowHeading);

			for (int i = 3; i <=row3.getPhysicalNumberOfCells()+2; i++) {
				if (row1.getCell(i) != null) {
					row1.getCell(i).setCellStyle(stylerowHeading);
				} else {
					row1.createCell(i);
					row1.getCell(i).setCellStyle(stylerowHeading);
				}

				if (row2.getCell(i) != null) {
					row2.getCell(i).setCellStyle(stylerowHeading1);
				} else {
					row2.createCell(i);
					row2.getCell(i).setCellStyle(stylerowHeading1);
				}

				if (row3.getCell(i) != null) {
					row3.getCell(i).setCellStyle(stylerowHeading1);
				} else {
					row3.createCell(i);
					row3.getCell(i).setCellStyle(stylerowHeading1);
				}

			}

			sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 17));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 8));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 9, 11));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 12, 13));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 14, 15));
		//	sheet.addMergedRegion(new CellRangeAddress(1, 1, 14, 17));
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 18, 32));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 18, 20));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 21, 23));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 24, 26));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 27, 28));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 29, 30));
		//	sheet.addMergedRegion(new CellRangeAddress(1, 1, 33, 35));
			
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 33, 33));
			return workbook;
			
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadBulkUploadUserTemplate {}", ExceptionUtils.getStackTrace(e));
			 throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e,
		     ResponseResource.R_CODE_ERROR);
		} 
	}
	
	
	
	public XSSFWorkbook downloadGvpnTemplate(HttpServletResponse response) throws TclCommonException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			Sheet sheet = workbook.createSheet("With Commercial");

			CellStyle stylerowHeading = getStyle1(workbook);
			CellStyle stylerowHeading1 = getStyle2(workbook);
			
			Row row1 = sheet.createRow(0);
			Row row2 = sheet.createRow(1);
			Row row3 = sheet.createRow(2);
			
			row1.createCell(0).setCellValue("Service Id");
			row1.getCell(0).setCellStyle(stylerowHeading);

			row1.createCell(1).setCellValue("PO Number");
			row1.getCell(1).setCellStyle(stylerowHeading);
			

			row1.createCell(2).setCellValue("PO Date (dd-mm-yyyy)");
			row1.getCell(2).setCellStyle(stylerowHeading);
			
			row1.createCell(3).setCellValue("PRIMARY");
			row2.createCell(3).setCellValue("VPN Port");
			row3.createCell(3).setCellValue("MRC");
			row3.createCell(4).setCellValue("NRC");
			row3.createCell(5).setCellValue("ARC");

			row2.createCell(6).setCellValue("CPE");
			row3.createCell(6).setCellValue("MRC");
			row3.createCell(7).setCellValue("NRC");
			row3.createCell(8).setCellValue("ARC");

			row2.createCell(9).setCellValue("Last mile");
			row3.createCell(9).setCellValue("MRC");
			row3.createCell(10).setCellValue("NRC");
			row3.createCell(11).setCellValue("ARC");


			row2.createCell(12).setCellValue("CPE Recovery Charges");
			row3.createCell(12).setCellValue("MRC");
			row3.createCell(13).setCellValue("NRC");
			row3.createCell(14).setCellValue("ARC");

			row2.createCell(15).setCellValue("Mast Cost");
			row3.createCell(15).setCellValue("NRC"); //euc

			row2.createCell(16).setCellValue("Burstable Bandwidth");
			row3.createCell(16).setCellValue("NRC"); //EUC

			// sheet.addMergedRegion(new CellRangeAddress(1, 1, 18, 20));

			row1.createCell(17).setCellValue("SECONDARY");
			
			row2.createCell(17).setCellValue("VPN Port");
			row3.createCell(17).setCellValue("MRC");
			row3.createCell(18).setCellValue("NRC");
			row3.createCell(19).setCellValue("ARC");

			row2.createCell(20).setCellValue("CPE");
			row3.createCell(20).setCellValue("MRC");
			row3.createCell(21).setCellValue("NRC");
			row3.createCell(22).setCellValue("ARC");

			row2.createCell(23).setCellValue("Last mile");
			row3.createCell(23).setCellValue("MRC");
			row3.createCell(24).setCellValue("NRC");
			row3.createCell(25).setCellValue("ARC");

			row2.createCell(26).setCellValue("CPE Recovery Charges");
			row3.createCell(26).setCellValue("MRC");
			row3.createCell(27).setCellValue("MRC");
			row3.createCell(28).setCellValue("ARC");

			row2.createCell(29).setCellValue("Mast Cost");
			row3.createCell(29).setCellValue("NRC"); //EUC

			row2.createCell(30).setCellValue("Burstable Bandwidth");
			row3.createCell(30).setCellValue("NRC");

			row1.createCell(31).setCellValue("GST NUMBER");
			row1.getCell(31).setCellStyle(stylerowHeading);
			
			for (int i = 3; i <=row3.getPhysicalNumberOfCells()+2; i++) {
				if (row1.getCell(i) != null) {
					row1.getCell(i).setCellStyle(stylerowHeading);
				} else {
					row1.createCell(i);
					row1.getCell(i).setCellStyle(stylerowHeading);
				}

				if (row2.getCell(i) != null) {
					row2.getCell(i).setCellStyle(stylerowHeading1);
				} else {
					row2.createCell(i);
					row2.getCell(i).setCellStyle(stylerowHeading1);
				}

				if (row3.getCell(i) != null) {
					row3.getCell(i).setCellStyle(stylerowHeading1);
				} else {
					row3.createCell(i);
					row3.getCell(i).setCellStyle(stylerowHeading1);
				}

			}

			sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 16));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 8));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 9, 11));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 12, 14));

			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 17, 30));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 17, 19));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 20, 22));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 23, 25));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 26, 28));
			
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 31, 31));
			
			return workbook;
			
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadBulkUploadUserTemplate {}", ExceptionUtils.getStackTrace(e));
			 throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e,
		     ResponseResource.R_CODE_ERROR);
		} 
	}
	
	public XSSFWorkbook downloadNplTemplate(HttpServletResponse response) throws TclCommonException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			Sheet sheet = workbook.createSheet("With Commercial");

			CellStyle stylerowHeading = getStyle1(workbook);
			CellStyle stylerowHeading1 =getStyle2(workbook);
			workbook.createCellStyle();
			
			Row row1 = sheet.createRow(0);
			Row row2 = sheet.createRow(1);
			Row row3 = sheet.createRow(2);

			row1.createCell(0).setCellValue("Service Id");
			row1.getCell(0).setCellStyle(stylerowHeading);
			
			row1.createCell(1).setCellValue("PO Number");
			row1.getCell(1).setCellStyle(stylerowHeading);
			

			row1.createCell(2).setCellValue("PO Date (dd-mm-yyyy)");
			row1.getCell(2).setCellStyle(stylerowHeading);

			row1.createCell(3).setCellValue("PRIMARY");
			row2.createCell(3).setCellValue("National Connectivity");
			row3.createCell(3).setCellValue("MRC");
			row3.createCell(4).setCellValue("NRC");
			row3.createCell(5).setCellValue("ARC");

			row2.createCell(6).setCellValue("Link Management Charges");
			row3.createCell(6).setCellValue("MRC");
			row3.createCell(7).setCellValue("NRC");
			row3.createCell(8).setCellValue("ARC");

			row1.createCell(9).setCellValue("GST NUMBER SITE-A");	
			row1.getCell(9).setCellStyle(stylerowHeading);
			row1.createCell(10).setCellValue("GST NUMBER SITE-B");
			row1.getCell(10).setCellStyle(stylerowHeading);

			for (int i = 3; i <=row3.getPhysicalNumberOfCells()+2; i++) {
				if (row1.getCell(i) != null) {
					row1.getCell(i).setCellStyle(stylerowHeading);
				} else {
					row1.createCell(i);
					row1.getCell(i).setCellStyle(stylerowHeading);
				}

				if (row2.getCell(i) != null) {
					row2.getCell(i).setCellStyle(stylerowHeading1);
				} else {
					row2.createCell(i);
					row2.getCell(i).setCellStyle(stylerowHeading1);
				}

				if (row3.getCell(i) != null) {
					row3.getCell(i).setCellStyle(stylerowHeading1);
				} else {
					row3.createCell(i);
					row3.getCell(i).setCellStyle(stylerowHeading1);
				}

			}

			sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 8));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 8));
			
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 10, 10));
						
			return workbook;
			
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadBulkUploadUserTemplate {}", ExceptionUtils.getStackTrace(e));
			 throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e,
		     ResponseResource.R_CODE_ERROR);
		} 
	}
	
	public XSSFWorkbook downloadNdeTemplate(HttpServletResponse response) throws TclCommonException, IOException {
		XSSFWorkbook workbook = new XSSFWorkbook();
		try {

			Sheet sheet = workbook.createSheet("With Commercial");

			CellStyle stylerowHeading = getStyle1(workbook);
			CellStyle stylerowHeading1 =getStyle2(workbook);
			workbook.createCellStyle();
			
			Row row1 = sheet.createRow(0);
			Row row2 = sheet.createRow(1);
			Row row3 = sheet.createRow(2);

			row1.createCell(0).setCellValue("Service Id");
			row1.getCell(0).setCellStyle(stylerowHeading);

			row1.createCell(1).setCellValue("PO Number");
			row1.getCell(1).setCellStyle(stylerowHeading);			

			row1.createCell(2).setCellValue("PO Date (dd-mm-yyyy)");
			row1.getCell(2).setCellStyle(stylerowHeading);
			
			row1.createCell(3).setCellValue("PRIMARY");
			row2.createCell(3).setCellValue("National Connectivity");
			row3.createCell(3).setCellValue("MRC");
			row3.createCell(4).setCellValue("NRC");
			row3.createCell(5).setCellValue("ARC");

			row2.createCell(6).setCellValue("Link Management Charges");
			row3.createCell(6).setCellValue("MRC");
			row3.createCell(7).setCellValue("NRC");
			row3.createCell(8).setCellValue("ARC");
    
			row1.createCell(9).setCellValue("GST NUMBER SITE-A");	
			row1.getCell(9).setCellStyle(stylerowHeading);
			row1.createCell(10).setCellValue("GST NUMBER SITE-B");
			row1.getCell(10).setCellStyle(stylerowHeading);
						

			for (int i = 3; i <=row3.getPhysicalNumberOfCells()+2; i++) {
				if (row1.getCell(i) != null) {
					row1.getCell(i).setCellStyle(stylerowHeading);
				} else {
					row1.createCell(i);
					row1.getCell(i).setCellStyle(stylerowHeading);
				}

				if (row2.getCell(i) != null) {
					row2.getCell(i).setCellStyle(stylerowHeading1);
				} else {
					row2.createCell(i);
					row2.getCell(i).setCellStyle(stylerowHeading1);
				}

				if (row3.getCell(i) != null) {
					row3.getCell(i).setCellStyle(stylerowHeading1);
				} else {
					row3.createCell(i);
					row3.getCell(i).setCellStyle(stylerowHeading1);
				}

			}

			sheet.addMergedRegion(new CellRangeAddress(0, 2, 0, 0));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 1, 1));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 2, 2));
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 3, 8));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 5));
			sheet.addMergedRegion(new CellRangeAddress(1, 1, 6, 8));
			
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 9, 9));
			sheet.addMergedRegion(new CellRangeAddress(0, 2, 10, 10));
						
			return workbook;
			
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadBulkUploadUserTemplate {}", ExceptionUtils.getStackTrace(e));
			 throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e,
		     ResponseResource.R_CODE_ERROR);
		}
	}
	public CellStyle getStyle1(XSSFWorkbook workbook ) {
		CellStyle stylerowHeading = workbook.createCellStyle();
		workbook.createCellStyle();
		stylerowHeading.setBorderBottom(BorderStyle.MEDIUM);
		stylerowHeading.setBorderTop(BorderStyle.MEDIUM);
		stylerowHeading.setBorderLeft(BorderStyle.MEDIUM);
		stylerowHeading.setBorderRight(BorderStyle.MEDIUM);
		stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
		stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) 13);
		stylerowHeading.setFont(font);
		stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
		stylerowHeading.setAlignment(HorizontalAlignment.CENTER);
		stylerowHeading.setWrapText(true);
		return stylerowHeading;	
	}
	
	public CellStyle getStyle2(XSSFWorkbook workbook ) {
		CellStyle stylerowHeading = workbook.createCellStyle();
		workbook.createCellStyle();
		stylerowHeading.setBorderBottom(BorderStyle.MEDIUM);
		stylerowHeading.setBorderTop(BorderStyle.MEDIUM);
		stylerowHeading.setBorderLeft(BorderStyle.MEDIUM);
		stylerowHeading.setBorderRight(BorderStyle.MEDIUM);
		stylerowHeading.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		XSSFFont font = workbook.createFont();
		font.setBold(true);
		font.setFontName(HSSFFont.FONT_ARIAL);
		font.setFontHeightInPoints((short) 13);
		stylerowHeading.setFont(font);
		stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
		stylerowHeading.setAlignment(HorizontalAlignment.CENTER);
		stylerowHeading.setWrapText(true);
		return stylerowHeading;	
	}

}
