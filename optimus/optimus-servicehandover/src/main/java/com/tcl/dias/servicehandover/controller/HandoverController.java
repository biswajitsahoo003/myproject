package com.tcl.dias.servicehandover.controller;

import com.tcl.dias.common.serviceinventory.bean.SdwanScOrderBean;
import com.tcl.dias.servicefulfillmentutils.beans.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.serviceinventory.bean.ScOrderBean;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.servicehandover.service.HandoverService;
import com.tcl.dias.servicehandover.service.ServiceInventoryHandoverService;
import com.tcl.dias.servicehandover.swagger.constants.SwaggerConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

                                                       /**
 * HandoverController this class is used to get the handover Related details
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/task")
public class HandoverController {

    @Autowired
    HandoverService handoverService;
    
    @Autowired
    ServiceInventoryHandoverService serviceInventoryHandoverService;

    /**
     * Provide Handover note Task.
     *
     * @param taskId
     * @param provideHandoverBean
     * @return ProvideHandoverBean
     * @author VISHESH AWASTHI
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.HANDOVER_NOTE_TASK)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/provide-handover-note", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<ProvideHandoverBean> getTaskCount(
                                                              @RequestBody ProvideHandoverBean provideHandoverBean) throws TclCommonException {
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
        		handoverService.provideHandoverNote(provideHandoverBean), Status.SUCCESS);
    }

    /**
     * Service Acceptance Task.
     *
     * @param taskId
     * @param serviceAcceptanceBean
     * @return ServiceAcceptance
     * @author VISHESH AWASTHI
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_ACCEPTANCE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/service-acceptance", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<ProvideHandoverBean> getTaskCount(
                                                              @RequestBody ServiceAcceptanceBean serviceAcceptanceBean) throws TclCommonException {
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
        		handoverService.serviceAcceptance(serviceAcceptanceBean), Status.SUCCESS);
        
        
    }
    
    /**
     * Service Handover Task.
     *
     * @param taskId
     * @param serviceHandoverBean
     * @return serviceHandoverBean
     * @author diksha garg
     */
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/service-handover", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<ServiceHandoverBean> serviceHandover(
                                                              @RequestBody ServiceHandoverBean serviceHandoverBean) throws TclCommonException {
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
        		handoverService.serviceHandover(serviceHandoverBean), Status.SUCCESS);
    }
    
    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.SERVICE_HANDOVER)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = AssignedGroupBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/sdwan/service-handover", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<SdwanServiceHandoverBean> sdwanServiceHandover(
                                                              @RequestBody SdwanServiceHandoverBean serviceHandoverBean) throws TclCommonException {
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
        		handoverService.sdwanServiceHandover(serviceHandoverBean), Status.SUCCESS);
    }

    @PostMapping("/service-handover/migrate")
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<Boolean> handOverToServiceInventory(@RequestBody List<String> serviceCodes) throws TclCommonException {
        Boolean response = serviceInventoryHandoverService.migrateToServiceInventoryBulk(serviceCodes);
        return new ResponseResource<Boolean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response, Status.SUCCESS);
    }

    @PostMapping("/service-handover/sdwan/{serviceCode}")
    @ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanScOrderBean.class),
           @ApiResponse(code = 403, message = Constants.FORBIDDEN),
           @ApiResponse(code = 422, message = Constants.NOT_FOUND),
           @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
    public ResponseResource<Boolean> handoverSdwanDetailsToInventory(@PathVariable("serviceCode") String serviceCode) throws TclCommonException {
       Boolean response = serviceInventoryHandoverService.handoverSdwanDetailsToServiceInventory(serviceCode);
       return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
               response, Status.SUCCESS);
    }


    @PostMapping("/service-handoverorder/{orderCode}/service/{serviceCode}/id/{serviceId}")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ScOrderBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ScOrderBean> handOverToServiceInventory(@PathVariable("orderCode") String orderCode,
													   @PathVariable("serviceCode") String serviceCode,
													   @PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				serviceInventoryHandoverService.handoverDetailsToServiceInventory(orderCode,serviceCode,serviceId), Status.SUCCESS);
	}

    @ApiOperation(value = SwaggerConstants.ApiOperations.Task.EXPERIENCE_SURVEY)
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ExperienceSurveyBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    @PostMapping(value = "/experience-survey", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResource<ExperienceSurveyBean> experienceSurvey(
                                                               @RequestBody ExperienceSurveyBean experienceSurveyBean) throws TclCommonException {
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                handoverService.experienceSurvey(experienceSurveyBean), Status.SUCCESS);
    }
    @PostMapping("/terminate-existing-billing-account")
    @ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ScOrderBean.class),
            @ApiResponse(code = 403, message = Constants.FORBIDDEN),
            @ApiResponse(code = 422, message = Constants.NOT_FOUND),
            @ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
    public ResponseResource<TerminateBillingAccBean> terminateExistingBillingAccount(
                                                                                     @RequestBody TerminateBillingAccBean terminateBillingAccBean) throws TclCommonException {
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
                handoverService.terminateExistingBillingAccount( terminateBillingAccBean), Status.SUCCESS);
    }
}