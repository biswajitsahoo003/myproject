package com.tcl.dias.common.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
*
* This file contains the ThreadPoolTaskExecutorConfig.java class. 
* ThreadPoolTaskExecutor provides Async Executor service to run parallel tasks.
* 
*
* @author Samuel.S
* @link http://www.tatacommunications.com/
* @copyright 2018 Tata Communications Limited
*/

@Configuration
@EnableAsync
public class ThreadPoolTaskExecutorConfig implements AsyncConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTaskExecutorConfig.class);
	
	
	private @Value("${threadpool.core-pool:50}") int corePoolSize;
	private @Value("${threadpool.max-pool:100}") int maxPoolSize;
	private @Value("${threadpool.queue.capacity:500}") int queueCapacity;
	private @Value("${threadpool.timeout:60}") int timeout;
	
	@Bean("asyncExecutor")
	@Override
	public Executor getAsyncExecutor() {
		logger.info(
				"Initializing threadpool with the following config corePoolSize : {} , maxPoolSize {} , queueCapacity {} , timeout {}",
				corePoolSize, maxPoolSize, queueCapacity, timeout);
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
		threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
		threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
		threadPoolTaskExecutor.setKeepAliveSeconds(timeout);
		threadPoolTaskExecutor.setThreadNamePrefix("tcl-async-");
		threadPoolTaskExecutor.setTaskDecorator(new ThreadPoolTaskDecorator());
		threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		threadPoolTaskExecutor.initialize();
		return threadPoolTaskExecutor;
	}
	
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {			
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				logger.error("AsyncUncaughtException in Method:{} exception:{}",method.getName(),ex);

			}
		};
	}
}
