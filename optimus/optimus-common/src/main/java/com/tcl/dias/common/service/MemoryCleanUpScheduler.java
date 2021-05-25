package com.tcl.dias.common.service;

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


@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class MemoryCleanUpScheduler {
	
	@Autowired
	ApplicationContext appCtx;
	
	@PostConstruct
	@Transactional
	public void freeUpMem() throws SchedulerException {
		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "MEMORY-CLEAN-JOB" + new Date().getTime();
		JobDetail jobDetail1 = JobBuilder.newJob(MemoryCleanupJob.class).withIdentity(jobKey, "memoryCleanupTrigger").build();
		jobDetail1.getJobDataMap().put("appCtx", appCtx);
		// Set the scheduler timings.
		Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "memoryCleanUpJobs")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 0/5 * * * ?")).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail1, trigger1);

		scheduler.start();
	}
}
