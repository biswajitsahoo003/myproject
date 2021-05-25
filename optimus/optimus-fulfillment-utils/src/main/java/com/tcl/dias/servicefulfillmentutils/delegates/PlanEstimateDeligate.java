package com.tcl.dias.servicefulfillmentutils.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("planEstimationDelegate")
public class PlanEstimateDeligate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(PlanEstimateDeligate.class);


	@Override
	public void execute(DelegateExecution execution) {
		logger.info("PlanEstimateDeligate started");
		
	}

}
