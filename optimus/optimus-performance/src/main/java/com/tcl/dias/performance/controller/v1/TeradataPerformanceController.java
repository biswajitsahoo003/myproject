package com.tcl.dias.performance.controller.v1;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.beans.CircuitUptimeResponseBean;
import com.tcl.dias.beans.MTTRSeverityResponseBean;
import com.tcl.dias.beans.MTTRTrendResponseBean;
import com.tcl.dias.beans.RFOServiceDetailsResponseBean;
import com.tcl.dias.beans.ServerityTicketResponse;
import com.tcl.dias.beans.SeverityTicketDetailsResponseBean;
import com.tcl.dias.beans.UptimeCircuitDetailsResponseBean;
import com.tcl.dias.beans.TrendResponseBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.performance.service.v1.TeradataPerformanceService;
import com.tcl.dias.performance.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the PerformanceController class for teradata API's.
 * 
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/performance/teradata")
public class TeradataPerformanceController {
	
	@Autowired
	TeradataPerformanceService performanceService;

	/**
	 * API to get the top RFO reasons with the ticket count in percentage for a given month.
	 * 
	 * @param month, month with year in the specified format 12-2018.
	 * @return {@ResponseResource}, the response.
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.GET_RFO_OUTAGES_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/rfoReport/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<Map<String, Object>> getRFOReport(@PathVariable("month") String month) throws TclCommonException {
		Map<String, Object> data = performanceService.getMonthlyOutageReport(month);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	/**
	 * API to get the circuit details for the given outage reason, for a given month.
	 * 
	 * @param month, month with year in the specified format 12-2018.
	 * @param outageSpecification, the cause of outage.
	 * @return {@ResponseResource}, the response contains a list of {@RFOServiceDetailsResponseBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.GET_RFO_OUTAGES_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/rfoReport/details/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<RFOServiceDetailsResponseBean>> getOutageCircuitDetails(
			@PathVariable("month") String month,
			@RequestParam(value="outageSpecification", required=true) String outageSpecification) throws TclCommonException {
		List<RFOServiceDetailsResponseBean> data = performanceService.getOutageCircuitDetails(month, outageSpecification);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	/**
	 * API to get the ticket trend - severity for the given month.
	 * 
	 * @param month, month with year in the specified format 12-2018. 
	 * @return {@ResponseResource}, the response contains a list of {ServerityTicketResponse}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.SEVERITY_TICKETS__MONTHLY_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/severityTickets/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServerityTicketResponse> getMontlySeverityTickets(
			@PathVariable("month") String month) 
			throws TclCommonException {
		ServerityTicketResponse data = performanceService.getMonthlySeverityTickets(month,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	/**
	 * API to get the ticket details for a product for an impact type, for a give month.
	 * 
	 * @param month, month with year in the specified format 12-2018.
	 * @param product, the product flavor.
	 * @param impact, the ticket impact.
	 * @return {@ResponseResource}, the response contains a list of {SeverityTicketDetailsResponseBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.SEVERITY_TICKETS__MONTHLY_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/severityTickets/details/{month}/{product}/{impact}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<SeverityTicketDetailsResponseBean>> getMontlySeverityTickets(
			@PathVariable("month") String month,
			@PathVariable("product") String product,
			@PathVariable("impact") String impact) 
			throws TclCommonException {
		List<SeverityTicketDetailsResponseBean> data = performanceService.getTicketDetailsBySeverity(month, product, impact);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	
	/**
	 * API to get the ticket trend - severity for the required time frame.
	 * 
	 * @return {@ResponseResource{ServerityTicketResponse}}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.SEVERITY_TICKETS__MONTHLY_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/severityTickets/trends", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServerityTicketResponse> getBulkSeverityTickets() 
			throws TclCommonException {
		ServerityTicketResponse data = performanceService.getTicketTrendSeverityWise();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	/**
	 * API to get the ticket details for a product for an impact type, for a give month.
	 * 
	 * @param month, month with year in the specified format 12-2018.
	 * @param product, the product flavor.
	 * @param impact, the ticket impact.
	 * @return {@ResponseResource}, the response contains a list of {SeverityTicketDetailsResponseBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.SEVERITY_TICKETS__MONTHLY_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/severityTickets/details/{month}/{impact}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<SeverityTicketDetailsResponseBean>> getMontlySeverityTickets(
			@PathVariable("month") String month,
			@PathVariable("impact") String impact) 
			throws TclCommonException {
		List<SeverityTicketDetailsResponseBean> data = performanceService.getTicketDetailsBySeverity(month, impact);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	/**
	 * API to get the Uptime percentage for circuits for the given month.
	 * 
	 * @param month, the month in mm-yy format.
	 * @return {@ResponseResource}, contains the response bean {@CircuitUptimeResponseBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.UPTIME_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/uptimeReports/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CircuitUptimeResponseBean> getUptimeReportMontly(
			@PathVariable("month") String month) 
			throws TclCommonException {
		CircuitUptimeResponseBean data = performanceService.getUptimeReportPerMonth(month);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	/**
	 * API to get the Uptime percentage for circuits for a product, for a given month.
	 * 
	 * @param month, the month in mm-yy format.
	 * @param product, the product flavor.
	 * @return {@ResponseResource} contains list of  circuit Id with uptime percentage value.
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.UPTIME_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/uptimeReports/{product}/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String, Object>>> getUptimeReportForCiruitsWithMostDowntime(
			@PathVariable("month") String month,
			@PathVariable("product") String product)
			throws TclCommonException {
		List<Map<String, Object>> data = performanceService.getCiruitsWithLeastUptime(month, product);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	/**
	 * API to get the ticket details for a product for an impact type, for a give month
	 * 
	 * @param month, month with year in the specified format mm-yy.
	 * @param product, the product flavor. 
	 * @return{@ResponseResource} contains a list of {@UptimeCircuitDetailsResponseBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.SEVERITY_TICKETS__MONTHLY_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/uptimeReports/details/{product}/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<UptimeCircuitDetailsResponseBean>> getCircuitUptimeDetails(
			@PathVariable("month") String month,
			@PathVariable("product") String product) 
			throws TclCommonException {
		List<UptimeCircuitDetailsResponseBean> data = performanceService.getCiruitsUptimeDetailedInfo(month, product);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}

	/**
	 * API to get the Uptime percentage for circuits for a duration of 6 months.
	 * 
	 * @param month, month with year in the specified format mm-yy.
	 * @return {@ResponseResource}, contains a list of {@UptimeTrendResponseBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.UPTIME_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/uptimeReports/trend", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<TrendResponseBean>> getUptimeTrendForCircuits(
			@RequestBody List<String> duration)
			throws TclCommonException {
		List<TrendResponseBean> data = performanceService.getUptimeTrendInfo(duration);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	/**
	 * API to get the SLA breach count for circuits for a duration of 6 months.
	 * 
	 * @param month, month with year in the specified format mm-yy.
	 * @return {@ResponseResource}, contains a list of {@UptimeTrendResponseBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.UPTIME_REPORT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/slaBreach/trend", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<TrendResponseBean>> getSLATrendForCircuits(
			@RequestBody List<String> duration)
			throws TclCommonException {
		List<TrendResponseBean> data = performanceService.geSLATrendInfo(duration);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
	/**
	 * API to get the MTTR report data - month wise, for Customer & TATA end.
	 * 
	 * @return {@ResponseResource}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.MTTR_REPORTS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/MTTR/trends", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<MTTRTrendResponseBean> getMTTRTrendsReport() 
			throws TclCommonException {
		MTTRTrendResponseBean data = performanceService.getMttrTrendReport();
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}

	/**
	 * API to get the MTTR severity wise ticket count, for a given Impact type.
	 * 
	 * @param impact, the ticket impact.
	 * @return {@ResponseResource} contains a list of {@MTTRSeverityResponseBean}
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Performance.MTTR_REPORTS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/MTTR/severity/{impact}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<MTTRSeverityResponseBean>> getMTTRSeveritywiseReport(
			@PathVariable("impact") String impact) 
			throws TclCommonException {
		List<MTTRSeverityResponseBean> data = performanceService.getMttrSeverityReport(impact);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, data,
				Status.SUCCESS);
	}
	
}