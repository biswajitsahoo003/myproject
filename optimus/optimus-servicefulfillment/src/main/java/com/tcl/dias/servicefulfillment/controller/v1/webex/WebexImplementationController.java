package com.tcl.dias.servicefulfillment.controller.v1.webex;

import com.tcl.dias.servicefulfillment.beans.webex.*;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;

import java.io.IOException;
import java.util.List;

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
import com.tcl.dias.servicefulfillment.beans.ConfigureCpeBean;
import com.tcl.dias.servicefulfillment.beans.gsc.AdvancedEnrichmentBean;
import com.tcl.dias.servicefulfillment.beans.gsc.GSCServiceDetailBean;
import com.tcl.dias.servicefulfillment.service.webex.WebexImplementationService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("v1/webex-implementation/task")
public class WebexImplementationController {

	@Autowired
	WebexImplementationService webexImplementationService;
	
	/****** Webex Application/License starts here******/
	
	/***Advanced Enrichment Application***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.ADVANCED_ENRICHMENT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-advanced-enrichment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AdvancedEnrichmentBean> advancedEnrichmentForWebex(
			@RequestBody AdvancedEnrichmentBean advancedEnrichment) throws TclCommonException {
		
		return new ResponseResource<AdvancedEnrichmentBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.advancedEnrichmentForWebex(advancedEnrichment), Status.SUCCESS);
	}
	
	/***Provide WBS/GLCC***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.WBSGLCC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ucwb-provide-wbsglcc-details")
	public ResponseResource<ProvideWbsglccWebexDetailBean> provideWbsglccDetails(
			@RequestBody ProvideWbsglccWebexDetailBean provideWbsglccWebexDetailBean) throws TclCommonException {
		return new ResponseResource<ProvideWbsglccWebexDetailBean>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS,
				webexImplementationService.provideWbsglccDetails(provideWbsglccWebexDetailBean, "license"), Status.SUCCESS);
	}

	/***Create Supply PR for License***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.PROVIDE_PR_FOR_WEBEX)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePrForWebexBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-pr-licence", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePrForWebexBean> providePrForWebex(@RequestBody ProvidePrForWebexBean prForWebex)
			throws TclCommonException {
		return new ResponseResource<ProvidePrForWebexBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.providePrForWebex(prForWebex), Status.SUCCESS);
	}

	/***Create Supply PO for License Delivery***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.PROVIDE_PO_FOR_WEBEX)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoForWebexBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-po-licence", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePoForWebexBean> providePoForWebex(@RequestBody ProvidePoForWebexBean poForWebex)
			throws TclCommonException {
		return new ResponseResource<ProvidePoForWebexBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.providePoForWebex(poForWebex), Status.SUCCESS);
	}

	/***Release PO for License and Order in CCW***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.PROVIDE_PO_REALEASE_FOR_WEBEX)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoReleaseForWebexBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-po-licence-release", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePoReleaseForWebexBean> providePoReleaseForWebex(
			@RequestBody ProvidePoReleaseForWebexBean poReleaseForWebex) throws TclCommonException {

		return new ResponseResource<ProvidePoReleaseForWebexBean>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, webexImplementationService.providePoReleaseForWebex(poReleaseForWebex),
				Status.SUCCESS);
	}
	
	/***Activate Microsite***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.ACTIVATE_MICROSITE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ActivateMicrositeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-activate-microsite", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ActivateMicrositeBean> activateMicrosite(
			                                @RequestBody ActivateMicrositeBean activateMicrositeBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.activateMicrosite(activateMicrositeBean), Status.SUCCESS);
	}
	
	/***TD Creation for Dedicated Numbers***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.TD_CREATION_DEDICATED_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TdCreationDedicationNumberBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-td-creation-dedicated-numbers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TdCreationDedicationNumberBean> tdCreationDedicatedNumbers(
			                                @RequestBody TdCreationDedicationNumberBean tdCreationDedicationNumberBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.tdCreationDedicatedNumbers(tdCreationDedicationNumberBean), Status.SUCCESS);
	}
	
	/***TD Creation for Shared Numbers***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.TD_CREATION_SHARED_NUMBERS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TdCreationDedicationNumberBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-td-creation-shared-numbers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TdCreationDedicationNumberBean> tdCreationSharedNumbers(
			                                @RequestBody TdCreationDedicationNumberBean tdCreationDedicationNumberBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.tdCreationDedicatedNumbers(tdCreationDedicationNumberBean), Status.SUCCESS);
	}
	
	/***Creation of Callback Groups***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.CREATION_CALLBACK_GROUPS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CreationCallbackGroupsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-creation-callback-groups", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreationCallbackGroupsBean> creationCallbackGroups(
			                                @RequestBody CreationCallbackGroupsBean creationCallbackGroupsBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.creationCallbackGroups(creationCallbackGroupsBean), Status.SUCCESS);
	}
	
	/***Egress Routing Profile***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.EGRESS_ROUTING_PROFILE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CreationCallbackGroupsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-egress-routing-profile", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<EgressRoutingProfileBean> egressRoutingProfile(
			                                @RequestBody EgressRoutingProfileBean egressRoutingProfileBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.egressRoutingProfile(egressRoutingProfileBean), Status.SUCCESS);
	}
	
	/*** Update CUG numbers in CCA Portal ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.CUSTOMER_ON_BOARDING_FOR_TRANNING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerOnBoardTranningBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-update-cug-numbers-cca", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CustomerOnBoardTranningBean> updateCUGNumberInCCA(@RequestBody UpdateCugInCCABean updateCugInCCABean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.updateCUGNumberInCCA(updateCugInCCABean), Status.SUCCESS);
	}

	/***Activate voice to Microsite***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.ACTIVATE_VOICE_TO_MICROSITE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ActivateVoiceMicrositeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-activate-voice-microsite", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ActivateVoiceMicrositeBean> activateMicrosite(
			                                @RequestBody ActivateVoiceMicrositeBean activateVoiceMicrositeBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.activeVoiceToMicrosite(activateVoiceMicrositeBean), Status.SUCCESS);
	}
	
	/***Hybrid services integration with webex***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.HYBRID_SERVICES_INTEGRATION_WITH_WEBEX)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ActivateVoiceMicrositeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-hybrid-services-integration", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<HybridServiceImplementationBean> hybridServicesIntegrationWithWebex(
			                                @RequestBody HybridServiceImplementationBean hybridServiceImplementationBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.hybridServiceImplementation(hybridServiceImplementationBean), Status.SUCCESS);
	}
	
	/***Component Testing***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.COMPONENT_TESTING)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-component-testing", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ComponentTestingBean> componentTesting(@RequestBody ComponentTestingBean componentTestingBean)
			throws TclCommonException {
		return new ResponseResource<ComponentTestingBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.componentTesting(componentTestingBean), Status.SUCCESS);
	}

	/***E2E Service Testing***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.SERVICE_TESTING)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-e2e-service-testing", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ServiceTestingBean> serviceTesting(@RequestBody ServiceTestingBean serviceTestingBean)
			throws TclCommonException {
		return new ResponseResource<ServiceTestingBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.serviceTesting(serviceTestingBean), Status.SUCCESS);
	}
	
	/*** Customer On boarding for Training ***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.GscImplementation.CUSTOMER_ON_BOARDING_FOR_TRANNING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerOnBoardTranningBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-customer-on-boarding-training", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CustomerOnBoardTranningBean> customerOnBoardTranning(@RequestBody CustomerOnBoardTranningBean customerOnBoardTranningBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.customerOnBoardTranning(customerOnBoardTranningBean), Status.SUCCESS);
	}

	/* Endpoint Implementation Starts here */
	
