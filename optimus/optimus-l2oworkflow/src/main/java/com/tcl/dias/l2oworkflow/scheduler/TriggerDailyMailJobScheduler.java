package com.tcl.dias.l2oworkflow.scheduler;

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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.l2oworkflow.jobs.TriggerDailyMailJob;

/**
 * 
 * This file contains the TriggerDailyMailJobScheduler.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class TriggerDailyMailJobScheduler {

	@Autowired
	ApplicationContext appCtx;

	@PostConstruct
	@Transactional
	public void triggerDailyMailScheduler() throws SchedulerException {
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "TRIGGER-MAILJOB-SCHEDULE" + new Date().getTime();
		JobDetail jobDetail1 = JobBuilder.newJob(TriggerDailyMailJob.class)
				.withIdentity(jobKey, "triggerMailJobTrigger").build();
		jobDetail1.getJobDataMap().put("appCtx", appCtx);
		// Set the scheduler timings.
		Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "triggerMailJobs")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 2 * * * ?")).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail1, trigger1);

		scheduler.start();
	}

}
