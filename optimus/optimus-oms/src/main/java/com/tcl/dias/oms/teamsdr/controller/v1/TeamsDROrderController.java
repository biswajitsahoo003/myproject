package com.tcl.dias.oms.teamsdr.controller.v1;

import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.EQUIPMENT;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.EQUIPMENT_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.EQUIPMENT_REF_NAME;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.PLAN;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.PLAN_ATTRIBUTES;
import static com.tcl.dias.oms.teamsdr.util.TeamsDRConstants.PLAN_REF_NAME;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.common.beans.GstAddressBean;
import com.tcl.dias.oms.gst.service.GstInService;
import com.tcl.dias.oms.teamsdr.beans.MediaGatewayOrderConfigurationBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRMultiOrderLeBean;
import org.json.simple.parser.ParseException;
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

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.OrderProductComponentBean;
import com.tcl.dias.oms.beans.UpdateRequest;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.dias.oms.teamsdr.beans.TeamsDROrderDataBean;
import com.tcl.dias.oms.teamsdr.beans.TeamsDRQuoteDataBean;
import com.tcl.dias.oms.teamsdr.service.v1.TeamsDROrderService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Rest controller for Teams DR order related
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v1/teamsdr/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class TeamsDROrderController {

	@Autowired
	TeamsDROrderService teamsDROrderService;

	@Autowired
	GstInService gstInService;

	/**
	 * Get order by order ID
	 *
	 * @param orderId
	 * @param isFilterNeeded
	 * @param productFamily
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRQuoteDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{orderId}")
	public ResponseResource<TeamsDROrderDataBean> getOrder(@PathVariable(name = "orderId") Integer orderId,
			@RequestParam(value = "isFilterNeeded", required = false) Boolean isFilterNeeded,
			@RequestParam(value = "productFamily", required = false) String productFamily) throws TclCommonException {
		TeamsDROrderDataBean response = teamsDROrderService.getTeamsDROrder(orderId, isFilterNeeded, productFamily);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to create / update enrichment attributes For plan
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param solutionId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{orderId}/le/{orderToLeId}/plan/{solutionId}/properties")
	public ResponseResource<List<OrderProductComponentBean>> updatePlanAttributes(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("solutionId") Integer solutionId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		List<OrderProductComponentBean> response = teamsDROrderService.createOrUpdateOrderEnrichmentAttributes(request,
				orderToLeId, PLAN, solutionId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to create / update enrichment attributes For equipment.
	 *
	 * @param orderId
	 * @param orderToLeId
	 * @param siteId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.SAVE_SITE_PROPERTIES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{orderId}/le/{orderToLeId}/equipment/{siteId}/properties")
	public ResponseResource<List<OrderProductComponentBean>> updateEndpointAttributes(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("siteId") Integer siteId,
			@RequestBody UpdateRequest request) throws TclCommonException {
		List<OrderProductComponentBean> response = teamsDROrderService.createOrUpdateOrderEnrichmentAttributes(request,
				orderToLeId, EQUIPMENT, siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to get plan enrichment attributes.
	 * 
	 * @param orderId
	 * @param orderToLeId
	 * @param solutionId
	 * @param attributeName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDER_ATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{orderId}/le/{orderToLeId}/plan/{solutionId}/properties")
	public ResponseResource<List<OrderProductComponentBean>> getPlanAttributes(@PathVariable("orderId") Integer orderId,
			@PathVariable("orderToLeId") Integer orderToLeId, @PathVariable("solutionId") Integer solutionId,
			@RequestParam(value = "filter", required = false) String attributeName) throws TclCommonException {
		List<OrderProductComponentBean> response = teamsDROrderService.getEnrichmentAttributes(orderId, orderToLeId,
				PLAN_ATTRIBUTES, solutionId, PLAN_REF_NAME, attributeName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Method to get equipment Attributes.
	 * 
	 * @param orderId
	 * @param orderToLeId
	 * @param solutionId
	 * @param attributeName
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_ORDER_ATTRIBUTES)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{orderId}/le/{orderToLeId}/equipment/{solutionId}/properties")
	public ResponseResource<List<MediaGatewayOrderConfigurationBean>> getEndpointAttributes(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("solutionId") Integer solutionId,
			@RequestParam(value = "filter", required = false) String attributeName) throws TclCommonException {
		List<MediaGatewayOrderConfigurationBean> response = teamsDROrderService.getEquipmentEnrichmentAttributes(
				orderId, orderToLeId, EQUIPMENT_ATTRIBUTES, solutionId, EQUIPMENT_REF_NAME, attributeName);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to download Zipped cofs.
	 * 
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_ZIPPED_COF)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{orderId}/zippedcof")
	public ResponseResource<String> downloadZippedCofs(@PathVariable("orderId") Integer orderId,
			HttpServletResponse response) throws TclCommonException {
		teamsDROrderService.downloadZippedCofs(orderId, response);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				Status.SUCCESS.toString(), Status.SUCCESS);
	}

	/**
	 * API to update order to le stage.
	 * @param orderId
	 * @param status
	 * @param subStatus
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ORDER_LE_STATUS)
	@RequestMapping(value = "/{orderId}/stage", method = RequestMethod.POST)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRMultiOrderLeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<List<TeamsDRMultiOrderLeBean>> updateQuoteToLeStatus(
			@PathVariable("orderId") Integer orderId, @RequestParam("status") String status,
			@RequestParam(value = "subStatus", required = false) String subStatus) throws TclCommonException {
		List<TeamsDRMultiOrderLeBean> response = teamsDROrderService.updateOrderToLeStatus(orderId, status, subStatus);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	/**
	 * Controller to get gst address based on gst number
	 *
	 * @param gstin
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.GstAddress.GET_GST_ADDRESS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GstAddressBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/gstin", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GstAddressBean> getGstData(@RequestParam("gstin") String gstin, @RequestParam(value = "siteId", required = false) Integer siteId)
			throws TclCommonException, ParseException {
		GstAddressBean gstAddressBean = teamsDROrderService.fetchGstAddress(gstin, siteId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, gstAddressBean,
				Status.SUCCESS);
	}
}
