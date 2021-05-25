package com.tcl.dias.oms.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.tcl.dias.oms.entity.entities.VwCommercialReport;
import com.tcl.dias.oms.entity.repository.VwCommercialReportRepository;

/**
 * 
 * This file contains the Service class for Commercial report.
 *
 * @author Kavya Singh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class CommercialReportService {
	
	@Autowired
	VwCommercialReportRepository vwCommercialReportRepository;

	/**
	 * Get details for Commercial Report
	 * @return
	 */
	
	public void getCommercialReport(HttpServletResponse response) throws IOException{
		List<VwCommercialReport> vwCommercialReports = vwCommercialReportRepository.findAll();
		
		XSSFWorkbook workbook = new XSSFWorkbook();
		if (vwCommercialReports != null && !vwCommercialReports.isEmpty()) {
			Sheet sheet = workbook.createSheet();

			sheet.setColumnWidth(50000, 50000);
			Row headerRow = sheet.createRow(0);
			
			Cell cell = headerRow.createCell(0);
			cell.setCellValue("Order Id");
			cell = headerRow.createCell(1);
			cell.setCellValue("Site Code");
			cell = headerRow.createCell(2);
			cell.setCellValue("Order Version");
			cell = headerRow.createCell(3);
			cell.setCellValue("Product Name");
			cell = headerRow.createCell(4);
			cell.setCellValue("Tps Sfdc Copf Id");
			cell = headerRow.createCell(5);
			cell.setCellValue("Stage");
			cell = headerRow.createCell(6);
			cell.setCellValue("Actual Mrc");
			cell = headerRow.createCell(7);
			cell.setCellValue("Actual Nrc");
			cell = headerRow.createCell(8);
			cell.setCellValue("Actual Arc");
			cell = headerRow.createCell(9);
			cell.setCellValue("Actual Tcv");
			cell = headerRow.createCell(10);
			cell.setCellValue("Fp Status");
			cell = headerRow.createCell(11);
			cell.setCellValue("Price Mode");
			cell = headerRow.createCell(12);
			cell.setCellValue("Pricing Type");
			cell = headerRow.createCell(13);
			cell.setCellValue("Actual Internet Port Mrc");
			cell = headerRow.createCell(14);
			cell.setCellValue("Optimus Port Mrc");
			cell = headerRow.createCell(15);
			cell.setCellValue("Actual Internet Port Nrc");
			cell = headerRow.createCell(16);
			cell.setCellValue("Optimus Port Nrc");
			cell = headerRow.createCell(17);
			cell.setCellValue("Actual Internet Port Arc");
			cell = headerRow.createCell(18);
			cell.setCellValue("Optimus Port ARC");
			cell = headerRow.createCell(19);
			cell.setCellValue("Predicted Discount");
			cell = headerRow.createCell(20);
			cell.setCellValue("Actual Last Mile Mrc");
			cell = headerRow.createCell(21);
			cell.setCellValue("optimus Last Mile Cost MRC");
			cell = headerRow.createCell(22);
			cell.setCellValue("Actual Last Mile Nrc");
			cell = headerRow.createCell(23);
			cell.setCellValue("Optimus Last Mile Cost NRC");
			cell = headerRow.createCell(24);
			cell.setCellValue("Actual Last Mile Arc");
			cell = headerRow.createCell(25);
			cell.setCellValue("Optimus Last Mile Cost ARC");
			cell = headerRow.createCell(26);
			cell.setCellValue("Actual Cpe Mrc");
			cell = headerRow.createCell(27);
			cell.setCellValue("Optimus Cpe Mrc");
			cell = headerRow.createCell(28);
			cell.setCellValue("Actual Cpe Nrc");
			cell = headerRow.createCell(29);
			cell.setCellValue("Optimus Cpe Nrc");
			cell = headerRow.createCell(30);
			cell.setCellValue("Actual Cpe Arc");
			cell = headerRow.createCell(31);
			cell.setCellValue("Optimus Cpe Arc");
			cell = headerRow.createCell(32);
			cell.setCellValue("Actual Additional Ip Mrc");
			cell = headerRow.createCell(33);
			cell.setCellValue("Optimus Additional Ip Mrc");
			cell = headerRow.createCell(34);
			cell.setCellValue("Actual Additional Ip Arc");
			cell = headerRow.createCell(35);
			cell.setCellValue("Optimus Additional Ip Arc");
			cell = headerRow.createCell(36);
			cell.setCellValue("Actual Additional Ip Nrc");
			cell = headerRow.createCell(37);
			cell.setCellValue("Site Latitude");
			cell = headerRow.createCell(38);
			cell.setCellValue("Site Longitude");
			cell = headerRow.createCell(39);
			cell.setCellValue("Prospect Name");
			cell = headerRow.createCell(40);
			cell.setCellValue("Bw Mbps");
			cell = headerRow.createCell(41);
			cell.setCellValue("Burstable Bw");
			cell = headerRow.createCell(42);
			cell.setCellValue("City");
			cell = headerRow.createCell(43);
			cell.setCellValue("Customer Segment");
			cell = headerRow.createCell(44);
			cell.setCellValue("Sales Org");
			cell = headerRow.createCell(45);
			cell.setCellValue("Product Name Feasibility");
			cell = headerRow.createCell(46);
			cell.setCellValue("Feasibility Created Date");
			cell = headerRow.createCell(47);
			cell.setCellValue("Local Loop Interface");
			cell = headerRow.createCell(48);
			cell.setCellValue("Contract Term");
			cell = headerRow.createCell(49);
			cell.setCellValue("Quotetype Quote");
			cell = headerRow.createCell(50);
			cell.setCellValue("Connection Type");
			cell = headerRow.createCell(51);
			cell.setCellValue("Sum No Of Sites Uni Len");
			cell = headerRow.createCell(52);
			cell.setCellValue("Cpe Variant");
			cell = headerRow.createCell(53);
			cell.setCellValue("Cpe Management Type");
			cell = headerRow.createCell(54);
			cell.setCellValue("Cpe Supply Type");
			cell = headerRow.createCell(55);
			cell.setCellValue("Topology");
			cell = headerRow.createCell(56);
			cell.setCellValue("Sum Onnet Flag");
			cell = headerRow.createCell(57);
			cell.setCellValue("Sum Offnet Flag");
			cell = headerRow.createCell(58);
			cell.setCellValue("lm Arc Bw Onwl");
			cell = headerRow.createCell(59);
			cell.setCellValue("lm Nrc Bw Onwl");
			cell = headerRow.createCell(60);
			cell.setCellValue("lm Nrc Mux Onwl");
			cell = headerRow.createCell(61);
			cell.setCellValue("lm Nrc Inbldg Onwl");
			cell = headerRow.createCell(62);
			cell.setCellValue("lm Nrc Ospcapex Onwl");
			cell = headerRow.createCell(63);
			cell.setCellValue("lm Nrc Nerental Onwl");
			cell = headerRow.createCell(64);
			cell.setCellValue("lm Arc Bw Prov Ofrf");
			cell = headerRow.createCell(65);
			cell.setCellValue("lm Nrc Bw Prov Ofrf");
			cell = headerRow.createCell(66);
			cell.setCellValue("lm Nrc Bw Mast Ofrf");
			cell = headerRow.createCell(67);
			cell.setCellValue("lm Arc Bw Onrf");
			cell = headerRow.createCell(68);
			cell.setCellValue("lm Nrc Bw Onrf");
			cell = headerRow.createCell(69);
			cell.setCellValue("lm Nrc Bw Mast Onrf");
			cell = headerRow.createCell(70);
			cell.setCellValue("Orch Connection");
			cell = headerRow.createCell(71);
			cell.setCellValue("Orch Lm Type");
			cell = headerRow.createCell(72);
			cell.setCellValue("Additional Ip Flag");
			cell = headerRow.createCell(73);
			cell.setCellValue("ip Address Arrangement");
			cell = headerRow.createCell(74);
			cell.setCellValue("ipv4 Address Pool Size");
			cell = headerRow.createCell(75);
			cell.setCellValue("ipv6 Address Pool Size");
			cell = headerRow.createCell(76);
			cell.setCellValue("Account Rtm Cust");
			cell = headerRow.createCell(77);
			cell.setCellValue("Feasibility Code");
			cell = headerRow.createCell(78);
			cell.setCellValue("Feasibility Mode");
			cell = headerRow.createCell(79);
			cell.setCellValue("Feasibility Check");
			cell = headerRow.createCell(80);
			cell.setCellValue("Backup Port Requested");
			cell = headerRow.createCell(81);
			cell.setCellValue("No Of Sites In Opportunity");
			cell = headerRow.createCell(82);
			cell.setCellValue("Feasibility Status");
			cell = headerRow.createCell(83);
			cell.setCellValue("Bucket Adjustment Type");
			cell = headerRow.createCell(84);
			cell.setCellValue("Cpe Discount");
			cell = headerRow.createCell(85);
			cell.setCellValue("Is Selected");
			cell = headerRow.createCell(86);
			cell.setCellValue("Provider");
			cell = headerRow.createCell(87);
			cell.setCellValue("Rate Card");
			int rowCount[] = {1};
			vwCommercialReports.stream().forEach(aBook->{
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
		String fileName = "CommercialReport.xlsx";
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

	private void writeBook(VwCommercialReport aBook, Row row) {
		Cell cell = row.createCell(0);
		cell.setCellValue(aBook.getOrderId()==null?"":aBook.getOrderId());

		cell = row.createCell(1);
		cell.setCellValue(aBook.getSiteCode()==null?"":aBook.getSiteCode());

		cell = row.createCell(2);
		cell.setCellValue(aBook.getOrderVersion()==null?"":aBook.getOrderVersion());

		cell = row.createCell(3);
		cell.setCellValue(aBook.getProductName()==null?"":aBook.getProductName());

		cell = row.createCell(4);
		cell.setCellValue(aBook.getTpsSfdcCopfId()==null?"":aBook.getTpsSfdcCopfId());

		cell = row.createCell(5);
		cell.setCellValue(aBook.getStage()==null?"":aBook.getStage());

		cell = row.createCell(6);
		cell.setCellValue(aBook.getActualMrc()==null?"":aBook.getActualMrc());

		cell = row.createCell(7);
		cell.setCellValue(aBook.getActualNrc()==null?"":aBook.getActualNrc());

		cell = row.createCell(8);
		cell.setCellValue(aBook.getActualArc()==null?"":aBook.getActualArc());

		cell = row.createCell(9);
		cell.setCellValue(aBook.getActualTcv()==null?"":aBook.getActualTcv());  //10

		cell = row.createCell(10);
		cell.setCellValue(aBook.getFpStatus()==null?"":aBook.getFpStatus());
		
		cell = row.createCell(11);
		cell.setCellValue(aBook.getPriceMode()==null?"":aBook.getPriceMode());
		
		cell = row.createCell(12);
		cell.setCellValue(aBook.getPricingType()==null?"":aBook.getPricingType());
		
		cell = row.createCell(13);
		cell.setCellValue(aBook.getActualInternetportMrc()==null?"":aBook.getActualInternetportMrc());
		
		cell = row.createCell(14);
		cell.setCellValue(aBook.getOptimusPortMrc()==null?"":aBook.getOptimusPortMrc());
		
		cell = row.createCell(15);
		cell.setCellValue(aBook.getActualInternetportNrc()==null?"":aBook.getActualInternetportNrc());
		
		cell = row.createCell(16);
		cell.setCellValue(aBook.getOptimusPortNrc()==null?"":aBook.getOptimusPortNrc());
		
		cell = row.createCell(17);
		cell.setCellValue(aBook.getActualInternetportArc()==null?"":aBook.getActualInternetportArc());
		
		cell = row.createCell(18);
		cell.setCellValue(aBook.getOptimusPortARC()==null?"":aBook.getOptimusPortARC());
		
		cell = row.createCell(19);
		cell.setCellValue(aBook.getPredictedDiscount()==null?"":aBook.getPredictedDiscount());
		
		cell = row.createCell(20);
		cell.setCellValue(aBook.getActualLastMileMrc()==null?"":aBook.getActualLastMileMrc());
		
		cell = row.createCell(21);
		cell.setCellValue(aBook.getOptimusLastMileCostMRC()==null?"":aBook.getOptimusLastMileCostMRC());
		
		cell = row.createCell(22);
		cell.setCellValue(aBook.getActualLastMileNrc()==null?"":aBook.getActualLastMileNrc());
		
		cell = row.createCell(23);
		cell.setCellValue(aBook.getOptimusLastMileCostNRC()==null?"":aBook.getOptimusLastMileCostNRC());
		
		cell = row.createCell(24);
		cell.setCellValue(aBook.getActualLastMileArc()==null?"":aBook.getActualLastMileArc());
		
		cell = row.createCell(25);
		cell.setCellValue(aBook.getOptimusLastMileCostARC()==null?"":aBook.getOptimusLastMileCostARC());
		
		cell = row.createCell(26);
		cell.setCellValue(aBook.getActualCpeMrc()==null?"":aBook.getActualCpeMrc());
		
		cell = row.createCell(27);
		cell.setCellValue(aBook.getOptimusCpeMrc()==null?"":aBook.getOptimusCpeMrc());
		
		cell = row.createCell(28);
		cell.setCellValue(aBook.getActualCpeNrc()==null?"":aBook.getActualCpeNrc());
		
		cell = row.createCell(29);
		cell.setCellValue(aBook.getOptimusCpeNrc()==null?"":aBook.getOptimusCpeNrc());
		
		cell = row.createCell(30);
		cell.setCellValue(aBook.getActualCpeArc()==null?"":aBook.getActualCpeArc());
		
		cell = row.createCell(31);
		cell.setCellValue(aBook.getOptimusCpeArc()==null?"":aBook.getOptimusCpeArc());
		
		cell = row.createCell(32);
		cell.setCellValue(aBook.getActualAdditionalipMrc()==null?"":aBook.getActualAdditionalipMrc());

		cell = row.createCell(33);
		cell.setCellValue(aBook.getOptimusAdditionalIpMrc()==null?"":aBook.getOptimusAdditionalIpMrc()); 
		
		cell = row.createCell(34);
		cell.setCellValue(aBook.getActualAdditionalIpArc()==null?"":aBook.getActualAdditionalIpArc());
		
		cell = row.createCell(35);
		cell.setCellValue(aBook.getOptimusAdditionalIpArc()==null?"":aBook.getOptimusAdditionalIpArc());
		
		cell = row.createCell(36);
		cell.setCellValue(aBook.getActualAdditionalIpNrc()==null?"":aBook.getActualAdditionalIpNrc());
		
		cell = row.createCell(37);
		cell.setCellValue(aBook.getSiteLatitude()==null?"":aBook.getSiteLatitude());
		
		cell = row.createCell(38);
		cell.setCellValue(aBook.getSiteLongitude()==null?"":aBook.getSiteLongitude());
		
		cell = row.createCell(39);
		cell.setCellValue(aBook.getProspectName()==null?"":aBook.getProspectName());
		
		cell = row.createCell(40);
		cell.setCellValue(aBook.getBwMbps()==null?"":aBook.getBwMbps());
		
		cell = row.createCell(41);
		cell.setCellValue(aBook.getBurstableBw()==null?"":aBook.getBurstableBw());
		
		cell = row.createCell(42);
		cell.setCellValue(aBook.getCity()==null?"":aBook.getCity());
		
		cell = row.createCell(43);
		cell.setCellValue(aBook.getCustomerSegment()==null?"":aBook.getCustomerSegment());
		
		cell = row.createCell(44);
		cell.setCellValue(aBook.getSalesOrg()==null?"":aBook.getSalesOrg());
		
		cell = row.createCell(45);
		cell.setCellValue(aBook.getProductNameFsbility()==null?"":aBook.getProductNameFsbility());
		
		cell = row.createCell(46);
		cell.setCellValue(aBook.getFeasibilityCreatedDate()==null?"":aBook.getFeasibilityCreatedDate());
		
		cell = row.createCell(47);
		cell.setCellValue(aBook.getLocalLoopInterface()==null?"":aBook.getLocalLoopInterface());
		
		cell = row.createCell(48);
		cell.setCellValue(aBook.getContractTerm()==null?"":aBook.getContractTerm());
		
		cell = row.createCell(49);
		cell.setCellValue(aBook.getQuotetypeQuote()==null?"":aBook.getQuotetypeQuote());
		
		cell = row.createCell(50);
		cell.setCellValue(aBook.getConnectionType()==null?"":aBook.getConnectionType());
		
		cell = row.createCell(51);
		cell.setCellValue(aBook.getSumNoOfSitesUniLen()==null?"":aBook.getSumNoOfSitesUniLen());
		
		cell = row.createCell(52);
		cell.setCellValue(aBook.getCpeVariant()==null?"":aBook.getCpeVariant());
		
		cell = row.createCell(53);
		cell.setCellValue(aBook.getCpeManagementType()==null?"":aBook.getCpeManagementType());
		
		cell = row.createCell(54);
		cell.setCellValue(aBook.getCpeSupplyType()==null?"":aBook.getCpeSupplyType());
		
		cell = row.createCell(55);
		cell.setCellValue(aBook.getTopology()==null?"":aBook.getTopology());
		
		cell = row.createCell(56);
		cell.setCellValue(aBook.getSumOnnetFlag()==null?"":aBook.getSumOnnetFlag());
		
		cell = row.createCell(57);
		cell.setCellValue(aBook.getSumOffnetFlag()==null?"":aBook.getSumOffnetFlag());
		
		cell = row.createCell(58);
		cell.setCellValue(aBook.getLmArcBwOnwl()==null?"":aBook.getLmArcBwOnwl());
		
		cell = row.createCell(59);
		cell.setCellValue(aBook.getLmNrcBwOnwl()==null?"":aBook.getLmNrcBwOnwl());
		
		cell = row.createCell(60);
		cell.setCellValue(aBook.getLmNrcMuxOnwl()==null?"":aBook.getLmNrcMuxOnwl());
		
		cell = row.createCell(61);
		cell.setCellValue(aBook.getLmNrcInbldgOnwl()==null?"":aBook.getLmNrcInbldgOnwl());
		
		cell = row.createCell(62);
		cell.setCellValue(aBook.getLmNrcOspcapexOnwl()==null?"":aBook.getLmNrcOspcapexOnwl());
		
		cell = row.createCell(63);
		cell.setCellValue(aBook.getLmNrcNerentalOnwl()==null?"":aBook.getLmNrcNerentalOnwl());
		
		cell = row.createCell(64);
		cell.setCellValue(aBook.getLmArcBwProvOfrf()==null?"":aBook.getLmArcBwProvOfrf());
		 
		cell = row.createCell(65);
		cell.setCellValue(aBook.getLmNrcBwProvOfrf()==null?"":aBook.getLmNrcBwProvOfrf());
		
		
		cell = row.createCell(66);
		cell.setCellValue(aBook.getLmNrcBwMastOfrf()==null?"":aBook.getLmNrcBwMastOfrf());
		
		cell = row.createCell(67);
		cell.setCellValue(aBook.getLmArcBwOnrf()==null?"":aBook.getLmArcBwOnrf());
		
		cell = row.createCell(68);
		cell.setCellValue(aBook.getLmNrcBwOnrf()==null?"":aBook.getLmNrcBwOnrf());
		
		cell = row.createCell(69);
		cell.setCellValue(aBook.getLmNrcBwMastOnrf()==null?"":aBook.getLmNrcBwMastOnrf());
		
		cell = row.createCell(70);
		cell.setCellValue(aBook.getOrchConnection()==null?"":aBook.getOrchConnection());
		
		cell = row.createCell(71);
		cell.setCellValue(aBook.getOrchLmType()==null?"":aBook.getOrchLmType());
		
		cell = row.createCell(72);
		cell.setCellValue(aBook.getAdditionalIpFlag()==null?"":aBook.getAdditionalIpFlag());
		
		cell = row.createCell(73);
		cell.setCellValue(aBook.getIpAddressArrangement()==null?"":aBook.getIpAddressArrangement());
		
		cell = row.createCell(74);
		cell.setCellValue(aBook.getIpv4AddressPoolSize()==null?"":aBook.getIpv4AddressPoolSize());
		
		cell = row.createCell(75);
		cell.setCellValue(aBook.getIpv6AddressPoolSize()==null?"":aBook.getIpv6AddressPoolSize());
		
		cell = row.createCell(76);
		cell.setCellValue(aBook.getAccountRtmCust()==null?"":aBook.getAccountRtmCust());
		
		cell = row.createCell(77);
		cell.setCellValue(aBook.getFeasibilityCode()==null?"":aBook.getFeasibilityCode());
		
		cell = row.createCell(78);
		cell.setCellValue(aBook.getFeasibilityMode()==null?"":aBook.getFeasibilityMode());
		
		cell = row.createCell(79);
		cell.setCellValue(aBook.getFeasibilityCheck()==null?"":aBook.getFeasibilityCheck());
		
		cell = row.createCell(80);
		cell.setCellValue(aBook.getBackupPortRequested()==null?"":aBook.getBackupPortRequested());
		
		cell = row.createCell(81);
		cell.setCellValue(aBook.getNoOfSitesInOpportunity()==null?"":aBook.getNoOfSitesInOpportunity());
		
		cell = row.createCell(82);
		cell.setCellValue(aBook.getFeasibilityStatus()==null?"":aBook.getFeasibilityStatus());
		
		cell = row.createCell(83);
		cell.setCellValue(aBook.getBucketAdjustmentType()==null?"":aBook.getBucketAdjustmentType());
		
		cell = row.createCell(84);
		cell.setCellValue(aBook.getCpeDiscount()==null?"":aBook.getCpeDiscount());
		
		cell = row.createCell(85);
		cell.setCellValue(aBook.getIsSelected()==null?"":aBook.getIsSelected());
		
		cell = row.createCell(86);
		cell.setCellValue(aBook.getProvider()==null?"":aBook.getProvider());
		
		cell = row.createCell(87);
		cell.setCellValue(aBook.getRateCard()==null?"":aBook.getRateCard());

	}
		
}
