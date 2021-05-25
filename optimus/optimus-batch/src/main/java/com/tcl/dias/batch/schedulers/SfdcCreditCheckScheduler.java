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

import com.tcl.dias.batch.sfdc.job.SfdcCreditCheckJob;
import com.tcl.dias.batch.sfdc.job.SfdcJob;

@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class SfdcCreditCheckScheduler {
	
	@Value("${scheduler.expression.sfdccreditcheck}")
	private String sfdcCreditCheckExpression;

	@Autowired
	ApplicationContext appCtx;

	@PostConstruct
	@Transactional
	public void scheduleSourceAlertTools() throws SchedulerException {

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "SFDC-CC-JOB" + new Date().getTime();
		JobDetail jobDetail1 = JobBuilder.newJob(SfdcCreditCheckJob.class).withIdentity(jobKey, "sfdcCreditCheckJobTrigger").build();
		jobDetail1.getJobDataMap().put("appCtx", appCtx);
		// Set the scheduler timings.
		Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "sfdcCreditCheckJobs")
				.withSchedule(CronScheduleBuilder.cronSchedule(sfdcCreditCheckExpression)).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail1, trigger1);
		scheduler.start();
	}

}
