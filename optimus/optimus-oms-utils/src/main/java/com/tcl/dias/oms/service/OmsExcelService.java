package com.tcl.dias.oms.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.oms.beans.GvpnIntlCustomFeasibilityRequest;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.oms.beans.CustomFeasibilityRequest;
import com.tcl.dias.oms.beans.CustomeFeasibilityRequestNpl;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.OmsExcelConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the OmsExcelService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class OmsExcelService {

	private static final Logger LOGGER = LoggerFactory.getLogger(OmsExcelService.class);

	@Value("${template.fp.custom}")
	String customfpFilePath;

	/**
	 * 
	 * downloadCustomFpTemplate - Process The downloadTemplate
	 * 
	 * @param response
	 * @throws IOException
	 * @throws TclCommonException
	 */
	public Resource downloadCustomFpTemplatev1(HttpServletResponse response) throws TclCommonException {
		Resource resource = null;
		try {
			File[] files = new File(customfpFilePath).listFiles();
			String attachmentPath = null;
			for (File file : files) {
				if (file.isFile()) {
					attachmentPath = file.getAbsolutePath();
					LOGGER.info("File Abs path :: {}", attachmentPath);
				}
			}
			Path attachmentLocation = Paths.get(attachmentPath);
			resource = new UrlResource(attachmentLocation.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadCustomFeasibilityTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
	}

	public void downloadCustomFpTemplate(HttpServletResponse response) throws TclCommonException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet(OmsExcelConstants.CUSTOM_FEASIBLITY);
			Row header = processCreateHeaderCells(sheet);
			applyHeaderStyles(workbook, sheet, header);
			processTypeValidation(sheet);
			processAccessTypeValidation(sheet);
			processFeasibilityStatusValidation(sheet);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			String fileName = OmsExcelConstants.CUSTOM_FEASIBILITY_TEMPLATE_XLSX;
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());
			outByteStream.flush();
			outByteStream.close();
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadCustomFeasibilityTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	public void downloadCustomFpTemplateIntl(HttpServletResponse response) throws TclCommonException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet(OmsExcelConstants.CUSTOM_FEASIBLITY_INTL);
			Row header = processCreateHeaderIntlCells(sheet);
			applyHeaderStyles(workbook, sheet, header);
			processTypeValidation(sheet);
			//processAccessTypeValidation(sheet);
			processFeasibilityStatusValidation(sheet);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			String fileName = OmsExcelConstants.INTL_CUSTOM_FEASIBILITY_TEMPLATE_XLSX;
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());
			outByteStream.flush();
			outByteStream.close();
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadCustomFeasibilityTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
	}

	/**
	 * processFeasibilityStatusValidation
	 * 
	 * @param sheet
	 */
	private void processFeasibilityStatusValidation(Sheet sheet) {
		DataValidation feasibleStatusDataValidation = null;
		DataValidationConstraint feasibleStatusConstraint = null;
		DataValidationHelper feasibleStatusValidationHelper = null;
		feasibleStatusValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList feasibilityStatusAddressList = new CellRangeAddressList(1, 50, 2, 2);
		// add the selected profiles
		List<String> feasibilityStatus = new ArrayList<>();
		feasibilityStatus.add(OmsExcelConstants.MANUAL_FEASIBLE);
		feasibilityStatus.add(OmsExcelConstants.NOT_FEASIBLE);
		feasibleStatusConstraint = feasibleStatusValidationHelper
				.createExplicitListConstraint(feasibilityStatus.stream().toArray(String[]::new));
		feasibleStatusDataValidation = feasibleStatusValidationHelper.createValidation(feasibleStatusConstraint,
				feasibilityStatusAddressList);
		feasibleStatusDataValidation.setSuppressDropDownArrow(true);
		feasibleStatusDataValidation.setShowErrorBox(true);
		feasibleStatusDataValidation.setShowPromptBox(true);
		sheet.addValidationData(feasibleStatusDataValidation);
	}

	/**
	 * processAccessTypeValidation
	 * 
	 * @param sheet
	 */
	private void processAccessTypeValidation(Sheet sheet) {
		DataValidation accessTypeDataValidation;
		DataValidationConstraint accessTypeConstraint;
		DataValidationHelper accessTypeValidationHelper;
		accessTypeValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList accessTypeCellRangeList = new CellRangeAddressList(1, 50, 1, 1);
		List<String> accessTypes = new ArrayList<>();
		accessTypes.add(OmsExcelConstants.ONNET_WIRELINE);
		accessTypes.add(OmsExcelConstants.OFFNET_WIRELINE);
		accessTypes.add(OmsExcelConstants.ONNET_RF);
		accessTypes.add(OmsExcelConstants.OFFNET_RF);
		accessTypeConstraint = accessTypeValidationHelper
				.createExplicitListConstraint(accessTypes.stream().toArray(String[]::new));
		accessTypeDataValidation = accessTypeValidationHelper.createValidation(accessTypeConstraint,
				accessTypeCellRangeList);
		accessTypeDataValidation.setSuppressDropDownArrow(true);
		accessTypeDataValidation.setShowErrorBox(true);
		accessTypeDataValidation.setShowPromptBox(true);
		sheet.addValidationData(accessTypeDataValidation);
	}

	private void processTypeValidation(Sheet sheet) {
		DataValidation typeDataValidation;
		DataValidationConstraint typeConstraint;
		DataValidationHelper typeValidationHelper;
		typeValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList accessTypeCellRangeList = new CellRangeAddressList(1, 50, 0, 0);
		List<String> accessTypes = new ArrayList<>();
		accessTypes.add(OmsExcelConstants.PRIMARY);
		accessTypes.add(OmsExcelConstants.SECONDARY);
		typeConstraint = typeValidationHelper.createExplicitListConstraint(accessTypes.stream().toArray(String[]::new));
		typeDataValidation = typeValidationHelper.createValidation(typeConstraint, accessTypeCellRangeList);
		typeDataValidation.setSuppressDropDownArrow(true);
		typeDataValidation.setShowErrorBox(true);
		typeDataValidation.setShowPromptBox(true);
		sheet.addValidationData(typeDataValidation);
	}

	/**
	 * applyHeaderStyles
	 * 
	 * @param workbook
	 * @param sheet
	 * @param header
	 */
	private void applyHeaderStyles(XSSFWorkbook workbook, Sheet sheet, Row header) {
		for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
			CellStyle stylerowHeading = workbook.createCellStyle();
			stylerowHeading.setBorderBottom(BorderStyle.THICK);
			stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 10);
			font.setColor(IndexedColors.WHITE.getIndex());
			stylerowHeading.setFont(font);
			stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
			stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

			stylerowHeading.setWrapText(true);
			header.getCell(i).setCellStyle(stylerowHeading);

			DataFormat fmt = workbook.createDataFormat();
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
			sheet.setDefaultColumnStyle(i, textStyle);
			sheet.autoSizeColumn(i);
		}
	}

	/**
	 * processCreateHeaderCells
	 * 
	 * @param sheet
	 * @return
	 */
	private Row processCreateHeaderCells(Sheet sheet) {
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue(OmsExcelConstants.TYPE);
		header.createCell(1).setCellValue(OmsExcelConstants.ACCESS_TYPE);
		header.createCell(2).setCellValue(OmsExcelConstants.FEASIBILITY_STATUS);
		header.createCell(3).setCellValue(OmsExcelConstants.PROVIDER_NAME);
		header.createCell(4).setCellValue(OmsExcelConstants.SFDC_FEASIBLITY_ID);
		header.createCell(5).setCellValue(OmsExcelConstants.CHARGEABLE_DISTANCE);
		header.createCell(6).setCellValue(OmsExcelConstants.TCL_POP);
		header.createCell(7).setCellValue(OmsExcelConstants.TCL_POP_ID);
		header.createCell(8).setCellValue(OmsExcelConstants.CONNECTED_BUILDING);
		header.createCell(9).setCellValue(OmsExcelConstants.CONNECTED_CUSTOMER);
		header.createCell(10).setCellValue(OmsExcelConstants.ARC_BW);
		header.createCell(11).setCellValue(OmsExcelConstants.OTC);
		header.createCell(12).setCellValue(OmsExcelConstants.IN_BUILDING_CAPEX);
		header.createCell(13).setCellValue(OmsExcelConstants.MUX_COST);
		header.createCell(14).setCellValue(OmsExcelConstants.NE_RENTAL);
		header.createCell(15).setCellValue(OmsExcelConstants.OSP_CAPEX);
		header.createCell(16).setCellValue(OmsExcelConstants.OSP_DISTANCE);
		header.createCell(17).setCellValue(OmsExcelConstants.CITY);
		header.createCell(18).setCellValue(OmsExcelConstants.MH_HH_ID);
		header.createCell(19).setCellValue(OmsExcelConstants.DELIVERY_TIMELINE);
		header.createCell(20).setCellValue(OmsExcelConstants.BANDWIDTH);
		header.createCell(21).setCellValue(OmsExcelConstants.INTERFACE);
		header.createCell(22).setCellValue(OmsExcelConstants.MAST_HEIGHT);
		header.createCell(23).setCellValue(OmsExcelConstants.TYPE_LM);
		header.createCell(24).setCellValue(OmsExcelConstants.P2P_PRESENCE);
		header.createCell(25).setCellValue(OmsExcelConstants.PMP_PRESENCE);
		header.createCell(26).setCellValue(OmsExcelConstants.HOP_DIS);
		header.createCell(27).setCellValue(OmsExcelConstants.BTS_ID);
		header.createCell(28).setCellValue(OmsExcelConstants.OTC_BW);
		header.createCell(29).setCellValue(OmsExcelConstants.MAST_COST);
		header.createCell(30).setCellValue(OmsExcelConstants.OFFNET_BTS_ID);
		header.createCell(31).setCellValue(OmsExcelConstants.BTS_ADDRESS);
		header.createCell(32).setCellValue(OmsExcelConstants.TENTATIVE_MAST_HEIGHT);
		return header;
	}

	
	
	/**
	 * processCreateHeaderIntlCells
	 * 
	 * @param sheet
	 * @return
	 */
	private Row processCreateHeaderIntlCells(Sheet sheet) {
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue(OmsExcelConstants.TYPE);
		header.createCell(1).setCellValue(OmsExcelConstants.ACCESS_TYPE);
		header.createCell(2).setCellValue(OmsExcelConstants.FEASIBILITY_STATUS);
		header.createCell(3).setCellValue(OmsExcelConstants.SFDC_FEASIBLITY_ID);
		header.createCell(4).setCellValue(OmsExcelConstants.PROVIDER_NAME);
		header.createCell(5).setCellValue(OmsExcelConstants.RECORD_TYPE);
		header.createCell(6).setCellValue(OmsExcelConstants.TCL_POP_ADDRESS);
		header.createCell(7).setCellValue(OmsExcelConstants.TCL_POP_SITE_CODE);
		header.createCell(8).setCellValue(OmsExcelConstants.PROVIDER_PRODUCT_NAME);
		header.createCell(9).setCellValue(OmsExcelConstants.CUSTOMER_LAT_LONG);
		header.createCell(10).setCellValue(OmsExcelConstants.LOCAL_LOOP_CAPACITY);
		header.createCell(11).setCellValue(OmsExcelConstants.LOCAL_LOOP_INTERFACE);
		header.createCell(12).setCellValue(OmsExcelConstants.INTER_CONNECTION_TYPE);
		header.createCell(13).setCellValue(OmsExcelConstants.CONTRACT_TERM_WITH_VENDOR);
		header.createCell(14).setCellValue(OmsExcelConstants.LM_CURRENCY);
		header.createCell(15).setCellValue(OmsExcelConstants.LM_BW_MRC);
		header.createCell(16).setCellValue(OmsExcelConstants.OTC_NRC_INSTALLATION);
		header.createCell(17).setCellValue(OmsExcelConstants.XCONNECT_CURRENCY);
		header.createCell(18).setCellValue(OmsExcelConstants.XCONNECT_MRC);
		header.createCell(19).setCellValue(OmsExcelConstants.XCONNECT_NRC);
		header.createCell(20).setCellValue(OmsExcelConstants.REMARKS);
		return header;
	}

	/**
	 * 
	 * extractCustomFeasibilty - This method returns the customFeasibility Bean from
	 * the excel
	 * 
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	public List<CustomFeasibilityRequest> extractCustomFeasibilty(MultipartFile file) throws TclCommonException {
		List<CustomFeasibilityRequest> customFeasibilityRequests = new ArrayList<>();
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8")) {
			for (Sheet sheet : workbook) {
				LOGGER.info("Total no of rows => {}", sheet.getPhysicalNumberOfRows());
				if (sheet.getPhysicalNumberOfRows() >= 2) {
					customFeasibilityRequests.add(extractExcelData(sheet, 1));
				} else {
					throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION,
							ResponseResource.R_CODE_ERROR);
				}
				if (sheet.getPhysicalNumberOfRows() >= 3) {
					customFeasibilityRequests.add(extractExcelData(sheet, 2));
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customFeasibilityRequests;
	}

	/**
	 * extractExcelData
	 * 
	 * @param customFeasibilityRequest
	 * @param sheet
	 * @param i
	 */
	private CustomFeasibilityRequest extractExcelData(Sheet sheet, int i) {
		DataFormatter dataFormatter = new DataFormatter();
		CustomFeasibilityRequest customFeasibilityRequest = new CustomFeasibilityRequest();
		customFeasibilityRequest.setType(sheet.getRow(i).getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest
				.setAccessType(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setFeasibilityStatus(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setProviderName(sheet.getRow(i).getCell(3, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(3, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setSfdcFeasibilityId(sheet.getRow(i).getCell(4, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(4, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setChargeableDistance(sheet.getRow(i).getCell(5, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(5, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest.setTclPop(sheet.getRow(i).getCell(6, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(6, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest.setTclPopId(sheet.getRow(i).getCell(7, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(7, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest
				.setConnectedBuilding(sheet.getRow(i).getCell(8, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(8, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setConnectedCustomer(sheet.getRow(i).getCell(9, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(9, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		if(sheet.getRow(i).getCell(10, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null)
		{
			String value = dataFormatter.formatCellValue(sheet.getRow(i).getCell(10, MissingCellPolicy.RETURN_BLANK_AS_NULL));
			customFeasibilityRequest.setArcBw(value);
		}
		else customFeasibilityRequest.setArcBw(null);

		customFeasibilityRequest.setOtc(sheet.getRow(i).getCell(11, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(11, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest
				.setInBuildingCapex(sheet.getRow(i).getCell(12, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(12, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest.setMuxCost(sheet.getRow(i).getCell(13, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(13, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest.setNeRental(sheet.getRow(i).getCell(14, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(14, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest.setOspCapex(sheet.getRow(i).getCell(15, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(15, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest
				.setOspDistance(sheet.getRow(i).getCell(16, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(16, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest.setCity(sheet.getRow(i).getCell(17, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(17, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest.setMhHhId(sheet.getRow(i).getCell(18, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(18, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest
				.setDeliveryTimeline(sheet.getRow(i).getCell(19, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(19, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setBandwidth(sheet.getRow(i).getCell(20, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(20, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest.setInterfce(sheet.getRow(i).getCell(21, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(21, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest
				.setMastHeight(sheet.getRow(i).getCell(22, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(22, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest.setTypeOfLm(sheet.getRow(i).getCell(23, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(23, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest
				.setP2pPresence(sheet.getRow(i).getCell(24, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(24, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setPmpPresence(sheet.getRow(i).getCell(25, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(25, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest.setHopDist(sheet.getRow(i).getCell(26, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(26, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest.setBtsId(sheet.getRow(i).getCell(27, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(27, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest.setOtcBw(sheet.getRow(i).getCell(28, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(28, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest.setMastCost(sheet.getRow(i).getCell(29, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(29, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest
				.setOffnetBtsId(sheet.getRow(i).getCell(30, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(30, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setBtsAddress(sheet.getRow(i).getCell(31, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(31, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setTentativeMastHeight(sheet.getRow(i).getCell(32, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(32, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		return customFeasibilityRequest;
	}
	
	/**
	 * 
	 * extractCustomFeasibilty - This method returns the customFeasibility Bean from
	 * the excel
	 * 
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	public CustomeFeasibilityRequestNpl extractCustomFeasibiltyNpl(MultipartFile file) throws TclCommonException {
		CustomeFeasibilityRequestNpl customeFeasibilityRequestNpl = null;
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8")) {
			for (Sheet sheet : workbook) {
				
				LOGGER.info("Total no of rows => {}", sheet.getPhysicalNumberOfRows());
				int rowIndex = 1;
				customeFeasibilityRequestNpl = new CustomeFeasibilityRequestNpl();
				while(rowIndex<=sheet.getPhysicalNumberOfRows()) {
					extractExcelDataNpl(sheet, rowIndex, customeFeasibilityRequestNpl);
					rowIndex++;
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return customeFeasibilityRequestNpl;
	}
	private CustomeFeasibilityRequestNpl extractExcelDataNpl(Sheet sheet, int i,CustomeFeasibilityRequestNpl customeFeasibilityRequestNpl) {
		switch (i) {
		case 1:
			customeFeasibilityRequestNpl.setTypeA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setTypeB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 2:
			customeFeasibilityRequestNpl.setAccessTypeA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setAccessTypeB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 3:
			customeFeasibilityRequestNpl.setFeasibilityStatusA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setFeasibilityStatusB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 4:
			customeFeasibilityRequestNpl.setProviderNameA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setProviderNameB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 5:
			customeFeasibilityRequestNpl.setSfdcFeasibilityIdA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setSfdcFeasibilityIdB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 6:
			customeFeasibilityRequestNpl.setChargeableDistanceA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setChargeableDistanceB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 7:
			customeFeasibilityRequestNpl.setTclPopA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setTclPopB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 8:
			customeFeasibilityRequestNpl.setTclPopIdA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setTclPopIdB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 9:
			customeFeasibilityRequestNpl.setConnectedBuildingA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setConnectedBuildingB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 10:
			customeFeasibilityRequestNpl.setConnectedCustomerA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setConnectedCustomerB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 11:
			customeFeasibilityRequestNpl.setArcBwA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setArcBwB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 12:
			customeFeasibilityRequestNpl.setOtcA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setOtcB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 13:
			customeFeasibilityRequestNpl.setInBuildingCapexA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setInBuildingCapexB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 14:
			customeFeasibilityRequestNpl.setMuxCostA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setMuxCostB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 15:
			customeFeasibilityRequestNpl.setNeRentalA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setNeRentalB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
			
		case 16:
			customeFeasibilityRequestNpl.setOspCapexA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setOspCapexB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 17:
			customeFeasibilityRequestNpl.setOspDistanceA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setOspDistanceB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 18:
			customeFeasibilityRequestNpl.setCityA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setCityB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;	
		case 19:
			customeFeasibilityRequestNpl.setMhHhIdA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setMhHhIdB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 20:
			customeFeasibilityRequestNpl.setDeliveryTimelineA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setDeliveryTimelineB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 21:
			customeFeasibilityRequestNpl.setBandwidthA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setBandwidthB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 22:
			customeFeasibilityRequestNpl.setInterfceA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setInterfceB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 23:
			customeFeasibilityRequestNpl.setMastHeightA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setMastHeightB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 24:
			customeFeasibilityRequestNpl.setTypeOfLmA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setTypeOfLmB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 25:
			customeFeasibilityRequestNpl.setP2pPresenceA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setP2pPresenceB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 26:
			customeFeasibilityRequestNpl.setPmpPresenceA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setPmpPresenceB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 27:
			customeFeasibilityRequestNpl.setHopDistA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setHopDistB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 28:
			customeFeasibilityRequestNpl.setBtsIdA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setBtsIdB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 29:
			customeFeasibilityRequestNpl.setOtcBwA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setOtcBwB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 30:
			customeFeasibilityRequestNpl.setMastCostA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setMastCostB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 31:
			customeFeasibilityRequestNpl.setOffnetBtsIdA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setOffnetBtsIdB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 32:
			customeFeasibilityRequestNpl.setBtsAddressA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setBtsAddressB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 33:
			customeFeasibilityRequestNpl.setTentativeMastHeightA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setTentativeMastHeightB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
		case 34:
			customeFeasibilityRequestNpl.setFeasiblityRemarksA(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
			customeFeasibilityRequestNpl.setFeasiblityRemarksB(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
					? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
					: null);
			break;
			
		default:
			break;
		}
		
		return customeFeasibilityRequestNpl;
	}
	/**
	 * This method returns the template for Manual feasiblity upload for NPL
	 * @author ANANDHI VIJAY
	 * @param response
	 * @throws TclCommonException
	 */
	public void downloadCustomFpTemplateForNpl(HttpServletResponse response) throws TclCommonException {
		try (XSSFWorkbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet(OmsExcelConstants.CUSTOM_FEASIBLITY);
			processCreateHeaderCellsNpl(sheet);
			processColumnCellsForNpl(sheet);
			applyHeaderStylesForNpl(workbook, sheet);
			processTypeValidationNpl(sheet);
			processAccessTypeValidationNpl(sheet);
			processFeasibilityStatusValidationNpl(sheet);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			workbook.write(outByteStream);
			byte[] outArray = outByteStream.toByteArray();
			String fileName = OmsExcelConstants.CUSTOM_FEASIBILITY_TEMPLATE_XLSX;
			response.reset();
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());
			outByteStream.flush();
			outByteStream.close();
		} catch (Exception e) {
			LOGGER.warn("Error in processing downloadCustomFeasibilityTemplate {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private void processCreateHeaderCellsNpl(Sheet sheet) {
		Row header = sheet.createRow(0);
		header.createCell(0).setCellValue(OmsExcelConstants.ATTRIBUTE);
		header.createCell(1).setCellValue(OmsExcelConstants.SITE_A);
		header.createCell(2).setCellValue(OmsExcelConstants.SITE_B);
	}
	
	private void processColumnCellsForNpl(Sheet sheet) {
		Row row1 = sheet.createRow(1);
		row1.createCell(0).setCellValue(OmsExcelConstants.TYPE);
		Row row2 = sheet.createRow(2);
		row2.createCell(0).setCellValue(OmsExcelConstants.ACCESS_TYPE);
		Row row3 = sheet.createRow(3);
		row3.createCell(0).setCellValue(OmsExcelConstants.FEASIBILITY_STATUS);
		Row row4 = sheet.createRow(4);
		row4.createCell(0).setCellValue(OmsExcelConstants.PROVIDER_NAME);
		Row row5 = sheet.createRow(5);
		row5.createCell(0).setCellValue(OmsExcelConstants.SFDC_FEASIBLITY_ID);
		Row row6 = sheet.createRow(6);
		row6.createCell(0).setCellValue(OmsExcelConstants.CHARGEABLE_DISTANCE);
		Row row7 = sheet.createRow(7);
		row7.createCell(0).setCellValue(OmsExcelConstants.TCL_POP);
		Row row8 = sheet.createRow(8);
		row8.createCell(0).setCellValue(OmsExcelConstants.TCL_POP_ID);
		Row row9 = sheet.createRow(9);
		row9.createCell(0).setCellValue(OmsExcelConstants.CONNECTED_BUILDING);
		Row row10 = sheet.createRow(10);
		row10.createCell(0).setCellValue(OmsExcelConstants.CONNECTED_CUSTOMER);
		Row row11 = sheet.createRow(11);
		row11.createCell(0).setCellValue(OmsExcelConstants.ARC_BW);
		Row row12 = sheet.createRow(12);
		row12.createCell(0).setCellValue(OmsExcelConstants.OTC);
		Row row13 = sheet.createRow(13);
		row13.createCell(0).setCellValue(OmsExcelConstants.IN_BUILDING_CAPEX);
		Row row14 = sheet.createRow(14);
		row14.createCell(0).setCellValue(OmsExcelConstants.MUX_COST);
		Row row15 = sheet.createRow(15);
		row15.createCell(0).setCellValue(OmsExcelConstants.NE_RENTAL);
		Row row16 = sheet.createRow(16);
		row16.createCell(0).setCellValue(OmsExcelConstants.OSP_CAPEX);
		Row row17 = sheet.createRow(17);
		row17.createCell(0).setCellValue(OmsExcelConstants.OSP_DISTANCE);
		Row row18 = sheet.createRow(18);
		row18.createCell(0).setCellValue(OmsExcelConstants.CITY);
		Row row19 = sheet.createRow(19);
		row19.createCell(0).setCellValue(OmsExcelConstants.MH_HH_ID);
		Row row20 = sheet.createRow(20);
		row20.createCell(0).setCellValue(OmsExcelConstants.DELIVERY_TIMELINE);
		Row row21 = sheet.createRow(21);
		row21.createCell(0).setCellValue(OmsExcelConstants.BANDWIDTH);
		Row row22 = sheet.createRow(22);
		row22.createCell(0).setCellValue(OmsExcelConstants.INTERFACE);
		Row row23 = sheet.createRow(23);
		row23.createCell(0).setCellValue(OmsExcelConstants.MAST_HEIGHT);
		Row row24 = sheet.createRow(24);
		row24.createCell(0).setCellValue(OmsExcelConstants.TYPE_LM);
		Row row25 = sheet.createRow(25);
		row25.createCell(0).setCellValue(OmsExcelConstants.P2P_PRESENCE);
		Row row26 = sheet.createRow(26);
		row26.createCell(0).setCellValue(OmsExcelConstants.PMP_PRESENCE);
		Row row27 = sheet.createRow(27);
		row27.createCell(0).setCellValue(OmsExcelConstants.HOP_DIS);
		Row row28 = sheet.createRow(28);
		row28.createCell(0).setCellValue(OmsExcelConstants.BTS_ID);
		Row row29 = sheet.createRow(29);
		row29.createCell(0).setCellValue(OmsExcelConstants.OTC_BW);
		Row row30 = sheet.createRow(30);
		row30.createCell(0).setCellValue(OmsExcelConstants.MAST_COST);
		Row row31 = sheet.createRow(31);
		row31.createCell(0).setCellValue(OmsExcelConstants.OFFNET_BTS_ID);
		Row row32 = sheet.createRow(32);
		row32.createCell(0).setCellValue(OmsExcelConstants.BTS_ADDRESS);
		Row row33 = sheet.createRow(33);
		row33.createCell(0).setCellValue(OmsExcelConstants.TENTATIVE_MAST_HEIGHT);
		Row row34 = sheet.createRow(34);
		row34.createCell(0).setCellValue(OmsExcelConstants.FEASIBLITY_REMARKS);
	} 
	
	private void applyHeaderStylesForNpl(XSSFWorkbook workbook, Sheet sheet) {
		for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
			Row header = sheet.getRow(i);
		
			for (int j = 0; j < header.getPhysicalNumberOfCells(); j++) {
			
			CellStyle stylerowHeading = workbook.createCellStyle();
			stylerowHeading.setBorderBottom(BorderStyle.THICK);
			stylerowHeading.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
			stylerowHeading.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontName(HSSFFont.FONT_ARIAL);
			font.setFontHeightInPoints((short) 10);
			font.setColor(IndexedColors.WHITE.getIndex());
			stylerowHeading.setFont(font);
			stylerowHeading.setVerticalAlignment(VerticalAlignment.CENTER);
			stylerowHeading.setAlignment(HorizontalAlignment.CENTER);

			stylerowHeading.setWrapText(true);
			header.getCell(j).setCellStyle(stylerowHeading);

			DataFormat fmt = workbook.createDataFormat();
			CellStyle textStyle = workbook.createCellStyle();
			textStyle.setDataFormat(fmt.getFormat(CommonConstants.AT));
			sheet.setDefaultColumnStyle(j, textStyle);
			sheet.autoSizeColumn(j);
			}
		}
	}
	
	private void processTypeValidationNpl(Sheet sheet) {
		DataValidation typeDataValidation;
		DataValidationConstraint typeConstraint;
		DataValidationHelper typeValidationHelper;
		typeValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList accessTypeCellRangeList = new CellRangeAddressList(1, 1, 1, 2);
		List<String> accessTypes = new ArrayList<>();
		accessTypes.add(OmsExcelConstants.INTERCITY);
		accessTypes.add(OmsExcelConstants.INTRACITY);
		accessTypes.add(OmsExcelConstants.LINK);
		accessTypes.add(OmsExcelConstants.NA);
		typeConstraint = typeValidationHelper.createExplicitListConstraint(accessTypes.stream().toArray(String[]::new));
		typeDataValidation = typeValidationHelper.createValidation(typeConstraint, accessTypeCellRangeList);
		typeDataValidation.setSuppressDropDownArrow(true);
		typeDataValidation.setShowErrorBox(true);
		typeDataValidation.setShowPromptBox(true);
		sheet.addValidationData(typeDataValidation);
	}
	
	private void processAccessTypeValidationNpl(Sheet sheet) {
		DataValidation accessTypeDataValidation;
		DataValidationConstraint accessTypeConstraint;
		DataValidationHelper accessTypeValidationHelper;
		accessTypeValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList accessTypeCellRangeList = new CellRangeAddressList(2, 2, 1, 2);
		List<String> accessTypes = new ArrayList<>();
		accessTypes.add(OmsExcelConstants.ONNET_WIRELINE);
		accessTypes.add(OmsExcelConstants.OFFNET_WIRELINE);
		accessTypes.add(OmsExcelConstants.ONNET_RF);
		accessTypes.add(OmsExcelConstants.OFFNET_RF);
		accessTypeConstraint = accessTypeValidationHelper
				.createExplicitListConstraint(accessTypes.stream().toArray(String[]::new));
		accessTypeDataValidation = accessTypeValidationHelper.createValidation(accessTypeConstraint,
				accessTypeCellRangeList);
		accessTypeDataValidation.setSuppressDropDownArrow(true);
		accessTypeDataValidation.setShowErrorBox(true);
		accessTypeDataValidation.setShowPromptBox(true);
		sheet.addValidationData(accessTypeDataValidation);
	}
	
	private void processFeasibilityStatusValidationNpl(Sheet sheet) {
		DataValidation feasibleStatusDataValidation = null;
		DataValidationConstraint feasibleStatusConstraint = null;
		DataValidationHelper feasibleStatusValidationHelper = null;
		feasibleStatusValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);
		CellRangeAddressList feasibilityStatusAddressList = new CellRangeAddressList(3, 3, 1, 2);
		// add the selected profiles
		List<String> feasibilityStatus = new ArrayList<>();
		feasibilityStatus.add(OmsExcelConstants.MANUAL_FEASIBLE);
		feasibilityStatus.add(OmsExcelConstants.NOT_FEASIBLE);
		feasibleStatusConstraint = feasibleStatusValidationHelper
				.createExplicitListConstraint(feasibilityStatus.stream().toArray(String[]::new));
		feasibleStatusDataValidation = feasibleStatusValidationHelper.createValidation(feasibleStatusConstraint,
				feasibilityStatusAddressList);
		feasibleStatusDataValidation.setSuppressDropDownArrow(true);
		feasibleStatusDataValidation.setShowErrorBox(true);
		feasibleStatusDataValidation.setShowPromptBox(true);
		sheet.addValidationData(feasibleStatusDataValidation);
	}

	/**
	 * Method to extract all the feasibility attributes from file
	 *
	 * @param file
	 * @return {@link List}
	 * @throws TclCommonException
	 */
	public List<GvpnIntlCustomFeasibilityRequest> extractCustomFeasibiltyForGvpnIntl(MultipartFile file) throws TclCommonException {
		List<GvpnIntlCustomFeasibilityRequest> gvpnIntlCustomFeasibilityRequests = new ArrayList<>();
		try (Workbook workbook = WorkbookFactory.create(file.getInputStream(), "UTF-8")) {
			for (Sheet sheet : workbook) {
				LOGGER.info("Total no of rows => {}", sheet.getPhysicalNumberOfRows());
				if (sheet.getPhysicalNumberOfRows() >= 2) {
					gvpnIntlCustomFeasibilityRequests.add(extractExcelDataGvpnIntl(sheet, 1));
				} else {
					throw new TclCommonException(ExceptionConstants.CUSTOM_FEASIBILITY_VALIDATIION, ResponseResource.R_CODE_ERROR);
				}
				if (sheet.getPhysicalNumberOfRows() >= 3) {
					gvpnIntlCustomFeasibilityRequests.add(extractExcelDataGvpnIntl(sheet, 2));
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return gvpnIntlCustomFeasibilityRequests;
	}

	/**
	 * Method to extract excel data for gvpn intl
	 *
	 * @param sheet
	 * @param i
	 * @return {@link GvpnIntlCustomFeasibilityRequest}
	 */
	private GvpnIntlCustomFeasibilityRequest extractExcelDataGvpnIntl(Sheet sheet, int i) {
		GvpnIntlCustomFeasibilityRequest customFeasibilityRequest = new GvpnIntlCustomFeasibilityRequest();
		customFeasibilityRequest.setType(sheet.getRow(i).getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
				? sheet.getRow(i).getCell(0, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
				: null);
		customFeasibilityRequest
				.setAccessType(sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(1, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setFeasibilityStatus(sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(2, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setSfdcFeasibilityId(sheet.getRow(i).getCell(3, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(3, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setProviderName(sheet.getRow(i).getCell(4, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(4, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setRecordType(sheet.getRow(i).getCell(5, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(5, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setTclPOPAddress(sheet.getRow(i).getCell(6, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(6, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setTclPOPSiteCode(sheet.getRow(i).getCell(7, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(7, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setProviderProductName(sheet.getRow(i).getCell(8, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(8, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setCustomerLatLong(sheet.getRow(i).getCell(9, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(9, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setLocalLoopCapacity(sheet.getRow(i).getCell(10, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(10, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setLocalLoopInterface(sheet.getRow(i).getCell(11, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(11, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setInterConnectionType(sheet.getRow(i).getCell(12, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(12, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setContractTermWithVendor(sheet.getRow(i).getCell(13, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(13, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setLmCurrency(sheet.getRow(i).getCell(14, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(14, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setLmBwMRC(sheet.getRow(i).getCell(15, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(15, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setOtcNrcInstallation(sheet.getRow(i).getCell(16, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(16, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setxConnectCurrency(sheet.getRow(i).getCell(17, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(17, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setxConnectMRC(sheet.getRow(i).getCell(18, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(18, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setxConnectNRC(sheet.getRow(i).getCell(19, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(19, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		customFeasibilityRequest
				.setRemarks(sheet.getRow(i).getCell(20, MissingCellPolicy.RETURN_BLANK_AS_NULL) != null
						? sheet.getRow(i).getCell(20, MissingCellPolicy.RETURN_BLANK_AS_NULL).getStringCellValue()
						: null);
		return customFeasibilityRequest;
	}
}
