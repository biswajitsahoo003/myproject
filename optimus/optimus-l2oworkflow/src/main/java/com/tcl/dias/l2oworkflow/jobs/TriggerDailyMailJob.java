package com.tcl.dias.l2oworkflow.jobs;

import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.l2oworkflow.entity.entities.NotificationMailJob;
import com.tcl.dias.l2oworkflow.entity.repository.NotificationMailJobRepository;

/**
 * 
 * This file contains the TriggerDailyMailJob.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class TriggerDailyMailJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerDailyMailJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("Starting Trigger Daily Mail Job");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
		NotificationMailJobRepository notificationMailJobRepository = appCtx
				.getBean(NotificationMailJobRepository.class);

		MQUtils mqUtils = appCtx.getBean(MQUtils.class);
		List<NotificationMailJob> notificationMailJobs = notificationMailJobRepository.findByStatus("NEW");
		for (NotificationMailJob notificationMailJob : notificationMailJobs) {
			try {
				LOGGER.info("MDC Filter token value in before Queue call processMailAttachment {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.sendAndReceive(notificationMailJob.getQueueName(), notificationMailJob.getMailRequest());
				notificationMailJob.setStatus("COMPLETED");
				notificationMailJobRepository.save(notificationMailJob);
			} catch (Exception e) {
				notificationMailJob.setStatus("FAILURE");
				notificationMailJobRepository.save(notificationMailJob);
				LOGGER.warn("Error in Notification Job {}", ExceptionUtils.getStackTrace(e));
			}

		}

	}

}