	/***Provide WBS/GLCC***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.WBSGLCC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ucwb-provide-wbsglcc-details-endpoint")
	public ResponseResource<ProvideWbsglccWebexDetailBean> provideWbsglccDetailsEndpoint(
			@RequestBody ProvideWbsglccWebexDetailBean provideWbsglccWebexDetailBean) throws TclCommonException {
		return new ResponseResource<ProvideWbsglccWebexDetailBean>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS,
				webexImplementationService.provideWbsglccDetails(provideWbsglccWebexDetailBean, "endpoint"), Status.SUCCESS);
	}
	
	/***Prepare PR for EndPoint Delivery***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.PROVIDE_PR_FOR_WEBEX)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePrForWebexBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-pr-endpoint", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePrForWebexEndpointBean> providePrForWebexEndpoint(
			@RequestBody ProvidePrForWebexEndpointBean prForWebexEndpoint) throws TclCommonException {
		return new ResponseResource<ProvidePrForWebexEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.providePrForWebexEndpoint(prForWebexEndpoint), Status.SUCCESS);
	}

	/***Create Supply PO for End Point Delivery***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.PROVIDE_PO_FOR_WEBEX)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoForWebexBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-po-endpoint", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePoForWebexEndpointBean> providePoForWebexEndpoint(
			@RequestBody ProvidePoForWebexEndpointBean poForWebexEndpoint) throws TclCommonException {
		return new ResponseResource<ProvidePoForWebexEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.providePoForWebexEndpoint(poForWebexEndpoint), Status.SUCCESS);
	}

	/***Release Supply PO for End Point Delivery***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.PROVIDE_PO_REALEASE_FOR_WEBEX)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoReleaseForWebexBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-po-endpoint-release", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePoReleaseForWebexEndpointBean> providePoReleaseForWebexEndpoint(
			@RequestBody ProvidePoReleaseForWebexEndpointBean poReleaseForWebexEndpoint) throws TclCommonException {
		return new ResponseResource<ProvidePoReleaseForWebexEndpointBean>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS,
				webexImplementationService.providePoReleaseForWebexEndpoint(poReleaseForWebexEndpoint), Status.SUCCESS);
	}
	
	/***Prepare PR for Endpoint Installation and Support***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.PREPARE_PR_FOR_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-pr-endpoint-install-support", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PreparePrForEndpointBean> preparePrForEndpointInstall(
			@RequestBody PreparePrForEndpointBean prForEndpoint) throws TclCommonException {
		return new ResponseResource<PreparePrForEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.preparePrForEndpointInstall(prForEndpoint), Status.SUCCESS);
	}

	/***Create PO for Endpoint Installation and Support***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.CREATE_PO_FOR_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-po-endpoint-install-support", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreatePoForEndpointBean> createPoForEndpointInstall(
			@RequestBody CreatePoForEndpointBean poForEndpoint) throws TclCommonException {
		return new ResponseResource<CreatePoForEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.createPoForEndpointInstall(poForEndpoint), Status.SUCCESS);
	}

	/***Release PO for End Point Installation and Support***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.RELEASE_PO_FOR_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-po-endpoint-release-install-support", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ReleasePoForEndpointBean> releasePoForEndpointInstall(
			@RequestBody ReleasePoForEndpointBean poForEndpointBean) throws TclCommonException {
		return new ResponseResource<ReleasePoForEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.releasePoForEndpointInstall(poForEndpointBean), Status.SUCCESS);
	}
	
	/***Confirm Material Availability Task***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.CONFIRM_MATERIAL_AVAILABILITY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-confirm-material-availability", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfirmMaterialAvailabilityBean> confirmMaterialAvailability(
			@RequestBody ConfirmMaterialAvailabilityBean materialAvailability) throws TclCommonException {
		return new ResponseResource<ConfirmMaterialAvailabilityBean>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS,
				webexImplementationService.confirmMaterialAvailability(materialAvailability), Status.SUCCESS);
	}

	/***Dispatch Endpoint***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.DISPATCH_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-dispatch-endpoint", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DispatchEndpointBean> dispatchEndpoint(
			@RequestBody DispatchEndpointBean dispatchEndpointBean) throws TclCommonException {
		return new ResponseResource<DispatchEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.dispatchEndpoint(dispatchEndpointBean), Status.SUCCESS);
	}

	/***Track EndPoint***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.TRACK_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-track-endpoint-delivery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TrackEndpointBean> trackEndpoint(@RequestBody TrackEndpointBean trackEndpointBean)
			throws TclCommonException {
		return new ResponseResource<TrackEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.trackEndpoint(trackEndpointBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.ADFS_SSO)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-adfs-sso-integration", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AdfsSsoIntegrationBean> adfsSsoIntegration(@RequestBody AdfsSsoIntegrationBean adfsSsoIntegrationBean)
			throws TclCommonException {
		return new ResponseResource<AdfsSsoIntegrationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.adfsSsoIntegration(adfsSsoIntegrationBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.PROVIDE_AD)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-provide-ad-file", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvideAdFileBean> provideAdFile(@RequestBody ProvideAdFileBean provideAdFileBean)
			throws TclCommonException {
		return new ResponseResource<ProvideAdFileBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.provideAdFile(provideAdFileBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.CUSTOMER_ADOPTION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-customer-adoption-training", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CustomerAdoptionBean> customerAdoption(@RequestBody CustomerAdoptionBean customerAdoptionBean)
			throws TclCommonException {
		return new ResponseResource<CustomerAdoptionBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.customerAdoption(customerAdoptionBean), Status.SUCCESS);
	}
	
	/***Install End Points***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.INSTALL_ENDPOINT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ucwb-install-endpoint")
	public ResponseResource<InstallEndpointBean> installEndpoint(@RequestBody InstallEndpointBean installEndpointBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.installEndpoint(installEndpointBean), Status.SUCCESS);
	}

	/***Config End Points***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.CONFIGURE_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ucwb-configure-endpoint")
	public ResponseResource<ConfigureEndpointBean> configureEndpoint(
			@RequestBody ConfigureEndpointBean configureEndpointBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.configureEndpoint(configureEndpointBean), Status.SUCCESS);
	}

	/***Access Code Activation***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.ACCESS_CODE_ACTIVATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-access-code-activation", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AccessCodeActivationBean> accessCodeActivation(@RequestBody AccessCodeActivationBean accessCode)
			throws TclCommonException {
		return new ResponseResource<AccessCodeActivationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.accessCodeActivation(accessCode), Status.SUCCESS);
	}

	/***Configuration of Access Code in Endpoint***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.CONFIG_ACCESS_CODE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-config-access-code", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfigAccessCodeBean> configAccessCodeEndpoint(@RequestBody ConfigAccessCodeBean configAccessCode)
			throws TclCommonException {
		return new ResponseResource<ConfigAccessCodeBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.configAccessCodeEndpoint(configAccessCode), Status.SUCCESS);
	}
	
	/***SRN Generation***/
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.SRN_GENERATION_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SrnGenerationEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-srn-generation-endpoint", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SrnGenerationEndpointBean> srnGenerationEndpoint(
			@RequestBody SrnGenerationEndpointBean srnGenerationEndpointBean) throws TclCommonException {
		return new ResponseResource<SrnGenerationEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.srnGenerationEndpoint(srnGenerationEndpointBean), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.CUSTOMER_HANDOVER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-customer-handover", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CustomerHandoverBean> customerHandover(@RequestBody CustomerHandoverBean customerHandoverBean)
			throws TclCommonException {
		return new ResponseResource<CustomerHandoverBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.customerHandover(customerHandoverBean), Status.SUCCESS);
	}
	
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ucwb-component-attributes/serviceId/{serviceId}")
	public ResponseResource<List<ComponentAttributeBean>> getWebexCompAttributeDetails(@PathVariable("serviceId") Integer serviceId) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.getWebexCompAttributeDetails(serviceId), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/solution-details-services/solution/{solutionCode}")
	public ResponseResource<List<SolutionViewDetailsBean>> getSolutionDetailsServices(@PathVariable("solutionCode") String solutionCode) throws TclCommonException, IOException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, webexImplementationService.getSolutionDetailsServices(solutionCode), Status.SUCCESS);
	}
	/*
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.GENERATE_WEBEX_ENDPOINT_INVOICE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = GenerateWebexEndpointInvoiceBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ucwb-generate-endpoint-invoice", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<GenerateWebexEndpointInvoiceBean> generateWebexEndpointInvoice(
			@RequestBody GenerateWebexEndpointInvoiceBean generateWebexEndpointInvoiceBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				webexImplementationService.generateWebexEndpointInvoice(generateWebexEndpointInvoiceBean),
				Status.SUCCESS);
	}
	*/

	/*
	 * @ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.
	 * GENERATE_MRN_FOR_WEBEX_ENDPOINT_TRANSFER)
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message = Constants.SUCCESS, response =
	 * GenerateMrnforWebexEndpointTransferBean.class),
	 * 
	 * @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	 * 
	 * @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	 * 
	 * @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	 * 
	 * @PostMapping("/generate-mrn-webex-endpoint-transfer") public
	 * ResponseResource<GenerateMrnforWebexEndpointTransferBean>
	 * generateMrnforWebexEndpointTransfer(
	 * 
	 * @RequestBody GenerateMrnforWebexEndpointTransferBean
	 * generateMrnforWebexEndpointTransferBean) throws TclCommonException { return
	 * new ResponseResource<>(ResponseResource.R_CODE_OK,
	 * ResponseResource.RES_SUCCESS,
	 * webexImplementationService.generateMrnforWebexEndpointTransfer(
	 * generateMrnforWebexEndpointTransferBean), Status.SUCCESS); }
	 */
}
