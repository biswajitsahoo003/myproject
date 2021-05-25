package com.tcl.dias.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
*
* This file contains the ContextClosedHandler.java class. 
* This class is used to shutdown the ThreadPool and release all the threads before shutdown the application.
* 
*
* @author Samuel.S
* @link http://www.tatacommunications.com/
* @copyright 2018 Tata Communications Limited
*/

@Component
public class ContextClosedHandler implements ApplicationListener<ContextClosedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ContextClosedHandler.class);
	
	@Autowired 
    @Qualifier("asyncExecutor")    
    private ThreadPoolTaskExecutor executor; 

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {   
    	LOGGER.info("shutdown ThreadPoolTaskExecutor...");
        executor.shutdown();
    }       
}
