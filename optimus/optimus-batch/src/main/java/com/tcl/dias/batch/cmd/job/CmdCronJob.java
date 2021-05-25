package com.tcl.dias.batch.cmd.job;

import java.util.ArrayList;
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

import com.tcl.dias.common.beans.MDMOmsRequestBean;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

/**
 * This class contains job for MDM Update customer billing details
 * 
 *
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class CmdCronJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(CmdCronJob.class);
	
	

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
			ThirdPartyServiceJobsRepository thirdServiceJobsRepository = appCtx
					.getBean(ThirdPartyServiceJobsRepository.class);
			List<String> statuses = new ArrayList<>();
			statuses.add(SfdcServiceStatus.NEW.toString());
			List<ThirdPartyServiceJob> cmdBillingDetails = thirdServiceJobsRepository
					.findByServiceStatusInAndThirdPartySource(statuses, ThirdPartySource.CMD.toString());				
			
			cmdBillingDetails.stream().forEach(thirdPartyService->{
				
				LOGGER.info("Cmd Filter token value in before Queue call execute {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				try {
					MDMOmsRequestBean mDMRequestBean = (MDMOmsRequestBean) Utils
							.convertJsonToObject(thirdPartyService.getRequestPayload(), MDMOmsRequestBean.class);
					mDMRequestBean.setTpsId(thirdPartyService.getId());
					thirdPartyService.setRequestPayload(Utils.convertObjectToJson(mDMRequestBean));
					LOGGER.info("service pay load"+thirdPartyService.getRequestPayload());
					mqUtils.send(thirdPartyService.getQueueName(), thirdPartyService.getRequestPayload());
					thirdPartyService.setServiceStatus(SfdcServiceStatus.INPROGRESS.toString());
					thirdPartyService.setUpdatedBy("system");
					thirdPartyService.setUpdatedTime(new Date());
					thirdServiceJobsRepository.save(thirdPartyService);
					
				} catch (TclCommonException | IllegalArgumentException e) {
					throw new TclCommonRuntimeException(ExceptionConstants.RUNTIME_EXCEPTION_ON_CMD_CRON,  e);
				}
					LOGGER.info("SFDC JOB Triggered for {}-{}", thirdPartyService.getRefId(), thirdPartyService.getId());
				
			});
			  
		} catch (Exception e) {
			LOGGER.error("Error in executing component cmd " + context.getJobDetail().getKey(), e);
		}
		LOGGER.trace("DatabaseNormalizerJob.execute method end.");

	}

	}
