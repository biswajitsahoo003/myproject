package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("basicEnrichmentDelegate")
public class BasicEnrichmentDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(BasicEnrichmentDelegate.class);
		
		@Autowired
		WorkFlowService workFlowService;

	public void execute(DelegateExecution execution) {
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);
		logger.info("BasicEnrichmentDelegate invoked for serviceCode {} with {} Id={}", serviceCode,execution.getCurrentActivityId(),
				execution.getId());
		String errorMessage = "";
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
	}

}
