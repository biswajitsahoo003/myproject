package com.tcl.dias.batch.schedulers;

import com.tcl.dias.batch.dao.ThirdPartyServiceJobDao;
import com.tcl.dias.batch.sfdc.job.EnterpriseTigerServiceJob;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.oms.entity.repository.GscServiceLogRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
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

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * This file contains Job scheduler to asynchronously processing Enterprise Tiger service requests
 *
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class EnterpriseTigerServiceJobScheduler {

	@Value("${scheduler.expression.tigerscheduler}")
	private String tigerJobSchedulerCronExpression;

	@Value("${tigerscheduler.max.retry.count:1}")
	private Integer maxRetryCount;

	@Autowired
	ApplicationContext appCtx;

	@PostConstruct
	@Transactional
	public void scheduleSourceAlertTools() throws SchedulerException {

		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		String jobKey = "ENTERPRISE-TIGER-JOB" + new Date().getTime();
		JobDetail jobDetail = JobBuilder.newJob(EnterpriseTigerServiceJob.class).withIdentity(jobKey, "tigerJobTrigger").build();
		jobDetail.getJobDataMap().put("mqUtils", appCtx.getBean(MQUtils.class));
		jobDetail.getJobDataMap().put("thirdPartyServiceJobDao", appCtx.getBean(ThirdPartyServiceJobDao.class));
		jobDetail.getJobDataMap().put("thirdPartyServiceJobsRepository", appCtx.getBean(ThirdPartyServiceJobsRepository.class));
		jobDetail.getJobDataMap().put("gscServiceLogRepository", appCtx.getBean(GscServiceLogRepository.class));
		jobDetail.getJobDataMap().put("maxRetryCount", maxRetryCount);
		// Set the scheduler timings.
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "tigerJobs")
				.withSchedule(CronScheduleBuilder.cronSchedule(tigerJobSchedulerCronExpression)).build();
		// Execute the job.
		scheduler.scheduleJob(jobDetail, trigger);

		scheduler.start();
	}
}
