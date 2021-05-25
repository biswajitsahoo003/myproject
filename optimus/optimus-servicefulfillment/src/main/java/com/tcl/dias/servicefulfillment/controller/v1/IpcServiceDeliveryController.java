package com.tcl.dias.servicefulfillment.controller.v1;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.ProcurementBean;
import com.tcl.dias.servicefulfillmentutils.beans.ProcurementDetails;
import com.tcl.dias.servicefulfillmentutils.beans.ProcurementResponse;
import com.tcl.dias.servicefulfillmentutils.beans.ProcurementSolutionDetailBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.IpcServiceDeliveryService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Savanya
 *
 */
@RestController
@RequestMapping("/v1/ipc/service-delivery")
public class IpcServiceDeliveryController {

	@Autowired
	IpcServiceDeliveryService ipcServiceDeliveryService;

	/**
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PROCUREMENT.CREATE_PROCUREMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProcurementResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/procurement_details", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public ResponseResource<ProcurementResponse> createProcurement(@RequestBody ProcurementBean request)
			throws TclCommonException {
		ProcurementResponse response = ipcServiceDeliveryService.createProcurementDetail(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @param scOrderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PROCUREMENT.FETCH_PROCUREMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProcurementDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/procurement_details/scOrder/{scOrderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public ResponseResource<ProcurementDetails> fetchProcurementByScOrder(@PathVariable("scOrderId") Integer scOrderId)
			throws TclCommonException {
		ProcurementDetails response = ipcServiceDeliveryService.fetchProcurementByScOrder(scOrderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @param procurementId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PROCUREMENT.GET_PROCUREMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProcurementBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/procurement_details/{procurementId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProcurementBean> getProcurement(@PathVariable("procurementId") Integer procurementId)
			throws TclCommonException {
		ProcurementBean response = ipcServiceDeliveryService.getProcurementDetail(procurementId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @param procurementId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PROCUREMENT.UPDATE_PROCUREMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProcurementResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/procurement_details/{procurementId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public ResponseResource<ProcurementResponse> updateProcurement(@PathVariable("procurementId") Integer procurementId, @RequestBody ProcurementBean updatedRequest)
			throws TclCommonException {
		ProcurementResponse response = ipcServiceDeliveryService.updateProcurementDetail(procurementId, updatedRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @param procurementId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PROCUREMENT.DELETE_PROCUREMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProcurementResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/procurement_details/{procurementId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public ResponseResource<ProcurementResponse> removeProcurement(@PathVariable("procurementId") Integer procurementId)
			throws TclCommonException {
		ProcurementResponse response = ipcServiceDeliveryService.deleteProcurementDetail(procurementId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * @param scServiceId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.PROCUREMENT.FETCH_SOLUTION_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ProcurementSolutionDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/procurement_solution_details/scServiceDetail/{scServiceDetailId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<ProcurementSolutionDetailBean>> fetchSolutionDetailForServiceId(@PathVariable("scServiceDetailId") Integer scServiceDetailId)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, ipcServiceDeliveryService.fetchSolutionDetailForServiceId(scServiceDetailId),
				Status.SUCCESS);
	}
	
	@PostMapping(value = "/project_initiation/customer_email")
	public ResponseResource<String> sendEmailContent(@RequestBody Map<String, String> emailAttributes) throws TclCommonException, IllegalArgumentException{
		String htmlContent = ipcServiceDeliveryService.getCustomerEmailContent(emailAttributes);
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, htmlContent, Status.SUCCESS);
	}
}
