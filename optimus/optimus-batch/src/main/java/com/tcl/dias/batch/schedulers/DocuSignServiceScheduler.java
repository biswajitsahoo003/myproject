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

import com.tcl.dias.batch.sfdc.job.DocuSignJob;

/**
 * This file contains the DocuSignServiceScheduler.java class.
 *
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class DocuSignServiceScheduler {

	@Value("${scheduler.expression.docusignscheduler}")
	private String docusignJobExpression;

	@Autowired
	ApplicationContext appCtx;

	@PostConstruct
	@Transactional
	public void scheduleDocusignService() throws SchedulerException {

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "DOCUSIGN-JOB" + new Date().getTime();
		JobDetail jobDetail1 = JobBuilder.newJob(DocuSignJob.class).withIdentity(jobKey, "docusignJobTrigger").build();
		jobDetail1.getJobDataMap().put("appCtx", appCtx);
		// Set the scheduler timings.
		Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "docusignJobs")
				.withSchedule(CronScheduleBuilder.cronSchedule(docusignJobExpression)).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail1, trigger1);
		scheduler.start();
	}

}
