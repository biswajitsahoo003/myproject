package com.tcl.dias.oms.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.oms.entity.entities.VwManualFeasibilityReport;


import com.tcl.dias.oms.entity.entities.VwQuoteFeasibilityOrderDetails;
import com.tcl.dias.oms.entity.repository.VwManualFeasibilityReportRepository;

import com.tcl.dias.oms.entity.repository.VwQuoteFeasibilityOrderDetailsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import com.tcl.dias.oms.entity.repository.VwManualFeasibilityReportRepository;


/**
 * 
 * This file contains the Service class for Manual Feasiblity report.
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ReportService {

	@Autowired
	VwManualFeasibilityReportRepository vwManualFeasibilityReportRepository;
	
	//@Autowired
	//VwOrderToDeliveryTrackingRepository vwOrderToDeliveryTrackingRepository;
	
	@Autowired
	VwQuoteFeasibilityOrderDetailsRepository vwQuoteFeasibilityOrderDetailsRepository;
	
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ReportService.class);




	/**
	 * Get details for Manual Feasiblity Report
	 * 
	 * @author ANANDHI VIJAY
	 * 
	 * @return
	 */

	public void getManualFeasiblityReport(HttpServletResponse response) throws IOException {
		List<VwManualFeasibilityReport> vwManualFeasibilityReports = vwManualFeasibilityReportRepository.findAll();

		XSSFWorkbook workbook = new XSSFWorkbook();
		if (vwManualFeasibilityReports != null && !vwManualFeasibilityReports.isEmpty()) {
			Sheet sheet = workbook.createSheet();

			sheet.setColumnWidth(50000, 50000);
			Row headerRow = sheet.createRow(0);
			Cell cell = headerRow.createCell(0);
			cell.setCellValue("Quote Id");
			cell = headerRow.createCell(1);
			cell.setCellValue("Quote Code");
			cell = headerRow.createCell(2);
			cell.setCellValue("Product Family");
			cell = headerRow.createCell(3);
			cell.setCellValue("Product Name");
			cell = headerRow.createCell(4);
			cell.setCellValue("Site Code");
			cell = headerRow.createCell(5);
			cell.setCellValue("Port speed in mbps");
			cell = headerRow.createCell(6);
			cell.setCellValue("Local Loop speed in mbps");
			cell = headerRow.createCell(7);
			cell.setCellValue("Customer Name");
			cell = headerRow.createCell(8);
			cell.setCellValue("User Name");
			cell = headerRow.createCell(9);
			cell.setCellValue("Email ID");
			cell = headerRow.createCell(10);
			cell.setCellValue("Address");
			cell = headerRow.createCell(11);
			cell.setCellValue("Lcon Name");
			cell = headerRow.createCell(12);
			cell.setCellValue("Lcon Contact Number");
			int rowCount[] = {1};
			vwManualFeasibilityReports.stream().forEach(aBook->{
				Row row = sheet.createRow(rowCount[0]);
				writeBook(aBook, row);
				rowCount[0]++;
			});
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		String fileName = "ManualFeasiblityReport.xlsx";
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

		outByteStream.close();
	}

	private void writeBook(VwManualFeasibilityReport aBook, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(aBook.getQuoteId());

		cell = row.createCell(1);
		cell.setCellValue(aBook.getQuoteCode());

		cell = row.createCell(2);
		cell.setCellValue(aBook.getProductFamily());

		cell = row.createCell(3);
		cell.setCellValue(aBook.getProductName());

		cell = row.createCell(4);
		cell.setCellValue(aBook.getSiteCode());

		cell = row.createCell(5);
		cell.setCellValue(aBook.getPortSpeedInMbps());

		cell = row.createCell(6);
		cell.setCellValue(aBook.getLocalLoopSpeedInMbps());

		cell = row.createCell(7);
		cell.setCellValue(aBook.getCustomerName());

		cell = row.createCell(8);
		cell.setCellValue(aBook.getUsername());

		cell = row.createCell(9);
		cell.setCellValue(aBook.getEmailId());

		cell = row.createCell(10);
		cell.setCellValue(aBook.getAddress());
		
		cell = row.createCell(11);
		cell.setCellValue(aBook.getLconName());
		
		cell = row.createCell(12);
		cell.setCellValue(aBook.getLconContactNumber());

	}

	/*
	 * private void writeBookOrderTrack(VwOrderToDeliveryTracking oBook, Row row) {
	 * 
	 * Cell cell = row.createCell(0); cell.setCellValue(oBook.getOrderId());
	 * 
	 * cell = row.createCell(1); cell.setCellValue(oBook.getAccount_manager_name());
	 * 
	 * cell = row.createCell(2); cell.setCellValue(oBook.getCustomerName());
	 * 
	 * cell = row.createCell(3); DateFormat outputFormatter = new
	 * SimpleDateFormat("MM/dd/yyyy"); if(oBook.getDocusignSentDate()!=null) {
	 * String output = outputFormatter.format(oBook.getDocusignSentDate());
	 * cell.setCellValue(output); }else {
	 * cell.setCellValue(oBook.getDocusignSentDate()); }
	 * 
	 * cell = row.createCell(4); cell.setCellValue(oBook.getFeasibilityCheck());
	 * 
	 * cell = row.createCell(5); if(oBook.getLocal_Loop_speed_in_mbps()==null) {
	 * cell.setCellValue(0); } else {
	 * cell.setCellValue(oBook.getLocal_Loop_speed_in_mbps()); }
	 * 
	 * 
	 * cell = row.createCell(6); cell.setCellValue(oBook.getM6ServiceID());
	 * 
	 * cell = row.createCell(7);
	 * cell.setCellValue(oBook.getOrderConfirmationMode());
	 * 
	 * 
	 * cell = row.createCell(8); if(oBook.getOrderPlacedDate()!=null) { String
	 * outputPlace = outputFormatter.format(oBook.getOrderPlacedDate());
	 * cell.setCellValue(outputPlace); } else {
	 * cell.setCellValue(oBook.getOrderPlacedDate()); } cell = row.createCell(9);
	 * cell.setCellValue(oBook.getOrder_ref_ID());
	 * 
	 * cell = row.createCell(10); cell.setCellValue(oBook.getOrderStatus());
	 * 
	 * cell = row.createCell(11); if(oBook.getPort_speed_in_mbps()==null) {
	 * cell.setCellValue(0); } else {
	 * cell.setCellValue(oBook.getPort_speed_in_mbps()); }
	 * 
	 * cell = row.createCell(12); cell.setCellValue(oBook.getProductFamily());
	 * 
	 * cell = row.createCell(13); cell.setCellValue(oBook.getProductName());
	 * 
	 * cell = row.createCell(14); cell.setCellValue(oBook.getProvider());
	 * 
	 * cell = row.createCell(15); cell.setCellValue(oBook.getSFDC_oppty_ID());
	 * 
	 * cell = row.createCell(16); cell.setCellValue(oBook.getSiteCode());
	 * 
	 * cell = row.createCell(17);
	 * 
	 * if(oBook.getSiteCreatedDate()!=null) { String outputCreate =
	 * outputFormatter.format(oBook.getSiteCreatedDate());
	 * cell.setCellValue(outputCreate); } else {
	 * cell.setCellValue(oBook.getSiteCreatedDate()); }
	 * 
	 * 
	 * 
	 * cell = row.createCell(18);
	 * 
	 * if(oBook.getSiteEffectiveDate()!=null) { String outputeffective =
	 * outputFormatter.format(oBook.getSiteEffectiveDate());
	 * cell.setCellValue(outputeffective); } else {
	 * cell.setCellValue(oBook.getSiteEffectiveDate()); }
	 * 
	 * 
	 * cell = row.createCell(19); cell.setCellValue(oBook.getSiteLevelStatus());
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * }
	 */

	/**
	 * Get details for Order Tracking Report
	 * 
	 * @author MUTHUSELVI S
	 * 
	 * @return
	 */
	
	/*
	 * public void getOrderToDeliveryTracking(HttpServletResponse response) throws
	 * IOException { List<VwOrderToDeliveryTracking>
	 * vwOrderToDeliveryTrackingReports =
	 * vwOrderToDeliveryTrackingRepository.findAll();
	 * 
	 * XSSFWorkbook workbook = new XSSFWorkbook(); if
	 * (vwOrderToDeliveryTrackingReports != null &&
	 * !vwOrderToDeliveryTrackingReports.isEmpty()) { Sheet sheet =
	 * workbook.createSheet();
	 * 
	 * sheet.setColumnWidth(50000, 50000); Row headerRow = sheet.createRow(0); Cell
	 * cell = headerRow.createCell(0); cell.setCellValue("Order Id"); cell =
	 * headerRow.createCell(1); cell.setCellValue("Account Manager Name"); cell =
	 * headerRow.createCell(2); cell.setCellValue("Customer Name"); cell =
	 * headerRow.createCell(3); cell.setCellValue("Docusign Sent Date"); cell =
	 * headerRow.createCell(4); cell.setCellValue("Feasibility Check"); cell =
	 * headerRow.createCell(5); cell.setCellValue("Local Loop Speed in mbps"); cell
	 * = headerRow.createCell(6); cell.setCellValue("M6Service ID"); cell =
	 * headerRow.createCell(7); cell.setCellValue("Order Confirmation Mode"); cell =
	 * headerRow.createCell(8); cell.setCellValue("Order Placed Date"); cell =
	 * headerRow.createCell(9); cell.setCellValue("Order Ref ID"); cell =
	 * headerRow.createCell(10); cell.setCellValue("Order Status"); cell =
	 * headerRow.createCell(11); cell.setCellValue("Port speed in mbps"); cell =
	 * headerRow.createCell(12); cell.setCellValue("Product Family"); cell =
	 * headerRow.createCell(13); cell.setCellValue("Product Name"); cell =
	 * headerRow.createCell(14); cell.setCellValue("Provider"); cell =
	 * headerRow.createCell(15); cell.setCellValue("SFDC Oppty ID"); cell =
	 * headerRow.createCell(16); cell.setCellValue("Site Code"); cell =
	 * headerRow.createCell(17); cell.setCellValue("Site Created Date"); cell =
	 * headerRow.createCell(18); cell.setCellValue("Site Effective Date"); cell =
	 * headerRow.createCell(19); cell.setCellValue("Site Level Status"); int
	 * rowCount[] = {1}; vwOrderToDeliveryTrackingReports.stream().forEach(oBook->{
	 * Row row = sheet.createRow(rowCount[0]); writeBookOrderTrack(oBook, row);
	 * rowCount[0]++; }); } ByteArrayOutputStream bos = new ByteArrayOutputStream();
	 * workbook.write(bos); ByteArrayOutputStream outByteStream = new
	 * ByteArrayOutputStream(); workbook.write(outByteStream); byte[] outArray =
	 * outByteStream.toByteArray(); String fileName =
	 * "OrderToDeliveryTrackingReports.xlsx"; response.reset();
	 * response.setContentType(
	 * "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	 * 
	 * response.setContentLength(outArray.length); response.setHeader("Expires:",
	 * "0"); response.setHeader("Content-Disposition", "attachment; filename=\"" +
	 * fileName + "\""); workbook.close(); try { FileCopyUtils.copy(outArray,
	 * response.getOutputStream()); } catch (Exception e) {
	 * 
	 * }
	 * 
	 * outByteStream.close(); }
	 */

	/**
	 * returnMisReportExcel - method holding functionality to export MIS Reporting for Access Team
	 * Excel
	 * 
	 * @param response
	 * @param fromDate
	 * @param toDate
	 * @throws IOException
	 * @throws TclCommonException
	 */

	public void returnMisReportExcel(HttpServletResponse response,Date fromDate,Date toDate) throws IOException, TclCommonException {
		List<VwQuoteFeasibilityOrderDetails> vwQuoteFeasibilityOrderDetailsReport = new ArrayList<>();
		if(Objects.isNull(fromDate) && Objects.isNull(toDate)) {
			throw new TclCommonException("Invalid Date", ResponseResource.R_CODE_ERROR);
		}
		Timestamp timeStampFromDate = new Timestamp(fromDate.getTime());
		SimpleDateFormat formatterFromDate = new SimpleDateFormat("yyyy/MM/dd 00:00:00");  
		String formatFromDate = formatterFromDate.format(timeStampFromDate);
		Timestamp timeStampToDate = new Timestamp(toDate.getTime());
		SimpleDateFormat formatterToDate = new SimpleDateFormat("yyyy/MM/dd 23:59:59");  
		String formatToDate = formatterToDate.format(timeStampToDate);
		vwQuoteFeasibilityOrderDetailsReport = vwQuoteFeasibilityOrderDetailsRepository.
				findAllByQuoteCreatedTime(formatFromDate, formatToDate);
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (vwQuoteFeasibilityOrderDetailsReport != null) {
			Sheet sheet = workbook.createSheet();

			sheet.setColumnWidth(50000, 50000);
			Row headerRow = sheet.createRow(0);
			Cell cell = headerRow.createCell(0);
			cell.setCellValue("Quote Id");
			cell = headerRow.createCell(1);
			cell.setCellValue("Quote Code");
			cell = headerRow.createCell(2);
			cell.setCellValue("Quote Created Time");
			cell = headerRow.createCell(3);
			cell.setCellValue("id");
			cell = headerRow.createCell(4);
			cell.setCellValue("Site Code");
			cell = headerRow.createCell(5);
			cell.setCellValue("Fp Status");
			cell = headerRow.createCell(6);
			cell.setCellValue("Feasibility Code");
			cell = headerRow.createCell(7);
			cell.setCellValue("Feasibility Mode");
			cell = headerRow.createCell(8);
			cell.setCellValue("Provider");
			cell = headerRow.createCell(9);
			cell.setCellValue("Feasibility Check");
			cell = headerRow.createCell(10);
			cell.setCellValue("Feasibility Selected");
			cell = headerRow.createCell(11);
			cell.setCellValue("Task Def Key");
			cell = headerRow.createCell(12);
			cell.setCellValue("Status");
			cell = headerRow.createCell(13);
			cell.setCellValue("Created Time");
			cell = headerRow.createCell(14);
			cell.setCellValue("Updated Time");
			cell = headerRow.createCell(15);
			cell.setCellValue("Completed Time");
			cell = headerRow.createCell(16);
			cell.setCellValue("Subject");
			cell = headerRow.createCell(17);
			cell.setCellValue("Assigned Group");
			cell = headerRow.createCell(18);
			cell.setCellValue("Feasibility Status");
			cell = headerRow.createCell(19);
			cell.setCellValue("Task Selected");
			cell = headerRow.createCell(20);
			cell.setCellValue("Feasibility Response Date");
			cell = headerRow.createCell(21);
			cell.setCellValue("Final Last Mile Provider");
			cell = headerRow.createCell(22);
			cell.setCellValue("Order Code");
			cell = headerRow.createCell(23);
			cell.setCellValue("Order Created Time");
			cell = headerRow.createCell(24);
			cell.setCellValue("BW in Mbps");
			cell = headerRow.createCell(25);
			cell.setCellValue("Mast 3Km Avg Mast Ht");
			int rowCount[] = {1};
			if(!vwQuoteFeasibilityOrderDetailsReport.isEmpty()) {
				vwQuoteFeasibilityOrderDetailsReport.stream().forEach(aBook->{
					Row row = sheet.createRow(rowCount[0]);
					writeMisBook(aBook, row);
					rowCount[0]++;
				});
			}
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		workbook.write(outByteStream);
		byte[] outArray = outByteStream.toByteArray();
		String fileName = "MIS Report.xlsx";
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

		outByteStream.close();
	}

	private void writeMisBook(VwQuoteFeasibilityOrderDetails aBook, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(aBook.getQuoteId());

		cell = row.createCell(1);
		cell.setCellValue(aBook.getQuoteCode());

		cell = row.createCell(2);
		cell.setCellValue("'"+aBook.getQuoteCreatedTime());

		cell = row.createCell(3);
		cell.setCellValue(aBook.getId());

		cell = row.createCell(4);
		cell.setCellValue(aBook.getSiteCode());

		cell = row.createCell(5);
		cell.setCellValue(aBook.getFpStatus());

		cell = row.createCell(6);
		cell.setCellValue(aBook.getFeasibilityCode());

		cell = row.createCell(7);
		cell.setCellValue(aBook.getFeasibilityMode());

		cell = row.createCell(8);
		cell.setCellValue(aBook.getProvider());

		cell = row.createCell(9);
		cell.setCellValue(aBook.getFeasibilityCheck());

		cell = row.createCell(10);
		cell.setCellValue(aBook.getFeasibilitySelected());
		
		cell = row.createCell(11);
		cell.setCellValue(aBook.getTaskDefKey());
		
		cell = row.createCell(12);
		cell.setCellValue(aBook.getStatus());
		
		cell = row.createCell(13);
		if(aBook.getCreatedTime() != null) {
			cell.setCellValue("'"+aBook.getCreatedTime());
		}else {
			cell.setCellValue(aBook.getCreatedTime());
		}
		
		cell = row.createCell(14);
		if(aBook.getCreatedTime() != null) {
			cell.setCellValue("'"+aBook.getUpdatedTime());
		}else {
			cell.setCellValue(aBook.getUpdatedTime());
		}
		
		cell = row.createCell(15);
		if(aBook.getCreatedTime() != null) {	
			cell.setCellValue("'"+aBook.getCompletedTime());
		}else {
			cell.setCellValue(aBook.getCompletedTime());
		}
		
		cell = row.createCell(16);
		cell.setCellValue(aBook.getSubject());
		
		cell = row.createCell(17);
		cell.setCellValue(aBook.getAssignedGroup());
		
		cell = row.createCell(18);
		cell.setCellValue(aBook.getFeasibilityStatus());
		
		cell = row.createCell(19);
		cell.setCellValue(aBook.getTaskSelected());
		
		cell = row.createCell(20);
		if(aBook.getFeasibilityResponseDate() != null) {	
			cell.setCellValue("'"+aBook.getFeasibilityResponseDate());
		}else {
			cell.setCellValue(aBook.getFeasibilityResponseDate());
		}
		
		cell = row.createCell(21);
		cell.setCellValue(aBook.getFinalLastMileProvider());
		
		cell = row.createCell(22);
		cell.setCellValue(aBook.getOrderCode());
		
		cell = row.createCell(23);
		cell.setCellValue(aBook.getOrderCreatedTime());
		
		cell = row.createCell(24);
		cell.setCellValue(aBook.getBwInMbps());
		
		cell = row.createCell(25);
		cell.setCellValue(aBook.getMast3kmAvgMstHt());

	}
}


