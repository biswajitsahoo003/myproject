package com.tcl.dias.batch.renewals.scheduler;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.batch.renewals.job.RenewalsGstUpdateJob;

@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class RenewalsGstUpdateScheduler {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RenewalsGstUpdateScheduler.class);

	@Value("${scheduler.expression.gst.renewals}")
	private String gstUpdate;

	@Autowired
	ApplicationContext appCtx;

	@PostConstruct
	@Transactional
	public void scheduleSourceAlertTools() throws SchedulerException {

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "RENEWALS-GST-JOB" + new Date().getTime();
		JobDetail jobDetail1 = JobBuilder.newJob(RenewalsGstUpdateJob.class).withIdentity(jobKey, "renewalsGstJobTrigger").build();
		jobDetail1.getJobDataMap().put("appCtx", appCtx);
		// Set the scheduler timings.
		Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "renewalsGstJob")
				.withSchedule(CronScheduleBuilder.cronSchedule(gstUpdate)).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail1, trigger1);
		LOGGER.info("Triggering Auto Gst Update job with key {}", jobKey);
		scheduler.start();
	}

}
