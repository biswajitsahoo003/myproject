package com.tcl.dias.common.service;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@DisallowConcurrentExecution
public class MemoryCleanupJob implements Job{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MemoryCleanupJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		int mb = 1024*1024;
		//Getting the runtime reference from system
		Runtime runtime = Runtime.getRuntime();
		LOGGER.debug("##### Heap utilization statistics [MB] #####");
		//Print used memory
		LOGGER.debug("Used Memory: {}" 
		,(runtime.totalMemory() - runtime.freeMemory()) / mb);

		//Print free memory
		LOGGER.debug("Free Memory: {}" 
		, runtime.freeMemory() / mb);
		//Print total available memory
		LOGGER.debug("Total Memory: {}", runtime.totalMemory() / mb);

		//Print Maximum available memory
		LOGGER.debug("Max Memory: {}" , runtime.maxMemory() / mb);

		runtime.gc();

		//Print used memory
		LOGGER.debug("Used Memory: {}" 
		, (runtime.totalMemory() - runtime.freeMemory()) / mb);

		//Print free memory
		LOGGER.debug("Free Memory: {}" 
		, runtime.freeMemory() / mb);
		//Print total available memory
		LOGGER.debug("Total Memory: {}" , runtime.totalMemory() / mb);

		//Print Maximum available memory
		LOGGER.debug("Max Memory: {}" , runtime.maxMemory() / mb);

		LOGGER.debug("##### Heap utilization statistics [MB] #####");
		
		
	}
	
}
