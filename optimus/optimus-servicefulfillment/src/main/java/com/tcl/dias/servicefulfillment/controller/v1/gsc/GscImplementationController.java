package com.tcl.dias.servicefulfillment.controller.v1.gsc;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.RaiseJeopardy;
import com.tcl.dias.servicefulfillment.beans.gsc.BillingProfileApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.BillingProfileCmsIdBean;
import com.tcl.dias.servicefulfillment.beans.gsc.BridgeDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ChangeConfigOutpulseDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ClosingTaskBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CommVettingApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CommercialVettingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.CreateSiteRequestBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DIDNumberTestBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DIDServiceAcceptenceBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DidNumberRepcJeopardyBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DidNumberTaskBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DidPortingNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DocumentBean;
import com.tcl.dias.servicefulfillment.beans.gsc.DocumentValidationBean;
import com.tcl.dias.servicefulfillment.beans.gsc.EntmmTaskBean;
import com.tcl.dias.servicefulfillment.beans.gsc.EntmmValidationApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GscChildServiceDetailBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GscSipTrunkConfigApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GscSipTrunkRouteLabelCreationApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.NumbersBean;
import com.tcl.dias.servicefulfillment.beans.gsc.OutpulseDetailsBean;
import com.tcl.dias.servicefulfillment.beans.gsc.PlaceOrderToSuppliersBean;
import com.tcl.dias.servicefulfillment.beans.gsc.PortingNumberUpdateListBean;
import com.tcl.dias.servicefulfillment.beans.gsc.PostTestNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ProvisioningValidationApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.RateUploadApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.RateUploadBean;
import com.tcl.dias.servicefulfillment.beans.gsc.RemoveNumberTaskBean;
import com.tcl.dias.servicefulfillment.beans.gsc.RepcDBCreationBean;
import com.tcl.dias.servicefulfillment.beans.gsc.SelectedSiteBean;
import com.tcl.dias.servicefulfillment.beans.gsc.SelectedSuppliersBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ServiceAcceptanceBean;
import com.tcl.dias.servicefulfillment.beans.gsc.SupplierResponseBean;
import com.tcl.dias.servicefulfillment.beans.gsc.SupplierResponseDetailBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TaxationBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TestNumberBasicEnrichBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TestNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.TestNumberWithRoutingBean;
import com.tcl.dias.servicefulfillment.beans.gsc.UifnProcurementListBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ValidateDocumentApprovalAllBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ValidateDocumentApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ValidateDocumentBean;
import com.tcl.dias.servicefulfillment.beans.gsc.ViewRoutingNumberBean;
import com.tcl.dias.servicefulfillment.beans.gsc.VoiceBasicEnrichmentBean;
import com.tcl.dias.servicefulfillment.beans.gsc.VoiceSalesEngrApprovalBean;
import com.tcl.dias.servicefulfillment.beans.gsc.VoiceSalesEngrBean;
import com.tcl.dias.servicefulfillment.service.gsc.GscImplementationService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DIDNumberDetails;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.NumberCountryDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.RepcUpdateJeopardyBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SharedInCircuitBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SipTrunkDetails;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SitesBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.TrunkBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * AdvanceEnrichmentController this class is used to Post the SIP & Service
 * details
 *
 * @author Venkata Naga Sai S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@RestController
@RequestMapping("/v1/gsc-implementation")
public class GscImplementationController {

