package com.tcl.dias.batch.schedulers;

import com.tcl.dias.batch.dao.ThirdPartyServiceJobDao;
import com.tcl.dias.batch.sfdc.job.ChangeOutpulseTigerServiceJob;
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
 * This file contains Job scheduler to asynchronously processing Change Outpulse service requests
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Configuration
@DisallowConcurrentExecution
@EnableTransactionManagement
public class ChangeOutpulseServiceJobScheduler {

    @Value("${scheduler.expression.changeoutpulse.scheduler}")
    private String changeOutpulseJobSchedulerCronExpression;

    @Value("${tigerscheduler.max.retry.count:1}")
    private Integer maxRetryCount;

    @Autowired
    ApplicationContext appCtx;

    @PostConstruct
    @Transactional
    public void scheduleSourceAlertTools() throws SchedulerException {

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        String jobKey = "CHANGE-OUTPULSE-JOB" + new Date().getTime();
        JobDetail jobDetail = JobBuilder.newJob(ChangeOutpulseTigerServiceJob.class).withIdentity(jobKey, "changeOutpulseJobTrigger").build();
        jobDetail.getJobDataMap().put("mqUtils", appCtx.getBean(MQUtils.class));
        jobDetail.getJobDataMap().put("thirdPartyServiceJobDao", appCtx.getBean(ThirdPartyServiceJobDao.class));
        jobDetail.getJobDataMap().put("thirdPartyServiceJobsRepository", appCtx.getBean(ThirdPartyServiceJobsRepository.class));
        jobDetail.getJobDataMap().put("gscServiceLogRepository", appCtx.getBean(GscServiceLogRepository.class));
        jobDetail.getJobDataMap().put("maxRetryCount", maxRetryCount);
        // Set the scheduler timings.
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("Trigger-" + jobKey, "changeOutpulseJobs")
                .withSchedule(CronScheduleBuilder.cronSchedule(changeOutpulseJobSchedulerCronExpression)).build();
        // Execute the job.
        scheduler.scheduleJob(jobDetail, trigger);

        scheduler.start();
    }
}
