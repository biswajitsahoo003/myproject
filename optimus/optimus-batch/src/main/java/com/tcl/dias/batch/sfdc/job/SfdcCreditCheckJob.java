package com.tcl.dias.batch.sfdc.job;

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

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.ExceptionConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.sfdc.bean.SfdcCreditCheckQueryRequest;
import com.tcl.dias.common.sfdc.constants.SfdcServiceTypeConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@DisallowConcurrentExecution
public class SfdcCreditCheckJob implements Job {
	
private static final Logger LOGGER = LoggerFactory.getLogger(SfdcCreditCheckJob.class);
	
	
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
			statuses.add(SfdcServiceStatus.INPROGRESS.toString());
			statuses.add(SfdcServiceStatus.FAILURE.toString());
			List<ThirdPartyServiceJob> cmdBillingDetails = thirdServiceJobsRepository
					.findByServiceStatusInAndThirdPartySource(statuses, ThirdPartySource.CREDITCHECK.toString());				
			
			cmdBillingDetails.stream().forEach(thirdPartyService->{
				
				LOGGER.info("Cmd Filter token value in before Queue call execute {} :",MDC.get(CommonConstants.MDC_TOKEN_KEY));
				try {
					if (thirdPartyService.getIsComplete() == CommonConstants.BDEACTIVATE) {
						processIncompleteRequests(appCtx, thirdPartyService);
					}
					
					LOGGER.info("cmdBillingDetails - picking job with ref id {}, payload {}", thirdPartyService.getRefId(), thirdPartyService.getRequestPayload());
					
					List<ThirdPartyServiceJob> listOfUpdateOpportunityJobs = thirdServiceJobsRepository.findByRefIdAndServiceTypeAndThirdPartySource(thirdPartyService.getRefId(),SfdcServiceTypeConstants.UPDATE_OPPORTUNITY, ThirdPartySource.SFDC.toString());
					if(listOfUpdateOpportunityJobs != null && !listOfUpdateOpportunityJobs.isEmpty()) {
						boolean isUpdateOpportunityInProgress = listOfUpdateOpportunityJobs.stream().anyMatch(job ->  !SfdcServiceStatus.SUCCESS.toString().equals(job.getServiceStatus()));
						if(isUpdateOpportunityInProgress) {
							LOGGER.info("Some update opportunity call is in progress/not successful, returning for ref id {} ", thirdPartyService.getRefId());
							return;
						}
					}
					SfdcCreditCheckQueryRequest sfdcRequestBean = (SfdcCreditCheckQueryRequest) Utils
							.convertJsonToObject(thirdPartyService.getRequestPayload(), SfdcCreditCheckQueryRequest.class);
					sfdcRequestBean.setTpsId(thirdPartyService.getId());
					thirdPartyService.setRequestPayload(Utils.convertObjectToJson(sfdcRequestBean));
					LOGGER.info("service pay load credit check "+thirdPartyService.getRequestPayload());
					mqUtils.send(thirdPartyService.getQueueName(), thirdPartyService.getRequestPayload());
					thirdPartyService.setServiceStatus(SfdcServiceStatus.INPROGRESS.toString());
					thirdPartyService.setUpdatedBy("system");
					thirdPartyService.setUpdatedTime(new Date());
					thirdServiceJobsRepository.save(thirdPartyService);
					LOGGER.info("modified job {}, ref id {}", thirdPartyService.getId(), thirdPartyService.getRefId());
					
				} catch (TclCommonException | IllegalArgumentException e) {
					LOGGER.info("Throwing error in credit check job {}", e);
					throw new TclCommonRuntimeException(ExceptionConstants.RUNTIME_EXCEPTION_ON_CMD_CRON,  e);
				}
					LOGGER.info("SFDC Credit Check JOB Triggered for {}-{}", thirdPartyService.getRefId(), thirdPartyService.getId());
				
			});
			  
		} catch (Exception e) {
			LOGGER.error("Error in executing component cmd " + context.getJobDetail().getKey(), e);
		}
		LOGGER.trace("DatabaseNormalizerJob.execute method end.");

	}
	
	
	private String getSfdcId(String refId, QuoteRepository quoteRepository) {
		List<String> sfdcIds = quoteRepository.findTpsSfdcOptyIdByQuoteCode(refId);
		for (String sfdc : sfdcIds) {
			return sfdc;
		}
		return null;
	}
	
	private void processIncompleteRequests(ApplicationContext appCtx, ThirdPartyServiceJob sfdcServiceJob)
			throws TclCommonRuntimeException {
		QuoteRepository quoteRepository = appCtx.getBean(QuoteRepository.class);

		try {
			SfdcCreditCheckQueryRequest queryBean = (SfdcCreditCheckQueryRequest) Utils
						.convertJsonToObject(sfdcServiceJob.getRequestPayload(), SfdcCreditCheckQueryRequest.class);
			queryBean.setWhereClause("Opportunity_ID__c=" + "'" + getSfdcId(sfdcServiceJob.getRefId(), quoteRepository) + "'");
				sfdcServiceJob.setRequestPayload(Utils.convertObjectToJson(queryBean));
				sfdcServiceJob.setIsComplete(CommonConstants.BACTIVE);
			} catch (TclCommonException e) {
				throw new TclCommonRuntimeException(ExceptionConstants.ERROR_PROCESSING_SFDC_REQUEST,e);
			}
	}

}
