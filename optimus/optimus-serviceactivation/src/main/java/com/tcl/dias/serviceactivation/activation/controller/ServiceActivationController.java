package com.tcl.dias.serviceactivation.activation.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import com.tcl.dias.serviceactivation.beans.CgwLogsUploadBean;
import com.tcl.dias.serviceactivation.beans.CustomerAcceptanceIssueBean;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.activation.fti.service.FTIService;
import com.tcl.dias.serviceactivation.activation.services.IPDetailsService;
import com.tcl.dias.serviceactivation.activation.services.ProductActivationConfigurationService;
import com.tcl.dias.serviceactivation.activation.services.SatSocService;
import com.tcl.dias.serviceactivation.beans.ConfigurationRequest;
import com.tcl.dias.serviceactivation.beans.CustomerDownTimeBean;
import com.tcl.dias.serviceactivation.beans.DownTimeBean;
import com.tcl.dias.serviceactivation.beans.IPServiceEndDateBean;
import com.tcl.dias.serviceactivation.beans.IpDetailsBean;
import com.tcl.dias.serviceactivation.beans.MuxJeopardyBean;
import com.tcl.dias.serviceactivation.beans.SatcoServiceDataRefreshBean;
import com.tcl.dias.serviceactivation.beans.SdwanProvisionDetailsBean;
import com.tcl.dias.serviceactivation.beans.SetCLRSyncBean;
import com.tcl.dias.serviceactivation.beans.TxJeopardyBean;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.NodeToBeConfigured;
import com.tcl.dias.serviceactivation.cramer.getipserviceasync.GetIpServiceEndPoint;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.NniProtectedPort;
import com.tcl.dias.serviceactivation.cramer.reverseisc.beans.VpnIllPortAttributes;
import com.tcl.dias.serviceactivation.listener.ServiceActivationListener;
import com.tcl.dias.serviceactivation.macd.AceRule;
import com.tcl.dias.serviceactivation.service.ActivationService;
import com.tcl.dias.serviceactivation.service.CramerService;
import com.tcl.dias.serviceactivation.service.ServiceActivationService;
import com.tcl.dias.serviceactivation.swagger.constants.SwaggerConstants;
import com.tcl.dias.servicefulfillmentutils.beans.BSODetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.CGWBean;
import com.tcl.dias.servicefulfillmentutils.beans.CustomerCloudConfigurationBean;
import com.tcl.dias.servicefulfillmentutils.beans.NodeConfigurations;
import com.tcl.dias.servicefulfillmentutils.beans.NodeToConfigureBean;
import com.tcl.dias.servicefulfillmentutils.beans.OrderDetailBean;
import com.tcl.dias.servicefulfillmentutils.beans.PlannedEventBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceConfigurationBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.beans.TxConfigurationJeopardyBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.CpeInventoryRequestBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.GOODSRECEIPT;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.InventoryCheckResponse;
import com.tcl.dias.servicefulfillmentutils.beans.sap.hana.MTVMISTOCKRES;
import com.tcl.dias.servicefulfillmentutils.beans.servicenow.CreateMuxPlannedEventBean;
import com.tcl.dias.servicefulfillmentutils.beans.servicenow.PlannedEventResponseBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapHanaService;
import com.tcl.dias.servicefulfillmentutils.custom.annotations.RestrictNetworkAccess;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import om.tcl.dias.serviceactivation.beans.izopc.WanIpDetailsBean;


