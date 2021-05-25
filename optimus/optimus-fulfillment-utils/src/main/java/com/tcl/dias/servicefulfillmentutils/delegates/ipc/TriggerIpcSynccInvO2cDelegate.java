package com.tcl.dias.servicefulfillmentutils.delegates.ipc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.IpcServiceFulfillmentService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author Ram
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("triggerIpcSyncInvO2cDelegate")
public class TriggerIpcSynccInvO2cDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(TriggerIpcSynccInvO2cDelegate.class);
	
    @Autowired
	WorkFlowService workFlowService;
    
    @Autowired
    IpcServiceFulfillmentService ipcServiceFulfillmentService;
    
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("TriggerIpcSynccInvO2cDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			ipcServiceFulfillmentService.loadDataFromInvAndUpdateO2c(serviceCode);
            logger.info("TriggerIpcSynccInvO2cDelegate completed");
            workFlowService.processServiceTaskCompletion(execution);
		} catch (Exception e) {
			logger.error("Exception in TriggerIpcSynccInvO2cDelegate{}", e);
		}
	}
}
