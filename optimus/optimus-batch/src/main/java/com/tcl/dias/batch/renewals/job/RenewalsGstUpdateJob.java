package com.tcl.dias.batch.renewals.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
@DisallowConcurrentExecution
public class RenewalsGstUpdateJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(RenewalsGstUpdateJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
		MQUtils mqUtils = appCtx.getBean(MQUtils.class);

		try {
			mqUtils.send("renewals_update_gst", "N");
		} catch (TclCommonException e) {
			// TODO Auto-generated catch block
			LOGGER.error("Exception occurred in renewals gst job", e);
		}
	}


}
