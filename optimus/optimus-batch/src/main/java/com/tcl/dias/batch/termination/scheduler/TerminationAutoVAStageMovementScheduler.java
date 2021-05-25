package com.tcl.dias.batch.termination.scheduler;

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

import com.tcl.dias.batch.termination.job.TerminationAutoVAStageMovementJob;

/**
 * 
 * This is the scheduler class for Termination to 
 * automatically move quotes to Verbal Agreement
 * stage in SFDC
 *
 * @author Veera Balasubramanian
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class TerminationAutoVAStageMovementScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(TerminationAutoVAStageMovementScheduler.class);

	@Value("${scheduler.expression.stagemovement.Termination}")
	private String stagemovementTerminationJobExpression;

	@Autowired
	ApplicationContext appCtx;

	@PostConstruct
	@Transactional
	public void scheduleSourceAlertTools() throws SchedulerException {

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "TERMINATION-STAGEMOVEMENT-JOB" + new Date().getTime();
		JobDetail jobDetail1 = JobBuilder.newJob(TerminationAutoVAStageMovementJob.class)
				.withIdentity(jobKey, "stageMovementTerminationJobTrigger").build();
		jobDetail1.getJobDataMap().put("appCtx", appCtx);
		// Set the scheduler timings.
		Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "terminationStageMovementJobs")
				.withSchedule(CronScheduleBuilder.cronSchedule(stagemovementTerminationJobExpression)).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail1, trigger1);
		LOGGER.info("Triggering Auto Stage Movement Termination job with key {}", jobKey);
		scheduler.start();
	}
	
}
