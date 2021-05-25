package com.tcl.dias.preparefulfillment.scheduler;

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

import com.tcl.dias.preparefulfillment.jobs.TriggerServiceCatalogJob;

/**
 * 
 * This file contains the TriggerServiceCatalogScheduler.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class TriggerServiceCatalogScheduler {

	@Autowired
	ApplicationContext appCtx;

	@PostConstruct
	@Transactional
	public void triggerServiceCatalogScheduler() throws SchedulerException {
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "TRIGGER-SERVICE-SCHEDULE" + new Date().getTime();
		JobDetail jobDetail1 = JobBuilder.newJob(TriggerServiceCatalogJob.class)
				.withIdentity(jobKey, "triggerServiceCatalogTrigger").build();
		jobDetail1.getJobDataMap().put("appCtx", appCtx);
		// Set the scheduler timings.
		Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "triggerServiceCatalogJobs")
				.withSchedule(CronScheduleBuilder.cronSchedule("0/30 * * * * ?")).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail1, trigger1);

		scheduler.start();
	}

}
