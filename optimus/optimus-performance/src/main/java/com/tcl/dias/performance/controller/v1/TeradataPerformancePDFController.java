package com.tcl.dias.performance.controller.v1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.tcl.dias.beans.FaultRate;
import com.tcl.dias.beans.MTTRSeverityResponseBean;
import com.tcl.dias.beans.MTTRTrendRFOWise;
import com.tcl.dias.beans.PDFRequestBean;
import com.tcl.dias.beans.SLABreachBean;
import com.tcl.dias.beans.ServerityTicketResponse;
import com.tcl.dias.beans.ServiceInventoryBean;
import com.tcl.dias.beans.TicketExcelBean;
import com.tcl.dias.beans.TrendResponseBean;
import com.tcl.dias.beans.UptimeExcelBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.performance.constants.ExceptionConstants;
import com.tcl.dias.performance.constants.HighChartConstants;
import com.tcl.dias.performance.service.v1.PDFReportService;
import com.tcl.dias.performance.service.v1.TeradataPerformanceService;
import com.tcl.dias.performance.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the PerformanceController class for generating all charts
 * to PDF.
 * 
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/performance/teradata")
public class TeradataPerformancePDFController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TeradataPerformancePDFController.class);

	@Autowired
	TeradataPerformanceService performanceService;

	@Autowired
	PDFReportService pdfReportService;

	@Autowired
	SpringTemplateEngine templateEngine;
	
	@Value("${rabbitmq.user.products.queue}")
	String inventoryConsumerQueue;
	
	@Autowired
	MQUtils mqUtils;

	/**
	 * @param request - post request containing request months
	 * @return PDF report containing all charts
	 * @throws TclCommonException
	 * @throws IOException
	 * @throws ParseException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.GET_PERFORMANCE_PDF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/getPDFReport", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<byte[]> getReportPDF(@RequestBody PDFRequestBean request)
			throws TclCommonException, IOException, ParseException {

		Context context = new Context();
		String monthRfo=request.getMonth();
		monthRfo = convertDateStrFormat("MM-yyyy", "MM/dd/yyyy", monthRfo);
		LOGGER.info("monthRfo:: {}", monthRfo);
		List<String> monthYearListForRFO = null;
		String startDate =null;
		String endDate =null;
		
		String[] monthsplitArr = request.getMonth().split("-");
		String monthStr = monthsplitArr[0];

		SimpleDateFormat sdf = new SimpleDateFormat("yy");
		monthsplitArr[1] = sdf.format(sdf.parse(monthsplitArr[1]));

		String trimmedMonth = "'" + Integer.valueOf(monthStr) + "-" + monthsplitArr[1] + "'";
		LOGGER.info("Trimmed Month for fetching fault rate: " + trimmedMonth);

		// calculate past 6 months from given month
		LOGGER.info("getting MonthRange");
		List<String> monthYearListForTrends = getMonthRange(Integer.valueOf(monthStr) + "-" + monthsplitArr[1], 3,"M-yy");
		
		LOGGER.info("monthYearListForTrends:: {}", monthYearListForTrends);

		// RFO report
		LOGGER.info("getMonthlyOutageReport");
		Map<String, Object> rfoPieMap = performanceService.getMonthlyOutageReport(request.getMonth());

		rfoPieMap.forEach((key, val) -> {
			LOGGER.info("KEY---------> " + key + "  Val----------> " + String.valueOf(val));
		});
		rfoPieMap.remove(HighChartConstants.TOTAL);
		rfoPieMap.remove(HighChartConstants.OTHER_KEYS);

		if (rfoPieMap.get("Others").equals(0)) {
			rfoPieMap.remove("Others");
		}

		if (rfoPieMap != null && !rfoPieMap.isEmpty()) {
			LOGGER.info("rfoPieMap :: {}");
			context.setVariable(HighChartConstants.PIECHART,
					pdfReportService.getRFOPieChart(rfoPieMap, HighChartConstants.PIECHART_TITLE));
		}
		LOGGER.info("performing Queue call for rabbitmq.user.products.queue");
		List<String> productsForUser = (List<String>) mqUtils.sendAndReceive(inventoryConsumerQueue, null);
		LOGGER.info("after Queue call for rabbitmq.user.products.queue ::{}", productsForUser);
		// service wise Incident per month
		ServerityTicketResponse serviceWiseIncidentPerMonth = performanceService
				.getMonthlySeverityTickets(request.getMonth(),productsForUser);
		
		if (serviceWiseIncidentPerMonth != null && serviceWiseIncidentPerMonth.getTrends() != null
				&& serviceWiseIncidentPerMonth.getTrends().size() > 0) {
			context.setVariable(HighChartConstants.SERVICE_WISE_INCIDENT_PER_MONTH,
					pdfReportService.getStackedColumnChart(serviceWiseIncidentPerMonth,
							HighChartConstants.SERVICE_WISE_INCIDENT_FOR_MONTH_TITLE, true,"Ticket Count"));
		}
		// service wise Ticket trend
		monthYearListForRFO= getMonthRange(monthRfo, 6,"MM/dd/yyyy");
		startDate=addQuote(monthYearListForRFO.get(monthYearListForRFO.size() -1));
		endDate = addQuote(getLastDate(monthRfo));
		List<String> monthListforStackChart = monthYearListForRFO.stream()
				.map(x -> convertDateStrFormat("MM/dd/yyyy", "MMM-yyyy", x)).collect(Collectors.toList());

		ServerityTicketResponse impactsWiseIncidentsTrend = performanceService
				.getTicketTrendSeverityWiseForGivenMonthRange(startDate,endDate,monthListforStackChart);
		LOGGER.info("getting impactsWiseIncidentsTrend");
		if (impactsWiseIncidentsTrend != null && impactsWiseIncidentsTrend.getTrends() != null
				&& !impactsWiseIncidentsTrend.getTrends().isEmpty()) {
			context.setVariable(HighChartConstants.TICKET_TREND_SEVERITY, pdfReportService.getStackedColumnChart(
					impactsWiseIncidentsTrend, HighChartConstants.IMPACTS_WISE_INCIDENTS_TREND_TITLE,false,"Ticket Count"));
		} 
		
		LOGGER.info("Impact wise incident trend with RFO Customer/ TATA : START DATE --->"+ startDate);
		LOGGER.info("Impact wise incident trend with RFO Customer/ TATA : END DATE --->"+ endDate);

		// service wise Ticket trend with RFO responsible as customer.
		ServerityTicketResponse impactsWiseIncidentsTrendRFOCust = performanceService
				.getTicketTrendSeverityWiseRFO(HighChartConstants.QUERT_PARAM_CUST_WITH_QUOTE,startDate,endDate,monthListforStackChart);
		LOGGER.info("impactsWiseIncidentsTrendRFOCust ::{}",impactsWiseIncidentsTrendRFOCust);

		if (impactsWiseIncidentsTrendRFOCust != null && impactsWiseIncidentsTrendRFOCust.getTrends() != null
				&& !impactsWiseIncidentsTrendRFOCust.getTrends().isEmpty()) {
			context.setVariable(HighChartConstants.TICKET_TEND_RFO_CUSTOMER, pdfReportService.getStackedColumnChart(
					impactsWiseIncidentsTrendRFOCust, HighChartConstants.TICKET_TREND_RFO_CUSTOMER_TITLE,false,"Ticket Count"));
		}

		// service wise Ticket trend with RFO responsible as TATA.
		ServerityTicketResponse impactsWiseIncidentsTrendTATARfo = performanceService
				.getTicketTrendSeverityWiseRFO(HighChartConstants.QUERY_PARAM_TATA_COMM,startDate,endDate,monthListforStackChart);
		LOGGER.info("impactsWiseIncidentsTrendTATARfo ::{}",impactsWiseIncidentsTrendTATARfo);

		if (impactsWiseIncidentsTrendTATARfo != null && impactsWiseIncidentsTrendTATARfo.getTrends() != null
				&& !impactsWiseIncidentsTrendTATARfo.getTrends().isEmpty()) {
			context.setVariable(HighChartConstants.TICKET_TEND_RFO_TATA, pdfReportService.getStackedColumnChart(
					impactsWiseIncidentsTrendTATARfo, HighChartConstants.TICKET_TREND_RFO_TATA_TITLE,false,"Ticket Count"));
		}
		// MTTR Trends
		// reassigning start date and end date as MTTR graphs needs 3 months trend only.
		
		monthYearListForRFO= getMonthRange(monthRfo, 3,"MM/dd/yyyy");
		startDate=addQuote(monthYearListForRFO.get(monthYearListForRFO.size() -1));
		endDate = addQuote(getLastDate(monthRfo));
		
		LinkedHashMap<String, HashMap<String, String>> mttrLine = performanceService.getMttrTrendReportForPDF(startDate,endDate);
		LOGGER.info("getMttrTrendReportForPDF ::{}",mttrLine);
		
		  if (mttrLine != null &&  !mttrLine.isEmpty()) {
		  context.setVariable(HighChartConstants.LINE_CHART,
		  pdfReportService.getBasicLineChart(mttrLine,
		  HighChartConstants.MTTR_FOR_ALL_INCIDENTS_X_TITLE)); }
		 

		// MTTR severity - total loss of service and Partial Loss
		List<MTTRSeverityResponseBean> mttrSeverityTLBean = performanceService
				.getMttrSeverityReportForPDF(HighChartConstants.TOTAL_LOSS_OF_SERVICE,startDate,endDate);
		LOGGER.info("mttrSeverityTLBean ::{}",mttrSeverityTLBean);
		List<MTTRSeverityResponseBean> mttrSeverityPLBean = performanceService
				.getMttrSeverityReportForPDF(HighChartConstants.PARTIAL_LOSS,startDate,endDate);
		LOGGER.info("mttrSeverityPLBean :: {}",mttrSeverityPLBean);

		if (mttrSeverityTLBean != null && mttrSeverityPLBean != null && !mttrSeverityTLBean.isEmpty()
				&& !mttrSeverityPLBean.isEmpty()) {
			context.setVariable(HighChartConstants.ALL_INCIDENTS_MTTR,
					pdfReportService.getMTTRColumn(mttrSeverityTLBean, mttrSeverityPLBean,
							HighChartConstants.MTTR_FOR_ALL_INCIDENTS_X_TITLE, HighChartConstants.MTTR_VALUES_Y_TITLE));
		}
		// RFO report
		Object[] rfoPieMapsObj = performanceService.getMonthlyOutageReportForCustAndNonCust(request.getMonth());
		LOGGER.info("rfoPieMapsObj :: {}",rfoPieMapsObj);

		Map<String, Object> custEndMap = (Map<String, Object>) rfoPieMapsObj[0];
		Map<String, Object> nonCustEndMap = (Map<String, Object>) rfoPieMapsObj[1];
		custEndMap.remove(HighChartConstants.TOTAL);
		nonCustEndMap.remove(HighChartConstants.TOTAL);

		if (custEndMap.get("Others").equals(0)) {
			custEndMap.remove("Others");
		}
		if (nonCustEndMap.get("Others").equals(0)) {
			nonCustEndMap.remove("Others");
		}

		if (custEndMap != null && !custEndMap.isEmpty()) {
			context.setVariable(HighChartConstants.RFO_CUST_END,
					pdfReportService.getRFOPieChart(custEndMap, HighChartConstants.RFO_CUSTOMER_END_TITLE));
		}
		if (nonCustEndMap != null && !nonCustEndMap.isEmpty()) {
			context.setVariable(HighChartConstants.RFO_NON_CUST_END,
					pdfReportService.getRFOPieChart(nonCustEndMap, HighChartConstants.RFO_OTHER_THAN_CUSTOMER_TITLE));
		}
		// MTTR (For incidents with RFO responsible as Customer)
		LOGGER.info("For incidents with RFO responsible as Customer");
		List<MTTRSeverityResponseBean> custEndTLRespList = performanceService.getMttrSeverityReportTataOrCustomer(
				HighChartConstants.TOTAL_LOSS_OF_SERVICE, HighChartConstants.QUERT_PARAM_CUST_WITH_QUOTE,startDate,endDate);
		LOGGER.info("getMttrSeverityReportTataOrCustomer::{}",custEndTLRespList);
		List<MTTRSeverityResponseBean> custEndPLRespList = performanceService.getMttrSeverityReportTataOrCustomer(
				HighChartConstants.PARTIAL_LOSS, HighChartConstants.QUERT_PARAM_CUST_WITH_QUOTE,startDate,endDate);
		LOGGER.info("getMttrSeverityReportTataOrCustomer::{}",custEndPLRespList);

		if (custEndTLRespList != null && custEndPLRespList != null && !custEndPLRespList.isEmpty()
				&& !custEndTLRespList.isEmpty()) {
			context.setVariable(HighChartConstants.MTTR_INCIDENT_RFO_CUST,
					pdfReportService.getMTTRColumn(custEndTLRespList, custEndPLRespList,
							HighChartConstants.MTTR_FOR_INCIDENTS_RFO_CUST_TITLE,
							HighChartConstants.MTTR_VALUES_Y_TITLE));
		}
		// MTTR (For incidents with RFO responsible as TATA)
		List<MTTRSeverityResponseBean> tataEndTLRespList = performanceService.getMttrSeverityReportTataOrCustomer(
				HighChartConstants.TOTAL_LOSS_OF_SERVICE, HighChartConstants.QUERT_PARAM_TATA_WITH_QOUTE,startDate,endDate);
		LOGGER.info("tataEndTLRespList ::{}",tataEndTLRespList);
		List<MTTRSeverityResponseBean> tataEndPLRespList = performanceService.getMttrSeverityReportTataOrCustomer(
				HighChartConstants.PARTIAL_LOSS, HighChartConstants.QUERT_PARAM_TATA_WITH_QOUTE,startDate,endDate);
		LOGGER.info("tataEndPLRespList ::{}",tataEndPLRespList);

		if (tataEndTLRespList != null && !tataEndTLRespList.isEmpty() && tataEndPLRespList != null
				&& !tataEndPLRespList.isEmpty()) {
			context.setVariable(HighChartConstants.MTTR_INCIDENT_RFO_TATA,
					pdfReportService.getMTTRColumn(tataEndTLRespList, tataEndPLRespList,
							HighChartConstants.MTTR_FOR_INCIDENTS_RFO_TATA_COMM,
							HighChartConstants.MTTR_VALUES_Y_TITLE));
		}

		List<MTTRTrendRFOWise> mttrTrendTLRFOResponsible = performanceService.getMttrTrendReportRfoResponsibleWise(
				HighChartConstants.QUERT_PARAM_CUST_WITH_QUOTE, HighChartConstants.QUERY_PARAM_TOTAL_LOSS_WITH_QUOTE,startDate,endDate);
		LOGGER.info("mttrTrendTLRFOResponsible {}",mttrTrendTLRFOResponsible);
		List<MTTRTrendRFOWise> mttrTrendPLRFOResponsible = performanceService.getMttrTrendReportRfoResponsibleWise(
				HighChartConstants.QUERT_PARAM_CUST_WITH_QUOTE, HighChartConstants.QUERY_PARAM_PARTIAL_LOSS_WITH_QUOTE,startDate,endDate);
		LOGGER.info("mttrTrendPLRFOResponsible {}",mttrTrendPLRFOResponsible);

		if (mttrTrendTLRFOResponsible != null && !mttrTrendTLRFOResponsible.isEmpty()
				&& mttrTrendPLRFOResponsible != null && !mttrTrendPLRFOResponsible.isEmpty()) {
			context.setVariable(HighChartConstants.LINE_MTTR_CUST_TREND,
					pdfReportService.getBasicLineChartForMTTRTrend(mttrTrendTLRFOResponsible, mttrTrendPLRFOResponsible,
							HighChartConstants.MTTR_FOR_INCIDENTS_RFO_CUST_TITLE));
		}

		List<MTTRTrendRFOWise> mttrTrendTLRFOTataResponsible = performanceService.getMttrTrendReportRfoResponsibleWise(
				HighChartConstants.QUERT_PARAM_TATA_WITH_QOUTE, HighChartConstants.QUERY_PARAM_TOTAL_LOSS_WITH_QUOTE,startDate,endDate);
		List<MTTRTrendRFOWise> mttrTrendPLRFOTataResponsible = performanceService.getMttrTrendReportRfoResponsibleWise(
				HighChartConstants.QUERT_PARAM_TATA_WITH_QOUTE, HighChartConstants.QUERY_PARAM_PARTIAL_LOSS_WITH_QUOTE,startDate,endDate); 

		if (mttrTrendTLRFOTataResponsible != null && mttrTrendPLRFOTataResponsible != null
				&& !mttrTrendPLRFOTataResponsible.isEmpty() && !mttrTrendTLRFOTataResponsible.isEmpty()) {

			context.setVariable(HighChartConstants.LINE_MTTR_TATA_TREND,
					pdfReportService.getBasicLineChartForMTTRTrend(mttrTrendTLRFOTataResponsible,
							mttrTrendPLRFOTataResponsible, HighChartConstants.MTTR_FOR_INCIDENTS_RFO_TATA_COMM));
		}
		// product Wise Uptime Trend for given months
		List<TrendResponseBean> productWiseUptimeTrendList = performanceService
				.getUptimeTrendInfo(monthYearListForTrends);
		LOGGER.info("productWiseUptimeTrendList {}",productWiseUptimeTrendList);
		List<String> monthListINMMYYYYFormat = monthYearListForTrends.stream()
				.map(x -> convertDateStrFormat("M-yy", "MM-yyyy", x)).collect(Collectors.toList());
		if (productWiseUptimeTrendList != null && !productWiseUptimeTrendList.isEmpty()) {
			context.setVariable(HighChartConstants.UPTIME_COLUMN,
					pdfReportService.getBasicColumnChart(productWiseUptimeTrendList,
							HighChartConstants.PRODUCT_WISE_UPTIME_TREND_XTITLE, HighChartConstants.UPTIME_YTITLE,
							monthListINMMYYYYFormat, false));
		}

		// get SLA trend for given months.
//		List<TrendResponseBean> slaList = performanceService.geSLATrendInfo(monthYearListForTrends);
//		LOGGER.info("slaList {}", slaList);
//
//		if (slaList != null && !slaList.isEmpty()) {
//			context.setVariable(HighChartConstants.COLUMN_SLA, pdfReportService.getBasicColumnChart(slaList,
//					HighChartConstants.SLA_BREACH_CASES_XTITLE, HighChartConstants.BREACH_TITLE,monthListINMMYYYYFormat,true));
//		}
		// Get faultRate for given month
		List<FaultRate> faultRate = performanceService.getFaultrate(trimmedMonth,productsForUser);
		LOGGER.info("faultRate ::{}",faultRate);

		if (faultRate != null && !faultRate.isEmpty()) {
			context.setVariable(HighChartConstants.FAULT_RATE, pdfReportService.getFaultRateColumnChart(faultRate,
					HighChartConstants.FAULT_RATE_X_TITLE, HighChartConstants.FAULT_RATE_YTITLE));
		}
		
		// Get SLA Breach cases for given month
		SimpleDateFormat sdfSLA = new SimpleDateFormat("MM-yyyy");
		Date requestDate = sdfSLA.parse(request.getMonth());
		
//		List<SLABreachBean> breachBean = performanceService.getSLABreachedCases(request.getMonth());
//		LOGGER.info("breachBean {}",breachBean);
//
//		if (breachBean != null && !breachBean.isEmpty()) {
//			context.setVariable(HighChartConstants.CONTEXT_VAR_SLA_REACH_CASES_REPORTS, breachBean);
//		} 
		
		// Get Service Inventory details Type wise-- Doughnut chart
		Map<String, String> siMap = performanceService.getServiceInventoryDetailTypeWiseForPDF(request.getMonth());
		if (siMap != null && !siMap.isEmpty()) {
			context.setVariable(HighChartConstants.SERVICE_INVENTORY_CONTEXT,
					pdfReportService.getDonutChart(siMap, HighChartConstants.SERVICE_WISE_INVENTORY));
		}
		// Get service inventory details status wise -- doughnut chart
		Map<String, String> siStatusMap = performanceService
				.getServiceInventoryDetailStatusWiseForPDF(request.getMonth());
		if (siStatusMap != null && !siStatusMap.isEmpty()) {
			context.setVariable(HighChartConstants.SERVICE_INVENTORY_STATUS_CONTEXT,
					pdfReportService.getDonutChart(siStatusMap, HighChartConstants.SERVICE_STATUS_WISE_INVENTORY));
		} 
		 context.setVariable("monthStr"," "+convertDateStrFormat("MM-yyyy", "MMM-yyyy",request.getMonth()).toUpperCase() );
		// Get Ticket Excel
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(baos);
		try {
			Map<String, byte[]> futureMap = new HashMap<String, byte[]>();
			SimpleDateFormat month_dateSDF = new SimpleDateFormat("MMM yyyy", Locale.ENGLISH);

			List<TicketExcelBean> beanList = performanceService.getTicketDetailsForExcel(request.getMonth());
			String msg = "No Tickets created on " + month_dateSDF.format(requestDate);
			byte[] excelByteArr = Utils.writeXLSXFile(beanList, "TicketDetails", "TicketDetails", msg);
			futureMap.put("TicketDetails.xlsx", excelByteArr);

			// uptime Report
			List<UptimeExcelBean> uptimeBeanList = performanceService.getUptimeForExcel(addQuote(request.getMonth()));
			String uptimeMsg = "No uptime data avaialble for the month :  " + month_dateSDF.format(requestDate);
			byte[] uptimeExcelByteArr = Utils.writeXLSXFile(uptimeBeanList, "UptimeReport", "UptimeReport", uptimeMsg);
			futureMap.put("UptimeReport.xlsx", uptimeExcelByteArr);
			
			// service Inventory excel sheet.	
			List<ServiceInventoryBean> siBeanList = performanceService.getserviceInventoryDetailsForExcel(addQuote(request.getMonth()));
			String siMsg = "No serviceInvetory data avaialble for the month :  " + month_dateSDF.format(requestDate);
			byte[] siExcelByteArr = Utils.writeXLSXFile(siBeanList, "CircuitInventory", "ServiceInventoryReports", siMsg);
			futureMap.put("ServiceInventoryReport.xlsx", siExcelByteArr);
			
			
			String html = templateEngine.process(HighChartConstants.HIGHCHARTS, context);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] pdfBytes = bos.toByteArray();
			futureMap.put("PerformanceReport.pdf", pdfBytes);
			// Iterate futureMap to get zip file
			futureMap.forEach((key, value) -> {
				ZipEntry entry = new ZipEntry(key);
				try {
					entry.setSize(value.length);
					zos.putNextEntry(entry);
					zos.write(value);
				} catch (Exception e) {
					LOGGER.error(ExceptionConstants.ERROR_IN_ZIP_GENERATION, ExceptionUtils.getStackTrace(e));
				}
			});
		} catch (Exception exc) {
			LOGGER.error(ExceptionConstants.ERROR_IN_ZIP_GENERATION, ExceptionUtils.getStackTrace(exc));
			throw new TclCommonException(ExceptionConstants.ERROR_IN_ZIP_GENERATION, exc,
					ResponseResource.R_CODE_ERROR);
		} finally {
			zos.closeEntry();
			zos.close();
		}
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType(HighChartConstants.APPLICATION_ZIP));
		headers.setContentDispositionFormData("fileName", HighChartConstants.ZIP_FILE_NAME);
		headers.setCacheControl(HighChartConstants.MUST_REVALIDATE_POST_CHECK_0_PRE_CHECK_0);
		ResponseEntity<byte[]> response = new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);
		return response;
	}

	/**
	 * Method to get last date of the month from given start date
	 * 
	 * @author KRUTSRIN
	 * @param monthRfo
	 * @return date string
	 */
	private String getLastDate(String monthRfo) {
		
		 SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
         Date convertedDate = null;
		try {
			convertedDate = dateFormat.parse(monthRfo);
		} catch (ParseException e) {
			LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
		}
         Calendar c = Calendar.getInstance();
         c.setTime(convertedDate);
         c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
	
      return dateFormat.format(c.getTime());
		
	}

	/**
	 * Method to get past months range
	 * 
	 * @param currentMonthYr
	 * @param range
	 * @return list of month range
	 */
	private List<String> getMonthRange(String currentMonthYr, int range,String format) {
		List<String> allDates = new ArrayList<>();
		String maxDate = currentMonthYr; // 2-19
		SimpleDateFormat monthDate = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(monthDate.parse(maxDate));
		} catch (ParseException e) {
			LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
		}

		IntStream.range(0, range).forEach(x -> {
			String month_name1 = monthDate.format(cal.getTime());
			allDates.add(month_name1);
			cal.add(Calendar.MONTH, -1);
		});

		return allDates;

	}

	/**
	 * Method to get difference between given dates in months
	 * 
	 * @param first
	 * @param last
	 * @return no of months
	 */
	public static int getDiffMonths(Date first, Date last) {
		Calendar a = getCalendar(first);
		Calendar b = getCalendar(last);
		int diff = b.get(Calendar.MONTH) - a.get(Calendar.MONTH);

		return diff;
	}

	/**
	 * Method to get cal instance
	 * 
	 * @param date
	 * @return {Calendar}
	 */
	public static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTime(date);
		return cal;
	}
	
	
	/**
	 * Converting a string date in one format to date string of another format.
	 * 
	 * @author KRUTSRIN
	 * @param sourceForm
	 * @param destinationForm
	 * @param strDate
	 * @return dateString
	 */
	public static String convertDateStrFormat(String sourceForm, String destinationForm, String strDate) {
		SimpleDateFormat sdfSource = new SimpleDateFormat(sourceForm);
		Date date = null;
		try {
			date = sdfSource.parse(strDate);
		} catch (ParseException e) {
			LOGGER.error(ExceptionConstants.ERROR_IN_PARSING_DATE, ExceptionUtils.getStackTrace(e));
		}
		SimpleDateFormat sdfDestination = new SimpleDateFormat(destinationForm);
		strDate = sdfDestination.format(date);
		return strDate;
	}
	
	/**
	 * surround single quote for given string.
	 * 
	 * @author KRUTSRIN
	 * @param str
	 * @return quoted string ex : '02-2019'
	 */
	public static String addQuote(String str) {
		String withQuote = "'" + str + "'";
		System.out.println(withQuote);
		return withQuote;
	}

}