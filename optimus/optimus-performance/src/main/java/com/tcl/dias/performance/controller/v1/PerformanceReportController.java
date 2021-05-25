package com.tcl.dias.performance.controller.v1;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.beans.ConferenceRoomUsageBean;
import com.tcl.dias.beans.ConferenceUsageReponse;
import com.tcl.dias.beans.TelchemyQOSSummaryBean;
import com.tcl.dias.beans.conferenceUsageReport.TelchemyQOSSummaryResponse;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.performance.request.ReportGeneratorRequest;
import com.tcl.dias.performance.service.v1.PerformanceReportService;
import com.tcl.dias.performance.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * This file contains the PerformanceReportController.java class. This class contains all
 * the API's related to Performance report
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/reports")
public class PerformanceReportController {

	@Autowired
	PerformanceReportService reportService;

	/**
	 * @author Biswajit 
	 * used to get performance report details
	 * @param ReportGeneratorRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.GET_PERFORMANCE_REPORT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/performance", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String,Object>>> getPerformanceReportDetails(@RequestBody ReportGeneratorRequest request)
			throws TclCommonException {
		List<Map<String, Object>> response = reportService.getPerformanceReportDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Biswajit 
	 * used to get usage report details
	 * @param ReportGeneratorRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.GET_USAGE_REPORT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/usage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String,Object>>> getUsageReportDetails(@RequestBody ReportGeneratorRequest request)
			throws TclCommonException {
		List<Map<String, Object>> response = reportService.getUsageReportDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Biswajit 
	 * used to get concurrent report details
	 * @param ReportGeneratorRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.GET_CONCURRENT_BANDWIDTH_REPORT_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/concurrent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<Map<String,Object>>> getConcurentAndBandwidthReportDetails(@RequestBody ReportGeneratorRequest request)
			throws TclCommonException {
		List<Map<String, Object>> response = reportService.getConcurentAndBandwidthReportDetails(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Biswajit 
	 * used to get country list
	 * @param ReportGeneratorRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.SOURCE_COUNTRY_LIST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sourceCountryList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getSourceCountryList(
			@RequestParam(required = true, value = "granularity") String granularity)
			throws TclCommonException {
		List<String> response = reportService.getSourceCountryList(granularity);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Biswajit 
	 * used to get origin location list
	 * @param ReportGeneratorRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.GET_ORIG_LOCATION_LIST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/sourceLocationList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getSourceLocationList(@RequestParam(required = true, value = "granularity") String granularity,
			@RequestParam(required = true, value = "countryName") String countryName)
			throws TclCommonException {
		List<String> response = reportService.getSourceLocationList(granularity,countryName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Biswajit 
	 * used to get destination location list
	 * @param ReportGeneratorRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.GET_DST_LOCATION_LIST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/dstLocationList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getDestinationLocationList(@RequestParam(required = true, value = "granularity") String granularity,
			@RequestParam(required = true, value = "countryName") String countryName)
			throws TclCommonException {
		List<String> response = reportService.getDestinationLocationList(granularity,countryName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Biswajit 
	 * used to get concurrent location details
	 * @param ReportGeneratorRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.GET_CONCURRENT_LOCATION_LIST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/concurrentlocationList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getLocationListForConcurrentReport(@RequestParam(required = true, value = "granularity") String granularity)
			throws TclCommonException {
		List<String> response = reportService.getLocationListForConcurrentReport(granularity);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * @author Biswajit 
	 * used to get country list
	 * @param ReportGeneratorRequest
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.DST_COUNTRY_LIST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/dstCountryList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<String>> getDestinationCountryList(
			@RequestParam(required = true, value = "granularity") String granularity)
			throws TclCommonException {
		List<String> response = reportService.getDestinationCountryList(granularity);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author kishore nagarajan 
	 * used to conference usage report
	 * @param month,year,customerName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.GET_CONFERENCE_USAGE_REPORT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ConferenceUsageReponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/conferenceUsageReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConferenceUsageReponse> getConferenceUsageReport(
			@RequestParam(required = true, value = "month") String month,
			@RequestParam(required = true, value = "year") String year,
			@RequestParam(required = true, value = "customerName") String customerName) throws TclCommonException {
		ConferenceUsageReponse response = reportService.toGetConferenceUsageReport(month, year, customerName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author kishore nagarajan 
	 * used to conference room usage report
	 * @param month,year,customerName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.GET_CONFERENCE_USAGE_REPORT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ConferenceUsageReponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/conferenceRoomUsageReport", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<ConferenceRoomUsageBean>> getConferenceRoomUsageReport(
			@RequestParam(required = true, value = "month") String month,
			@RequestParam(required = true, value = "year") String year,
			@RequestParam(required = true, value = "customerName") String customerName) throws TclCommonException {
		List<ConferenceRoomUsageBean> response = reportService.toGetConferenceRoomUsageReport(month, year, customerName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	/**
	 * API for Getting TelchemyQosStatsSummary
	 * @param month
	 * @param year
	 * @param cuid
	 * @return
	 * @throws TclCommonException
	 */
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Report.GET_TELCHEMY_QOS_STATS_SUMMARY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TelchemyQOSSummaryResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/telchemyQosStatsSummary", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TelchemyQOSSummaryResponse> getTelchemyQosStatsSummaryReport(
			@RequestParam(required = true, value = "month") String month,
			@RequestParam(required = true, value = "year") String year,
			@RequestParam(required = true, value = "cuid") String cuid) throws TclCommonException {
		TelchemyQOSSummaryResponse response = reportService.getTelchemyQosStatusSummary(month, year, cuid);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	
}
