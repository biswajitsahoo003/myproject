package com.tcl.dias.billing.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author KRUTSRIN
 * 
 * A config class to create an executor service instance.
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
public class ThreadConfig {
	
	 @Value("${executor.thread.core.pool.size}")
	 Integer executorThrdCorePoolSize;
	
	 @Bean
	    public ExecutorService threadPoolTaskExecutor() {
			return Executors.newFixedThreadPool(executorThrdCorePoolSize);

	    }

}
