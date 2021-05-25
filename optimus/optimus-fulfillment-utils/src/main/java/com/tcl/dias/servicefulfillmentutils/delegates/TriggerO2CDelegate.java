package com.tcl.dias.servicefulfillmentutils.delegates;

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
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("triggerO2CDelegate")
public class TriggerO2CDelegate implements JavaDelegate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerO2CDelegate.class);
	
	
	@Value("${rabbitmq.o2c.sdwan.trigger}")
	String o2cTriggerQueue;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	MQUtils mqUtils;
	
	
	public void execute(DelegateExecution execution) {
		LOGGER.info("TriggerO2CDelegate.execute method invoked");
		try {
			workFlowService.processServiceTask(execution);
			String errorMessage = "";
			Integer serviceId = (Integer) execution.getVariable("serviceId");
			String serviceCode = (String) execution.getVariable("serviceCode");
			String orderCode = (String) execution.getVariable("orderCode");
			LOGGER.info("TriggerO2CDelegate.serviceId={},serviceCode={},orderCode={},excutionProcessInstId={}", serviceId,serviceCode,orderCode,execution.getProcessInstanceId());
			mqUtils.send(o2cTriggerQueue, orderCode);
			LOGGER.info("Execution Variables:{}", execution.getVariables());
	        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
		}catch (Exception e) {
			LOGGER.error(
					"TriggerO2CDelegate------------------- getting error details----------->{}",e);
		}
	}
}
