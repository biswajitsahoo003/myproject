package com.tcl.dias.oms.izosdwan.controller.v1;


import java.util.List;

import com.tcl.dias.oms.izosdwan.beans.SEASiteInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.common.beans.LocationInputDetails;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.OrdersBean;

import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;

import com.tcl.dias.oms.beans.QuoteDetail;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.entity.entities.OrderProductComponent;

import com.tcl.dias.oms.izosdwan.beans.IzosdwanOrdersBean;
import com.tcl.dias.oms.izosdwan.beans.OrderIzosdwanSiteBean;
import com.tcl.dias.oms.izosdwan.beans.ViewSitesSummaryBean;
import com.tcl.dias.oms.izosdwan.service.v1.IzosdwanOrderService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/v1/izosdwan/orders")
public class IzosdwanOrderController {
	@Autowired
	IzosdwanOrderService izosdwanOrderService;

	/**@author Madhumiethaa Palanisamy
	 * 
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrdersBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<IzosdwanOrdersBean> getOrderDetails(@PathVariable("orderId") Integer orderId
			,@RequestParam(required = false, name = "feasiblesites") String feasibleSites,
			@RequestParam(required = false, name = "siteproperities") Boolean siteproperities,
			@RequestParam(required = false, name = "siteid") Integer siteId)
			throws TclCommonException {
		IzosdwanOrdersBean response = izosdwanOrderService.getOrderDetails(orderId,feasibleSites, siteproperities, siteId,
				null);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_QUOTE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/sites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<IzosdwanOrdersBean> getQuoteConfigurationBySites(@PathVariable("orderId") Integer orderId,
			@RequestParam(required = false, name = "feasiblesites") String feasibleSites,
			@RequestParam(required = false, name = "siteproperities") Boolean siteproperities,
			@RequestBody List<Integer> siteIds) throws TclCommonException {
		IzosdwanOrdersBean response = izosdwanOrderService.getOrderDetails(orderId,feasibleSites, siteproperities, null,
				siteIds);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.UPDATE_SITE_PROPERTIES)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites/propertiesattribute", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<IzosdwanOrdersBean> updateSitePropertiesAttributes(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody List<UpdateRequest> request)
			throws TclCommonException {
		IzosdwanOrdersBean response = izosdwanOrderService.updateSitePropertiesAttributes(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderProductComponent.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<OrderProductComponentBean>> getSiteProperties(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
			@RequestParam(value = "filter", required = false) String attributeName) throws TclCommonException {
		List<OrderProductComponentBean> response = izosdwanOrderService.getSiteProperties(siteId, attributeName);

		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateSiteProperties this method
	 *            is used to map quote to order
	 * @return ResponseResource
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites/{siteId}/properties", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<OrderIzosdwanSiteBean> updateSiteProperties(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		OrderIzosdwanSiteBean response = izosdwanOrderService.updateSiteProperties(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**
	 * @author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited updateOrderSites this method is
	 *            used to updated requestor date
	 * @return ResponseResource
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_SITES)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateOrderSites(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestBody UpdateRequest request)
			throws TclCommonException {
		QuoteDetail response = izosdwanOrderService.updateOrderSites(request);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	/**@author Madhumiethaa Palanisamy
	 * @link http://www.tatacommunications.com/
	 * @copyright 2018 Tata Communications Limited getViewSitesSummaryDetails 
	 *        
	 * throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.GET_CONFIGURATION_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/viewsites", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<List<ViewSitesSummaryBean>> getViewSitesSummaryDetails(
			@PathVariable("orderId") Integer orderId, @RequestParam("type") String type,
			@RequestBody(required = false) LocationInputDetails request) throws TclCommonException {

		List<ViewSitesSummaryBean> response = izosdwanOrderService.getSitesBasedOnSiteType(orderId, type, request);

		return new ResponseResource<List<ViewSitesSummaryBean>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
	}

	/**
	 * @author Rjahan
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
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<QuoteDetail> updateOrderToLeStatus(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @RequestParam("stage") String status)
			throws TclCommonException {
		QuoteDetail response = izosdwanOrderService.updateOrderToLeStatus(orderToLeId, status);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	/**
	 * This Api is used to update the gst info for cgw
	 * @author mpalanis
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ATTRIBUTES_IZOSDWAN_CGW)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/cgw/updateattributes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateCgwAttributeDetails(@PathVariable("orderId") Integer orderId,@RequestBody List<UpdateRequest> request)
			throws TclCommonException {
		izosdwanOrderService.updateCgwAttributeDetails(orderId,request);
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}

	/**
	 * This Api is used to update the gst info for selected cgw
	 * @author abhutkar
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ATTRIBUTES_IZOSDWAN_CGW)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/cgw/updateattributes/{cgwId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> updateCgwWiseAttributeDetails(@PathVariable("orderId") Integer orderId, @PathVariable("cgwId") Integer cgwId, @RequestBody List<UpdateRequest> request)
			throws TclCommonException {
		izosdwanOrderService.updateCgwWiseAttributeDetails(cgwId, request);
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.GstAddress.GET_GST_ADDRESS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GstAddressBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/sites/{siteId}/gstin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GstAddressBean> getGstData(@RequestParam("gstin") String gstin,@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("siteId") Integer siteId) throws TclCommonException {
		GstAddressBean gstAddressBean = izosdwanOrderService.getGstData(gstin,orderId, orderToLeId, siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gstAddressBean,
				Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.GstAddress.GET_GST_ADDRESS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GstAddressBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/le/sites/gstin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GstAddressBean> getCgwGstData(@RequestParam("gstin") String gstin,@RequestParam("cgwLocation") Integer cgwLocation) throws TclCommonException {
		GstAddressBean gstAddressBean = izosdwanOrderService.getCgwGstData(gstin,cgwLocation);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gstAddressBean,
				Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.GstAddress.GET_GST_ADDRESS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GstAddressBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/le/gstAddress", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<String> getGstAddressData(@RequestParam("gstin") String gstin) throws TclCommonException {
		String gstAddress = izosdwanOrderService.getGstAddress(gstin);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gstAddress,
				Status.SUCCESS);
	}

	/**
	 *
	 * Get eligible locations for vutm
	 * @author Anway Bhutkar
	 * @param orderId
	 * @param orderToLeId
	 * @param location
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Quotes.SAVE_VPROXY_SOLUTIONS)
	@RequestMapping(value = "/{orderId}/le/{orderToLeId}/vutm/locations/details", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = QuoteDetail.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<SEASiteInfoBean> getVutmLocationsDetails(@PathVariable("orderId") Integer orderId,
																	 @PathVariable("orderToLeId") Integer orderToLeId, @RequestParam(required = false, value = "location") String location)
			throws TclCommonException {
		SEASiteInfoBean response = izosdwanOrderService.getSiteInformationForVutm(orderId, orderToLeId, location);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
}
