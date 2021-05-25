package com.tcl.dias.l2oworkflowutils.service.v1;

import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class TimeOutServiceCall {
	private static final Logger logger = LoggerFactory.getLogger(TimeOutServiceCall.class);


	public static final String TIME_OUT_ERROR = " response timed out.";
	public static final String TIME_OUT_CODE = "504";
	public static final String CRAMER = "Cramer";
	public static final String NETP = "NetP";



	@Transactional(readOnly = false)
	public void processTimeOutTask(DelegateExecution execution) throws TclCommonException {
		
	}

}
