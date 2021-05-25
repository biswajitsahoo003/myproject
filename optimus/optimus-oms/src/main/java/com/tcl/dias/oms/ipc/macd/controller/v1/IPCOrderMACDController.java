package com.tcl.dias.oms.ipc.macd.controller.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.ipc.beans.AttributeUpdateRequest;
import com.tcl.dias.oms.ipc.beans.CloudComponentUpdateRequest;
import com.tcl.dias.oms.ipc.beans.IPCOrdersBean;
import com.tcl.dias.oms.ipc.macd.service.v1.IPCOrderMACDService;
import com.tcl.dias.oms.ipc.service.v1.IPCOrderService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * This file contains the IPCOrderMACDController.java class. This class contains all
 * the API's related to MACD Orders for IPC product
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/ipc/order/macd")
public class IPCOrderMACDController {
	
	@Autowired
	IPCOrderMACDService ipcOrderMACDService;
	
	@Autowired
	IPCOrderService ipcOrderService;
	
	 /**
   	 * getMACDOrderDetails - This method is used to get macd order details 
   	 *
   	 * @param orderId
   	 * @return ResponseResource<IPCOrdersBean>
   	 * @throws TclCommonException
   	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = IPCOrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<IPCOrdersBean> getMACDOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		IPCOrdersBean response = ipcOrderMACDService.getMACDOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
   	 * launchCloud - This method is used to launch cloud 
   	 *
   	 * @param orderId,orderToLeId
   	 * @return ResponseResource<String>
   	 * @throws TclCommonException
   	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.LAUNCH_VM)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/{orderId}/le/{orderToLeId}/launch")
    public ResponseResource<String> launchCloud(@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId) throws TclCommonException {
		ipcOrderMACDService.launchCloud(orderId, orderToLeId, false);
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
    }

	 /**
   	 * updateLeaglEntityAttribute - This method is used to update legal entity attributes 
   	 *
   	 * @param orderId,orderToLeId,request
   	 * @return ResponseResource<QuoteDetail>
   	 * @throws TclCommonException
   	 */
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
	
	 /**
   	 * updateOrderCloudAttribute - This method is used to update order cloud attributes 
   	 *
   	 * @param orderId,orderToLeId,request
   	 * @return ResponseResource<QuoteDetail>
   	 * @throws TclCommonException
   	 */
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
}
