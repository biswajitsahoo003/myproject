package com.tcl.dias.ticketing.controller.v1;

import java.util.Arrays;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.filters.service.v1.FilterService;
import com.tcl.dias.response.beans.ServiceResponseBean;
import com.tcl.dias.ticketing.request.PlannedEventsRequest;
import com.tcl.dias.ticketing.response.CreateTicketResponse;
import com.tcl.dias.ticketing.response.PlannedEventsResponse;
import com.tcl.dias.ticketing.service.v1.PlannedEventsService;
import com.tcl.dias.ticketing.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/plannedevents")
public class PlannedEventsController {

	@Autowired
	PlannedEventsService plannedEventsService;
	
	@Autowired
	FilterService filterService;
	/**
	 * @author ANNE NISHA getTicketDetails used to get all the ticketing details
	 * 
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PlannedOutage.GET_PLANNED_OUTAGE_TICKETS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateTicketResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/tickets", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PlannedEventsResponse> getTicketDetails(@RequestBody PlannedEventsRequest plannedEventsRequest)
			throws TclCommonException {
		PlannedEventsResponse response = plannedEventsService.getTicketDetails(plannedEventsRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @author chetan chaudhary
	 * @param searchBy
	 * @param valueFor
	 * @param serviceType	
	 * @return
	 * @throws TclCommonException
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceRequestManagement.FILTERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/filters", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseResource<RestResponse> filters(
			@RequestParam(required = false, name = "searchBy") String searchBy,
			@RequestParam(required = true, name = "valueFor") String valueFor,
			@RequestParam(required = false, name ="serviceType") String serviceType) throws TclCommonException{
		HashMap<String, String> mapping = new HashMap<>(); 
		String[] vArray = {"serviceType"};
		mapping.put("serviceType", "u_product_name");
		if(Arrays.stream(vArray).noneMatch(valueFor::equals)) {
			return new ResponseResource<>(ResponseResource.R_CODE_BAD_REQUEST, ResponseResource.RES_FAILURE, null,
					Status.ERROR);
		}
		RestResponse response = filterService.filterValuesForPlannedEvents(searchBy, serviceType, mapping.get(valueFor));
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS); 
	}
}



