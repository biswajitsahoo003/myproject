package com.tcl.dias.servicefulfillment.controller.v1;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicefulfillment.beans.ConfirmSupplierConfiguration;
import com.tcl.dias.servicefulfillment.beans.DefineSowProjPlan;
import com.tcl.dias.servicefulfillment.beans.HandoffDetailsBean;
import com.tcl.dias.servicefulfillment.beans.LmDelivery;
import com.tcl.dias.servicefulfillment.beans.NegotiateCommercialsLMProvide;
import com.tcl.dias.servicefulfillment.beans.OffnetSiteSurvey;
import com.tcl.dias.servicefulfillment.beans.ProvideKlmVlanBean;
import com.tcl.dias.servicefulfillment.beans.ProvidePOToCCProvider;
import com.tcl.dias.servicefulfillment.beans.ProvidePOToLMProvider;
import com.tcl.dias.servicefulfillment.beans.ProvidePOToLMProviderSite2;
import com.tcl.dias.servicefulfillment.beans.ProvidePoForCPEOrderBean;
import com.tcl.dias.servicefulfillment.beans.RaiseJeopardy;
import com.tcl.dias.servicefulfillment.beans.SupplierAcceptance;
import com.tcl.dias.servicefulfillment.service.OffnetLMService;
import com.tcl.dias.servicefulfillment.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Rest point controller for all Rf Lm implementation tasks.
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/offnet-lm-implementation/task")
public class OffnetLmImplementationController {

