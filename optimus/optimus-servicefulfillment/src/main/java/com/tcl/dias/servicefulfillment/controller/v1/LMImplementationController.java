package com.tcl.dias.servicefulfillment.controller.v1;

import java.io.IOException;
import java.util.List;
import java.util.Set;


import com.tcl.dias.servicefulfillment.beans.SdwanAdditionalTechnicalDetails;
import com.tcl.dias.servicefulfillment.beans.TerminateDateBean;
import com.tcl.dias.servicefulfillment.beans.UpdateDependencyRemarksBean;
import com.tcl.dias.servicefulfillmentutils.beans.LLDAndMigrationBean;
import com.tcl.dias.servicefulfillmentutils.beans.LLDPreparationBean;
import com.tcl.dias.servicefulfillmentutils.beans.SdwanOrderDetailsBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.AdditionalTechnicalDetailsBean;
import com.tcl.dias.servicefulfillment.beans.BuildingAuthorityContractRequest;
import com.tcl.dias.servicefulfillment.beans.CapexBean;
import com.tcl.dias.servicefulfillment.beans.CompleteAcceptanceTestingBean;
import com.tcl.dias.servicefulfillment.beans.CompleteOspAcceptanceBean;
import com.tcl.dias.servicefulfillment.beans.ConductCrossConnectSiteSurveyBean;
import com.tcl.dias.servicefulfillment.beans.ConductSiteSurveyBean;
import com.tcl.dias.servicefulfillment.beans.ConductSiteSurveyManBean;
import com.tcl.dias.servicefulfillment.beans.ConfigureMuxBean;
import com.tcl.dias.servicefulfillment.beans.ConfirmAccessRingBean;
import com.tcl.dias.servicefulfillment.beans.ConfirmLmAcceptanceBean;
import com.tcl.dias.servicefulfillment.beans.CreateInventoryRecord;
import com.tcl.dias.servicefulfillment.beans.DefineScopeWorkProjectPlanBean;
import com.tcl.dias.servicefulfillment.beans.DefineScopeWorkProjectPlanBeanList;
import com.tcl.dias.servicefulfillment.beans.DeliverMuxBean;
import com.tcl.dias.servicefulfillment.beans.DemarcDetailsBean;
import com.tcl.dias.servicefulfillment.beans.IBDBean;
import com.tcl.dias.servicefulfillment.beans.InstallKroneBean;
import com.tcl.dias.servicefulfillment.beans.InstallMuxBean;
import com.tcl.dias.servicefulfillment.beans.IntegrateMuxBean;
import com.tcl.dias.servicefulfillment.beans.InternalCablingCompletionStatus;
import com.tcl.dias.servicefulfillment.beans.InternalCablingDetails;
import com.tcl.dias.servicefulfillment.beans.InternalCablingRequest;
import com.tcl.dias.servicefulfillment.beans.IzopcAdditionalTechnicalDetailsBean;
import com.tcl.dias.servicefulfillment.beans.LocationUploadValidationBean;
import com.tcl.dias.servicefulfillment.beans.MastInstallationPermissionBean;
import com.tcl.dias.servicefulfillment.beans.MastInstallationPlanBean;
import com.tcl.dias.servicefulfillment.beans.MrnForMuxBean;
import com.tcl.dias.servicefulfillment.beans.MrnOspMuxRequest;
import com.tcl.dias.servicefulfillment.beans.NetworkAugmentation;
import com.tcl.dias.servicefulfillment.beans.NetworkInventoryBean;
import com.tcl.dias.servicefulfillment.beans.OSPBean;
import com.tcl.dias.servicefulfillment.beans.PRowRequest;
import com.tcl.dias.servicefulfillment.beans.PaymentBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePoBuildingAuthorityBean;
import com.tcl.dias.servicefulfillment.beans.ProvideRfDataJeopardyBean;
import com.tcl.dias.servicefulfillment.beans.ProwCostApproval;
import com.tcl.dias.servicefulfillment.beans.RaiseDependencyBean;
import com.tcl.dias.servicefulfillment.beans.RowBeanRequest;
import com.tcl.dias.servicefulfillment.beans.SdwaCloudGatewayBean;
import com.tcl.dias.servicefulfillment.beans.SiteReadinessDetailBean;
import com.tcl.dias.servicefulfillment.beans.SiteSurveyRescheduleRequest;
import com.tcl.dias.servicefulfillment.beans.VendorDetailsBean;
import com.tcl.dias.servicefulfillment.service.LmService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeResponse;
import com.tcl.dias.servicefulfillmentutils.beans.AssistCmipBean;
import com.tcl.dias.servicefulfillmentutils.beans.ByonReadinessBean;
import com.tcl.dias.servicefulfillmentutils.beans.CustomerAppointmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.FieldEngineerDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.MuxDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.PlannedEventBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceLevelUpdateBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * LMImplementationController this class is used to perform last mile
 * implementation related tasks
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/lm-implementation/task")
public class LMImplementationController {

	@Autowired
	LmService lmService;	

