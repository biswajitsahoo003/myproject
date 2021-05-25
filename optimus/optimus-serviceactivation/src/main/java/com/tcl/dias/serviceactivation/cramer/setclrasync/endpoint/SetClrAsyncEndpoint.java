package com.tcl.dias.serviceactivation.cramer.setclrasync.endpoint;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.constants.CramerConstants;
import com.tcl.dias.serviceactivation.cramer.getclrasync.beans.GetCLRInfoResponse;
import com.tcl.dias.serviceactivation.cramer.setclrasync.beans.SetCLRResponse;
import com.tcl.dias.serviceactivation.entity.entities.NetworkInventory;
import com.tcl.dias.serviceactivation.entity.repository.NetworkInventoryRepository;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;

@Endpoint
public class SetClrAsyncEndpoint {

	private static final Logger logger = LoggerFactory.getLogger(SetClrAsyncEndpoint.class);

	@Autowired
	RuntimeService runtimeService;

//	    @Autowired
//	    GetClrInfoAsyncDao getClrInfoAsyncDao;

	@Autowired
	NetworkInventoryRepository networkInventoryRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@PayloadRoot(namespace = "http://ACE_SetCLR_Module/SetCLRResponse", localPart = "setCLRResponse")
	@ResponsePayload
	public void processSetClrAsyncRequest(@RequestPayload SetCLRResponse setCLRResponse) {
		logger.info("SetCLRResponse received input SetCLRResponse {} ", setCLRResponse);
		NetworkInventory networkInventory = new NetworkInventory();
		String serviceCode = "";
		String errorMessage = "";
		String errorCode = "";
		String type = "Set-clr-async";

		try {
			if (java.util.Objects.nonNull(setCLRResponse) && java.util.Objects.nonNull(setCLRResponse.getResponse())) {
				String processInstanceId = setCLRResponse.getResponse().getRequestID();
            	String[] instanceId=processInstanceId.split("#");
            	processInstanceId=instanceId[1];
            	String inputType=instanceId[2];
            	serviceCode=instanceId[3];
            	
            	Execution execution = null;
            	
            	try {
					Thread.sleep(10000);
				} catch (Exception e) {
					logger.error("Exception in processSetClrAsyncRequest-threadwait", e);
				}
            	
            	if(inputType!=null && inputType.equals("MFD")) {

				 execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
						.activityId("Set-mfd-clr-async").singleResult();
				 	type ="Set-mfd-clr-async";
            	}else {
            		execution = runtimeService.createExecutionQuery().processInstanceId(instanceId[1])
    						.activityId("Set-clr-async").singleResult();
            	}
				
				//serviceCode = processInstanceId;
            	String status = setCLRResponse.getResponse().getStatus();
            	logger.info("SetCLRResponse processInstanceId={} inputType= {} serviceCode={} status= {} ", processInstanceId,inputType,serviceCode,status);
								
					networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
					networkInventory.setServiceCode(serviceCode);
					networkInventory.setType(type);
					networkInventory.setRequestId(processInstanceId);
					if(status!=null && "true".equalsIgnoreCase(status)) {
						if (execution != null) {	
							runtimeService.setVariable(execution.getId(), "setCLRSuccess", true);
							runtimeService.trigger(execution.getId());
						} else {
							logger.info("processSetClrAsyncRequest Execution is null for {} type={}", processInstanceId,type);
						}
					}
			} else {
				logger.info(
						"setClrAsyncEndPoint::processSetClrAsyncRequest id/process instance id is null or invalid to trigger flowable execution {} ",
						setCLRResponse);
			}
		} catch (Exception e) {
			networkInventory.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			networkInventory.setServiceCode(serviceCode);
			networkInventory.setType(type);
			networkInventory.setRequestId("NAE");
			errorCode="500";
			errorMessage=com.tcl.dias.servicefulfillmentutils.constants.CramerConstants.SYSTEM_ERROR;
			logger.error(e.getMessage(), e);
		}

		try {
			networkInventory.setRequest(Utils.convertObjectToXmlString(setCLRResponse, SetCLRResponse.class));
			networkInventory.setResponse("VOID");
			networkInventory.setServiceCode(serviceCode);
			networkInventoryRepository.save(networkInventory);
		} catch (Exception e) {
			logger.error("Exception in processGetClrAsyncRequest", e);

		}
		
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
		
    	logger.info("getErrorMessageDetails condition meet for SetClrAsyncEndpoint with serviceCode:{}",serviceCode);


		if (scServiceDetail != null && StringUtils.isNotBlank(errorMessage)) {
			try {
		    	logger.info("getErrorMessageDetails condition meet for SetClrAsyncEndpoint");

				componentAndAttributeService.updateAdditionalAttributes(scServiceDetail, "serviceDesignCallFailureReason",
						componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
						AttributeConstants.ERROR_MESSAGE,type);
			} catch (Exception e) {
				logger.error("cancelServiceConfigurationDelegate getting error message details----------->{}", e);
			}
		}

	}

}
