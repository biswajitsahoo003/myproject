package com.tcl.dias.customer.controller.v1;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.tcl.dias.customer.constants.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.customer.dto.CustomerDto;
import com.tcl.dias.customer.service.v1.CustomerService;
import com.tcl.dias.customer.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class contains APis for customer which are accessed in user management
 * @author archchan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v2/customers")

public class CustomerControllerV2 {

	@Autowired
	CustomerService customerService;
	
	/**
	 * getCustomersList API to List<CustomerDto> for given customerIds
	 * @param customerIds
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.Customer.GET_CUSTOMER_LIST)
	@RequestMapping(value = "/list/customers", method = RequestMethod.GET)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	public ResponseResource<List<CustomerDto>> getCustomersList(@RequestParam("customerIds") List<Integer> customerIds)
			throws TclCommonException {
		List<CustomerDto> customerList = customerService.getCustomerList(customerIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,customerList,
				Status.SUCCESS);
	}

	/**
	 *
	 * This method uploads the xlsx file with the list of dop.
	 * @param file
	 * @return LocationItContact
	 * @throws TclCommonException
	 */
	@ApiOperation(value = "Upload Dop Matrix")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/dop/upload", method = RequestMethod.POST)
	@Transactional
	public ResponseResource<Object> locationsExcelUploadIas(@RequestParam("file") MultipartFile file)
			throws TclCommonException {
		Object response = null;
		try {
			response = customerService.dopExcelMapping(file);
		} catch (IOException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		Status status = null;
		String responseStatus = "";
		if (response instanceof List<?> || response instanceof String) {
			responseStatus = ResponseResource.RES_SUCCESS;
			status = Status.SUCCESS;
		} else if (response instanceof Set<?>) {
			responseStatus = ResponseResource.RES_FAILURE;
			status = Status.FAILURE;
		}

		return new ResponseResource<>(ResponseResource.R_CODE_OK, responseStatus, response, status);

	}
}
