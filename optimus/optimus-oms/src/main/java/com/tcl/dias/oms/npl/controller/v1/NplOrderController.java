package com.tcl.dias.oms.npl.controller.v1;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.DashBoardBean;
import com.tcl.dias.oms.beans.OrderIllSiteBean;
import com.tcl.dias.oms.beans.OrderLinkRequest;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.dto.OrderToLeDto;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;
import com.tcl.dias.oms.entity.entities.OrderSiteProvisionAudit;
import com.tcl.dias.oms.npl.beans.DashboardOrdersBean;
import com.tcl.dias.oms.npl.beans.NplLinkBean;
import com.tcl.dias.oms.npl.beans.NplOrdersBean;
import com.tcl.dias.oms.npl.beans.NplQuoteDetail;
import com.tcl.dias.oms.npl.beans.OrderNplSiteBean;
import com.tcl.dias.oms.npl.service.v1.NplOrderService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Controller class hosting order related API for NPL product 
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/v1/npl/orders")
public class NplOrderController {

	@Autowired
	NplOrderService nplOrderService;
	
	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getOrderDetails used to fetch all
	 *            the order details
	 * @param customerId
	 * @param quoteId
	 * @return NplQuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplOrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NplOrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId)
			throws TclCommonException {
		NplOrdersBean response = nplOrderService.getOrderDetails(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}	


	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited editOrderSites this method is
	 *            used to edit the order ill site info
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.EDIT_ORDER_LINKS)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/links/{linkId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> editOrderLinks(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody UpdateRequest request,
			@PathVariable("linkId") Integer linkId) throws TclCommonException {
		NplQuoteDetail response = nplOrderService.editLinkComponent(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateOrderSites this method is
	 *            used to updated requestor date
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_SITES)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> updateOrderSites(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		NplQuoteDetail response = nplOrderService.updateOrderSites(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	
	

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getOrderDetails used to fetch all
	 *            the order details
	 * @param customerId
	 * @param quoteId
	 * @return NplQuoteDetail
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_SITE_INFO)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderIllSiteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/links/{linkId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<NplLinkBean> getLinkDetails(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderLeId, @PathVariable("linkId") Integer linkId)
			throws TclCommonException {
		NplLinkBean response = nplOrderService.getLinkDetails(linkId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * getAttachments - Get attachment Details
	 * 
	 * @param orderId
	 * @return ResponseResource<List<OmsAttachBean>>
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDER_ATTACHEMENTS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OmsAttachBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/attachments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<OmsAttachBean>> getAttachments(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId) throws TclCommonException {
		List<OmsAttachBean> response = nplOrderService.getOmsAttachments(orderToLeId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited dashboard this is used to display
	 *            the dashboard details based on order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	// TO MOVE to a different controller

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DASH_BOARD_DETAILS)
	@RequestMapping(value = "/dashboard", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashBoardBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<DashBoardBean> getDashboardDetails(
			@RequestParam(required = false, value = "legalEntityId") Integer legalEntityId) throws TclCommonException {
		DashBoardBean response = nplOrderService.getDashboardDetails(legalEntityId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * getAttributesAndSites Method to details with respect to orderToLe id
	 * 
	 * @author Dinahar Vivekanandan
	 * @param orderToLeId
	 * @return ResponseResource<OrderToLeDto>
	 * @throws TclCommonException
	 */
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_TAX_EXEMPTION_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderToLeDto.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderToLeDto> getAttributes(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId,
			@RequestParam(value = "attributeName", required = false) String attributeName) throws TclCommonException {
		OrderToLeDto response = nplOrderService.getAttributes(orderToLeId, attributeName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited editOrderSites this method is
	 *            used to edit the order ill site info
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_LEGALENTITY_ATTRIBUTE)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/attributes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> updateLegalEntityAttribute(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		NplQuoteDetail response = nplOrderService.updateLegalEntityProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateOrderToLeStatus this method
	 *            is used to update the status of order to le
	 * @param orderToleId,
	 *            status
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ORDER_LE_STATUS)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<NplQuoteDetail> updateOrderToLeStatus(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestParam("stage") String status)
			throws TclCommonException {
		NplQuoteDetail response = nplOrderService.updateOrderToLeStatus(orderToLeId, status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateOrderSiteStatus this method
	 *            is used to update the status of order Ill sites
	 * @param orderIllSiteId,
	 *            status
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_QUOTE_LE_STATUS)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/link/{linkId}/stage", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSiteProvisionAudit.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<Boolean> updateOrderLinkStatus(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("linkId") Integer linkId, @RequestBody OrderLinkRequest request) throws TclCommonException {
		Boolean response = nplOrderService.updateOrderLinkStatus(linkId, request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getAllOrdersByProductName this
	 *            method is used to get all orders based on the productName
	 * @param productName
	 * @return ResponseResource<List<DashboardOrdersBean>>
	 * @throws TclCommonException
	 */
	// Move to Dashboard controller
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ALL_ORDERS)
	@RequestMapping(value = "/productorders", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DashboardOrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<DashboardOrdersBean>> getAllOrdersByProductName(
			@RequestParam("productName") String productName) throws TclCommonException {
		List<DashboardOrdersBean> response = nplOrderService.getAllOrdersByProductName(productName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NplQuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderNplSiteBean> updateOrderSiteProperties(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request,@RequestParam(value="type", required = false) String type) throws TclCommonException {
		OrderNplSiteBean response = nplOrderService.updateOrderSiteProperties(request,type);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SFDC_UPDATE_STAGE)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/sfdc/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<String> updateSdfcStage(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestParam("stage") String stage)
			throws TclCommonException {
		nplOrderService.updateSfdcStage(orderToLeId, stage);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);

	}

	/**
	 * 
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderProductComponent.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderProductComponentBean>> getSiteProperties(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,@RequestParam(value="filter", required = false) String attributeName,
			@RequestParam(value="type", required = false) String type)
			throws TclCommonException {
		List<OrderProductComponentBean> response = nplOrderService.getSiteProperties(siteId,attributeName,type);
		
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	 
	/*@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDER_ILLSITE_AUDIT_TRAIL)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/link/{linkId}/audittrail", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderSiteProvisionAudit.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<AuditBean>> getOrderLinkAuditTrail(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("linkId") Integer linkId)
			throws TclCommonException {
		List<AuditBean> response = nplOrderService.getOrderLinkAuditTrail(orderId, orderToLeId, linkId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}*/
	 /**
     * 
      * @link http://www.tatacommunications.com/
     * @copyright 2018 Tata Communications Limited updateRequestorDate this method is
     *            used to updated requestor date
     * @return ResponseResource
     * @throws TclCommonException
     */

     @ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_REQUESTOR_DATE_FOR_LINK)
     @RequestMapping(value = "/{orderId}/le/{orderToLeId}/links/{linkId}/requestorDate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
     @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
                   @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                   @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                   @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     public ResponseResource<String> updateLinkRequestorDate(@PathVariable("orderId") Integer orderId,
 			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody UpdateRequest request)
                   throws TclCommonException {
            String response = nplOrderService.updateLinkDetails(request);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);

     }
	
    
	
}