	@Autowired
	OffnetLMService offnetLMService;
	
	
    /**
     * This method is used to negotiate commercials witk LM Provider.
     *
     * @param taskId
     * @param negotiateCommercialsLMProvide
     * @return NegotiateCommercialsLMProvide
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.NEGOTIATE_COMMERCIALS_WITH_LM_PROVIDERS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoForCPEOrderBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping("/negotiate-offnet-lm-provider")
    public ResponseResource<NegotiateCommercialsLMProvide> negotiateCommercialsLMProvide(
                                                          @RequestBody NegotiateCommercialsLMProvide negotiateCommercialsLMProvide) throws TclCommonException {
        return new ResponseResource<NegotiateCommercialsLMProvide>(ResponseResource.R_CODE_OK,ResponseResource.RES_SUCCESS,
                offnetLMService.negotiateCommercialsLMProvide(negotiateCommercialsLMProvide),
                Status.SUCCESS);
    }

    /**
     * This method is used to provide PO details to LM Provider.
     *
     * @param taskId
     * @param providePOToLMProvider
     * @return ProvidePOToLMProvider
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.PROVIDE_PO_TO_LM_PROVIDERS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePoForCPEOrderBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping("/po-offnet-lm-provider")
    public ResponseResource<ProvidePOToLMProvider> providePOToLMProvider(
                                                          @RequestBody ProvidePOToLMProvider providePOToLMProvider) throws TclCommonException {
        return new ResponseResource<ProvidePOToLMProvider>(ResponseResource.R_CODE_OK,ResponseResource.RES_SUCCESS,
                offnetLMService.providePOToLMProvider(providePOToLMProvider),
                Status.SUCCESS);
    }

    /**
     * This method is used to provide PO details to Cross-Connect Provider.
     *
     * @param taskId
     * @param providePOToCCProvider
     * @return ProvidePOToCCProvider
     * @throws TclCommonException
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.PROVIDE_PO_TO_CROSS_CONNECT_PROVIDERS)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePOToCCProvider.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping("/po-offnet-cc-provider")
    public ResponseResource<ProvidePOToCCProvider> providePOToCCProvider(
                                                  @RequestBody ProvidePOToCCProvider providePOToCCProvider) throws TclCommonException {
        return new ResponseResource<ProvidePOToCCProvider>(ResponseResource.R_CODE_OK,ResponseResource.RES_SUCCESS,
                offnetLMService.providePOToCCProvider(providePOToCCProvider),
                Status.SUCCESS);
    }

	/**
	 * This method is used to save details of supplier acceptance.
	 *
	 * @param taskId
	 * @param supplierAcceptance
	 * @return SupplierAcceptance
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.SUPPLIER_ACCEPTANCE)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SupplierAcceptance.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/get-offnet-supplier-acceptance")
	public ResponseResource<SupplierAcceptance> getSupplierAcceptance(
			@RequestBody SupplierAcceptance supplierAcceptance) throws TclCommonException {
		return new ResponseResource<SupplierAcceptance>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.getSupplierAcceptance(supplierAcceptance), Status.SUCCESS);
	}

	/**
	 * This method is used for raise jeopardy task.
	 *
	 * @param taskId
	 * @param raiseJeopardy
	 * @return RaiseJeopardy
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.RAISE_JEOPARDY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = RaiseJeopardy.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/raise-jeopardy")
	public ResponseResource<RaiseJeopardy> raiseJeopardy(
			@RequestBody RaiseJeopardy raiseJeopardy) throws TclCommonException {
		return new ResponseResource<RaiseJeopardy>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.raiseJeopardy(raiseJeopardy), Status.SUCCESS);
	}

	/**
	 * @author diksha garg
	 * 
	 * @param taskId
	 * @param defineSowProjPlan
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.DEFINE_SOW_PROJECT_PLAN)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DefineSowProjPlan.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/define-offfnet-project-plan")
	public ResponseResource<LmDelivery> defineSowProjectPlan(
			@RequestBody LmDelivery lmDelivery) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.defineSowProjectPlan(lmDelivery), Status.SUCCESS);
	}
	
	
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.DEFINE_SOW_PROJECT_PLAN)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DefineSowProjPlan.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/provide-offnet-klm-vlan")
	public ResponseResource<HandoffDetailsBean> provideOffnetKlmVlan(
			@RequestBody HandoffDetailsBean handoffDetailsBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.provideOffnetKlmVlan(handoffDetailsBean), Status.SUCCESS);
	}

	/**
	 * @author diksha garg
	 * 
	 * @param taskId
	 * @param lmDelivery
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.TRACK_COMPLETE_LM_DELIVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LmDelivery.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/track-offnet-lm-delivery")
	public ResponseResource<LmDelivery> trackCompleteLmDelivery(
			@RequestBody LmDelivery lmDelivery) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.trackCompleteLmDelivery(lmDelivery), Status.SUCCESS);
	}
	
	/**
	 * @author diksha garg
	 * 
	 * @param taskId
	 * @param offnetSiteSurvey
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.TRACK_COMPLETE_LM_DELIVERY)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LmDelivery.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/offnet-ss-details")
	public ResponseResource<OffnetSiteSurvey> offnetSsDetails(
			@RequestBody OffnetSiteSurvey offnetSiteSurvey) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.offnetSsDetails(offnetSiteSurvey), Status.SUCCESS);
	}
	
	@ApiOperation(value ="LM Jeopardy")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = LmDelivery.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/lm-jeopardy/task/{task_id}/wftask/{wfTaskId}")
	public ResponseResource<Map<String, Object>> lmJeopardy(@PathVariable("task_id") Integer taskId,@PathVariable("wfTaskId") String wfTaskId,
			@RequestBody Map<String, Object> map) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.lmJeopardy(taskId,wfTaskId, map), Status.SUCCESS);
	}
	
	
	/**
	 * @author diksha garg
	 * 
	 * @param taskId
	 * @param defineSowProjPlan
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.DEFINE_SOW_PROJECT_PLAN)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DefineSowProjPlan.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/select-cloud-name")
	public ResponseResource<HandoffDetailsBean> selectCloudName(
			@RequestBody HandoffDetailsBean HandoffDetailsBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.selectCloudName(HandoffDetailsBean), Status.SUCCESS);
	}
	
	/**
	 * @author diksha garg
	 * 
	 * @param taskId
	 * @param defineSowProjPlan
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.DEFINE_SOW_PROJECT_PLAN)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DefineSowProjPlan.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/provide-ior-detail")
	public ResponseResource<HandoffDetailsBean> provideIORDetail(
			@RequestBody HandoffDetailsBean HandoffDetailsBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.provideIORDetail(HandoffDetailsBean), Status.SUCCESS);
	}
	
	
	
	/**
	 * @author Sarath Kumar.M
	 * 
	 * @param taskId
	 * @param defineSowProjPlan
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.PROVIDE_KLM_VLAN_DETAILS)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = DefineSowProjPlan.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/provide-klm-vlan-details")
	public ResponseResource<ProvideKlmVlanBean> provideKlmVlanBean(
			@RequestBody ProvideKlmVlanBean provideKlmVlanBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.provideKlmVlanDetails(provideKlmVlanBean), Status.SUCCESS);
	}
	
	/**
	 * @author diksha garg
	 * 
	 * @param taskId
	 * @param confirmSupplierConfiguration
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.CONFIRM_SUPPLIER_CONFIRMATION)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfirmSupplierConfiguration.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/confirm-supplier-configuration")
	public ResponseResource<ConfirmSupplierConfiguration> confirmSupplierConfiguration(
			@RequestBody ConfirmSupplierConfiguration confirmSupplierConfiguration) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				offnetLMService.confirmSupplierConfiguration(confirmSupplierConfiguration), Status.SUCCESS);
	}

	/**
	 * This method is used to provide PO details to LM Provider for Site 2.
	 *
	 * @param taskId
	 * @param providePOToLMProviderSite2
	 * @return ProvidePOToLMProviderSite2
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.CPEImplementation.PROVIDE_PO_TO_LM_PROVIDERS_SITE2)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = ProvidePOToLMProviderSite2.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/po-offnet-lm-provider-site2/{task_id}")
	public ResponseResource<ProvidePOToLMProviderSite2> providePOToLMProviderSite2(
			@PathVariable("task_id") Integer taskId, @RequestBody ProvidePOToLMProviderSite2 providePOToLMProviderSite2)
			throws TclCommonException {
		return new ResponseResource<ProvidePOToLMProviderSite2>(ResponseResource.R_CODE_OK,
				ResponseResource.RES_SUCCESS,
				offnetLMService.providePOToLMProviderSite2(taskId, providePOToLMProviderSite2), Status.SUCCESS);
	}	
	
}
