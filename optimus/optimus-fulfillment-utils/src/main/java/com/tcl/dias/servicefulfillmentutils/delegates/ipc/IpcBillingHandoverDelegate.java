package com.tcl.dias.servicefulfillmentutils.delegates.ipc;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("ipcBillingHandoverDelegate")
public class IpcBillingHandoverDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(IpcBillingHandoverDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("IpcBillingHandoverDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			Map<String, Object> processMap = execution.getVariables();

			workFlowService.processServiceTask(execution);
			
			
            logger.info("IpcBillingHandoverDelegate completed");
            
            workFlowService.processServiceTaskCompletion(execution);
			
		} catch (Exception e) {
			logger.error("Exception in IpcBillingHandoverDelegate{}", e);
		}

	}





}