/**
 * This file contains the ServiceActivationController.java class.
 *
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@RestController
@RequestMapping("/v1/service")
public class ServiceActivationController {
	private static final Logger logger = LoggerFactory.getLogger(ServiceActivationController.class);
	

	@Autowired
	IPDetailsService iPDetailsService;

	@Autowired
	ActivationService activationService;
	
	@Autowired
	CramerService cramerService;
	
	@Autowired
	ServiceActivationService serviceActivationService;
	
	 @Autowired
	 ProductActivationConfigurationService productActivationConfigurationService;

	 @Autowired
    GetIpServiceEndPoint getIpServiceEndPoint;
	 
	 @Autowired
	 AceRule aceRule;
	 
	 @Autowired
	 SatSocService satSocService;

	@Autowired
	FTIService ftiService;
	
	@Autowired
	ServiceActivationListener serviceActivationListener;
	
	@Autowired
	private SapHanaService sapHanaService;

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<TaskBean> getIpDetails(@RequestParam(value="taskId", required = false) Integer taskId,
												   @RequestParam(value = "version", required = false) Integer version,
												   @RequestParam(value="serviceId", required = false) String serviceId,
												   @RequestParam(value="wfTaskId", required = false) String wfTaskId) throws TclCommonException {
		TaskBean response = iPDetailsService.getIpServiceDetails(taskId, version, serviceId,wfTaskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@RequestMapping(value = "/test/{taskId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = OrderDetailBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<TaskBean> test(@PathVariable("taskId") Integer taskId) throws TclCommonException {
		TaskBean response = iPDetailsService.test(taskId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}

	@RequestMapping(value = "/config/action", method = RequestMethod.POST)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = ConfigurationRequest.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<ConfigurationRequest> configureAction(@RequestBody ConfigurationRequest configurationRequest) throws TclCommonException {
		ConfigurationRequest response = iPDetailsService.configureAction(configurationRequest);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/update/serviceDetails")
	public ResponseResource<TaskBean> updateServiceIpDetails(@RequestBody TaskBean taskBean) throws TclCommonException {
		TaskBean result = iPDetailsService.updateServiceIpDetails(taskBean);
		Map<String, Object> wfMap = new HashMap<>();
		String action = StringUtils.trimToEmpty(taskBean.getAction());
		if("rf-configuration-jeopardy".equals(taskBean.getTaskDefKey())){
			wfMap.put("rfConfigurationAction",action);
		}else{
			wfMap.put("serviceConfigurationAction",action);
		}
		return new ResponseResource<TaskBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
//				iPDetailsService.makeTaskDataEntry(taskBean.getTaskId(), taskBean, wfMap),
				Status.SUCCESS);
	}

	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = TaskBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/update")
	@RestrictNetworkAccess
	public ResponseResource<TaskBean> updateIPDetails(@RequestBody TaskBean taskBean) throws TclCommonException {
		TaskBean result = iPDetailsService.updateIPDetails(taskBean);
		Map<String, Object> wfMap = new HashMap<>();
		String action = StringUtils.trimToEmpty(taskBean.getAction());
		if("rf-configuration-jeopardy".equals(taskBean.getTaskDefKey())){
			wfMap.put("rfConfigurationAction",action);
		}else{
			wfMap.put("serviceConfigurationAction",action);
		}
		wfMap.put("action",action);
		return new ResponseResource<TaskBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				iPDetailsService.makeTaskDataEntry(taskBean.getTaskId(), taskBean, wfMap),
				Status.SUCCESS);
	}


	/**
	 * This method is used to raise planned Event
	 *
	 * @author Naveen
	 * @param taskId
	 * @param plannedEventBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = PlannedEventBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/check-pe-available-window")
	public ResponseResource<PlannedEventBean> raisePlannedEvent(
			@RequestBody PlannedEventBean plannedEventBean) throws TclCommonException {
		return new ResponseResource<PlannedEventBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.checkPlannedEvent( plannedEventBean), Status.SUCCESS);
	}


	/**
	 * This method is used to return the status of the planned event id
	 *
	 * @author Naveen
	 * @param peId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = PlannedEventBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/get-pe-available-window/{peId}")
	public ResponseResource<PlannedEventBean> raisePlannedEvent(@PathVariable("peId") String peId) throws TclCommonException {
		return new ResponseResource<PlannedEventBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.getPlannedEventInfo(peId), Status.SUCCESS);
	}


	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = CreateMuxPlannedEventBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/create-planned-event")
	public ResponseResource<PlannedEventResponseBean> createPlannedEvent(
																		 @RequestBody CreateMuxPlannedEventBean createMuxPlannedEventBean) throws TclCommonException {
		return new ResponseResource<PlannedEventResponseBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.createPlannedEvent(createMuxPlannedEventBean), Status.SUCCESS);
	}

	/**
	 * manual task to configure tx node for specified taskId.
	 *
	 * @param taskId
	 * @param nodeToBeConfigured
	 * @return {@link NodeToBeConfigured}
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = NodeToBeConfigured.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/nodes/task/{task_id}/wftask/{wfTaskId}")
	public ResponseResource saveNodesToBeConfigured(@PathVariable("task_id") Integer taskId,@PathVariable("wfTaskId") String wfTaskId,
													@RequestBody NodeConfigurations nodeToBeConfigured) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.saveNodesToBeConfigured(taskId,wfTaskId,nodeToBeConfigured), Status.SUCCESS);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/endRecord")
	public void endServiceDetailsRecord(@RequestBody IPServiceEndDateBean ipServiceEndDateBean) throws TclCommonException {
		iPDetailsService.endServiceConfigRecords(ipServiceEndDateBean);
	}

	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/rollback")
	public ResponseResource<String> rollbackAndTerminate(@RequestBody IPServiceEndDateBean ipServiceEndDateBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,iPDetailsService.rollbackAndCancel(ipServiceEndDateBean),Status.SUCCESS);
	}


	@ApiOperation(value = SwaggerConstants.ApiOperations.SET_RF_PING_STABILITY)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CpeInventoryRequestBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/set-ping-stability/{circuitId}")
	public ResponseResource startPingTest(@PathVariable("circuitId") String circuitId){
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.setPingStability("testing#"+circuitId), Status.SUCCESS);
	}


	@ApiOperation(value = SwaggerConstants.ApiOperations.GET_PING_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CpeInventoryRequestBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/get-ping-stability/{uniqueId}")
	public ResponseResource getPingTestDetails(@PathVariable("uniqueId") String uniqueId) {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.getPingTestResult("abc#"+uniqueId), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.GET_SS_DUMP_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CpeInventoryRequestBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@GetMapping("/ss-dump/{circuitId}")
	public ResponseResource getSSDumpDetails(@PathVariable("circuitId") String circuitId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.getSSDumpDetails("testing#"+circuitId), Status.SUCCESS);
	}

	@PostMapping("/raise-jeopardy")
	public ResponseResource raiseJeopardy(@RequestBody MuxJeopardyBean muxJeopardyBean
										 ) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.raiseJeopardy(muxJeopardyBean), Status.SUCCESS);
	}

	@PostMapping("/raise-tx-jeopardy")
	@RestrictNetworkAccess
	public ResponseResource raiseTxJeopardy(@RequestBody TxJeopardyBean txJeopardyBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.raiseTxJeopardy(txJeopardyBean), Status.SUCCESS);
	}

	@GetMapping("/netp/{serviceId}/{requestId}")
	public ResponseResource netP(@PathVariable("serviceId") String serviceId,@PathVariable("requestId") String requestId) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				productActivationConfigurationService.processIpConfigurationXml(serviceId,"PE_PROV_CONFIG",requestId), Status.SUCCESS);
	}
	
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Response.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/setclr")
	public ResponseResource<Response> setclr(@RequestBody SetCLRSyncBean clrBean) throws TclCommonException {

		return new ResponseResource<Response>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cramerService.setCLR(Utils.convertObjectToJson(clrBean)), Status.SUCCESS);
	}
	
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Response.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/setmfdclr")
	public ResponseResource<Response> setmfdclr(@RequestBody SetCLRSyncBean clrBean) throws TclCommonException {

		return new ResponseResource<Response>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cramerService.setMFDCLR(Utils.convertObjectToJson(clrBean)), Status.SUCCESS);
	}
	
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Response.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/setterminateclr")
	public ResponseResource<Response> setterminateclr(@RequestBody SetCLRSyncBean clrBean) throws TclCommonException {

		return new ResponseResource<Response>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cramerService.setTERMINATECLR(Utils.convertObjectToJson(clrBean)), Status.SUCCESS);
	}


	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Response.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping("/txDownTime")
	public ResponseResource downTimeWS(@RequestBody DownTimeBean downTimeBean ) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				cramerService.getTxDownTime(Utils.convertObjectToJson(downTimeBean)), Status.SUCCESS);
	}
	
	
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/save/servicedetails")
	public String saveServiceDetails(@RequestBody Map<String, Object> map) throws TclCommonException {
		String serviceCode = (String) map.get("serviceCode");
		String orderCode = (String) map.get("orderCode");
		Integer serviceId = (Integer) map.get("serviceId");
		
		logger.info("saveServiceDetails serviceCode={} orderCode={},serviceId={}",serviceCode,orderCode,serviceId);
		
		serviceActivationService.saveServiceDetails(serviceCode,orderCode,serviceId);
		return "SUCCESS";
	}


    @PostMapping("/saveRf/{circuitId}")
    public ResponseResource saveRfInvData(@PathVariable("circuitId") String circuitId) throws TclCommonException {
        return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceActivationService.confirmOrderRFtoInv(circuitId, true, null)
                , Status.SUCCESS);
    }

	@PostMapping("/saveRfMacd/{circuitId}")
	public ResponseResource saveRfMacdInvData(@PathVariable("circuitId") String circuitId) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceActivationService.confirmOrderMacdRFtoInv(circuitId, true, null)
				, Status.SUCCESS);
	}

	@GetMapping("/lmdetails/{serviceCode}")
	public ResponseResource enrichRfDetails(@PathVariable("serviceCode") String serviceCode)  throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceActivationService.enrichRfDetails(serviceCode)
				, Status.SUCCESS);
	}

	@PostMapping("/RfRefresh/{circuitId}")
	public ResponseResource RFInventoryRefresh(@PathVariable("circuitId") String circuitId) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceActivationService.RFInventoryRefresh(circuitId)
				, Status.SUCCESS);
	}

	@PostMapping("/saveCambiumLastmile/{serviceCode}")
	public ResponseResource saveManualCambiumLastmile(@PathVariable("serviceCode") String serviceCode,
	@RequestBody(required = true)  Map<String, String> map) throws TclCommonException {
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, serviceActivationService.saveManualCambiumLastmile(serviceCode, map)
				, Status.SUCCESS);
	}

	@PostMapping("/downtime-check/{serviceId}")
	public ResponseResource downtimeRequired(@PathVariable("serviceId") String serviceId) throws TclCommonException {
		aceRule.isDowntimeRequired(serviceId);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,"SUCCESS"
				, Status.SUCCESS);
	}

	@PostMapping("/portchange-check/{serviceId}")
	public ResponseResource portChange(@PathVariable("serviceId") String serviceId) throws TclCommonException {
		aceRule.isPortChanged(serviceId);
		return new ResponseResource(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,"SUCCESS"
				, Status.SUCCESS);
	}
	
	@RequestMapping(value = "/ipDetails/{serviceId}", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = IpDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<IpDetailsBean> getIpDetails(@PathVariable("serviceId") Integer serviceId) throws TclCommonException {
		IpDetailsBean response = iPDetailsService.getIpDetails(serviceId);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);

	}
	
	
	/**
	 * This method is used to updateservicedetails status from L2O Amendment. Don't add/update any code to reuse it
	 *
	 * @param servicecode
	 * @return
	 * @throws TclCommonException
	 * @author dimples
	 */
	@RequestMapping(value = "/update/service/status/{servicecode}", method = RequestMethod.GET)
	public ResponseResource updateServiceStatus(@PathVariable("servicecode") String serviceCode) throws TclCommonException {
		serviceActivationService.updateServiceStatus(serviceCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, "SUCCESS",
				Status.SUCCESS);

	}

	/**
	 *
	 * This method used for SatSoc Handover
	 *
	 * @author diksha garg
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/satsoc-reverseisc/{serviceId}", method = RequestMethod.GET)
	public ResponseResource<VpnIllPortAttributes> satSocReverseIsc(@PathVariable("serviceId") String serviceId,@RequestParam(value="page", required = false) String page)
			throws Exception {
		VpnIllPortAttributes response = satSocService.getReverseIsc(serviceId,page);
		return new ResponseResource<VpnIllPortAttributes>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * This API is the get the cgw details
	 * @param ReverseISCOutput
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/cgw/{serviceId}/{serviceCode}", method = RequestMethod.GET)
	public ResponseResource<CGWBean> getCGWDetails(@PathVariable("serviceId") Integer serviceId,@PathVariable("serviceCode") String serviceCode)
			throws Exception {
		CGWBean response = satSocService.getCGWDetails(serviceId,serviceCode);
		return new ResponseResource<CGWBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	@PostMapping("/create-service")
	public ResponseResource<Response> createService(@RequestBody Map<String, String> request) throws TclCommonException {
		Response response = null;
		try {
			response = cramerService.createService(request);
		} catch (JAXBException e) {
			logger.error("Error in Create service api call : ", e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,e,ResponseResource.R_CODE_ERROR);
		}
		return new ResponseResource<Response>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response
				, Status.SUCCESS);
	}

	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = Response.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping("/txdowntime/{taskId}")
	public ResponseResource<CustomerDownTimeBean> persistDownTimeDetails(@PathVariable("taskId") Integer taskId,@RequestBody CustomerDownTimeBean customerDownTimeBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.persistDownTimeDetails(taskId,customerDownTimeBean), Status.SUCCESS);
	}

	@ApiOperation(value ="FTI Refresh Data using File")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/fti/save")
	public void processMigrationData(){
		ftiService.processMigrationFiles();
	}

	@ApiOperation(value ="FTI Refresh using API")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/fti/refresh/{serviceId}")
	public void refreshFTIData(@PathVariable("serviceId") Integer scServiceDetailId,@RequestBody List<String> serviceCodes){
		serviceActivationService.refreshFTIData(serviceCodes,null,scServiceDetailId);
	}
	
	/**
	 * This method is used to fetch BSO ID Details .
	 *
	 * @param serviceCode
	 * @return
	 * @throws TclCommonException
	 * @author Ankit kumar
	 */
	@RequestMapping(value = "/bso-details", method = RequestMethod.GET)
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = BSODetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	public ResponseResource<BSODetailsBean> getBsoDetails(@RequestParam(value="serviceCode") String serviceCode) throws TclCommonException {
		BSODetailsBean bsoresponse = iPDetailsService.getBsoDetails(serviceCode);
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, bsoresponse,
				Status.SUCCESS);

	}
	
	/**
	 * This method is used to open tx-config-jeopardy
	 * 
	 * @author diksha garg
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = TxConfigurationJeopardyBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/tx-configuration-jeopardy/{task_id}")
	public ResponseResource<TxConfigurationJeopardyBean> txConfigurationJeopardy(@RequestBody TxConfigurationJeopardyBean txConfigurationJeopardyBean) throws TclCommonException {
		return new ResponseResource<TxConfigurationJeopardyBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.txConfigJeopardy(txConfigurationJeopardyBean), Status.SUCCESS);
	}
	
	/**
	 * @param taskId
	 * @param failoverTestingBean
	 * @return FailoverTestingBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_FAILOVER_TESTING_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/failover-testing-details")
	public ResponseResource<ServiceConfigurationBean> saveFailoverTestingDetails(@RequestBody ServiceConfigurationBean failoverTestingBean) throws TclCommonException {
		return new ResponseResource<ServiceConfigurationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.saveFailoverTestingDetails(failoverTestingBean),
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * This API is the save the cpe config task data for Izosdwan
	 * @param izosdwanCpeConfigBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_FAILOVER_TESTING_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/config-cpe")
	public ResponseResource<ServiceConfigurationBean> saveCpeConfigDetails(@RequestBody ServiceConfigurationBean izosdwanCpeConfigBean) throws TclCommonException {
		return new ResponseResource<ServiceConfigurationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.saveCpeConfigDetailsIzosdwan(izosdwanCpeConfigBean),
				Status.SUCCESS);
	}
	
	
	/**
	 * 
	 * This API is the save the cpe config  tda task data for Izosdwan
	 * @param izosdwanCpeConfigBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_FAILOVER_TESTING_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/config-cpe-tda")
	public ResponseResource<ServiceConfigurationBean> saveCpeConfigTdaDetails(@RequestBody ServiceConfigurationBean izosdwanCpeConfigBean) throws TclCommonException {
		return new ResponseResource<ServiceConfigurationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.saveCpeConfigTdaDetailsIzosdwan(izosdwanCpeConfigBean),
				Status.SUCCESS);
	}

	/**
	 *
	 * This API is the save billing issue commissioning date Izosdwan
	 * @param sdwanBillingIssueBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.IZOSDWAN_SAVE_BILLING_ISSUE_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping("/sdwan/billing-issue")
	public ResponseResource billingIssueSdwan(@RequestBody CustomerAcceptanceIssueBean sdwanBillingIssueBean
	) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.billingIssueSdwan(sdwanBillingIssueBean), Status.SUCCESS);
	}
	
	/**
	 * 
	 * This API is used to update Service Issue task data
	 * @author AnandhiV
	 * @param customerAcceptanceIssueBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.IZOSDWAN_SAVE_SERVICE_ISSUE_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping("/sdwan/service-issue")
	public ResponseResource serviceIssueSdwan(@RequestBody CustomerAcceptanceIssueBean customerAcceptanceIssueBean
	) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.updateServiceIssueTaskDetails(customerAcceptanceIssueBean), Status.SUCCESS);
	}
	
	/**
	 * 
	 * This API is used to update Raise turnup Issue task data
	 * @param customerAcceptanceIssueBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_RAISE_TURNUP_ISSUE_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping("/sdwan/raise-turnup-request")
	public ResponseResource raiseTurnupIssueSdwan(@RequestBody CustomerAcceptanceIssueBean customerAcceptanceIssueBean
	) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.updateTurnupIssueTaskDetails(customerAcceptanceIssueBean), Status.SUCCESS);
	}

	/**
	 *
	 * This API is used to upload Cgw logs in cgw configuration
	 * @author KavyaSin
	 * @param cgwLogsUploadBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.UPLOAD_CGW_LOGS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = CgwLogsUploadBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping("/sdwan/cgw-config")
	public ResponseResource serviceIssueSdwan(@RequestBody CgwLogsUploadBean cgwLogsUploadBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.cgwLogsUpload(cgwLogsUploadBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_BILLING_ISSUE_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerAcceptanceIssueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ucwb/billing-issue")
	public ResponseResource billingIssueUCWB(@RequestBody CustomerAcceptanceIssueBean ucwbBillingIssueBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.billingIssueUcwb(ucwbBillingIssueBean), Status.SUCCESS);
	}

	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_SERVICE_ISSUE_DETAILS)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerAcceptanceIssueBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping("/ucwb/service-issue")
	public ResponseResource serviceIssueUCWB(@RequestBody CustomerAcceptanceIssueBean customerAcceptanceIssueBean)
			throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.updateServiceIssueTaskDetails(customerAcceptanceIssueBean), Status.SUCCESS);
	}

	@ApiOperation(value ="SATSOC data with Cramer refresh")
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@PostMapping(value = "/satco-data/refresh")
	public ResponseResource<SatcoServiceDataRefreshBean> refreshSATSOCData(@RequestBody SatcoServiceDataRefreshBean satcoServiceDataRefreshBean){
		return new ResponseResource<SatcoServiceDataRefreshBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, 
				serviceActivationService.refreshSatcoData(satcoServiceDataRefreshBean), Status.SUCCESS);
	}
	
	
	@GetMapping(value = "/test-sap-hana/material-quantity")
	public void checMaterialQuantityAndPlaceMrn(@RequestParam("serviceCode") String serviceCode,
			@RequestParam("serviceId") Integer serviceId, @RequestParam("type") String type,
			@RequestParam("invType") String invType, @RequestParam("typeOfExpenses") String typeOfExpenses)
			throws TclCommonException, ParseException {
		sapHanaService.checMaterialQuantityInInventory(serviceCode, serviceId, type, invType, typeOfExpenses, null);
	}
	
	@GetMapping(value = "/test-sap-hana/wbs-check")
	public void wbsCheck(@RequestParam("serviceCode") String serviceCode,
			@RequestParam("serviceId") Integer serviceId, @RequestParam("type") String type,
			@RequestParam("invType") String invType, @RequestParam("typeOfExpenses") String typeOfExpenses)
			throws TclCommonException, ParseException {
		sapHanaService.checMaterialAvailableInWbsVmi(serviceCode, serviceId, type, invType, typeOfExpenses, null);
	}
	
	@GetMapping(value = "/test-sap-hana/wbs-transfer")
	public void wbsToWbsTransfer(@RequestParam("serviceCode") String serviceCode,
			@RequestParam("serviceId") Integer serviceId, @RequestParam("type") String type,
			@RequestParam("invType") String invType, @RequestParam("typeOfExpenses") String typeOfExpenses)
			throws TclCommonException {
		sapHanaService.wbsToWbsTransfer(serviceCode, invType, typeOfExpenses, type);
	}
	
	@GetMapping(value = "/test-sap-hana/process-prpo")
	public void processAutoPr(@RequestParam("serviceCode") String serviceCode, @RequestParam("type") String type,
			@RequestParam("isMaterial") Boolean isMaterial, @RequestParam("typeOfExpenses") String typeOfExpenses)
			throws TclCommonException {
		sapHanaService.processAutoPr(serviceCode, type, isMaterial, typeOfExpenses);
	}

	@GetMapping(value = "/test-sap-hana/vmi-stock")
	public ResponseResource<MTVMISTOCKRES> checkVmiStockInventory(@RequestParam("serviceCode") String serviceCode, @RequestParam("type") String type,
																					@RequestParam("invType") String invType, @RequestParam("materialCode") String materialCode) throws TclCommonException, ParseException {
		return new ResponseResource<MTVMISTOCKRES>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				sapHanaService.checkVmiStockInventory(serviceCode, type, invType, materialCode), Status.SUCCESS);
	}
	
	@GetMapping(value = "/test-sap-hana/save-cpe-request")
	public void saveCpeMaterialRequest(@RequestParam("serviceCode") String serviceCode, @RequestParam("type") String type) throws TclCommonException, ParseException {
		sapHanaService.saveCpeMaterialRequestWithBasicDetails(serviceCode, type);
	}

	@GetMapping(value = "/test-sap-hana/intsall-cpe")
	public void installCpeRequest(@RequestBody GOODSRECEIPT goodsReceipt) throws TclCommonException, ParseException {
		sapHanaService.installCpeRequest(null,null,null,null,null);
	}


	@PostMapping("/create-service-gvpn")
	public ResponseResource<Response> createServiceGvpn(@RequestBody Map<String, String> request) throws TclCommonException {
		Response response = null;
		response = cramerService.createServiceGvpn(request);
		return new ResponseResource<Response>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response
				, Status.SUCCESS);
	}
	
	@PostMapping("/auto-migration")
	public ResponseResource<Boolean> autoMigration(@RequestBody Map<String, String> request) throws TclCommonException {
		Boolean response = null;
		response = serviceActivationListener.autoMigration(request);
		return new ResponseResource<Boolean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response
				, Status.SUCCESS);
	}
	
	@PostMapping("/migrate-service-details")
	public ResponseResource<Boolean> persistMigrationData(@RequestBody Map<String, String> request) throws TclCommonException {
		Boolean response = iPDetailsService.persistMigrationData(request.get("serviceId"),Integer.parseInt(request.get("scServiceDetailsId")));
		return new ResponseResource<Boolean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,response
				, Status.SUCCESS);
	}


	@PostMapping(value = "/createnode/{serviceId}")
	public ResponseResource<String> createNodeManualInNetP(@RequestBody String serviceId) throws TclCommonException {
		cramerService.createNodeManualInNetP(serviceId);
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,"SUCCESS"
				, Status.SUCCESS);
	}
	
	/**
	 * @param taskId
	 * @param e2eSdwanTesting
	 * @return ServiceConfigurationBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_E2E_TESTING_SDWAN)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/e2etesting")
	public ResponseResource<ServiceConfigurationBean> saveE2eTestingDetailsSdwan(@RequestBody ServiceConfigurationBean e2eSdwanTesting) throws TclCommonException {
		return new ResponseResource<ServiceConfigurationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.saveE2eTestingDetailsSdwan(e2eSdwanTesting),
				Status.SUCCESS);
	}

	/**
	 * @param taskId
	 * @param sdwanProvisionDetails
	 * @return SdwanProvisionDetailsBean
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_SDWAN_PROVISION_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = SdwanProvisionDetailsBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/sdwan/provisiondetails")
	public ResponseResource<SdwanProvisionDetailsBean> saveSdwanProvisionDetails(@RequestBody SdwanProvisionDetailsBean sdwanProvisionDetails) throws TclCommonException {
		return new ResponseResource<SdwanProvisionDetailsBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.saveSdwanProvisionDetails(sdwanProvisionDetails),
				Status.SUCCESS);
	}
	
	/**
	 * @param serviceCode
	 * @param bsIp
	 * @return String
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.GET_SS_IP_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = String.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@GetMapping(value = "/ssipdetails/{serviceCode}/{bsIp}")
	public ResponseResource<String> get(@PathVariable("serviceCode") String serviceCode,@PathVariable("bsIp") String bsIp) throws TclCommonException {
		return new ResponseResource<String>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.getSsIpDetails(serviceCode,bsIp),Status.SUCCESS);
	}

	@PostMapping("/izopc/wan-ip-details")
	public ResponseResource<WanIpDetailsBean> saveServiceDetails(@RequestBody WanIpDetailsBean wanIpDetailsBean) throws TclCommonException {
		return new ResponseResource<>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.saveServiceDetails(wanIpDetailsBean), Status.SUCCESS);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = Constants.SUCCESS, response = Boolean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED) })
	@RequestMapping(value = "/satsoc-reverseisc/secondary/{serviceId}", method = RequestMethod.GET)
	public ResponseResource<NniProtectedPort> satSocSecondaryReverseIsc(@PathVariable("serviceId") String serviceId,@RequestParam(value="page", required = false) String page)
			throws Exception {
		NniProtectedPort response = satSocService.getSecondaryReverseIsc(serviceId,page);
		return new ResponseResource<NniProtectedPort>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS, response,
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * This API is the save the cloud configuration task data for izopc
	 * @param serviceConfigurationBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_FAILOVER_TESTING_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = ServiceConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/izopc/cloud-configuration")
	public ResponseResource<ServiceConfigurationBean> saveCloudConfiguration(@RequestBody ServiceConfigurationBean serviceConfigurationBean) throws TclCommonException {
		return new ResponseResource<ServiceConfigurationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.saveCloudConfiguration(serviceConfigurationBean),
				Status.SUCCESS);
	}
	
	/**
	 * 
	 * This API is the save the customer cloud configuration task data for izopc
	 * @param customerCloudConfigurationBean
	 * @return
	 * @throws TclCommonException
	 */
	@ApiOperation(value = SwaggerConstants.ApiOperations.ServiceActivation.SAVE_FAILOVER_TESTING_DETAILS)
	@ApiResponses(value = {@ApiResponse(code = 200, message = Constants.SUCCESS, response = CustomerCloudConfigurationBean.class),
			@ApiResponse(code = 403, message = Constants.FORBIDDEN),
			@ApiResponse(code = 422, message = Constants.NOT_FOUND),
			@ApiResponse(code = 417, message = Constants.EXCEPTION_FAILED)})
	@PostMapping(value = "/izopc/customer-cloud-configuration")
	public ResponseResource<CustomerCloudConfigurationBean> saveCustomerCloudConfiguration(@RequestBody CustomerCloudConfigurationBean customerCloudConfigurationBean) throws TclCommonException {
		return new ResponseResource<CustomerCloudConfigurationBean>(ResponseResource.R_CODE_OK, ResponseResource.RES_SUCCESS,
				activationService.saveCustomerCloudConfiguration(customerCloudConfigurationBean),
				Status.SUCCESS);
	}

}
