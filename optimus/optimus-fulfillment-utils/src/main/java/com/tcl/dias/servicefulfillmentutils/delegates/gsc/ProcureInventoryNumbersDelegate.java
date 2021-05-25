/**
 * 
 */
package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.CmmnHelperService;

/**
 * @author ASyed
 *
 */
@Component("procureInventoryNumbers")
public class ProcureInventoryNumbersDelegate implements PlanItemJavaDelegate {


	@Autowired
	CmmnHelperService cmmnHelperService;
	
	private static final Logger logger = LoggerFactory.getLogger(ProcureInventoryNumbersDelegate.class);
	@Override
	public void execute(DelegatePlanItemInstance planItemInstance) {
		logger.info("Triggered ################## ProcureInventoryNumbers ############## ");
		Map<String, Object> vars = planItemInstance.getVariables();
		logger.info("vars :{}",vars);
		logger.info("local vars :{}",planItemInstance.getVariablesLocal());
		logger.info("trans vars :{}",planItemInstance.getTransientVariables());
		logger.info("trans local vars :{}",planItemInstance.getTransientVariablesLocal());
		
		
		Random random = new Random();
		Map<String, Object> params1 = new HashMap<String, Object>();
		params1.put("orderid", random.nextInt());

	}

}
