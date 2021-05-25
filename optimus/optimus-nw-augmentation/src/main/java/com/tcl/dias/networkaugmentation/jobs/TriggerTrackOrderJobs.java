package com.tcl.dias.networkaugmentation.jobs;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

//import com.tcl.dias.servicefulfillmentutils.service.v1.ProjectTimelineStatusTrackService;

import java.util.Date;

/**
 * 
 * This file contains the TriggerTrackOrderJobs.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class TriggerTrackOrderJobs implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerTrackOrderJobs.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("Started Cron Job for Tracking at {}",  new Date());
		LOGGER.info("Starting Trigger Track Order Job");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
//		ProjectTimelineStatusTrackService projectTimelineStatusTrackService = appCtx
//				.getBean(ProjectTimelineStatusTrackService.class);
//		
//		try {
//
//		projectTimelineStatusTrackService.processTemplateCalculation();
//		}
//		catch (Exception e) {
//			LOGGER.error("error is daily track:{}",e);
//		}
	


	}

}
