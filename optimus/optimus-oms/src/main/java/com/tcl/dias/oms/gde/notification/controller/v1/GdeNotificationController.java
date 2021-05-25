package com.tcl.dias.oms.gde.notification.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.gde.beans.FeasibilityCheckResponse;
import com.tcl.dias.oms.gde.beans.FeasibilityRequestBean;
import com.tcl.dias.oms.gde.beans.GdeOdrScheduleAuditBean;
import com.tcl.dias.oms.gde.service.v1.GdePricingFeasibilityService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * Controller class contains API to notify GDE feasibility status
 * @author archchan
 *
 */
@RestController
@RequestMapping(value = "/v1/gde", produces = MediaType.APPLICATION_JSON_VALUE)
public class GdeNotificationController {
	
	@Autowired
	GdePricingFeasibilityService gdePricingFeasibilityService;

	@ApiOperation(value = SwaggerConstants.ApiOperations.GDE.GET_FEASIBILITY_STATUS)
	@RequestMapping(value = "/schedule/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> notifyFeasibilityCheck(@RequestBody FeasibilityCheckResponse feasibilityResponse)
			throws TclCommonException {
		String response = gdePricingFeasibilityService.notifyFeasibilityCheck(feasibilityResponse);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				response, Status.SUCCESS);

	}
}
