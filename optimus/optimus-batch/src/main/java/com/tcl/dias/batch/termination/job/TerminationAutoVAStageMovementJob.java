package com.tcl.dias.batch.termination.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.MQUtils;

/**
 * 
 * This is the job class for Termination to 
 * automatically move quotes to Verbal Agreement
 * stage in SFDC
 * 
 * @author Veera Balasubramanian
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class TerminationAutoVAStageMovementJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(TerminationAutoVAStageMovementJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			LOGGER.info("Inside Execute Method of Termination Stage Movement Job");
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
			
			MQUtils mqUtils = appCtx.getBean(MQUtils.class);
			
			mqUtils.send("rabbitmq_termination_stagemovement_queue", "ALL");
			
		} catch (Exception e) {
			LOGGER.error("Exception occurred in termination stage movement job", e);
		}
	}
		
}
