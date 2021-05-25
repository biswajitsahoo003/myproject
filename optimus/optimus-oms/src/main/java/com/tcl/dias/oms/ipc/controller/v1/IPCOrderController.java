package com.tcl.dias.oms.ipc.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.OrdersBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.ipc.beans.AttributeUpdateRequest;
import com.tcl.dias.oms.ipc.beans.CloudComponentUpdateRequest;
import com.tcl.dias.oms.ipc.beans.IPCOrdersBean;
import com.tcl.dias.oms.ipc.beans.OrderIPCCloudBean;
import com.tcl.dias.oms.ipc.beans.SecurityGroupCatalystBean;
import com.tcl.dias.oms.ipc.beans.SecurityGroupResponse;
import com.tcl.dias.oms.ipc.beans.CatalystVdomWrapperAPIResponse;
import com.tcl.dias.oms.ipc.service.v1.IPCOrderService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This file contains the IPCOrderController.java class. This class contains all
 * the API's related to Quotes for IPC product
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/ipc/orders")
public class IPCOrderController {

    @Autowired
    IPCOrderService ipcOrderService;

    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/version", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<String> getVersion() {
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "1.0",
                Status.SUCCESS);
    }

    /**
     * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited getOrderDetails used to fetch all
     *            the order details
     * @param customerId
     * @param quoteId
     * @return QuoteDetail
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDERS)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<IPCOrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
            throws TclCommonException {
        IPCOrdersBean response = ipcOrderService.getOrderDetails(orderId, null);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_SECURITY_GRP)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/securityGroup")
	public ResponseResource<SecurityGroupResponse> getSecurityGroupDetails(
			@RequestBody SecurityGroupCatalystBean request,
			@RequestParam(value = "vDomName", required = false) String vDomName) throws TclCommonException {
		SecurityGroupResponse response = ipcOrderService.fetchSecurityGroupCatalystDetails(request, vDomName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
    
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_SECURITY_GRP)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @GetMapping(value = "/{customer}/{location}/vdom")
    public ResponseResource<CatalystVdomWrapperAPIResponse> getVdomDetails(@PathVariable("customer") String customer, @PathVariable("location") String location)
            throws TclCommonException {
    	CatalystVdomWrapperAPIResponse response = ipcOrderService.fetchCatalystVdomDetails(customer,location);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }
    
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_LEGALENTITY_ATTRIBUTE)
    @RequestMapping(value = "/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<QuoteDetail> updateLeaglEntityAttribute(@PathVariable("orderId") Integer orderId,
                                                                    @PathVariable("orderToLeId") Integer orderToLeId, @RequestBody List<AttributeUpdateRequest> request) throws TclCommonException {
        QuoteDetail response = ipcOrderService.updateLegalEntityProperties(orderToLeId, request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.ADD_ORDER_COMPONENT_ATTR)
    @RequestMapping(value = "/{orderId}/le/{orderToLeId}/cloud/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<QuoteDetail> updateOrderCloudAttribute(@PathVariable("orderId") Integer orderId,
                                        @PathVariable("orderToLeId") Integer orderToLeId, @RequestBody List<CloudComponentUpdateRequest> request) throws TclCommonException {
        QuoteDetail response = ipcOrderService.updateOrderCloudAttributes(orderToLeId, request);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                Status.SUCCESS);
    }

    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.LAUNCH_VM)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/{orderId}/le/{orderToLeId}/launch")
    public ResponseResource<String> launchCloud(@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId) throws TclCommonException {
        ipcOrderService.launchCloud(orderId, orderToLeId, false);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
    }
    
    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.LAUNCH_VM)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/{orderId}/le/{orderToLeId}/servicedelivery")
    public ResponseResource<String> proceedToServiceDelivery(@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId) throws TclCommonException {
        ipcOrderService.launchCloud(orderId, orderToLeId, true);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
    }
    
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.LAUNCH_VM)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{orderCode}/provision_catalyst/{provisionType}")
	public ResponseResource<String> provisionInCatalyst(@PathVariable("orderCode") String orderCode,
			@PathVariable("provisionType") String provisionType) throws TclCommonException {
		ipcOrderService.autoProvisionInCatalyst(orderCode, provisionType, false);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

    @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.LAUNCH_VM)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/{orderId}/le/{orderToLeId}/cloud/status/update")
    public ResponseResource<String> updateClouldProvisioningStatus(@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
    		@RequestBody List<OrderIPCCloudBean> clouds) throws TclCommonException {
    	ipcOrderService.updateClouldProvisioningStatus(orderToLeId, clouds);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(), Status.SUCCESS);
    }

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ORDER_LE_STATUS)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateOrderToLeStatus(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestParam("status") String status)
			throws TclCommonException {
		ipcOrderService.updateOrderToLeStatus(orderToLeId, status,"","");
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, Status.SUCCESS.toString(),
				Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.GstAddress.GET_GST_ADDRESS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GstAddressBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/gstin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GstAddressBean> getGstData(@RequestParam("gstin") String gstin,@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId) throws TclCommonException {
		GstAddressBean gstAddressBean = ipcOrderService.getGstAddressIPC(gstin,orderId, orderToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gstAddressBean,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceResponse> upLoadFile(@RequestParam(name="file") MultipartFile file,
			@RequestParam(value = "orderToLeId", required = false) Integer orderToLeId,
			@RequestParam(value = "quoteToLeId", required = false) Integer quoteToLeId,
			@RequestParam("referenceId") List<Integer> referenceId, @RequestParam("referenceName") String referenceName,
			@RequestParam("attachmentType") String attachmentType) throws TclCommonException {
		ServiceResponse response = ipcOrderService.processUploadFiles(file, orderToLeId, quoteToLeId, attachmentType,
				referenceId, referenceName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/documentupload", method = RequestMethod.POST)
	public ResponseResource<ServiceResponse> updateDocumentDetail(@RequestParam("referenceId") List<Integer> referenceId,
			@RequestParam("referenceName") String referenceName, @RequestParam("attachmentType") String attachmentType,
			@RequestParam("requestId") String requestId,
			@RequestParam(value = "orderToLeId", required = false) Integer orderToLeId,
			@RequestParam(value = "quoteToLeId", required = false) Integer quoteToLeId,
			@RequestParam(value = "url") String url) throws TclCommonException {
		ServiceResponse response = ipcOrderService.updateDocumentUploadedDetails(orderToLeId, quoteToLeId, referenceId,
				referenceName, requestId, attachmentType, url);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @RequestMapping(value = "/customers/{erfCusCustomerId}/orders/{dcOrderCode}/isdcorderpresent", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<Boolean> isDCOrderExists(@PathVariable("dcOrderCode") String dcOrderCode, @PathVariable("erfCusCustomerId") Integer erfCusCustomerId) {
		Boolean response = ipcOrderService.isDCOrderExists(dcOrderCode, erfCusCustomerId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
    }
}
