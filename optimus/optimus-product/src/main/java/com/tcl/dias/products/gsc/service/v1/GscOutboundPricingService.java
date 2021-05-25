package com.tcl.dias.products.gsc.service.v1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.productcatelog.entity.entities.GscOutboundPricing;
import com.tcl.dias.productcatelog.entity.entities.GscOutboundSurchargePrices;
import com.tcl.dias.productcatelog.entity.repository.GscOutboundPricingRepository;
import com.tcl.dias.productcatelog.entity.repository.GscOutboundSurchargePricingRepository;
import com.tcl.dias.products.constants.ExceptionConstants;
import com.tcl.dias.products.gsc.beans.GscOutboundPricingBean;
import com.tcl.dias.products.gsc.beans.OutboundSurchargePricingBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Class contains all method related to gscoutboundpricing table.
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GscOutboundPricingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GscOutboundPricingService.class);

	@Autowired
	GscOutboundPricingRepository gscOutboundPricingRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Autowired
	GscOutboundSurchargePricingRepository gscOutboundSurchargePricingRepository;

	/**
	 * Method to convert to json
	 * 
	 * @param object
	 * @return
	 */
	public static String toJson(Object object) {
		String json = null;
		try {
			json = Utils.convertObjectToJson(object);
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return json;
	}

	/**
	 * Method to download outboundprices
	 * 
	 * @param response
	 * @param fileType
	 * @return ({@link HttpServletResponse}
	 * @throws IOException
	 * @throws DocumentException
	 * @throws TclCommonException
	 */
	public HttpServletResponse downloadOutboundPrices(HttpServletResponse response, String fileType)
			throws IOException, DocumentException, TclCommonException {
		Objects.requireNonNull("FileType cannot be null");
		List<GscOutboundPricingBean> gscOutboundPricingBeans = getOutboundPrices();
		String fileName = "GscOutBoundPrices";
		String template = "outboundpricing_template";
		if (fileType.equalsIgnoreCase("pdf")) {
			generatePdf(response, gscOutboundPricingBeans, fileName, template);
		} else if (fileType.equalsIgnoreCase("excel")) {
			generateExcel(response, gscOutboundPricingBeans, fileName);
		} else {
			throw new TclCommonException("Unsupported file type, choose pdf or excel");
		}

		return response;
	}

	/**
	 * Generate data in Excel
	 * 
	 * @param response
	 * @param gscOutboundPricingBeans
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	private HttpServletResponse generateExcel(HttpServletResponse response,
			List<GscOutboundPricingBean> gscOutboundPricingBeans, String fileName)
			throws IOException, TclCommonException {

		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		Context context = new Context();
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		sheet.setColumnWidth(50000, 50000);

		context.row = sheet.createRow(context.rowCount);
		writeBookColoumnNames(context.row, context.rowCount);
		context.rowCount++;
		gscOutboundPricingBeans.forEach(gscOutboundPricingBean -> {
			context.row = sheet.createRow(context.rowCount);
			writeBook(gscOutboundPricingBean, context.row, context.rowCount);
			context.rowCount++;
		});

		outArray = outByteStream.toByteArray();
		response.reset();
		response.setContentType("application/ms-excel");
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xls" + "\"");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT");
		response.setHeader("Access-Control-Allow-Headers", "Content-Type");
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		response.setHeader("Pragma", "no-cache");
		workbook.close();
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		outByteStream.flush();
		outByteStream.close();

		return response;

	}

	/**
	 * Generate data in pdf
	 * 
	 * @param response
	 * @param gscOutboundPricingBeans
	 * @param fileName
	 * @param template
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 * @throws TclCommonException
	 */
	private HttpServletResponse generatePdf(HttpServletResponse response,
			List<GscOutboundPricingBean> gscOutboundPricingBeans, String fileName, String template)
			throws DocumentException, IOException, TclCommonException {
		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		Map<String, Object> variable = objectMapper
				.convertValue(ImmutableMap.of("gscOutboundPricingBeans", gscOutboundPricingBeans), Map.class);
		org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
		context.setVariables(variable);
		String html = templateEngine.process(template, context);
		PDFGenerator.createPdf(html, outByteStream);
		outArray = outByteStream.toByteArray();
		response.reset();
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setContentLength(outArray.length);
		response.setHeader("Expires:", "0");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".pdf" + "\"");
		try {
			FileCopyUtils.copy(outArray, response.getOutputStream());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		outByteStream.flush();
		outByteStream.close();

		return response;
	}

	/**
	 * write coloumns in sheet
	 * 
	 * @param row
	 * @param rowCount
	 */
	private void writeBookColoumnNames(Row row, Integer rowCount) {
		Cell cell = row.createCell(0);
		cell.setCellValue("UID");

		cell = row.createCell(1);
		cell.setCellValue("country");

		cell = row.createCell(2);
		cell.setCellValue("countryCode");

		cell = row.createCell(3);
		cell.setCellValue("phoneType");

		cell = row.createCell(4);
		cell.setCellValue("destId");

		cell = row.createCell(5);
		cell.setCellValue("destinationName");

		cell = row.createCell(6);
		cell.setCellValue("serviceLevel");

		cell = row.createCell(7);
		cell.setCellValue("comments");

		cell = row.createCell(8);
		cell.setCellValue("region");

		cell = row.createCell(9);
		cell.setCellValue("currencyCode");

		cell = row.createCell(10);
		cell.setCellValue("cdaFloor");

		cell = row.createCell(11);
		cell.setCellValue("spRegionDiscount");

		cell = row.createCell(12);
		cell.setCellValue("spDiscount3");

		cell = row.createCell(13);
		cell.setCellValue("spDiscount2");

		cell = row.createCell(14);
		cell.setCellValue("spDiscount1");

		cell = row.createCell(15);
		cell.setCellValue("serviceProviderFloor");

		cell = row.createCell(16);
		cell.setCellValue("epRegionDiscount");

		cell = row.createCell(17);
		cell.setCellValue("enterpriseDiscount3");

		cell = row.createCell(18);
		cell.setCellValue("enterpriseDiscount2");

		cell = row.createCell(19);
		cell.setCellValue("enterpriseDiscount1");

		cell = row.createCell(20);
		cell.setCellValue("enterpriseSalesFloor");

		cell = row.createCell(21);
		cell.setCellValue("highRate");

		cell = row.createCell(22);
		cell.setCellValue("highestPossibleObcSurcharge");

		cell = row.createCell(24);
		cell.setCellValue("internalComments");

		cell = row.createCell(25);
		cell.setCellValue("isActiveInd");

		cell = row.createCell(26);
		cell.setCellValue("effectiveFromDate");

		cell = row.createCell(27);
		cell.setCellValue("effectiveToDate");

		cell = row.createCell(28);
		cell.setCellValue("reasonText");

		cell = row.createCell(29);
		cell.setCellValue("createdBy");

		cell = row.createCell(30);
		cell.setCellValue("createdDate");

		cell = row.createCell(31);
		cell.setCellValue("updatedBy");

		cell = row.createCell(32);
		cell.setCellValue("updatedDate");

	}

	/**
	 * 
	 * @author AVALLAPI
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	public static class Context {
		Integer rowCount = 0;
		Row row;
	}

	/**
	 * Get outbound prices from view
	 * 
	 * @return {@link List<GscOutboundPricingBean>}
	 */
	public List<GscOutboundPricingBean> getOutboundPrices() {
		List<GscOutboundPricing> gscOutboundPrices = gscOutboundPricingRepository.findAll();
		return gscOutboundPrices.stream().map(GscOutboundPricingBean::fromGscOutboundPricing)
				.collect(Collectors.toList());
	}

	/**
	 * write all data to Book
	 * 
	 * @param gscOutboundPricingBean
	 * @param row
	 * @param rowCount
	 */
	private void writeBook(GscOutboundPricingBean gscOutboundPricingBean, Row row, Integer rowCount) {

		Cell cell = row.createCell(0);
		Optional.ofNullable(gscOutboundPricingBean.getuID()).ifPresent(cell::setCellValue);

		cell = row.createCell(1);
		Optional.ofNullable(gscOutboundPricingBean.getCountry()).ifPresent(cell::setCellValue);

		cell = row.createCell(2);
		Optional.ofNullable(gscOutboundPricingBean.getCountryCode()).ifPresent(cell::setCellValue);

		cell = row.createCell(3);
		Optional.ofNullable(gscOutboundPricingBean.getPhoneType()).ifPresent(cell::setCellValue);

		cell = row.createCell(4);
		Optional.ofNullable(gscOutboundPricingBean.getDestId()).ifPresent(cell::setCellValue);

		cell = row.createCell(5);
		Optional.ofNullable(gscOutboundPricingBean.getDestinationName()).ifPresent(cell::setCellValue);

		cell = row.createCell(6);
		Optional.ofNullable(gscOutboundPricingBean.getServiceLevel()).ifPresent(cell::setCellValue);

		cell = row.createCell(7);
		Optional.ofNullable(gscOutboundPricingBean.getComments()).ifPresent(cell::setCellValue);

		cell = row.createCell(8);
		Optional.ofNullable(gscOutboundPricingBean.getRegion()).ifPresent(cell::setCellValue);

		cell = row.createCell(9);
		Optional.ofNullable(gscOutboundPricingBean.getCurrencyCode()).ifPresent(cell::setCellValue);

		cell = row.createCell(10);
		Optional.ofNullable(gscOutboundPricingBean.getCdaFloor()).ifPresent(cell::setCellValue);

		cell = row.createCell(11);
		Optional.ofNullable(gscOutboundPricingBean.getSpRegionDiscount()).ifPresent(cell::setCellValue);

		cell = row.createCell(12);
		Optional.ofNullable(gscOutboundPricingBean.getSpDiscount3()).ifPresent(cell::setCellValue);

		cell = row.createCell(13);
		Optional.ofNullable(gscOutboundPricingBean.getSpDiscount2()).ifPresent(cell::setCellValue);

		cell = row.createCell(14);
		Optional.ofNullable(gscOutboundPricingBean.getSpDiscount1()).ifPresent(cell::setCellValue);

		cell = row.createCell(15);
		Optional.ofNullable(gscOutboundPricingBean.getServiceProviderFloor()).ifPresent(cell::setCellValue);

		cell = row.createCell(16);
		Optional.ofNullable(gscOutboundPricingBean.getEpRegionDiscount()).ifPresent(cell::setCellValue);

		cell = row.createCell(17);
		Optional.ofNullable(gscOutboundPricingBean.getEnterpriseDiscount3()).ifPresent(cell::setCellValue);

		cell = row.createCell(18);
		Optional.ofNullable(gscOutboundPricingBean.getEnterpriseDiscount2()).ifPresent(cell::setCellValue);

		cell = row.createCell(19);
		Optional.ofNullable(gscOutboundPricingBean.getEnterpriseDiscount1()).ifPresent(cell::setCellValue);

		cell = row.createCell(20);
		Optional.ofNullable(gscOutboundPricingBean.getEnterpriseSalesFloor()).ifPresent(cell::setCellValue);

		cell = row.createCell(21);
		Optional.ofNullable(gscOutboundPricingBean.getHighRate()).ifPresent(cell::setCellValue);

		cell = row.createCell(22);
		Optional.ofNullable(gscOutboundPricingBean.getHighestPossibleObcSurcharge()).ifPresent(cell::setCellValue);

		cell = row.createCell(23);
		Optional.ofNullable(gscOutboundPricingBean.getInternalComments()).ifPresent(cell::setCellValue);

		cell = row.createCell(24);
		Optional.ofNullable(gscOutboundPricingBean.getIsActiveInd()).ifPresent(cell::setCellValue);

		cell = row.createCell(25);
		Optional.ofNullable(gscOutboundPricingBean.getEffectiveFromDate()).ifPresent(cell::setCellValue);

		cell = row.createCell(26);
		Optional.ofNullable(gscOutboundPricingBean.getEffectiveToDate()).ifPresent(cell::setCellValue);

		cell = row.createCell(27);
		Optional.ofNullable(gscOutboundPricingBean.getReasonText()).ifPresent(cell::setCellValue);

		cell = row.createCell(28);
		Optional.ofNullable(gscOutboundPricingBean.getCreatedBy()).ifPresent(cell::setCellValue);

		cell = row.createCell(29);
		Optional.ofNullable(gscOutboundPricingBean.getCreatedDate()).ifPresent(cell::setCellValue);

		cell = row.createCell(30);
		Optional.ofNullable(gscOutboundPricingBean.getUpdatedBy()).ifPresent(cell::setCellValue);

		cell = row.createCell(31);
		Optional.ofNullable(gscOutboundPricingBean.getUpdatedDate()).ifPresent(cell::setCellValue);

	}

	/**
	 * Get Destination Id and comments from view
	 * 
	 * @author PRABUBALASUBRAMANIAN
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return {@link List<GscOutboundPricingBean>}
	 */
	public List<GscOutboundPricingBean> getDestinationIdAndComments(String destinationName) {
		LOGGER.info("Destination Names is {} ", destinationName);
		List<String> destinationNames = Arrays
				.asList(destinationName.replace("[", "").replace("]", "").split("\\s*,\\s*"));
		List<GscOutboundPricing> gscOutboundPrices = gscOutboundPricingRepository
				.findByDestinationNameIn(destinationNames);
		return gscOutboundPrices.stream().map(GscOutboundPricingBean::fromGscOutboundPricing)
				.collect(Collectors.toList());
	}

	/**
	 * Get outbound surcharge charges
	 * 
	 * @return {@link List<OutboundSurchargePricingBean>}
	 */
	public List<OutboundSurchargePricingBean> getAllSurchargeCharges() {
		List<GscOutboundSurchargePrices> gscOutboundSurchargePrices = gscOutboundSurchargePricingRepository.findAll();
		return gscOutboundSurchargePrices.stream().map(OutboundSurchargePricingBean::fromGscOutboundSurchargePricing)
				.collect(Collectors.toList());
	}

	/**
	 * Get Outbound details from view
	 * 
	 * @author PRABUBALASUBRAMANIAN
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 * @return {@link List<GscOutboundPricingBean>}
	 */
	public List<GscOutboundPricingBean> getOutboundDetails(String rate) {
		List<GscOutboundPricing> gscOutboundPrices = gscOutboundPricingRepository.findAll();
		return gscOutboundPrices.stream().map(GscOutboundPricingBean::fromGscOutboundPricing)
				.collect(Collectors.toList());
	}

}
