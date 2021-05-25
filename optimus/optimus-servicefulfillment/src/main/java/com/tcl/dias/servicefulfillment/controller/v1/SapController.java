package com.tcl.dias.servicefulfillment.controller.v1;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillmentutils.beans.sap.MinResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.MinResponseBean;
import com.tcl.dias.servicefulfillmentutils.sap.response.MrnCreationReponse;
import com.tcl.dias.servicefulfillmentutils.sap.response.SapQuantityAvailableRequest;
import com.tcl.dias.servicefulfillmentutils.service.v1.ProjectTimelineStatusTrackService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This file contains the ServiceActivationController.java class.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/sap")
public class SapController {

	@Autowired
	SapService sapService;

	@Autowired
	ProjectTimelineStatusTrackService projectTimelineStatusTrackService;

	@RequestMapping(value = "/material/details/outbound/{serviceCode}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> checkMaterialQuantity(@PathVariable("serviceCode") String serviceCode,
			@RequestParam(value = "serviceId", required = false) Integer serviceId, @RequestParam("type") String type,
			@RequestParam(value = "purchaseGroup", required = false) String purchaseGroup,@RequestParam(value = "invtype", required = false) String invtype,@RequestParam(value = "typeOfExpenses", required = false) String typeOfExpenses)
			throws TclCommonException, ParseException {
		boolean response = sapService.checMaterialQuantityAndPlaceMrn(serviceCode,serviceId, type, invtype, typeOfExpenses,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Boolean.valueOf(response), Status.SUCCESS);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MinResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/minstatus")
	public ResponseResource getMinResponse(@RequestBody MinResponseBean minCreation) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				sapService.getMinStatus(minCreation),
				Status.SUCCESS);
	}

	@PostMapping(value = "/wbs/transfer/{serviceCode}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> placeWbsTransfer(@RequestBody SapQuantityAvailableRequest sapQuantityAvailableRequest,
														   @PathVariable("serviceCode") String serviceCode,
														   @RequestParam(value = "invtype", required = false) String invtype,
														   @RequestParam(value = "typeOfExpenses", required = false) String typeOfExpenses) throws TclCommonException {
		boolean response = sapService.placeWbsTransfer(sapQuantityAvailableRequest, serviceCode, invtype, typeOfExpenses,null,null,null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Boolean.valueOf(response), Status.SUCCESS);
	}

	@PostMapping(value = "/mrn/placeorder/{serviceCode}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> placeMrnOrder(@RequestBody SapQuantityAvailableRequest sapQuantityAvailableRequest,
													  @PathVariable("serviceCode") String serviceCode,
				                                      @RequestParam(value = "type") String type,
													  @RequestParam(value = "invtype", required = false) String invtype,
													  @RequestParam(value = "typeOfExpenses", required = false) String typeOfExpenses) throws TclCommonException {
		boolean response = sapService.placeMrnOrder( serviceCode, type, invtype, typeOfExpenses,null,null,null,"A");
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Boolean.valueOf(response), Status.SUCCESS);
	}


}
