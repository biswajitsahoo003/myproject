package com.tcl.dias.batch.izosdwan.schedulers;

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

import com.tcl.dias.batch.izosdwan.job.IzosdwanByonFailureRetriggerJob;

/**
 * 
 * This is the schedular class for re-triggering the Failed records
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class IzosdwanByonFailureRetriggerSchedular {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanByonLocationScheduler.class);

	@Value("${scheduler.expression.byon.izosdwan.failure}")
	private String byonIzosdwanJobExpression;

	@Autowired
	ApplicationContext appCtx;

	@PostConstruct
	@Transactional
	public void scheduleSourceAlertTools() throws SchedulerException {

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "IZOSDWAN-BYON-FAILURE-JOB" + new Date().getTime();
		JobDetail jobDetail1 = JobBuilder.newJob(IzosdwanByonFailureRetriggerJob.class)
				.withIdentity(jobKey, "byonFailureJobTrigger").build();
		jobDetail1.getJobDataMap().put("appCtx", appCtx);
		// Set the scheduler timings.
		Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "izosdwanByonFailureJobs")
				.withSchedule(CronScheduleBuilder.cronSchedule(byonIzosdwanJobExpression)).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail1, trigger1);
		LOGGER.info("Triggering the BYON FAILURE SDWAN job with key {}", jobKey);
		scheduler.start();
	}

}
