package com.tcl.dias.oms.cancellation.controller.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.CancellationBean;
import com.tcl.dias.oms.beans.CancellationRequest;
import com.tcl.dias.oms.beans.CancelledServicesBean;
import com.tcl.dias.oms.beans.MDMServiceInventoryBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.QuoteOrderAmendmentBean;
import com.tcl.dias.oms.beans.ServiceDetailBeanForAmendment;
import com.tcl.dias.oms.cancellation.service.v1.CancellationService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
* This file contains the GvpnCancellationController.java class. This class contains all
* the API's related to cancellation Quotes for GVPN product
*
* @author ANNE NISHA
* @link http://www.tatacommunications.com/
* @copyright 2018 Tata Communications Limited
*/
@RestController
@RequestMapping(value = "/v1/cancellation", produces = MediaType.APPLICATION_JSON_VALUE)
public class CancellationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CancellationController.class);
	
	@Autowired
	CancellationService cancellationService;
	


	    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderCancellation.CREATE_QUOTE)
	    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
	            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	    @RequestMapping(value="/createQuote",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseResource<CancellationBean> quoteCreate(@RequestBody CancellationBean cancellationBean) throws Exception {
	    	CancellationBean response = cancellationService.createQuoteForOrderCancellation(cancellationBean);
	        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	                Status.SUCCESS);
	    }
	    
	    
	    @ApiOperation(value = SwaggerConstants.ApiOperations.OrderCancellation.GET_IF_QUOTE_EXISTS)
	    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceDetailBeanForAmendment.class),
	            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	    @RequestMapping(value = "/{orderCode}/checkquote", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseResource<QuoteOrderAmendmentBean> getIfQuoteExists(@PathVariable("orderCode") String orderCode)
	            throws TclCommonException {
	        QuoteOrderAmendmentBean response =cancellationService.getCancellationQuoteCreatedForParentOrder(orderCode);
	        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
	                Status.SUCCESS);
	    }
	    
	    
	    @ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_SERVICE_DETAILS_FOR_CANCELLATION)
		@GetMapping(value = "/servicedetails")
		public ResponseResource<MDMServiceInventoryBean> getServiceDetailsForOrderCancellation(@RequestParam("page") Integer page,@RequestParam("size") Integer size,@RequestParam(value="customerId",required = false) Integer customerId,
				@RequestParam(value="customerLeId",required = false) Integer customerLeId,
				@RequestParam(value="opportunityId",required = false) Integer opportunityId,@RequestParam(value="orderCode",required = false) String orderCode,@RequestParam(value="serviceId",required = false) String serviceId,
				@RequestParam(value="status",required = false) String status, @RequestParam(value="customerName", required=false) String customerName) throws TclCommonException {
			
			MDMServiceInventoryBean response = cancellationService.getServiceDetailsForOrderCancellation(page, size, customerId, customerLeId, opportunityId, orderCode, serviceId, status, customerName);
			
			
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
					Status.SUCCESS);
		}	
	    
	    @ApiOperation(value = SwaggerConstants.ApiOperations.INTERNALSTAGEVIEW.GET_SERVICE_DETAILS_FOR_CANCELLATION)
		@PostMapping(value = "/cancelledservices",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
		public ResponseResource<QuoteDetail> persistCancelledServiceDetailsForOrderCancellation(@RequestBody CancellationRequest cancellationRequest) throws TclCommonException {
			
			QuoteDetail detail = cancellationService.persistCancelledServiceDetailsForOrderCancellation(cancellationRequest);
			
			
			return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, detail,
					Status.SUCCESS);
		}
	    
	

}
