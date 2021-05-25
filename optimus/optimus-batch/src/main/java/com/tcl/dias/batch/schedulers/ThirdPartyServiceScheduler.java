package com.tcl.dias.batch.schedulers;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.batch.sfdc.cron.ThirdPartyServiceJobRetryJob;

/**
 * This file contains the ThirdPartyServiceScheduler.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class ThirdPartyServiceScheduler {
	
	@Value("${scheduler.expression.tpsscheduler}")
	private String tpsJobSchedulerCronExpression;

	@Autowired
	ApplicationContext appCtx;

	@PostConstruct
	@Transactional
	public void scheduleTps() throws SchedulerException {

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "TPS-JOB" + new Date().getTime();
		JobDetail jobDetail = JobBuilder.newJob(ThirdPartyServiceJobRetryJob.class).withIdentity(jobKey, "tpsJobTrigger").build();
		jobDetail.getJobDataMap().put("appCtx", appCtx);
		// Set the scheduler timings.
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "tpsJobs")
				.withSchedule(CronScheduleBuilder.cronSchedule(tpsJobSchedulerCronExpression)).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail, trigger);

		scheduler.start();
	}

}
