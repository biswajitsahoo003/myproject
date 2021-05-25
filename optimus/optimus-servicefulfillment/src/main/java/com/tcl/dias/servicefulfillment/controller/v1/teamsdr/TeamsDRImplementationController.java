package com.tcl.dias.servicefulfillment.controller.v1.teamsdr;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.ConfigureCpeBean;
import com.tcl.dias.servicefulfillment.beans.gsc.AdvancedEnrichmentBean;
import com.tcl.dias.servicefulfillment.beans.teamsdr.*;
import com.tcl.dias.servicefulfillment.service.teamsdr.TeamsDRImplementationService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.TeamsDRHandoverPdfService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Controller to handle teams dr service fulfillment tasks
 *
 * @author Srinivasa Raghavan
 */
@RestController
@RequestMapping("v1/teamsdr-implementation/task")
public class TeamsDRImplementationController {

	@Autowired
	private TeamsDRImplementationService teamsDRImplementationService;

	@Autowired
	TeamsDRHandoverPdfService teamsDRHandoverPdfService;

	/**
	 * API to save WBSGLCC details for endpoint
	 *
	 * @param provideWbsglccTeamsDRDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.WBSGLCC)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureCpeBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-provide-wbsglcc-details-endpoint")
	public ResponseResource<ProvideWbsglccTeamsDRDetailBean> provideWbsglccDetailsEndpoint(
			@RequestBody ProvideWbsglccTeamsDRDetailBean provideWbsglccTeamsDRDetailBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.provideWbsglccDetails(provideWbsglccTeamsDRDetailBean, "endpoint"),
				Status.SUCCESS);
	}

	/**
	 * Provide PR detals for teamsDR media gateways
	 *
	 * @param prForTeamsDRMediaGatewayBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.PROVIDE_PR_FOR_MEDIA_GATEWAY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePrForTeamsDREndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-pr-endpoint", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePrForTeamsDREndpointBean> providePrForTeamsDRMediaGateway(
			@RequestBody ProvidePrForTeamsDREndpointBean prForTeamsDRMediaGatewayBean) throws TclCommonException {
		return new ResponseResource<ProvidePrForTeamsDREndpointBean>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.providePrForTeamsDRMediaGateway(prForTeamsDRMediaGatewayBean),
				Status.SUCCESS);
	}

	/**
	 * Provide PR for media gateway install support
	 *
	 * @param prForEndpoint
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.PREPARE_PR_FOR_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-pr-endpoint-install-support", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<PreparePrForTeamsDREndpointBean> preparePrForEndpointInstall(
			@RequestBody PreparePrForTeamsDREndpointBean prForEndpoint) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.preparePrForMediaGatewayInstall(prForEndpoint), Status.SUCCESS);
	}

	/**
	 * Provide PO for Teams Media Gateway
	 *
	 * @param poForTeamsDRBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.PROVIDE_PO_FOR_MEDIA_GATEWAY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoForTeamsDREndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-po-endpoint", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePoForTeamsDREndpointBean> providePoForMediaGateway(
			@RequestBody ProvidePoForTeamsDREndpointBean poForTeamsDRBean) throws TclCommonException {
		return new ResponseResource<ProvidePoForTeamsDREndpointBean>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, teamsDRImplementationService.providePoForMediaGateway(poForTeamsDRBean),
				Status.SUCCESS);
	}

	/**
	 * Create PO for media gateway installation
	 *
	 * @param poForEndpoint
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.CREATE_PO_FOR_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-po-endpoint-install-support", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CreatePoForEndpointBean> createPoForEndpointInstall(
			@RequestBody CreatePoForEndpointBean poForEndpoint) throws TclCommonException {
		return new ResponseResource<CreatePoForEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.createPoForEndpointInstall(poForEndpoint), Status.SUCCESS);
	}

	/**
	 * Provide PO release for TeamsDR Media Gateway
	 *
	 * @param poReleaseForTeamsDREndpoint
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.PROVIDE_PO_RELEASE_FOR_MEDIA_GATEWAY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoReleaseForTeamsDREndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-po-endpoint-release", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ProvidePoReleaseForTeamsDREndpointBean> providePoReleaseForMediaGateway(
			@RequestBody ProvidePoReleaseForTeamsDREndpointBean poReleaseForTeamsDREndpoint) throws TclCommonException {
		return new ResponseResource<ProvidePoReleaseForTeamsDREndpointBean>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.providePoReleaseForMediaGateway(poReleaseForTeamsDREndpoint),
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.RELEASE_PO_FOR_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-po-endpoint-release-install-support", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ReleasePoForEndpointBean> releasePoForEndpointInstall(
			@RequestBody ReleasePoForEndpointBean poForEndpointBean) throws TclCommonException {
		return new ResponseResource<ReleasePoForEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.releasePoForEndpointInstall(poForEndpointBean), Status.SUCCESS);
	}

	/**
	 * To confirm material availability for media gateway
	 *
	 * @param materialAvailability
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.CONFIRM_MATERIAL_AVAILABILITY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-confirm-material-availability", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<ConfirmMaterialAvailabilityBean> confirmMaterialAvailability(
			@RequestBody ConfirmMaterialAvailabilityBean materialAvailability) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.confirmMaterialAvailability(materialAvailability), Status.SUCCESS);
	}

	/**
	 * To dispatch endpoint for teamsdr.
	 * 
	 * @param dispatchEndpointBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.DISPATCH_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-dispatch-endpoint", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<DispatchEndpointBean> dispatchEndpoint(
			@RequestBody DispatchEndpointBean dispatchEndpointBean) throws TclCommonException {
		return new ResponseResource<DispatchEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.dispatchEndpoint(dispatchEndpointBean), Status.SUCCESS);
	}

	/**
	 * Method to track endpoint for teamsdr.
	 * 
	 * @param trackEndpointBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.TRACK_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-track-endpoint-delivery", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<TrackEndpointBean> trackEndpoint(@RequestBody TrackEndpointBean trackEndpointBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.trackEndpoint(trackEndpointBean), Status.SUCCESS);
	}

	/**
	 * Method to install teamsdr endpoint..
	 * 
	 * @param teamsDRCommonRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.INSTALL_ENDPOINT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-install-endpoint")
	public ResponseResource<TeamsDRCommonRequestBean> installEndpoint(
			@RequestBody TeamsDRCommonRequestBean teamsDRCommonRequestBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.installEndpoint(teamsDRCommonRequestBean), Status.SUCCESS);
	}

	/**
	 * To save advanced enrichment attributes of media gateway
	 *
	 * @param advancedEnrichmentMgBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.ADVANCED_ENRICHMENT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-advanced-enrichment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<AdvancedEnrichmentMediaGatewayBean> advancedEnrichmentForMediaGateway(
			@RequestBody AdvancedEnrichmentMediaGatewayBean advancedEnrichmentMgBean) throws TclCommonException {
		return new ResponseResource<AdvancedEnrichmentMediaGatewayBean>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.advancedEnrichmentForMediaGateway(advancedEnrichmentMgBean),
				Status.SUCCESS);
	}

	/**
	 * SRN Generation for endpoint
	 *
	 * @param srnGenerationEndpointBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.SRN_GENERATION_ENDPOINT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SrnGenerationEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-srn-generation-endpoint", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<SrnGenerationEndpointBean> srnGenerationEndpoint(
			@RequestBody SrnGenerationEndpointBean srnGenerationEndpointBean) throws TclCommonException {
		return new ResponseResource<SrnGenerationEndpointBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.srnGenerationEndpoint(srnGenerationEndpointBean), Status.SUCCESS);
	}

	/**
	 * Customer handover media gateway
	 *
	 * @param customerHandoverBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.WebExImplementation.CUSTOMER_HANDOVER)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdvancedEnrichmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/teamsdr-customer-handover", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseResource<CustomerHandoverBean> customerHandoverMediaGateway(
			@RequestBody CustomerHandoverBean customerHandoverBean) throws TclCommonException {
		return new ResponseResource<CustomerHandoverBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.customerHandoverMediaGateway(customerHandoverBean), Status.SUCCESS);
	}

	/**
	 * Method to get required documents and configure endpoint.
	 *
	 * @param teamsDRCommonRequestBean
	 * @return
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.INSTALL_ENDPOINT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-configure-mg")
	public ResponseResource<TeamsDRCommonRequestBean> configureEndpoint(
			@RequestBody TeamsDRCommonRequestBean teamsDRCommonRequestBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveDocsAndConfigureEndpoint(teamsDRCommonRequestBean), Status.SUCCESS);
	}

	/**
	 * Method to save gsmc firewall details.
	 * 
	 * @param teamsDRCommonRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.INSTALL_ENDPOINT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-open-firewall-mg")
	public ResponseResource<TeamsDRCommonRequestBean> openFirewallMg(
			@RequestBody TeamsDRCommonRequestBean teamsDRCommonRequestBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveGsmcFirewallDetails(teamsDRCommonRequestBean), Status.SUCCESS);
	}

	/**
	 * Method to save gsmc firewall details.
	 * 
	 * @param teamsDRCommonRequestBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.INSTALL_ENDPOINT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-perform-uat-for-mg")
	public ResponseResource<TeamsDRCommonRequestBean> performUatForMg(
			@RequestBody TeamsDRCommonRequestBean teamsDRCommonRequestBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveUatForMgDetails(teamsDRCommonRequestBean), Status.SUCCESS);
	}

	/**
	 * Close advanced enrichment task
	 *
	 * @param teamsDRMSAdvanceEnrichmentBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.MS_ADVANCE_ENRICHMENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-ms-advance-enrichment")
	public ResponseResource<TeamsDRMSAdvanceEnrichmentBean> managedServicesAdvanceEnrichment(
			@RequestBody TeamsDRMSAdvanceEnrichmentBean teamsDRMSAdvanceEnrichmentBean, @RequestParam("action") String action) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveMSAdvanceEnrichmentDetails(teamsDRMSAdvanceEnrichmentBean, action),
				Status.SUCCESS);
	}
	
	/**
	 * Perform UAT Testing - Managed Service
	 *
	 * @param teamsDRMSAdvanceEnrichmentBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.MS_UAT_TESTING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-ms-uat-testing")
	public ResponseResource<TeamsDRUatTestingBean> managedServicesUatTesting(
			@RequestBody TeamsDRUatTestingBean teamsDRMSAdvanceEnrichmentBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveMSUATTestingDetails(teamsDRMSAdvanceEnrichmentBean),
				Status.SUCCESS);
	}

	/**
	 * Method to save user mapping details and create batches
	 *
	 * @param teamsDRUserMappingBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.MS_USER_MAPPING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallEndpointBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-user-mapping")
	public ResponseResource<TeamsDRUserMappingBean> saveUserMapping(
			@RequestBody TeamsDRUserMappingBean teamsDRUserMappingBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveUserMapping(teamsDRUserMappingBean), Status.SUCCESS);
	}

	/**
	 * Method to save direct routing details
	 *
	 * @param teamsDRMSDirectRoutingBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.MS_USER_MAPPING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRMSDirectRoutingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-ms-direct-routing")
	public ResponseResource<TeamsDRMSDirectRoutingBean> saveMsDirectRouting(
			@RequestBody TeamsDRMSDirectRoutingBean teamsDRMSDirectRoutingBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveMSDirectRoutingDetails(teamsDRMSDirectRoutingBean), Status.SUCCESS);
	}

	/**
	 * Method to end user training details
	 *
	 * @param teamsDRTrainingBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.MS_END_USER_TRAINING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRTrainingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-ms-training-details")
	public ResponseResource<TeamsDRTrainingBean> saveMsTrainingDetails(
			@RequestBody TeamsDRTrainingBean teamsDRTrainingBean,@RequestParam("type") String type) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveTrainingDetails(teamsDRTrainingBean,type), Status.SUCCESS);
	}

	/**
	 * Method to save management and monitoring task details
	 *
	 * @param teamsDRManagementAndMonitoringBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.MS_END_USER_TRAINING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRManagementAndMonitoringBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-ms-management-and-monitoring")
	public ResponseResource<TeamsDRManagementAndMonitoringBean> saveMsManagementAndMonitoring(
			@RequestBody TeamsDRManagementAndMonitoringBean teamsDRManagementAndMonitoringBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveMSManagementAndMonitoring(teamsDRManagementAndMonitoringBean), Status.SUCCESS);
	}

	/**
	 * Method to test handover note pdf
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.MS_END_USER_TRAINING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRManagementAndMonitoringBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-handover")
	public ResponseResource<String> teamsdrHandover(@RequestParam("serviceId") Integer serviceId,
													@RequestParam("serviceCode") String serviceCode,
													@RequestParam("scComponentId") Integer scComponentId,
													@RequestParam("batchId")Integer batchId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRHandoverPdfService.processHandoverNotePdf(serviceCode,serviceId,scComponentId,batchId), Status.SUCCESS);
	}

	/**
	 * Method to save management and monitoring task details
	 *
	 * @param taskDetailsBaseBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.MS_END_USER_TRAINING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRManagementAndMonitoringBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-service-assurance")
	public ResponseResource<TaskDetailsBaseBean> saveTeamsDRServiceAssurance(
			@RequestBody TaskDetailsBaseBean taskDetailsBaseBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveTeamsDRServiceAssurance(taskDetailsBaseBean), Status.SUCCESS);
	}

	/**
	 * Method to save management and monitoring task details
	 *
	 * @param taskDetailsBaseBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.TeamsDRImplementation.MS_END_USER_TRAINING)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TeamsDRManagementAndMonitoringBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/teamsdr-service-acceptance")
	public ResponseResource<TaskDetailsBaseBean> saveTeamsDRCustomerHandover(
			@RequestBody TaskDetailsBaseBean taskDetailsBaseBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				teamsDRImplementationService.saveTeamsDRCustomerHandover(taskDetailsBaseBean), Status.SUCCESS);
	}
}
