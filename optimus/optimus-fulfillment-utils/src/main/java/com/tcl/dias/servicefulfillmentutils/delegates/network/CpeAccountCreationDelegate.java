package com.tcl.dias.servicefulfillmentutils.delegates.network;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("cpeAccountCreationDelegate")
public class CpeAccountCreationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(CpeAccountCreationDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.accountCreateSync.network.cpe}")
	String accountCreateQueue;
    
    @Autowired
	MQUtils mqUtils;

    @Override
	public void execute(DelegateExecution execution) {
		logger.info("CpeAccountCreationDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			workFlowService.processServiceTask(execution);
   			Map<String, Object> processMap = execution.getVariables();
   			String orderCode = (String) processMap.get(ORDER_CODE);
   			String serviceCode = (String) processMap.get(SERVICE_CODE);
   			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
   			Integer cpeOverlayComponentId = (Integer) processMap.getOrDefault("cpeOverlayComponentId",0);
			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode)
					.concat("#").concat(serviceId.toString()).concat("#").concat(cpeOverlayComponentId.toString());
   			logger.info("CPE Account Creation for serviceCode={} PROCESS ID={}",serviceCode , execution.getProcessInstanceId());
   			String status = (String) mqUtils.sendAndReceive(accountCreateQueue, req);
   			
   			logger.info("CPE Account Creation for serviceCode={} status={}",serviceCode , status);
			if (!StringUtils.isBlank(status) && status.equals("Success")) {
				execution.setVariable("cpeAccountCreationAck", true);
			} else {
				execution.setVariable("cpeAccountCreationAck", false);
				execution.setVariable("cpeAccountCreationErrorMsg", status.split("Success|Fail")[1]);
			}

			logger.info("CpeAccountCreationDelegate completed");
   			
			
		} catch (Exception e) {
			logger.error("Exception in CpeAccountCreationDelegate{}", e);
		}
		
		workFlowService.processServiceTaskCompletion(execution);

	}
}
