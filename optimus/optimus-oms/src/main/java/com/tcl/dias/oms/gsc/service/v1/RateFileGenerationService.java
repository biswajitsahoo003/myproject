package com.tcl.dias.oms.gsc.service.v1;

import static com.tcl.dias.oms.gsc.util.GscConstants.DOMESTIC_VOICE_OUTBOUND_PRICES;
import static com.tcl.dias.oms.gsc.util.GscConstants.GLOBAL_OUTBOUND_SURCHARGE;
import static com.tcl.dias.oms.gsc.util.GscConstants.GLOBAL_OUTBOUND_VOICE_PRICES;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.CALL_TYPE;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.COMMENTS;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.CURRENCY;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.DESTINATION_COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.DESTINATION_ID;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.DESTINATION_NAME;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.DEST_ID;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.EXTERIOR_REGION_NAME;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.ORIGIN_COUNTRY_NAME;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.PHONE_TYPE;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.PRICE;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.RATE_PER_MINUTE;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.REGION_COUNTRY;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.REGION_DESTINATION;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.REMARKS;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.SERIAL_NO;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.STATUS;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.SURCHAGE_AMOUNT;
import static com.tcl.dias.oms.gsc.util.GscRateFileGenExcelConstants.SURCHARGE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.gsc.beans.GscOutboundSurchargePricingBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.gsc.beans.GscQuoteConfigurationBean;
import com.tcl.dias.oms.gsc.pdf.beans.GlobalOutboundDisplayRatesBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscCofOutboundPricesPdfBean;
import com.tcl.dias.oms.gsc.pdf.beans.GscTerminationBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Services to generate rate files in excel format
 *
 * @author Venkata Naga Sai S
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Service
public class RateFileGenerationService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(RateFileGenerationService.class);

	/**
     * Generate Rate Files for DID orders in Excel format.
     *
     * @param gscCofOutboundPricesPdfBean
     * @param surchargePricingBeans
     * @throws IOException
     * @throws TclCommonException
     */
	
	public byte[] generateExcelByteArrayFromBean(GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean, List<GscOutboundSurchargePricingBean> surchargePricingBeans) throws TclCommonException, IOException {
		LOGGER.info("Inside generateExcelByteArrayFromBean method..!");
		XSSFWorkbook workbook = new XSSFWorkbook();
	     ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
	     byte[] outArray;
	     try {
	    	 String sheetName = "";
	    	 
	    	 if(gscCofOutboundPricesPdfBean == null && !CollectionUtils.isEmpty(surchargePricingBeans)) {
	    		 sheetName = GLOBAL_OUTBOUND_SURCHARGE;
	    	 }else if (!CollectionUtils.isEmpty(gscCofOutboundPricesPdfBean.getGscQuoteConfigurationBeans())) {
	    		 sheetName = DOMESTIC_VOICE_OUTBOUND_PRICES;
	    	 }else if(!CollectionUtils.isEmpty(gscCofOutboundPricesPdfBean.getGlobalOutboundDisplayRatesBeans())) {
	    		 sheetName = GLOBAL_OUTBOUND_VOICE_PRICES;
	    	 }

	    	 createSheet(workbook, gscCofOutboundPricesPdfBean, surchargePricingBeans, sheetName);
	    	 workbook.write(outByteStream);
	         outArray = outByteStream.toByteArray();
	         
	     }catch (Exception e) {
	            throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
	     }finally {
	            workbook.close();
	            outByteStream.flush();
	            outByteStream.close();
	     }
	     LOGGER.info("Exit generateExcelByteArrayFromBean method..!");
	     return outArray;
	}

	
	
	private void createSheet(XSSFWorkbook workbook, GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean,List<GscOutboundSurchargePricingBean> surchargePricingBeans
			,String sheetName) throws TclCommonException {
		switch (sheetName) {
    		case DOMESTIC_VOICE_OUTBOUND_PRICES:
    				LOGGER.info("Fetching Domestic voice prices for ratefile..!");
    				List<GscQuoteConfigurationBean> gscQouteConfigurations = gscCofOutboundPricesPdfBean.getGscQuoteConfigurationBeans();
    				if (!CollectionUtils.isEmpty(gscQouteConfigurations)) {
    					XSSFSheet sheet = workbook.createSheet(sheetName);
    					CellStyle style = unlockCellStyle(workbook);
    					int rowId = writeCofDetailsIntoSheet(gscCofOutboundPricesPdfBean, style, sheet, workbook);
    					List<String> heading = Arrays.asList(COUNTRY,CALL_TYPE,DEST_ID,DESTINATION_NAME,CURRENCY,RATE_PER_MINUTE);
    					int headerRowId = ++rowId;
    					Row row1 = sheet.createRow(headerRowId);
    					writeHeaderIntoSheet(heading, row1, workbook);
    					int rowCount = ++rowId;
    					for (GscQuoteConfigurationBean gscQouteConfig : gscQouteConfigurations) {
    						if(!CollectionUtils.isEmpty(gscQouteConfig.getTerminations())) {
    							for(GscTerminationBean termination : gscQouteConfig.getTerminations()) {
    								Row row = sheet.createRow(rowCount);
    								writeDomesticVoiceSheet(gscQouteConfig, termination, gscCofOutboundPricesPdfBean.getPaymentCurrency(), style, row, workbook);
    								rowCount++;
    							}
    						}else {
    							LOGGER.info("No record found..!");
    						}
    					}
    					LOGGER.debug("{} records fetched for Domestic voice rate file",rowCount);
    					styleHeader(workbook, sheet, headerRowId);
    				}else {
    					LOGGER.info("No record found..!");
    				}
    				break;
    		
    		case GLOBAL_OUTBOUND_VOICE_PRICES:
    				LOGGER.info("Fetching Global outbound voice prices for ratefile..!");
    				List<GlobalOutboundDisplayRatesBean> globalOutboundRates = gscCofOutboundPricesPdfBean.getGlobalOutboundDisplayRatesBeans();
    				if (!CollectionUtils.isEmpty(globalOutboundRates)) {
    					XSSFSheet sheet = workbook.createSheet(sheetName);
    					CellStyle style = unlockCellStyle(workbook);
    					int rowId = writeCofDetailsIntoSheet(gscCofOutboundPricesPdfBean, style, sheet, workbook);
    					List<String> heading = Arrays.asList(SERIAL_NO,DESTINATION_ID,DESTINATION_NAME,DESTINATION_COUNTRY,CURRENCY,PHONE_TYPE,PRICE,STATUS,COMMENTS,REMARKS);
    					int headerRowId = ++rowId;
    					Row row1 = sheet.createRow(headerRowId);
    					writeHeaderIntoSheet(heading, row1, workbook);
    					int rowCount = ++rowId;
    					for(GlobalOutboundDisplayRatesBean globalOutBoundRateBean : globalOutboundRates) {
    						Row row = sheet.createRow(rowCount);
    						writeGlobalOutboundSheet(globalOutBoundRateBean, style, row, workbook, headerRowId);
    						rowCount++;
    					}
    					LOGGER.debug("{} records fetched for Global outbound voice rate file",rowCount);
    					styleHeader(workbook, sheet, headerRowId);
    				}else {
    					LOGGER.info("No record found..!");
    				}
    				break;
    		case GLOBAL_OUTBOUND_SURCHARGE:
    			LOGGER.info("Fetching Global outbound surcharge prices for ratefile..!");
    				if (!CollectionUtils.isEmpty(surchargePricingBeans)) {
    					XSSFSheet sheet = workbook.createSheet(sheetName);
    					CellStyle style = unlockCellStyle(workbook);
    					List<String> heading = Arrays.asList(REGION_COUNTRY,REGION_DESTINATION,ORIGIN_COUNTRY_NAME,EXTERIOR_REGION_NAME,SURCHARGE,SURCHAGE_AMOUNT,CURRENCY);		
    					Row row1 = sheet.createRow(0);
    					writeHeaderIntoSheet(heading, row1, workbook);
    					int rowCount = 1;
    					for(GscOutboundSurchargePricingBean surchargePricingBean: surchargePricingBeans) {
    						Row row = sheet.createRow(rowCount);
    						writeGlobalOutboundSurchargeSheet(surchargePricingBean, style, row, workbook);
    						rowCount++;
    					}
    					LOGGER.debug("{} records fetched for Global outbound surcharge file",rowCount);
    					styleHeader(workbook, sheet, 0);
    				}else {
    					LOGGER.info("No record found..!");
    				}
    				break;
    		default:
    				break;
		}
	}

	private int writeCofDetailsIntoSheet(GscCofOutboundPricesPdfBean gscCofOutboundPricesPdfBean, CellStyle style, XSSFSheet sheet, XSSFWorkbook workbook) {
		int rowCount = 0; 
		
		Row row = sheet.createRow(rowCount);
		Cell cell = row.createCell(0);
	    cell.setCellValue("COF ref. no.:");
	  
	    cell = row.createCell(1);
        cell.setCellValue(gscCofOutboundPricesPdfBean.getCofRefernceNumber());
        cell.setCellStyle(style);
	    
        row = sheet.createRow(++rowCount);
        cell = row.createCell(0);
	    cell.setCellValue("Date :");
	  
	    cell = row.createCell(1);
        cell.setCellValue(gscCofOutboundPricesPdfBean.getOrderDate());
        cell.setCellStyle(style);
        
        row = sheet.createRow(++rowCount);
        cell = row.createCell(0);
	    cell.setCellValue("Type :");
	  
	    cell = row.createCell(1);
        cell.setCellValue(gscCofOutboundPricesPdfBean.getOrderType());
        cell.setCellStyle(style);
        
        return rowCount;
        
	}


	private void writeHeaderIntoSheet(List<String> headings, Row row, XSSFWorkbook workbook) {
		 Cell cell = row.createCell(0);
	     cell.setCellValue(headings.get(0));
	     
	     for(int i=1;i<headings.size();i++) {
	    	 cell = row.createCell(i);
	         cell.setCellValue(headings.get(i));
	     }
	}



	private void writeGlobalOutboundSurchargeSheet(GscOutboundSurchargePricingBean surchargePricingBean, CellStyle style, Row row,
			XSSFWorkbook workbook) throws TclCommonException {
		if (Objects.isNull(surchargePricingBean) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
		
		int i = 0;

        Cell cell = row.createCell(i);
        cell.setCellValue(surchargePricingBean.getRegionCountry());
        
        cell = row.createCell(++i);
        cell.setCellValue(surchargePricingBean.getRegionCountryDestination());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(surchargePricingBean.getOriginCountryName());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(surchargePricingBean.getExteriorRegionName());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(surchargePricingBean.getSurcharge());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(surchargePricingBean.getSurchargeAmount());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(surchargePricingBean.getCurrency());
        cell.setCellStyle(style);
	}



	private void writeDomesticVoiceSheet(GscQuoteConfigurationBean gscQouteConfig, GscTerminationBean termination, String paymentCurrency
			, CellStyle style, Row row, XSSFWorkbook workbook) throws TclCommonException {
		if (Objects.isNull(termination) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
		
		
		int i = 0;

        Cell cell = row.createCell(i);
        cell.setCellValue(gscQouteConfig.getSource());
        
        cell = row.createCell(++i);
        cell.setCellValue(termination.getPhoneType());
        cell.setCellStyle(style);
        
        Integer terminationId = termination.getTerminationId();
        cell = row.createCell(++i);
        if(terminationId == null || terminationId==0){
        	cell.setCellValue("NA");
        }else {
        	cell.setCellValue(terminationId);
        }
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(termination.getTerminationName());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(paymentCurrency);
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(termination.getTerminationRate());
        cell.setCellStyle(style);
        
	}

	private void writeGlobalOutboundSheet(GlobalOutboundDisplayRatesBean globalOutBoundRateBean, CellStyle style, Row row,
			XSSFWorkbook workbook, int headerRowId) throws TclCommonException {
		if (Objects.isNull(globalOutBoundRateBean) || Objects.isNull(row)) {
            throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
        }
		
		
		int i = 0;
		
		Cell cell = row.createCell(i);
        cell.setCellValue(row.getRowNum()-headerRowId);
        
        cell = row.createCell(++i);
        cell.setCellValue(globalOutBoundRateBean.getDestId());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(globalOutBoundRateBean.getDestinationName());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(globalOutBoundRateBean.getDestinationCountry());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(globalOutBoundRateBean.getDisplayCurrency());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(globalOutBoundRateBean.getPhoneType());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(globalOutBoundRateBean.getPrice());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(globalOutBoundRateBean.getStatus());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(globalOutBoundRateBean.getComments()==null?"NA":globalOutBoundRateBean.getComments());
        cell.setCellStyle(style);
        
        cell = row.createCell(++i);
        cell.setCellValue(globalOutBoundRateBean.getRemarks()==null?"NA":globalOutBoundRateBean.getRemarks());
        cell.setCellStyle(style);
	}

	private void styleHeader(XSSFWorkbook workbook, XSSFSheet sheet, int rowId) {
		//style heading
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFont(font);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row heading = sheet.getRow(rowId);

        for (int i = 0; i < heading.getLastCellNum(); i++) {
            sheet.setColumnWidth(i, 7000);
            heading.getCell(i).setCellStyle(style);
        }
        // Freeze header row
        sheet.createFreezePane(0, 1);
	}

	private CellStyle unlockCellStyle(XSSFWorkbook workbook) {
		 CellStyle unlockedCellStyle = workbook.createCellStyle();
	     //unlock cell
	     unlockedCellStyle.setLocked(false);
	     //make cell as text
	     DataFormat dataFormat = workbook.createDataFormat();
	     unlockedCellStyle.setDataFormat(dataFormat.getFormat("@"));
	     return unlockedCellStyle;
	}
	
}
