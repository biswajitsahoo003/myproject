package com.tcl.dias.oms.service;

import com.tcl.dias.oms.entity.entities.VwManualFeasibilityReport;
import com.tcl.dias.oms.entity.repository.VwManualFeasibilityReportRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 
 * This file contains the Service class for Manual Feasiblity report.
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class ManualFeasiblityReportService {

	@Autowired
	VwManualFeasibilityReportRepository vwManualFeasibilityReportRepository;

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
}
