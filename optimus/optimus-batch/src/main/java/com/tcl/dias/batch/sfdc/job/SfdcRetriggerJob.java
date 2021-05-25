package com.tcl.dias.batch.sfdc.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;

/**
 * This file contains the SfdcRetriggerJob.java class.
 * 
 *
 * @author PARUNACH
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SfdcRetriggerJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(SfdcRetriggerJob.class);
	/**
	 * execute
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	@Transactional
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
			ThirdPartyServiceJobsRepository thirdServiceJobsRepository = appCtx
					.getBean(ThirdPartyServiceJobsRepository.class);
			List<String> statuses = new ArrayList<>();
			statuses.add(SfdcServiceStatus.STRUCK.toString());
			statuses.add(SfdcServiceStatus.FAILURE.toString());
			statuses.add(SfdcServiceStatus.INPROGRESS.toString());
			List<ThirdPartyServiceJob> inprogressSfdcBeans = thirdServiceJobsRepository
					.findByServiceStatusInAndThirdPartySourceAndIsActive(statuses, ThirdPartySource.SFDC.toString(),CommonConstants.BACTIVE);
			inprogressSfdcBeans.forEach(sfdcServiceJob -> {
				sfdcServiceJob.setServiceStatus(SfdcServiceStatus.NEW.toString()); // changing failure status to new so that it can be triggered by sfdc job 
				sfdcServiceJob.setUpdatedBy("system");
				sfdcServiceJob.setUpdatedTime(new Date());
				thirdServiceJobsRepository.save(sfdcServiceJob);
			});
		} catch (Exception e) {
			LOGGER.error("Error in executing component " + context.getJobDetail().getKey(), e);
		}
		LOGGER.trace("SfdcRetriggerJob.execute method end.");

	}

}
