package com.tcl.dias.batch.sfdc.job;

import java.util.Date;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the DocusignServiceJob.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class DocuSignJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocuSignJob.class);

	/**
	 * execute
	 * 
	 * @param context
	 * @throws JobExecutionException
	 */
	@Override
	@Transactional
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
			MQUtils mqUtils = appCtx.getBean(MQUtils.class);
			ThirdPartyServiceJobsRepository docusignServiceJobsRepository = appCtx
					.getBean(ThirdPartyServiceJobsRepository.class);
			List<ThirdPartyServiceJob> sfdcServiceJobs = docusignServiceJobsRepository
					.findByServiceStatusAndThirdPartySource(SfdcServiceStatus.NEW.toString(),
							ThirdPartySource.DOCUSIGN.toString());
			for (ThirdPartyServiceJob thirdPartyServiceJob : sfdcServiceJobs) {
				LOGGER.info("MDC Filter token value in before Queue call execute {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				try {
					mqUtils.send(thirdPartyServiceJob.getQueueName(), thirdPartyServiceJob.getRequestPayload());
				} catch (TclCommonException | IllegalArgumentException e) {
					LOGGER.error("Error in sending the request to docusign " + context.getJobDetail().getKey(), e);
					thirdPartyServiceJob.setResponsePayload(e.getMessage());
				}
				thirdPartyServiceJob.setServiceStatus(SfdcServiceStatus.SUCCESS.toString());
				thirdPartyServiceJob.setUpdatedBy("system");
				thirdPartyServiceJob.setUpdatedTime(new Date());
				docusignServiceJobsRepository.save(thirdPartyServiceJob);
				LOGGER.info("DOCUSIGN JOB Triggered for {}-{}", thirdPartyServiceJob.getRefId(),
						thirdPartyServiceJob.getId());
			}

		} catch (Exception e) {
			LOGGER.error("Error in executing component " + context.getJobDetail().getKey(), e);
		}

	}

}