    /**
     * @param taskId
     * @param additionalTechnicalDetailsBean
     * @return AdditionalTechnicalDetailsBean
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_ADDITIONAL_TECHNICAL_DETAILS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdditionalTechnicalDetailsBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/additional-technical-details")
    public ResponseResource<AdditionalTechnicalDetailsBean> saveAdditionalTechnicalDetails(@RequestBody AdditionalTechnicalDetailsBean additionalTechnicalDetailsBean) throws TclCommonException {
        return new ResponseResource<AdditionalTechnicalDetailsBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
        		lmService.saveAdditionalTechnicalDetails(additionalTechnicalDetailsBean),
                Status.SUCCESS);
    }
    
    /**
	 * @param taskId
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_ACE_IPS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Set.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/additional-technical-details/aceip/task/{taskId}/wftask/{wfTaskId}")
	public ResponseResource<Set<LocationUploadValidationBean>> saveProcessAceIps(@PathVariable("taskId") Integer taskId,
			@PathVariable("wfTaskId") String wfTaskId,
			@RequestParam(name = "file", value = "file", required = false) MultipartFile file)
			throws TclCommonException {
		return new ResponseResource<Set<LocationUploadValidationBean>>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, lmService.processAceIps(taskId,wfTaskId, file),Status.SUCCESS);
	}
	
	
	 
	/**This API gets the list of ace ips by service id
	 * @param taskId
	 * @param file
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.GET_ACE_IPS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = List.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping(value = "/additional-technical-details/aceip/task/{taskId}/wftask/{wfTaskId}")
	public ResponseResource<List<String>> getIps(@PathVariable("taskId") Integer taskId,
			@PathVariable("wfTaskId") String wfTaskId) throws TclCommonException {
		return new ResponseResource<List<String>>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.getAceIps(taskId,wfTaskId), Status.SUCCESS);
	}
    

	/**
	 * Check for site readiness confirmation.
	 *
	 * @param taskId
	 * @param siteReadinessDetailBean
	 * @return SiteReadinessDetailBean
	 * @throws TclCommonException
	 * @author Vishesh Awasthi
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_SITE_READINESS_CONFIRMATION_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteReadinessDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/site-readiness-confirmation")
	public ResponseResource<SiteReadinessDetailBean> siteReadinessConfirmation(
			@RequestBody SiteReadinessDetailBean siteReadinessDetailBean) throws TclCommonException {

		return new ResponseResource<SiteReadinessDetailBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.siteReadinessConfirmation(siteReadinessDetailBean), Status.SUCCESS);
	}

	/**
	 * This method is used to Save MUX Details.
	 *
	 * @param taskId
	 * @param muxDetailBean
	 * @return MuxDetailBean
	 * @throws TclCommonException
	 * @author diksha garg
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_MUX_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MuxDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mux-details")
	public ResponseResource<MuxDetailBean> saveMuxDetails(@RequestBody MuxDetailBean muxDetailBean) throws TclCommonException {
		return new ResponseResource<MuxDetailBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveMuxDetails(muxDetailBean), Status.SUCCESS);
	}

	/**
	 * This method is used to Save field Engineer Details.
	 *
	 * @param taskId
	 * @param fieldEngineerDetailsBean
	 * @return FieldEngineerDetailsBean
	 * @throws TclCommonException
	 * @author diksha garg
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_FIELD_ENGINEER_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = FieldEngineerDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/field-engineer-arrangements")
	public ResponseResource<FieldEngineerDetailsBean> saveFieldEngineerDetails(
			@RequestBody FieldEngineerDetailsBean fieldEngineerDetailsBean) throws TclCommonException {
		return new ResponseResource<FieldEngineerDetailsBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveFieldEngineerDetails(fieldEngineerDetailsBean), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_FIELD_ENGINEER_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = FieldEngineerDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/offnet-fe-arrangements")
	public ResponseResource<FieldEngineerDetailsBean> saveOffnetFieldEngineerDetails(
			@RequestBody FieldEngineerDetailsBean fieldEngineerDetailsBean) throws TclCommonException {
		return new ResponseResource<FieldEngineerDetailsBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveOffnetFieldEngineerDetails(fieldEngineerDetailsBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_VENDOR_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = VendorDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/select-vendors")
	public ResponseResource<VendorDetailsBean> saveVendorDetails(
			@RequestBody VendorDetailsBean vendorDetailsBean) throws TclCommonException {
		return new ResponseResource<VendorDetailsBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveVendorDetails(vendorDetailsBean), Status.SUCCESS);
	}

	/**
	 * This method is used to schedule Customer Appointment
	 * <p>
	 * 
	 * @author Mayank S
	 * @param taskId
	 * @param customerAppointmentBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SCHEDULE_CUSTOMER_APPOINTMENT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerAppointmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/customer-appointments")
	public ResponseResource<CustomerAppointmentBean> scheduleCustomerAppointment(
			@RequestBody CustomerAppointmentBean customerAppointmentBean)
			throws TclCommonException {
		CustomerAppointmentBean response = (CustomerAppointmentBean) lmService.scheduleCustomerAppointment(
				customerAppointmentBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SCHEDULE_CUSTOMER_APPOINTMENT)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerAppointmentBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/sdwan/customer-appointments")
	public ResponseResource<CustomerAppointmentBean> scheduleSdwanCustomerAppointment(
			@RequestBody CustomerAppointmentBean customerAppointmentBean)
			throws TclCommonException {
		CustomerAppointmentBean response = (CustomerAppointmentBean) lmService.scheduleSdwanCustomerAppointment(
				customerAppointmentBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to Create MRN for Mux
	 *
	 * @param taskId
	 * @param mrnForMuxBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg createMrnForMux
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CREATE_MRN_FOR_MUX)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MrnForMuxBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mux-mrns")
	public ResponseResource<MrnForMuxBean> createMrnForMux(
			@RequestBody MrnForMuxBean mrnForMuxBean) throws TclCommonException {
		MrnForMuxBean response = lmService.createMnrForMux(mrnForMuxBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to Release MRN for OSP/IBD Material
	 *
	 * @param taskId
	 * @param mrnForMuxBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg releaseMrnForOspIbdMaterial
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.RELEASE_MRN_FOR_OSP_IBD_MATERIAL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MrnOspMuxRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/mrn-osp-ibd-materials")
	public ResponseResource<MrnOspMuxRequest> releaseMrnForOspIbdMaterial(
			@RequestBody MrnOspMuxRequest mrnOspMuxRequest) throws TclCommonException {
		MrnOspMuxRequest response = lmService.releaseMrnForOspIbdMaterial(mrnOspMuxRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to Install Mux
	 *
	 * @param taskId
	 * @param installMuxBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg installMux
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.INSTALL_MUX)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallMuxBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/install-mux")
	public ResponseResource<InstallMuxBean> installMux(
			@RequestBody InstallMuxBean installMuxBean) throws TclCommonException {
		InstallMuxBean response = lmService.installMux(installMuxBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to Define Scope of Work & Project Plan
	 *
	 * @param taskId
	 * @param defineScopeWorkProjectPlanBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg defineScopeWorkProjectPlan
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.DEFINE_SCOPE_WORK_PROJECT_PLAN)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = DefineScopeWorkProjectPlanBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ss-project-plan")
	public ResponseResource<DefineScopeWorkProjectPlanBeanList> defineScopeWorkProjectPlan(
			@RequestBody DefineScopeWorkProjectPlanBeanList defineScopeWorkProjectPlanBeanList)
			throws TclCommonException {
		DefineScopeWorkProjectPlanBeanList response = lmService.defineScopeWorkProjectPlan(
				defineScopeWorkProjectPlanBeanList);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to Provide PO to Building Authority
	 *
	 * @param taskId
	 * @param providePoBuildingAuthorityBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg providePoBuildingAuthority
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.PROVIDE_PO_BUILDING_AUTHORITY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoBuildingAuthorityBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/prow-contract-pos")
	public ResponseResource<ProvidePoBuildingAuthorityBean> providePoBuildingAuthority(
			@RequestBody ProvidePoBuildingAuthorityBean providePoBuildingAuthorityBean) throws TclCommonException {
		ProvidePoBuildingAuthorityBean response = lmService.providePoBuildingAuthority(
				providePoBuildingAuthorityBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	

	/**
	 * This method is used to approve deviation in Capex
	 * <p>
	 * approveCapex
	 *
	 * @author Mayank S
	 * @param taskId
	 * @param capexBean
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.APPROVE_CAPEX)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CapexBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/approve-capex")
	public ResponseResource<CapexBean> approveCapex(@RequestBody CapexBean capexBean) throws TclCommonException {
		return new ResponseResource<CapexBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.approveCapex(capexBean), Status.SUCCESS);
	}

	/**
	 * Complete OSP Work for Service Delivery
	 * <p>
	 * completeOSPWork
	 *
	 * @author Mayank S
	 * @param taskId
	 * @param ospBean
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.COMPLETE_OSP_WORK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OSPBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/osp-works")
	public ResponseResource<OSPBean> completeOSPWork(
			@RequestBody OSPBean ospBean) throws TclCommonException {
		OSPBean response = (OSPBean) lmService.completeOSPWork(ospBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Complete IBD Work for Service Delivery
	 * <p>
	 * completeIBDWork
	 *
	 * @author Mayank S
	 * @param taskId
	 * @param ospBean
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.COMPLETE_IBD_WORK)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OSPBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ibd-works")
	public ResponseResource<IBDBean> completeIBDWork(@RequestBody IBDBean ibdBean) throws TclCommonException {
		IBDBean response = (IBDBean) lmService.completeIBDWork(ibdBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Pay to Building Authority to conduct IBD Work
	 *
	 * @param taskId
	 * @param documentIds
	 * @return
	 * @throws TclCommonException
	 * @throws IOException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.PAY_BUILDING_AUTHORITY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ResponseResource.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/pay-building-authority")
	public ResponseResource<PaymentBean> payBuildingAuthority(
			@RequestBody PaymentBean paymentBean) throws TclCommonException {
		PaymentBean response = lmService.payBuildingAuthority(paymentBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}



	/**
	 * @param taskId
	 * @param rowBean
	 * @return RowBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.BUILDING_AUTHORITY_CONTRACT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RowBeanRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/building-authority-contract")
	public ResponseResource<BuildingAuthorityContractRequest> buildingAuthorityContract(
			@RequestBody BuildingAuthorityContractRequest buContractRequest)
			throws TclCommonException {
		return new ResponseResource<BuildingAuthorityContractRequest>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS, lmService.buildingAuthorityContract(buContractRequest),
				Status.SUCCESS);
	}

	/**
	 * @param rowBean
	 * @param taskId
	 * @return RowBean
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.APPLY_PROW)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RowBeanRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/apply-for-prow")
	public ResponseResource<PRowRequest> applyForPRow(@RequestBody PRowRequest pRowRequest) throws TclCommonException {
		return new ResponseResource<PRowRequest>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.applyForPRow(pRowRequest), Status.SUCCESS);
	}

	/**
	 * @param attachmentIdBeans
	 * @param taskId
	 * @return
	 * @throws IOException
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.PAY_BUILDING_AUTHORITY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RowBeanRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/row-govt-payments")
	public ResponseResource<PaymentBean> rowGovtPayments(@RequestBody PaymentBean paymentBean) throws TclCommonException {
		return new ResponseResource<PaymentBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.rowGovtPayments(paymentBean), Status.SUCCESS);
	}

	/**
	 * This method is used to Complete IBD Acceptance Testing
	 *
	 * @param taskId
	 * @param completeAcceptanceTestingBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg completeAcceptanceTesting
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.COMPLETE_ACCEPTANCE_TESTING)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CompleteAcceptanceTestingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/ibd-acceptance")
	public ResponseResource<CompleteAcceptanceTestingBean> completeAcceptanceTesting(
			@RequestBody CompleteAcceptanceTestingBean completeAcceptanceTestingBean) throws TclCommonException {
		CompleteAcceptanceTestingBean response = lmService.completeAcceptanceTesting(
				completeAcceptanceTestingBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to Complete OSP Acceptance Testing
	 *
	 * @param taskId
	 * @param CompleteOspAcceptanceBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg completeOspAcceptanceTesting
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.COMPLETE_OSP_ACCEPTANCE_TESTING)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CompleteOspAcceptanceBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/osp-acceptance")
	public ResponseResource<CompleteOspAcceptanceBean> completeOspAcceptanceTesting(
			@RequestBody CompleteOspAcceptanceBean completeOspAcceptanceBean)
			throws TclCommonException {
		CompleteOspAcceptanceBean response = lmService.completeOspAcceptance( completeOspAcceptanceBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * @param taskId
	 * @param confirmAccessRingBean
	 * @return ConfirmAccessRingBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CONFIRM_ACCESS_RING)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfirmAccessRingBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/confirm-access-ring")
	public ResponseResource<ConfirmAccessRingBean> confirmAccessRing(
			@RequestBody ConfirmAccessRingBean confirmAccessRingBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.confirmAccessRing( confirmAccessRingBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.APPLY_FOR_ROW_PERMISSION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/apply-for-row-permission")
	public ResponseResource<RowBeanRequest> applyForRowPermission(@RequestBody RowBeanRequest rowBean
			) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.applyForRowPermission( rowBean), Status.SUCCESS);
	}
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.APPLY_FOR_ROW_PERMISSION)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = AssigneeResponse.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/lm-apply-row-exception")
	public ResponseResource<RowBeanRequest> applyForRowPermissionException(@RequestBody RowBeanRequest rowBean
			) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.applyForRowPermissionException( rowBean), Status.SUCCESS);
	}
	
	/**
	 * This method is used to Deliver Mux
	 *
	 * @param taskId
	 * @param deliverMuxBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg deliverMux
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.DELIVER_MUX)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DeliverMuxBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/deliver-mux")
	public ResponseResource<DeliverMuxBean> deliverMux(
			@RequestBody DeliverMuxBean deliverMuxBean) throws TclCommonException {
		DeliverMuxBean response = lmService.deliverMux( deliverMuxBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * This method is used to Confirm Last Mile Acceptance
	 *
	 * @param taskId
	 * @param ConfirmLmAcceptanceBean
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg confirmLmAcceptance
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CONFIRM_LM_ACCEPTANCE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfirmLmAcceptanceBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/confirm-last-mile-acceptance")
	public ResponseResource<ConfirmLmAcceptanceBean> confirmLmAcceptance(
			@RequestBody ConfirmLmAcceptanceBean confirmLmAcceptanceBean) throws TclCommonException {
		ConfirmLmAcceptanceBean response = lmService.confirmLmAcceptance( confirmLmAcceptanceBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}

	/**
	 * Create Work Order for Internal Cabling.
	 * 
	 * @author arjayapa
	 * @param taskId
	 * @param internalCablingDetails
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CREATE_WORKORDER_INTERNAL_CABLING)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = InternalCablingDetails.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/create-workorder-internal-cabling")
	public void createWorkOrderForInternalCabling(
			@RequestBody InternalCablingDetails internalCablingDetails) throws TclCommonException {

		lmService.orderForInternalCabling( internalCablingDetails);

	}

	/**
	 * Complete Internal Cabling - CE.
	 * 
	 * @author arjayapa
	 * @param taskId
	 * @param internalCablingRequest
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.COMPLETE_INTERNAL_CABLING)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = InternalCablingRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/complete-internal-cabling")
	public void completeInternalCabling(
			@RequestBody InternalCablingRequest internalCablingRequest) throws TclCommonException {

		lmService.completeInternalCabling( internalCablingRequest);

	}

	/**
	 * Complete Internal Cabling - CE.
	 * 
	 * @author arjayapa
	 * @param taskId
	 * @param completionStatus
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.INTERNAL_CABLING_COMPLETION_CONFIRMATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = InternalCablingCompletionStatus.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/internal-cabling-completion-confirmation")
	public void confirmInternalCablingCompletion(
			@RequestBody InternalCablingCompletionStatus completionStatus) throws TclCommonException {

		lmService.confirmInternalCablingCompletion( completionStatus);

	}

	/**
	 * This method is used for Mast Installation Plan
	 *
	 * @param taskId
	 * @param mastInstallationPlanBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh mastInstallationPlan
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.MAST_INSTALLATION_PLAN)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = MastInstallationPlanBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/mast-installation-plan")
	public ResponseResource<MastInstallationPlanBean> mastInstallationPlan(
			@RequestBody MastInstallationPlanBean mastInstallationPlanBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.mastInstallationPlan( mastInstallationPlanBean), Status.SUCCESS);
	}

	/**
	 * This method is used to integrate Mux for Service Delivery
	 * 
	 * @author Mayank S
	 * @param taskId
	 * @param integrateMuxBean
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.INTEGRATE_MUX)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = IntegrateMuxBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/integrate-mux")
	public ResponseResource<IntegrateMuxBean> integrateMux(
			@RequestBody IntegrateMuxBean integrateMuxBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.integrateMux( integrateMuxBean), Status.SUCCESS);
	}

	/**
	 * This method is used to configure Mux
	 * 
	 * @author Mayank S
	 * @param taskId
	 * @param configureMuxBean
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CONFIGURE_MUX)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigureMuxBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/configure-mux")
	public ResponseResource<ConfigureMuxBean> configureMux(
			@RequestBody ConfigureMuxBean configureMuxBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.configureMux( configureMuxBean), Status.SUCCESS);
	}

	/**
	 * This method is used for OSP inventory Record.
	 *
	 * @author Vishesh Awasthi
	 * @param taskId
	 * @param createInvetoryRecord
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CREATE_INVENTORY_RECORD)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateInventoryRecord.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/osp-inventory-record")
	public ResponseResource<CreateInventoryRecord> createInventoryRecord(
			@RequestBody CreateInventoryRecord createInvetoryRecord) throws TclCommonException {
		return new ResponseResource<CreateInventoryRecord>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.createInventoryRecord( createInvetoryRecord), Status.SUCCESS);
	}

	/**
	 * This method is used to raise planned Event
	 * 
	 * @author Vishesh Awasthi
	 * @param taskId
	 * @param plannedEventBean
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.RAISE_PLANNED_EVENT)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = PlannedEventBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/planned-event")
	public ResponseResource<PlannedEventBean> raisePlannedEvent(
			@RequestBody PlannedEventBean plannedEventBean) throws TclCommonException {
		return new ResponseResource<PlannedEventBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.raisePlannedEvent( plannedEventBean), Status.SUCCESS);
	}
	
	/**
	 * This method is used to raise planned Event
	 * 
	 * @author vivek
	 * @param taskId
	 * @param plannedEventBean
	 * @return
	 * @throws TclCommonException
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.PROW_COST_APPROVAL)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = PlannedEventBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/prow-cost-approval")
	public ResponseResource<ProwCostApproval> prowCostApproval(
			@RequestBody ProwCostApproval costApproval) throws TclCommonException {
		return new ResponseResource<ProwCostApproval>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.prowCostApproval( costApproval), Status.SUCCESS);
	}
	
	/** This conduct site survey is for OSP CC team. For MAN we have separte endpoint in this class.
     * @param taskId
     * @param conductCrossConnectSiteSurveyBean
     * @return conductCrossConnectSiteSurveyBean
     * @throws TclCommonException
     */
     @ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CONDUCT_SITE_SURVEY)
     @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MuxDetailBean.class),
                   @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                   @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                   @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     @PostMapping(value = "/conduct-cc-site-survey")
     public ResponseResource<ConductCrossConnectSiteSurveyBean> saveConductCrossConnectSiteSurveyDetails(
                   @RequestBody ConductCrossConnectSiteSurveyBean conductCrossConnectSiteSurveyBean) throws TclCommonException {

    	 ConductCrossConnectSiteSurveyBean response = (ConductCrossConnectSiteSurveyBean) lmService.saveConductCrossConnectSiteSurveyBean(
    			 conductCrossConnectSiteSurveyBean);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);
     }
	
	/** This conduct site survey is for OSP team. For MAN we have separte endpoint in this class.
     * @param taskId
     * @param conductSiteSurveyBean
     * @return conductSiteSurveyBean
     * @throws TclCommonException
     */
     @ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CONDUCT_SITE_SURVEY)
     @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MuxDetailBean.class),
                   @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                   @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                   @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     @PostMapping(value = "/conduct-site-survey")
     public ResponseResource<ConductSiteSurveyBean> saveConductSiteSurveyDetails(
                   @RequestBody ConductSiteSurveyBean conductSiteSurveyBean) throws TclCommonException {

            ConductSiteSurveyBean response = (ConductSiteSurveyBean) lmService.saveConductSiteSurveyBean(
                         conductSiteSurveyBean);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);
     }
     
     /** This conduct site survey is field ops / Man team.
     * @param taskId
     * @param conductSiteSurveyBean
     * @return conductSiteSurveyBean
     * @throws TclCommonException
     */
     @ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CONDUCT_SITE_SURVEY)
     @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MuxDetailBean.class),
                   @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                   @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                   @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
     @PostMapping(value = "/conduct-site-survey-man")
     public ResponseResource<ConductSiteSurveyManBean> saveConductSiteSurveyManDetails(
                   @RequestBody ConductSiteSurveyManBean conductSiteSurveyManBean) throws TclCommonException {

            ConductSiteSurveyManBean response = (ConductSiteSurveyManBean) lmService.saveConductSiteSurveyManBean(
                         conductSiteSurveyManBean);
            return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                         Status.SUCCESS);
     }
     
     
   
      @ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CONDUCT_SITE_SURVEY)
      @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteSurveyRescheduleRequest.class),
                    @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                    @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                    @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
      @PostMapping(value = "/sitesurvery/reschedule")
      public ResponseResource<SiteSurveyRescheduleRequest> siteSurveyReschedule(
                    @RequestBody SiteSurveyRescheduleRequest siteSurveyRescheduleRequest) throws TclCommonException {
    	  SiteSurveyRescheduleRequest response = (SiteSurveyRescheduleRequest) lmService.siteSurveyReschedule(
    			  siteSurveyRescheduleRequest);
           
             return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                          Status.SUCCESS);
      }
      
      @ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.LM_JEOPARDY)
      @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NetworkAugmentation.class),
                    @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                    @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                    @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
      @PostMapping(value = "/lm-jeopardy")
      public ResponseResource<NetworkAugmentation> lmJeopardy(
                    @RequestBody NetworkAugmentation networkAugmentation) throws TclCommonException {
    	  NetworkAugmentation response = (NetworkAugmentation) lmService.lmJeopardy(
    			  networkAugmentation);
           return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                          Status.SUCCESS);
      }
      
      
      @ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CONDUCT_SITE_SURVEY)
      @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SiteSurveyRescheduleRequest.class),
                    @ApiResponse(code = 403, message = Constants.FORBIDDEN),
                    @ApiResponse(code = 422, message = Constants.NOT_FOUND),
                    @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
      @PostMapping(value = "/appoitment/new")
      public ResponseResource<SiteSurveyRescheduleRequest> newAppoitment(
                    @RequestBody SiteSurveyRescheduleRequest siteSurveyRescheduleRequest) throws TclCommonException {
    	  SiteSurveyRescheduleRequest response = (SiteSurveyRescheduleRequest) lmService.newAppointment(
    			  siteSurveyRescheduleRequest);
           
             return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
                          Status.SUCCESS);
      }
      
      /**
      *
      * @param task id
      * @param mastInstallationPermissionBean
      * @return
      * @throws TclCommonException
      */
     @ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.MAST_INSTALLATION_PERMISSION)
     @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MastInstallationPermissionBean.class),
             @ApiResponse(code = 403, message = Constants.FORBIDDEN),
             @ApiResponse(code = 422, message = Constants.NOT_FOUND),
             @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
     @PostMapping(value = "/mast-installation-permission")
     public ResponseResource<MastInstallationPermissionBean> mastInstallationPermission(
    		 											@RequestBody MastInstallationPermissionBean mastInstallationPermissionBean) throws TclCommonException {
         return new ResponseResource<MastInstallationPermissionBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, lmService.mastInstallationPermission(mastInstallationPermissionBean),
                 Status.SUCCESS);
     }
     
    @PostMapping("/raise-dependency")
 	public ResponseResource<RaiseDependencyBean> raiseJeopardy(@RequestBody RaiseDependencyBean raiseDependencyBean) throws TclCommonException {
 		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
 				lmService.raiseDependency(raiseDependencyBean), Status.SUCCESS);
 	}

	/**
	 *
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.PROVIDE_DEMARC_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ConductSiteSurveyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping("/provide-demarc-details")
	public ResponseResource<DemarcDetailsBean> provideDemarcDetails(@RequestBody DemarcDetailsBean demarcDetailsBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.provideDemarcDetails( demarcDetailsBean), Status.SUCCESS);
	}
	
	/**
	 *
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.PROVIDE_DEMARC_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskDetailsBaseBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping("/provide-mandatory-details")
	public ResponseResource<TaskDetailsBaseBean> provideMandatoryDetails(@RequestBody TaskDetailsBaseBean taskDetailsBaseBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.provideMandatoryDetails( taskDetailsBaseBean), Status.SUCCESS);
	}

    /**
    *@author diksha garg
    *
    * @param task id
    * @param NetworkInventoryBean
    * @return
    * @throws TclCommonException
    */
   @ApiOperation(value = SwaggerConstants.ApiOperations.ATTACHMENT.MAST_INSTALLATION_PERMISSION)
   @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = MastInstallationPermissionBean.class),
           @ApiResponse(code = 403, message = Constants.FORBIDDEN),
           @ApiResponse(code = 422, message = Constants.NOT_FOUND),
           @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
   @PostMapping(value = "/access-ring")
   public ResponseResource<NetworkInventoryBean> mastInstallationPermission(
  		 											@RequestBody NetworkInventoryBean networkInventoryBean) throws TclCommonException {
	   return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.updateAttributesComponent( networkInventoryBean), Status.SUCCESS);
   }
   

	/**
	 * @author diksha garg
	 *
	 * @param taskid
	 * @param provideRfDataJeopardyBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.PROVIDE_RF_DATA_JEOPARDY)
	   @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvideRfDataJeopardyBean.class),
	           @ApiResponse(code = 403, message = Constants.FORBIDDEN),
	           @ApiResponse(code = 422, message = Constants.NOT_FOUND),
	           @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	   @PostMapping(value = "/provide-rf-data-jeopardy")
	   public ResponseResource<ProvideRfDataJeopardyBean> provideRfDataJeopardy(
	  		 											@RequestBody ProvideRfDataJeopardyBean provideRfDataJeopardyBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.provideRfDataJjeopardy( provideRfDataJeopardyBean), Status.SUCCESS);
	   }
	/**
	 * @author
	 *
	 * @param taskId
	 * @param updateDependencyRemarksBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.PROVIDE_DEPENDANCY_REMARKS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvideRfDataJeopardyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/provide-remarks")
	public ResponseResource<UpdateDependencyRemarksBean> provideRemarks(
																		@RequestBody UpdateDependencyRemarksBean updateDependencyRemarksBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.updateProvideRemarks( updateDependencyRemarksBean), Status.SUCCESS);
	}


	/**
	 * This method is used to update termination date
	 *
	 * @param taskId
	 * @param terminateDateBean
	 * @return terminateDateBean
	 * @throws TclCommonException
	 * @author Thamizhselvi perumal
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.UPDATE_TERMINATION_DATE)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = TerminateDateBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/terminate-offnet-po")
	public ResponseResource<TerminateDateBean> updateTerminateDate(
			@RequestBody TerminateDateBean terminateDateBean) throws TclCommonException {
		TerminateDateBean response = lmService.updateTerminationDate(
				terminateDateBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method for Krone Mrn creation
	 *
	 * @param taskId
	 * @param mrnForMuxBean
	 * @return
	 * @throws TclCommonException
	 * @author Yogesh
	 */

	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.CREATE_MRN_FOR_MUX)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = MrnForMuxBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/krone-mrns")
	public ResponseResource<MrnForMuxBean> createMrnForKrone(
			@RequestBody MrnForMuxBean mrnForMuxBean) throws TclCommonException {
		MrnForMuxBean response = lmService.createMnrForKrone( mrnForMuxBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * Method to Install Krone
	 * 
	 * @param taskId
	 * @param installKroneBean
	 * @return
	 * @throws TclCommonException
	 * 
	 * @author yogesh
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.INSTALL_KRONE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = InstallKroneBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/install-krone")
	public ResponseResource<InstallKroneBean> installKrone(
			@RequestBody InstallKroneBean installKroneBean) throws TclCommonException {
		InstallKroneBean response = lmService.installKrone(installKroneBean);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}


	/**
	 * @param taskId
	 * @param additionalTechnicalDetailsBean
	 * @return AdditionalTechnicalDetailsBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_ADDITIONAL_TECHNICAL_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AdditionalTechnicalDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/additional-technical-details")
	public ResponseResource<SdwanAdditionalTechnicalDetails> saveSdwanAdditionalTechnicalDetails(@RequestBody SdwanAdditionalTechnicalDetails sdwanAdditionalTechnicalDetailsBean) throws TclCommonException {
		return new ResponseResource<SdwanAdditionalTechnicalDetails>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveSdwanAdditionalTechnicalDetails(sdwanAdditionalTechnicalDetailsBean),
				Status.SUCCESS);
	}
	
	/**
	 * @param taskId
	 * @param byonReadinessBean
	 * @return ByonReadinessBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_BYON_READINESS_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ByonReadinessBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/byon-readiness-details")
	public ResponseResource<ByonReadinessBean> saveByonReadinessDetails(@RequestBody ByonReadinessBean byonReadinessBean) throws TclCommonException {
		return new ResponseResource<ByonReadinessBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveByonReadinessDetails(byonReadinessBean),
				Status.SUCCESS);
	}
	
	/**
	 * @param taskId
	 * @param sdwanCloudGatewayBean
	 * @return SdwaCloudGatewayBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_SDWAN_CGW_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwaCloudGatewayBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/cgw-advanced-enrichment")
	public ResponseResource<SdwaCloudGatewayBean> saveSdwanCgwAdvanceEnrichmentDetails(@RequestBody SdwaCloudGatewayBean sdwanCloudGatewayBean) throws TclCommonException {
		return new ResponseResource<SdwaCloudGatewayBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveSdwanCgwAdvanceEnrichmentDetails(sdwanCloudGatewayBean),
				Status.SUCCESS);
	}

	/**
	 * @param taskId
	 * @param lldAndMigrationBean
	 * @return LLDAndMigrationBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.UPLOAD_SDWAN_LLD_MIGRATION_DOCUMENTS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = LLDAndMigrationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/upload-lld-migration-document")
	public ResponseResource<LLDAndMigrationBean> saveLLDAndMigrationDocuments(@RequestBody LLDAndMigrationBean lldAndMigrationBean) throws TclCommonException {
		return new ResponseResource<LLDAndMigrationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveLLDAndMigrationDocuments(lldAndMigrationBean),
				Status.SUCCESS);
	}

	/**
	 * @param taskId
	 * @param lldPreparationBean
	 * @return LLDPreparationBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SDWAN_LLD_SCHEDULE_CALL)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = LLDAndMigrationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/lld-schedule-call")
	public ResponseResource<LLDPreparationBean> saveLldScheduleCall(@RequestBody LLDPreparationBean lldPreparationBean) throws TclCommonException {
		return new ResponseResource<LLDPreparationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveLldScheduleCall(lldPreparationBean),
				Status.SUCCESS);
	}

	/**
	 * 
	 * Persist the Assist CMIP task details for IZOSDWAN
	 * @author AnandhiV
	 * @param assistCmipBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SDWAN_ASSIST_CMIP)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = LLDAndMigrationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/assist-cmip")
	public ResponseResource<AssistCmipBean> saveAssistCMIPDetails(@RequestBody AssistCmipBean assistCmipBean)
			throws TclCommonException {
		return new ResponseResource<AssistCmipBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.persistAssistCmipTaskDetails(assistCmipBean), Status.SUCCESS);
	}
	
	/**
	 * Teamsdr Upload LLD document
	 * 
	 * @param taskId
	 * @param lldAndMigrationBean
	 * @return LLDAndMigrationBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.UPLOAD_SDWAN_LLD_MIGRATION_DOCUMENTS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = LLDAndMigrationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/teamsdr/upload-lld-migration-document")
	public ResponseResource<LLDAndMigrationBean> saveTeamsdrLLDAndMigrationDocuments(@RequestBody LLDAndMigrationBean lldAndMigrationBean) throws TclCommonException {
		return new ResponseResource<LLDAndMigrationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveLLDAndMigrationDocuments(lldAndMigrationBean),
				Status.SUCCESS);
	}

	/**
	 * Teamsdr LLD Schedule Call
	 * 
	 * @param taskId
	 * @param lldPreparationBean
	 * @return LLDPreparationBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SDWAN_LLD_SCHEDULE_CALL)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = LLDAndMigrationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/teamsdr/lld-schedule-call")
	public ResponseResource<LLDPreparationBean> saveTeamsdrLldScheduleCall(@RequestBody LLDPreparationBean lldPreparationBean) throws TclCommonException {
		return new ResponseResource<LLDPreparationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveLldScheduleCall(lldPreparationBean),
				Status.SUCCESS);
	}

	 /**
     * @param taskId
     * @param additionalTechnicalDetailsBean
     * @return AdditionalTechnicalDetailsBean
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SAVE_ADDITIONAL_TECHNICAL_DETAILS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = IzopcAdditionalTechnicalDetailsBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/izopc/additional-technical-details")
    public ResponseResource<IzopcAdditionalTechnicalDetailsBean> saveAdditionalTechnicalDetails(@RequestBody IzopcAdditionalTechnicalDetailsBean additionalTechnicalDetailsBean) throws TclCommonException {
        return new ResponseResource<IzopcAdditionalTechnicalDetailsBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
        		lmService.saveIzopcAdditionalTechnicalDetails(additionalTechnicalDetailsBean),
                Status.SUCCESS);
    }

	/**
	 * Save Sdwan Order Details
	 * @param sdwanOrderDetailsBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.LmImplementation.SDWAN_ORDER_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanOrderDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/orderdetails")
	public ResponseResource<SdwanOrderDetailsBean> saveSdwanOrderDetails(@RequestBody SdwanOrderDetailsBean sdwanOrderDetailsBean)
			throws TclCommonException {
		return new ResponseResource<SdwanOrderDetailsBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				lmService.saveSdwanOrderDetails(sdwanOrderDetailsBean), Status.SUCCESS);
	}

}
