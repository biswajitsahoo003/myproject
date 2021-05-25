package com.tcl.dias.common.config;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

/**
*
* This file contains the ThreadPoolTaskDecorator.java class. 
* ThreadPoolTaskDecorator is to copy the Context Map from 
* Main thread to Child thread and clear the context Map from Child thread after execution. 
*
* @author Samuel.S
* @link http://www.tatacommunications.com/
* @copyright 2018 Tata Communications Limited
*/

public class ThreadPoolTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		Map<String,String> contextMap = MDC.getCopyOfContextMap();
		return () -> {
			try {
				if(contextMap !=null) MDC.setContextMap(contextMap);
				runnable.run();
			}finally{
				MDC.clear();
			}
		};
	}

}
