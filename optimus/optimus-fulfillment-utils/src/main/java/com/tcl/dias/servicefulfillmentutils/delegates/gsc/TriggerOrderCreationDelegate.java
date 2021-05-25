/**
 * 
 */
package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import java.util.Map;

import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ASyed
 *
 */
@Component("triggerOrderCreation")
public class TriggerOrderCreationDelegate implements PlanItemJavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(TriggerOrderCreationDelegate.class);
	@Override
	public void execute(DelegatePlanItemInstance planItemInstance) {
		logger.info("Triggered ################## TriggerOrderCreation ############## ");
		Map<String, Object> vars = planItemInstance.getVariables();
		logger.info("vars :{}",vars);
		logger.info("local vars :{}",planItemInstance.getVariablesLocal());
		logger.info("trans vars :{}",planItemInstance.getTransientVariables());
		logger.info("trans local vars :{}",planItemInstance.getTransientVariablesLocal());
	}

}
