package com.tcl.dias.servicefulfillmentutils.delegates;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("trackOrderProgressDelegate")
public class TrackOrderProgressDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(TrackOrderProgressDelegate.class);

	@Override
	public void execute(DelegateExecution execution) {
		logger.info("trackOrderProgressDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
	
	}

}
