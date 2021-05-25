package com.tcl.dias.batch.izosdwan.job;

import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.oms.entity.entities.QuoteIzosdwanByonUploadDetail;
import com.tcl.dias.oms.entity.repository.QuoteIzosdwanByonUploadDetailRepository;

/**
 * 
 * This job is the retrigger the failure BYON uploaded records
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class IzosdwanByonFailureRetriggerJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanByonFailureRetriggerJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
			QuoteIzosdwanByonUploadDetailRepository quoteIzosdwanByonUploadDetailRepository = appCtx
					.getBean(QuoteIzosdwanByonUploadDetailRepository.class);
			List<QuoteIzosdwanByonUploadDetail> quoteIzosdwanByonUploadDetails = quoteIzosdwanByonUploadDetailRepository
					.findByStatus(IzosdwanCommonConstants.FAILURE);
			if (quoteIzosdwanByonUploadDetails != null && !quoteIzosdwanByonUploadDetails.isEmpty()) {
				quoteIzosdwanByonUploadDetails.stream().forEach(details -> {
					if (details.getQuote() != null && details.getQuote().getId() != null) {
						details.setStatus(IzosdwanCommonConstants.OPEN);
					}
				});
				quoteIzosdwanByonUploadDetailRepository.saveAll(quoteIzosdwanByonUploadDetails);
			}

		} catch (Exception e) {
			LOGGER.error("Error occured while excecuting the IzosdwanByonFailureRetriggerJob ",e);
		}

	}

}
