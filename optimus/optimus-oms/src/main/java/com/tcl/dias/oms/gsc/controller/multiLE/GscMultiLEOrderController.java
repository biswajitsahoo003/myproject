package com.tcl.dias.oms.gsc.controller.multiLE;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.gsc.beans.GscCountrySpecificDocumentBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.oms.beans.ServiceResponse;
import com.tcl.dias.oms.gsc.beans.GscApiRequest;
import com.tcl.dias.oms.gsc.beans.GscAttachmentBean;
import com.tcl.dias.oms.gsc.beans.GscMultiLEOrderDataBean;
import com.tcl.dias.oms.gsc.beans.GscOrderConfigurationBean;
import com.tcl.dias.oms.gsc.beans.GscOrderSolutionBean;
import com.tcl.dias.oms.gsc.beans.GscOrderStatusStageUpdate;
import com.tcl.dias.oms.gsc.beans.GscTfnBean;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEOrderDetailService;
import com.tcl.dias.oms.gsc.service.multiLE.GscMultiLEOrderService;
import com.tcl.dias.oms.gsc.service.v1.GscOrderDetailService;
import com.tcl.dias.oms.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Rest controller for covering multiple LE scenario for order GSC+Teams DR
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@RestController
@RequestMapping(value = "/v2/gsc/multiLE/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class GscMultiLEOrderController {

	@Autowired
	GscMultiLEOrderService gscMultiLEOrderService;

	@Autowired
	GscMultiLEOrderDetailService gscMultiLEOrderDetailService;

	@Autowired
	GscOrderDetailService gscOrderDetailService;

	/**
	 * Method to save attributes for solutions.
	 *
	 * @param orderId
	 * @param solutions
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_ATTRIBUTES)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderSolutionBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{orderId}/solutions/gscorders/configurations/attributes")
	public ResponseResource<List<GscOrderSolutionBean>> updateOrderProductComponentAttributesForSolutions(
			@PathVariable("orderId") Integer orderId, @RequestBody GscApiRequest<List<GscOrderSolutionBean>> solutions)
			throws TclCommonException {
		List<GscOrderSolutionBean> response = gscMultiLEOrderService
				.updateProductComponentAttributesForSolutions(orderId, solutions.getData());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Update one or more order configurations data by id
	 *
	 * @param orderId
	 * @param orderGscId
	 * @param solutionId
	 * @return GscOrderConfigurationBean
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPDATE_CONFIGURATIONS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/{orderId}/solutions/{orderSolutionId}/gscorders/{gscOrderId}/configurations")
	public ResponseResource<List<GscOrderConfigurationBean>> updateOrderConfigurationDetails(
			@PathVariable("orderId") Integer orderId, @PathVariable("gscOrderId") Integer orderGscId,
			@PathVariable("orderSolutionId") Integer solutionId,
			@RequestBody List<GscOrderConfigurationBean> configurationsUpdateRequest) throws TclCommonException {
		List<GscOrderConfigurationBean> response = gscMultiLEOrderDetailService.updateOrderConfigurations(orderId,
				solutionId, orderGscId, configurationsUpdateRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Fetch toll free numbers for given configuration
	 *
	 * @param configurationId
	 * @param count
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CONFIGURATION_NUMBERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscTfnBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/configurations/{orderConfigurationId}/numbers")
	public ResponseResource getAvailableNumbers(@PathVariable("orderConfigurationId") Integer configurationId,
			@RequestParam(required = false) String city,
			@RequestParam(defaultValue = "10", required = false) Integer count,
			@RequestParam("autoReserve") Boolean autoReserve) {
		List<GscTfnBean> response = gscOrderDetailService.getAvailableNumbers(configurationId, city, count,
				autoReserve);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get applicable documents
	 *
	 * @param configurationId
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.GET_CONFIGURATION_DOCUMENTS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscCountrySpecificDocumentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/configurations/{orderConfigurationId}/documents")
	public ResponseResource getApplicableDocuments(@PathVariable("orderConfigurationId") Integer configurationId) {
		List<GscAttachmentBean> response = gscOrderDetailService.getDocumentsForConfigurationId(configurationId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to upload country documents of customer in object storage.
	 *
	 * @param file
	 * @param configurationId
	 * @param documentId
	 * @return {@link GscAttachmentBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPLOAD_CONFIGURATION_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/object/configurations/{orderConfigurationId}/documents/{documentId}")
	public ResponseResource<ServiceResponse> uploadObjectConfigurationDocument(@RequestParam("file") MultipartFile file,
			@PathVariable("orderConfigurationId") Integer configurationId,
			@PathVariable("documentId") Integer documentId) throws TclCommonException {
		ServiceResponse serviceResponse = gscOrderDetailService.uploadObjectConfigurationDocument(file,
				configurationId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceResponse,
				Status.SUCCESS);
	}

	/**
	 * API to update the file uploaded details in oms level
	 *
	 * @param configurationId
	 * @param documentId
	 * @param requestId
	 * @param urlPath
	 * @return {@link GscAttachmentBean}
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.UPLOAD_CONFIGURATION_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = GscAttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/update/object/configurations/{orderConfigurationId}/documents/{documentId}")
	public ResponseResource<GscAttachmentBean> updateUploadObjectConfigurationDocument(
			@PathVariable("orderConfigurationId") Integer configurationId,
			@PathVariable("documentId") Integer documentId, @RequestParam("requestId") String requestId,
			@RequestParam(value = "url") String urlPath) throws TclCommonException {
		GscAttachmentBean response = gscMultiLEOrderDetailService
				.updateUploadObjectConfigurationDocument(configurationId, documentId, requestId, urlPath);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * API to download configuration document temporary url.
	 * 
	 * @param configurationId
	 * @param documentId
	 * @param downloadTemplate
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.Orders.DOWNLOAD_CONFIGURATION_DOCUMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/configurations/{orderConfigurationId}/temporaryUrl/{documentId}")
	public ResponseResource<String> downloadConfigurationDocumentTemporaryUrl(
			@PathVariable("orderConfigurationId") Integer configurationId,
			@PathVariable("documentId") Integer documentId,
			@RequestParam(name = "template", defaultValue = "false", required = false) Boolean downloadTemplate)
			throws TclCommonException {
		String response = gscMultiLEOrderDetailService.getObjectStorageConfigurationAttachmentForId(configurationId,
				documentId, downloadTemplate);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Updates the order status and stage
	 * 
	 * @param orderId
	 * @param orderToLeId
	 * @param orderConfigurationId
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscOrderStatusStageUpdate.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/{orderId}/le/{orderToLeId}/configuration/{orderConfigurationId}/stage")
	public ResponseResource<GscOrderStatusStageUpdate> updateGscOrderDetailSiteStatus(
			@PathVariable("orderId") Integer orderId, @PathVariable("orderToLeId") Integer orderToLeId,
			@PathVariable("orderConfigurationId") Integer orderConfigurationId,
			@RequestBody GscApiRequest<GscOrderStatusStageUpdate> request) throws TclCommonException {
		GscOrderStatusStageUpdate response = gscMultiLEOrderService
				.updateOrderConfigurationStageStatus(orderConfigurationId, request.getData());
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Get order by order ID
	 *
	 * @param orderId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscMultiLEOrderDataBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/{orderId}")
	public ResponseResource<GscMultiLEOrderDataBean> getOrder(@PathVariable(name = "orderId") Integer orderId)
			throws TclCommonException {
		GscMultiLEOrderDataBean response = gscMultiLEOrderService.getTeamsDROrder(orderId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
}
