package com.tcl.dias.servicefulfillmentutils.delegates;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("createOspInventoryRecordDelegate")
public class CreateOspInventoryRecordDelegate  implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(CreateOspInventoryRecordDelegate.class);

	@Autowired
	private RuntimeService runtimeService;


	@Override
	public void execute(DelegateExecution execution) {
		logger.info("CreateOspInventoryRecordDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		
		execution.setVariable("createOspInventoryRecordSuccess", true); 
	}
}
