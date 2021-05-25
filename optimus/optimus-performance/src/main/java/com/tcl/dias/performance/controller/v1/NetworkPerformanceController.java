package com.tcl.dias.performance.controller.v1;

import java.text.ParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.performance.service.v1.NetworkPerformanceService;
import com.tcl.dias.performance.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 
 * PerformanceController.java
 * 		Controller class for network performance for ILL, GVPN
 * 		interface parameters
 * 		IP SLA parameters
 *
 * @author chetan chaudhary
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/performance/network")
public class NetworkPerformanceController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkPerformanceController.class);
	
	@Autowired
	NetworkPerformanceService networkPerformanceService;	

	/**
	 * @author chetan chaudhary
	 * @param startTime
	 * @param endTime
	 * @param netOperation
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.NetworkPerformance.NETWORK_PERFORMANCE)
	@RequestMapping(value = "/interfaces/{interfaceId}/performance/{paramGroup}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<RestResponse> getInterfacePerformance(
			@PathVariable("interfaceId") String interfaceId,
			@PathVariable("paramGroup") String paramGroup,
			@RequestParam(required = true, value = "startDate") String startTime, 
			@RequestParam(required = true, value = "endDate") String endTime,
			@RequestParam(required = true, value = "top") String top,
			@RequestParam(required = true, value = "skip") String skip,
			@RequestParam(required = true, value = "top1") String top1,
			@RequestParam(required = true, value = "timezone") String timezone,
			@RequestParam(required = true, value = "interfaceParam") List<String> parameter) throws TclCommonException{
	
		RestResponse response = networkPerformanceService.getInterfacePerformance(interfaceId, startTime, endTime, paramGroup, parameter, top, skip, top1, timezone);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);  
	}

	/**
	 * @author chetan chaudhary
	 * @param startTime
	 * @param endTime
	 * @param netOperation
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.NetworkPerformance.NETWORK_PERFORMANCE)
	@RequestMapping(value = "/services/{serviceId}/performance/{paramGroup}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<RestResponse> getServicePerformance(
			@PathVariable("serviceId") String serviceId,
			@PathVariable("paramGroup") String paramGroup,
			@RequestParam(required = true, value = "expand") String component,
			@RequestParam(required = true, value = "startDate") String startTime, 
			@RequestParam(required = true, value = "endDate") String endTime,
			@RequestParam(required = true, value = "top") String top,
			@RequestParam(required = true, value = "skip") String skip,
			@RequestParam(required = true, value = "top1") String top1,
			@RequestParam(required = true, value = "timezone") String timezone,
			@RequestParam(required = true, value = "interfaceParam") List<String> parameter) throws TclCommonException{
		
	
			RestResponse response = networkPerformanceService.getServicePerformance(serviceId, startTime, endTime, paramGroup, parameter, component, top, skip, top1, timezone);
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		
	}
	
	/**
	 * @author chetan chaudhary
	 * @param startTime
	 * @param endTime
	 * @param netOperation
	 * @return
	 * @throws TclCommonException
	 * @throws ParseException 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.NetworkPerformance.NETWORK_PERFORMANCE)
	@RequestMapping(value = "/devices/{deviceId}/performance/{paramGroup}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<RestResponse> getDevicePerformance(
			@PathVariable("deviceId") String deviceId,
			@PathVariable("paramGroup") String paramGroup,
			@RequestParam(required = true, value = "startDate") String startTime, 
			@RequestParam(required = true, value = "endDate") String endTime,
			@RequestParam(required = true, value = "top") String top,
			@RequestParam(required = true, value = "skip") String skip,
			@RequestParam(required = true, value = "top1") String top1,
			@RequestParam(required = true, value = "timezone") String timezone,@RequestParam(required = true, value = "resolution") String resolution,
			@RequestParam(required = true, value = "deviceParam") List<String> parameter) throws TclCommonException{
	
		RestResponse response = networkPerformanceService.getDevicePerformance(deviceId, startTime, endTime, paramGroup, parameter, top, skip, top1, timezone,resolution);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);  
	}
}
	
