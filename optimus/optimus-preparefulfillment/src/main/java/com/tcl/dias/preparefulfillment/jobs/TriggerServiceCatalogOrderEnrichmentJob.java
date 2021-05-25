package com.tcl.dias.preparefulfillment.jobs;

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

import com.tcl.dias.preparefulfillment.servicefulfillment.service.ServiceFulfillmentService;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceFulfillmentJob;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceFulfillmentJobRepository;

/**
 * 
 * This file contains the TriggerServiceCatalogJob.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class TriggerServiceCatalogOrderEnrichmentJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerServiceCatalogOrderEnrichmentJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("Starting Trigger Service Catalog Job ");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
		ServiceFulfillmentJobRepository serviceFulfillmentJobRepository = appCtx
				.getBean(ServiceFulfillmentJobRepository.class);
		ServiceFulfillmentService serviceFulfillmentService = appCtx.getBean(ServiceFulfillmentService.class);
		List<ServiceFulfillmentJob> serviceFulfillments = serviceFulfillmentJobRepository.findByStatusAndType("FAILURE",
				"ORDER_ENRICHMENT");
		try {
			for (ServiceFulfillmentJob serviceFulfillmentJob : serviceFulfillments) {
				//serviceFulfillmentJob.setStatus("SUCCESS");
				//serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
				 serviceFulfillmentService.initiateBasicEnrichment(serviceFulfillmentJob.getServiceId(),serviceFulfillmentJob.getErfOdrServiceId(),null);
				//serviceFulfillmentService.retriggerOE(serviceFulfillmentJob.getServiceId(),
					//	serviceFulfillmentJob.getErfOdrServiceId());

			}
		} catch (Exception e) {
			LOGGER.error("Error in service catalogue", e);
		}

	}

}