	@Autowired
	GscImplementationService gscImplementationService;

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.BASIC_ENRICHMENT_FOR_VOICE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = VoiceBasicEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/basic-enrichment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<VoiceBasicEnrichmentBean> basicEnrichmentForVoice(
			@RequestBody VoiceBasicEnrichmentBean voiceBasicEnrichmentBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.basicEnrichmentForVoice(voiceBasicEnrichmentBean), Status.SUCCESS);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TestNumberBasicEnrichBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/basic-enrichment/getportingdetails/{service_id}")
	public ResponseResource<List<TestNumberBasicEnrichBean>> getPortingOutpulseBasicEnrich(
			@PathVariable("service_id") String serviceId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getPortingOutpulseBasicEnrich(serviceId), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.BASIC_ENRICHMENT_FOR_VOICE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = PortingNumberUpdateListBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/basic-enrichment/portingdetails/{service_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PortingNumberUpdateListBean> updatePortingNumber(@PathVariable("service_id") Integer serviceId,
			@RequestBody PortingNumberUpdateListBean portingNumberUpdateListBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.updatePortingNumber(serviceId, portingNumberUpdateListBean), Status.SUCCESS);
	}

	
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.VALIDATE_DOCUMENT_FOR_VOICE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/basic-enrichment/close", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ClosingTaskBean> closeBasicEnrichment(
			@RequestBody ClosingTaskBean ClosingTaskBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeBasicEnrichmentTask(ClosingTaskBean), Status.SUCCESS);
	}
	
	
	/** This method is used to approve the document or reject the document. */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.APPROVE_OR_CLARIFY_DOC)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ValidateDocumentApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/validate-supporting-document/approveOrClarify")
	public ResponseResource<ValidateDocumentApprovalBean> approveOrClarifyDocument(
			@RequestBody ValidateDocumentApprovalBean validateDocumentApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.validateDocumentApproveOrClarify(validateDocumentApprovalBean),
				Status.SUCCESS);
	}

	/** This method is used to approve the document or reject the document. */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.APPROVE_OR_CLARIFY_DOC)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ValidateDocumentApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/validate-supporting-document/approveOrClarifyAll")
	public ResponseResource<ValidateDocumentApprovalAllBean> approveOrClarifyAllDocument(
			@RequestBody ValidateDocumentApprovalAllBean validateDocumentApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.validateDocumentApproveOrClarifyAll(validateDocumentApprovalBean),
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.VALIDATE_DOCUMENT_FOR_VOICE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/validate-supporting-document", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<VoiceBasicEnrichmentBean> validateDocument(
			@RequestBody ValidateDocumentBean validateDocumentBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.validateDocumentForVoice(validateDocumentBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.SAVE_SUPPLIER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/select-suppliers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SelectedSuppliersBean> selectedSuppliers(
			@RequestBody SelectedSuppliersBean supplierDetailBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.saveSelectedSuppliers(supplierDetailBean), Status.SUCCESS);
	}

	@Deprecated
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewRoutingNumberBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getroutingnumberdetails/service/{service_id}/supplier/{supplier_id}")
	public ResponseResource<List<ViewRoutingNumberBean>> getRoutingOutpulseDetails(
			@PathVariable("service_id") Integer serviceId, @PathVariable("supplier_id") String supplierId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getRoutingNumberDetails(serviceId, supplierId), Status.SUCCESS);
	}
	
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ViewRoutingNumberBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getroutingnumberdetails/task/{taskId}/wftask/{wfTaskId}/supplier/{supplier_id}")
	public ResponseResource<List<ViewRoutingNumberBean>> getRoutingOutpulseDetailsNew(@PathVariable("taskId") Integer taskId,
			@PathVariable("wfTaskId") String wfTaskId, @PathVariable("supplier_id") String supplierId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getRoutingNumberDetails(taskId, wfTaskId, supplierId), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.SAVE_SUPPLIER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/router-details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PlaceOrderToSuppliersBean> routingDetail(
			@RequestBody PlaceOrderToSuppliersBean supplierDetailBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.placeSupplierOrderDetail(supplierDetailBean), Status.SUCCESS);
	}

	/*
	 * @ApiOperation(value =
	 * SwaggerConstants.ApiOperations.BasicEnrichment.GET_VALIDATE_DOCUMENTS_INFO)
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message = Constants.SUCCESS, response =
	 * DocumentValidationBean.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	 * 
	 * @GetMapping(value = "/documents/{parent_service_id}") public
	 * ResponseResource<List<DocumentValidationBean>> getDocumentsInfoForValidation(
	 * 
	 * @PathVariable("parent_service_id") String parentServiceId) { return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * gscImplementationService.getDocumentsInfoForValidation(parentServiceId),
	 * Status.SUCCESS); }
	 */

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TestNumberBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getportingdetails/{service_id}")
	public ResponseResource<List<TestNumberBean>> getPortingOutpulseDetails (
			@PathVariable("service_id") String serviceId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getPortingOutpulseDetails(serviceId), Status.SUCCESS);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TestNumberWithRoutingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/testingNumber/task/{taskId}/wftask/{wfTaskId}")
	public ResponseResource<List<TestNumberWithRoutingBean>> getVasTestingDetails(@PathVariable("taskId") Integer taskId,
			@PathVariable("wfTaskId") String wfTaskId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getVasTestingDetails(taskId, wfTaskId), Status.SUCCESS);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = PostTestNumberBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/test-numbers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PostTestNumberBean> postTestNumber(@RequestBody PostTestNumberBean postTestNumberBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.postTestNumber(postTestNumberBean), Status.SUCCESS);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SupplierResponseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/supplier-response/task/{taskId}/wftask/{wfTaskId}")
	public ResponseResource<List<SupplierResponseBean>> getSupplierResponseDetails(@PathVariable("taskId") Integer taskId,
			@PathVariable("wfTaskId") String wfTaskId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getSupplierResponseDetails(taskId,wfTaskId), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.SAVE_SUPPLIER_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AttachmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/supplier-response", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SupplierResponseDetailBean> suppliersResponse(
			@RequestBody SupplierResponseDetailBean supplierResponseDetailBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.supplierResponse(supplierResponseDetailBean), Status.SUCCESS);
	}

	/***
	 * This method is used to approve the document or reject the document for Rate
	 * Upload task.
	 ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.RATE_UPLOAD_APPROVAL)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = RateUploadApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-rate-upload-approval")
	public ResponseResource<RateUploadApprovalBean> rateUploadApproval(
			@RequestBody RateUploadApprovalBean rateUploadApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.rateUploadApproval(rateUploadApprovalBean), Status.SUCCESS);
	}

	/*** Rate Upload task. ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.RATE_UPLOAD)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RateUploadBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-rate-upload")
	public ResponseResource<RateUploadBean> rateUpload(@RequestBody RateUploadBean rateUploadBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.rateUpload(rateUploadBean), Status.SUCCESS);
	}

	/*
	 * @ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.
	 * GET_PROVISIONING_VALIDATION_INFO)
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message = Constants.SUCCESS, response =
	 * ProvisioningValidationBean.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	 * 
	 * @GetMapping(value = "/provisioning-validation/{parent_service_id}") public
	 * ResponseResource<List<ProvisioningValidationBean>>
	 * getProvisioningValidationInfo(
	 * 
	 * @PathVariable("parent_service_id") String parentServiceId) { return new
	 * ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
	 * gscImplementationService.getProvisioningValidationInfo(parentServiceId),
	 * Status.SUCCESS); }
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.PROVISIONING_VALIDATION_APPROVAL)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvisioningValidationApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/provisioning-validation/approveOrClarify")
	public ResponseResource<ProvisioningValidationApprovalBean> approveOrClarifyProvisioning(
			@RequestBody ProvisioningValidationApprovalBean provisioningValidApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.provisioningValidationApproval(provisioningValidApprovalBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CLOSE_PROVISIONING_INFO_VALIDATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = VoiceBasicEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/provisioning-validation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<VoiceBasicEnrichmentBean> provisioningValidation(
			@RequestBody ValidateDocumentBean validateDocumentBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.provisioningValidation(validateDocumentBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CLOSE_PROVISIONING_INFO_VALIDATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = VoiceBasicEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/provisioning-validation-did", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<VoiceBasicEnrichmentBean> provisioningValidationDid(
			@RequestBody ValidateDocumentBean validateDocumentBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.provisioningValidationDid(validateDocumentBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.UPDATE_BRIDGE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = BridgeDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/provisioning-validation/updateBridge", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<BridgeDetailsBean> updateBridgeInfo(@RequestBody BridgeDetailsBean bridgeDetailsBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.updateBridge(bridgeDetailsBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.GET_VALIDATE_DOCUMENTS_INFO)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = DocumentValidationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getchildservicedetails/task/{taskId}/wftask/{wfTaskId}/service/{parentServiceId}")
	public ResponseResource<List<GscChildServiceDetailBean>> getGscChildServices(
			@PathVariable("parentServiceId") String parentServiceId, @PathVariable("taskId") Integer taskId,
			@PathVariable("wfTaskId") String wfTaskId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getGscChildServices(parentServiceId, taskId, wfTaskId), Status.SUCCESS);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = DocumentValidationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getCommercialVetting/service/{serviceId}")
	public ResponseResource<CommercialVettingBean> getCommercialVetting(
			@PathVariable("serviceId") Integer serviceId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getCommercialVetting(serviceId), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.GET_SUPPLIER_INFO_FOR_VALIDATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OutpulseDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/validate-supplier-internal-db/task/{taskId}/wftask/{wfTaskId}")
	public ResponseResource<List<OutpulseDetailsBean>> getSupplierInfoForValidation(@PathVariable("taskId") Integer taskId,
			@PathVariable("wfTaskId") String wfTaskId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getSupplierInfoForValidation(taskId, wfTaskId), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CLOSE_VALIDATE_SUPPLIER_INTERNAL_DB)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = VoiceBasicEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/validate-supplier-internal-db", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ValidateDocumentBean> validateSupplierInternalDB(
			@RequestBody ValidateDocumentBean validateDocumentBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.validateSupplierInternalDB(validateDocumentBean), Status.SUCCESS);
	}

	/*** ENTMM task. ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.ENTMM_VALIDATION_APPROVAL)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvisioningValidationApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/entmm-task/approveOrClarify")
	public ResponseResource<EntmmValidationApprovalBean> approveOrClarifyEntmm(
			@RequestBody EntmmValidationApprovalBean entmmValidationApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.entmmValidationApproval(entmmValidationApprovalBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.ENTMM_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = EntmmTaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/entmm-task")
	public ResponseResource<EntmmTaskBean> closeEntmmTask(@RequestBody EntmmTaskBean entmmTaskBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeEntmmTask(entmmTaskBean), Status.SUCCESS);
	}

	/*** Billing profile task. ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.BILLING_PROFILE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = BillingProfileCmsIdBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-billing-profile")
	public ResponseResource<BillingProfileCmsIdBean> billingProfile(
			@RequestBody BillingProfileCmsIdBean billingProfileCmsIdBean) throws TclCommonException {
		Boolean flag =  gscImplementationService.validateIndiaLe(billingProfileCmsIdBean);
		if(flag != true) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.billingProfile(billingProfileCmsIdBean), Status.SUCCESS);
		}else {
			billingProfileCmsIdBean.setMessage("Taxation task not applicaple for INDIA Supplier LE");
			return new ResponseResource<>(ResponseResource.R_CODE_BAD_REQUEST, ResponseResource.RES_FAILURE,
					billingProfileCmsIdBean, Status.ERROR);
		}
		
	}

	/***
	 * This method is used to approve or reject the document for Billing Profile
	 * task.
	 ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.BILLING_PROFILE_APPROVAL)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = BillingProfileApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-billing-profile-approval")
	public ResponseResource<BillingProfileApprovalBean> billingProfileApproval(
			@RequestBody BillingProfileApprovalBean billingProfileApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.billingProfileApproval(billingProfileApprovalBean), Status.SUCCESS);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = NumberCountryDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc-service-acceptence/task/{taskId}/wftask/{wfTaskId}")
	public ResponseResource<List<NumberCountryDetailsBean>> getNumberCountryDetails(
			@PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getNumberCountryDetails(taskId, wfTaskId), Status.SUCCESS);
	}

	/*** Service Acceptance task ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.BILLING_PROFILE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceAcceptanceBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-service-acceptence")
	public ResponseResource<ServiceAcceptanceBean> serviceAcceptence(
			@RequestBody ServiceAcceptanceBean serviceAcceptanceBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.serviceAcceptence(serviceAcceptanceBean), Status.SUCCESS);
	}

	/*** Taxation task ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.BILLING_PROFILE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TaxationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-taxation")
	public ResponseResource<TaxationBean> closeTaxation(@RequestBody TaxationBean taxationBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeTaxation(taxationBean), Status.SUCCESS);
	}
	
	/*** Commercial Vetting task ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.BILLING_PROFILE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskDetailsBaseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-comm-vetting")
	public ResponseResource<TaskDetailsBaseBean> closeCommercialVetting(@RequestBody TaskDetailsBaseBean taskDetailsBaseBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeCommercialVetting(taskDetailsBaseBean), Status.SUCCESS);
	}
	
	/** This method is used to approve or reject the commercial vetting. */
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.APPROVE_OR_CLARIFY_DOC)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CommVettingApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-comm-vetting/approveOrClarifyAll")
	public ResponseResource<CommVettingApprovalBean> approveOrClarifyCommVetting(
			@RequestBody CommVettingApprovalBean commVettingApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.commVettingApproval(commVettingApprovalBean),
				Status.SUCCESS);
	}
	
	/*** UIFN Procurement task ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.BILLING_PROFILE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = UifnProcurementListBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-uifn-procure")
	public ResponseResource<UifnProcurementListBean> closeUifnProcure(@RequestBody UifnProcurementListBean uifnProcurementListBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeUifnProcure(uifnProcurementListBean), Status.SUCCESS);
	}

	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = DocumentValidationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getTrunks/service/{serviceId}")
	public ResponseResource<List<TrunkBean>> getTrunks(
			@PathVariable("serviceId") Integer serviceId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getTrunks(serviceId), Status.SUCCESS);
	}
	
	/***
	 * This method is used to approve the document or reject the document for Voice Sales Engr Task.
	 ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.VOICE_SALES_ENGR_APPROVAL)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = VoiceSalesEngrApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-voice-sales-engr/approveOrClarifyAll") 
	public ResponseResource<VoiceSalesEngrApprovalBean> approveOrClarifyVoiceSalesEngrApproval(
			@RequestBody VoiceSalesEngrApprovalBean voiceSalesApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.voiceSalesApproval(voiceSalesApprovalBean), Status.SUCCESS);
	}
	
	/*** Voice Sales Engr task ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.VOICE_SALES_ENGR)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = VoiceSalesEngrBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-voice-sales-engr")
	public ResponseResource<VoiceSalesEngrBean> voiceSalesEngr(@RequestBody VoiceSalesEngrBean voiceSalesBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.voiceSalesEngr(voiceSalesBean), Status.SUCCESS);
	}
	
	/*** Fetching filtered Sites from REPC ***/
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SitesBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/getSites/service/{serviceId}")
	public ResponseResource<SitesBean> getSites(
			@PathVariable("serviceId") Integer serviceId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getSites(serviceId), Status.SUCCESS);
	}
	
	/*** Saving the selected Site ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.SAVE_SELECTED_SITE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SelectedSiteBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/select-site", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SelectedSiteBean> saveSelectedSite(
			@RequestBody SelectedSiteBean selectedSiteBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.saveSelectedSite(selectedSiteBean), Status.SUCCESS);
	}
	
	/** get DID number details **/
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SitesBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/did-number-details/task/{taskId}/wftask/{wfTaskId}")
	public ResponseResource<DIDNumberDetails> getSelectedSupplier(
			@PathVariable("taskId") Integer taskId,	@PathVariable("wfTaskId") String wfTaskId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getDIDNumberDetails(taskId, wfTaskId), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CLOSE_DID_NUMBER_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidNumberTaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/place-supplier-didorder", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DidNumberTaskBean> closeDidNumberTask(
			@RequestBody DidNumberTaskBean didNumberTaskBean) throws TclCommonException {
		return new ResponseResource<DidNumberTaskBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeDidNumberTask(didNumberTaskBean), Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CLOSE_DID_NUMBER_SERVICE_ACCEPTENCE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidNumberTaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/place-supplier-didorder-service-acceptence", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DIDServiceAcceptenceBean> closeDidNumberTask(
			@RequestBody DIDServiceAcceptenceBean didNumberTaskBean) throws TclCommonException {
		return new ResponseResource<DIDServiceAcceptenceBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeDidNumberServiceAcceptenceTask(didNumberTaskBean), Status.SUCCESS);

	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CLOSE_DID_NUMBER_TASK_TESTING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidNumberTaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/place-supplier-didorder-testing", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DIDNumberTestBean> closeDidNumberTaskTesting(
			@RequestBody DIDNumberTestBean didNumberTaskBean) throws TclCommonException {
		return new ResponseResource<DIDNumberTestBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeDidNumberTestingTask(didNumberTaskBean), Status.SUCCESS);

	}
	
    /*** Creating New Site ***/
    @ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CREATE_NEW_SITE)
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateSiteRequestBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    @PostMapping(value = "/gsc-create-new-site", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<CreateSiteRequestBean> createNewSite(
            @RequestBody CreateSiteRequestBean createSiteRequestBean) throws TclCommonException {
        return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                gscImplementationService.createNewSite(createSiteRequestBean), Status.SUCCESS);
    }
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CLOSE_DID_PORTING_RS_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidPortingNumberBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/did-porting-number-info-rs-config", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DidPortingNumberBean> closeDidPortingNumberTask(
			@RequestBody DidPortingNumberBean didPortingNumberBean) throws TclCommonException {
		return new ResponseResource<DidPortingNumberBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeDidPortingNumberTask(didPortingNumberBean), Status.SUCCESS);

	}
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CLOSE_DID_REPC_JEOPARDY_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidNumberRepcJeopardyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/did-new-number-order-creation-repc-jeopardy", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DidNumberRepcJeopardyBean> closeDidNumberRepcJeopardyTask(
			@RequestBody DidNumberRepcJeopardyBean didNumberRepcJeopardyBean) throws TclCommonException {
		return new ResponseResource<DidNumberRepcJeopardyBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeDidNumberRepcJeopardyTask(didNumberRepcJeopardyBean), Status.SUCCESS);
	}
	
	/**
	 * Used to fetch all the details in excel sheet format
	 *
	 * @param serviceId
	 * @param taskId
	 * @param wfTaskId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/didexcel/task/{taskId}/wftask/{wfTaskId}/servicedetail/{serviceId}/download")
	public ResponseResource<HttpServletResponse> getExcel(@PathVariable("serviceId") Integer serviceId, @PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId, HttpServletResponse response) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.returnDidNumberExcelSheet(serviceId, taskId, wfTaskId, response), Status.SUCCESS);
	}
	
	/*** Fetching Selected Supplier Trunks ***/
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SharedInCircuitBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/get-selected-supplier-trunks/service/{serviceId}")
	public ResponseResource<List<SharedInCircuitBean>> getSelectedSupplierTrunks(
			@PathVariable("serviceId") Integer serviceId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getSelectedSupplierTrunks(serviceId), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.CLOSE_REPC_UPDATE_JEOPARDY_TASK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidNumberRepcJeopardyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-repc-order-update-jeopardy", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<RepcUpdateJeopardyBean> closeRepcUpdateJeopardyTask(@RequestBody RepcUpdateJeopardyBean repcUpdateJeopardyBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeRepcUpdateJeopardy(repcUpdateJeopardyBean), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.GET_CHANGE_CONFIG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ChangeConfigOutpulseDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/change-config/task/{taskId}/wftask/{wfTaskId}")
	public ResponseResource<List<ChangeConfigOutpulseDetailsBean>> getChangeConfigDetails(@PathVariable("taskId") Integer taskId,
			@PathVariable("wfTaskId") String wfTaskId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getChangeConfigDetails(taskId, wfTaskId), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.BasicEnrichment.POST_CHANGE_CONFIG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidNumberRepcJeopardyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/change-config", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TaskDetailsBaseBean> closeChangeConfig(@RequestBody TaskDetailsBaseBean taskDetailsBaseBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeChangeConfig(taskDetailsBaseBean), Status.SUCCESS);
	}
	
	/**
	 * get didServiceHandoverPdf
	 * @param serviceId
	 * @param taskId
	 * @param wfTaskId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/didServiceHandoverPdf/task/{taskId}/wftask/{wfTaskId}/servicedetail/{serviceId}/download")
	public ResponseResource<String> getServiceAcceptancePdf(@PathVariable("serviceId") Integer serviceId, @PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId, HttpServletResponse response) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getServiceAcceptancePdf(taskId, wfTaskId,serviceId,response), Status.SUCCESS);

	}
	
	/** This method is used to approve or reject the sip trunk config. */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SipConfig.APPROVE_SIPCONFIG)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscSipTrunkConfigApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-sip-trunk-config/approve")
	public ResponseResource<GscSipTrunkConfigApprovalBean> approveGscSipTrunkConfig(
			@RequestBody GscSipTrunkConfigApprovalBean gscSipConfigApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.approveGscSipTrunkConfigTask(gscSipConfigApprovalBean), Status.SUCCESS);
	}

	/** This method is used to close the sip trunk config task*/
	@ApiOperation(value = SwaggerConstants.ApiOperations.SipConfig.CLOSE_SIP_TRUNK_CONFIG)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ClosingTaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-sip-trunk-config", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ClosingTaskBean> closeSipTrunkConfigTask(
			@RequestBody ClosingTaskBean closingTaskBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeSipTrunkConfigTask(closingTaskBean), Status.SUCCESS);
	}
	
	/** This method is used to approve or reject the sip trunk route label creation. */
	@ApiOperation(value = SwaggerConstants.ApiOperations.SipConfig.APPROVE_SIP_ROUTELABEL_CREATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GscSipTrunkRouteLabelCreationApprovalBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-sip-routelabel-creation/approve")
	public ResponseResource<GscSipTrunkRouteLabelCreationApprovalBean> approveGscSipRouteLabelCreation(
			@RequestBody GscSipTrunkRouteLabelCreationApprovalBean gscSipRouteLabelCreationApprovalBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.approveGscSipTrunkRouteLabelCreation(gscSipRouteLabelCreationApprovalBean), Status.SUCCESS);
	}
	
	/** This method is used to close the sip trunk route label creation task*/
	@ApiOperation(value = SwaggerConstants.ApiOperations.SipConfig.CLOSE_SIP_ROUTELABEL_CREATION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ClosingTaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-sip-routelabel-creation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ClosingTaskBean> closeSipRouteLabelCreationTask(
			@RequestBody ClosingTaskBean closingTaskBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeSipRouteLabelCreationTask(closingTaskBean), Status.SUCCESS);
	}
	
	/*** REPC DB creation (Circuit creation) ***/
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = RepcDBCreationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/repc-db-circuitcreation/service/{serviceId}")
	public ResponseResource<RepcDBCreationBean> getRepcDbCreationInfo(
			@PathVariable("serviceId") Integer serviceId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getRepcDbCreationInfo(serviceId), Status.SUCCESS);
	}
	
	
	/*** Closing REPC DB creation (Circuit creation) task ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.SipConfig.CLOSE_REPC_DB_CREATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = RepcDBCreationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/repc-db-circuitcreation")
	public ResponseResource<RepcDBCreationBean> closeRepcDBCircuitCreationTask(@RequestBody RepcDBCreationBean repcDBCreationBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeRepcDBCircuitCreationTask(repcDBCreationBean), Status.SUCCESS);
	}
	
	/**
	 * get sipServiceHandoverPdf
	 * @param serviceId
	 * @param taskId
	 * @param wfTaskId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = HttpServletResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/sipServiceHandoverPdf/task/{taskId}/wftask/{wfTaskId}/servicedetail/{serviceId}/download")
	public ResponseResource<String> getSipServiceAcceptancePdf(@PathVariable("serviceId") Integer serviceId, @PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId, HttpServletResponse response) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getSipServiceAcceptancePdf(taskId, wfTaskId,serviceId,response), Status.SUCCESS);

	}

	@GetMapping("/gsc-vas-servicehandovernote/task/{taskId}/wftask/{wfTaskId}/servicedetail/{serviceId}/download")
	public ResponseResource<String> getVASServiceAcceptancePdf(@PathVariable("serviceId") Integer serviceId, @PathVariable("taskId") Integer taskId, @PathVariable("wfTaskId") String wfTaskId,
			HttpServletResponse response) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getVASServiceAcceptancePdf(taskId, wfTaskId,serviceId, response), Status.SUCCESS);

	}
	
	/*** Fetching Sip Trunks Details for Sip Service Acceptance Task ***/
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SipTrunkDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/get-sip-trunks-details/service/{serviceId}")
	public ResponseResource<List<SipTrunkDetails>> getSipTrunkDetails(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getSipTrunkDetails(serviceId), Status.SUCCESS);
	}
	
	/**
	 * This method is used for raise jeopardy task.
	 *
	 * @param taskId
	 * @param raiseJeopardy
	 * @return RaiseJeopardy
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.JeopardyFlow.JEOPARDY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RaiseJeopardy.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/raise-jeopardy")
	public ResponseResource<RaiseJeopardy> raiseJeopardy(
			@RequestBody RaiseJeopardy raiseJeopardy) throws TclCommonException {
		return new ResponseResource<RaiseJeopardy>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.raiseJeopardy(raiseJeopardy), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.REMOVE_NUMBER)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidNumberRepcJeopardyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/gsc-remove-numbers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TaskDetailsBaseBean> closeRemoveNumbers(@RequestBody RemoveNumberTaskBean removeNumberTaskBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.closeRemoveNumbers(removeNumberTaskBean), Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.GET_REMOVED_NUMBERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidNumberRepcJeopardyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/gsc-remove-numbers/get-numbers/{serviceId}")
	public ResponseResource<List<NumbersBean>> getTollFreeNumbersForRemoveNumTask(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getTollFreeNumbersForRemoveNumTask(serviceId), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.GET_REMOVED_NUMBERS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DidNumberRepcJeopardyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/get-cdrlog-serviceaccept/{serviceId}")
	public ResponseResource<List<DocumentBean>> getCDRLogForServiceAcceptanceTask(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				gscImplementationService.getCDRLogForServiceAcceptanceTask(serviceId), Status.SUCCESS);
	}
}
