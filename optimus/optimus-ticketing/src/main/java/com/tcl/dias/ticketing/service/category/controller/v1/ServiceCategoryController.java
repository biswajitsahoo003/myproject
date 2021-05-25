package com.tcl.dias.ticketing.service.category.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.response.beans.ServiceCategoryResponse;
import com.tcl.dias.response.beans.ServiceResponseBean;
import com.tcl.dias.ticketing.service.category.service.v1.ServiceCategoryService;
import com.tcl.dias.ticketing.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the ServiceCategoryController.java class.
 * used for the service and category related end point
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/servicecategory")
public class ServiceCategoryController {

	@Autowired
	ServiceCategoryService serviceCategoryService;

	/**
	 * @author vivek getServiceDetails
	 * used to get the service details
	 * @param serviceId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Service.GET_SERVICE_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/details/{serviceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceResponseBean> getServiceDetails(@PathVariable("serviceId") String serviceId)
			throws TclCommonException {
		ServiceResponseBean response = serviceCategoryService.getServiceDetails(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * getCategories
	 * used to get the categoried details
	 * @param impact
	 * @param serviceId
	 * @param productType
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Ticketing.GET_TICKET_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceCategoryResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceCategoryResponse> getCategories(
			@RequestParam(value = "serviceId", required = true) List<String> serviceId
			) throws TclCommonException {
		ServiceCategoryResponse response = serviceCategoryService.getCategories( serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

}
