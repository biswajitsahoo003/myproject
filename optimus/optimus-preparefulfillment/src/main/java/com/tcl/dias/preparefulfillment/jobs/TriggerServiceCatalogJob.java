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

import com.tcl.dias.common.servicefulfillment.beans.ServiceFulfillmentRequest;
import com.tcl.dias.preparefulfillment.service.ProcessL2OService;
import com.tcl.dias.preparefulfillment.service.ServiceCatalogueService;
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
public class TriggerServiceCatalogJob implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerServiceCatalogJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LOGGER.info("Starting Trigger Service Catalog Job ");
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
		ServiceFulfillmentJobRepository serviceFulfillmentJobRepository = appCtx
				.getBean(ServiceFulfillmentJobRepository.class);
		ServiceCatalogueService serviceCatalogueService = appCtx.getBean(ServiceCatalogueService.class);
		ProcessL2OService processL2OService = appCtx.getBean(ProcessL2OService.class);

		List<ServiceFulfillmentJob> serviceFulfillments = serviceFulfillmentJobRepository.findByStatusAndType("NEW","NEW_SERVICE");
		for (ServiceFulfillmentJob serviceFulfillmentJob : serviceFulfillments) {

			ServiceFulfillmentRequest svcFulfillment = serviceCatalogueService
					.processServiceFulFillmentData(serviceFulfillmentJob.getServiceId());
			serviceFulfillmentJob.setStatus("INPROGRESS");
			serviceFulfillmentJobRepository.save(serviceFulfillmentJob);
			boolean ipcFlag = svcFulfillment.getOrderInfo().getOptimusOrderCode().startsWith("IPC");
			boolean nplFlag = svcFulfillment.getOrderInfo().getOptimusOrderCode().startsWith("NPL");
			if(ipcFlag) {
				LOGGER.info("TriggerServiceCatalogJob - IPC work flow triggered for the order code: {}", svcFulfillment.getOrderInfo().getOptimusOrderCode());
				processL2OService.processIPCL2ODataToFlowable(svcFulfillment);
			} else if(nplFlag) {
				LOGGER.info("TriggerServiceCatalogJob processNPLL2ODataToFlowable: {}", serviceFulfillmentJob.getServiceId());
				processL2OService.processNPLL2ODataToFlowable(serviceFulfillmentJob.getServiceId(), null,true);
			}else {
				LOGGER.info("TriggerServiceCatalogJob processL2ODataToFlowable: {}", serviceFulfillmentJob.getServiceId());
				processL2OService.processL2ODataToFlowable(serviceFulfillmentJob.getServiceId(),null,true);
			}
		}

	}

}
