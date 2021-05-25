package com.tcl.dias.oms.ill.controller.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.SlaUpdateRequest;
import com.tcl.dias.oms.beans.SlabBean;
import com.tcl.dias.oms.ill.service.v1.IllSlaService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the IllSlaController.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/sla")
public class IllSlaController {

	@Autowired
	IllSlaService illSlaService;

	/**
	 * @author VIVEK KUMAR K
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited saveSla
	 * @param request
	 * @return bean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Sla.SAVE_SLA)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SlabBean> saveSla(@RequestBody SlaUpdateRequest request) throws TclCommonException {

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, null, Status.SUCCESS);
	}

}
