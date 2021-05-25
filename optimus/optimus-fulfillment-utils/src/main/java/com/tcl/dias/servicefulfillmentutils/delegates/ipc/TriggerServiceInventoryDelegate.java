package com.tcl.dias.servicefulfillmentutils.delegates.ipc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.ServiceInventoryService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 TATA Communications Limited
 */
@Component("triggerServiceInventoryDelegate")
public class TriggerServiceInventoryDelegate implements JavaDelegate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AccountCreationDelegate.class);
	
	@Autowired
	private WorkFlowService workFlowService;
	
	@Autowired
	private ServiceInventoryService orderInventoryService;
	
	@Override
	public void execute(DelegateExecution execution) {
		LOGGER.info("TriggerServiceInventoryDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		Map<String, Object> processMap = execution.getVariables();
		String orderCode = (String) processMap.get(ORDER_CODE);
		orderInventoryService.processOrderInventoryRequest(orderCode);
		LOGGER.info("TriggerServiceInventoryDelegate completed");
	}

}
