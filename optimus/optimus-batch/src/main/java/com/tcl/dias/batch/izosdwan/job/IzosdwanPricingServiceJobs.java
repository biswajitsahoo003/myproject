package com.tcl.dias.batch.izosdwan.job;

import java.util.Date;
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

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.entity.entities.IzosdwanPricingService;
import com.tcl.dias.oms.entity.repository.IzosdwanPricingServiceRepository;
/**
 * 
 * This file contains the IzosdwanPricingServiceJobs.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@DisallowConcurrentExecution
public class IzosdwanPricingServiceJobs implements Job {

	private static final Logger LOGGER = LoggerFactory.getLogger(IzosdwanPricingServiceJobs.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			LOGGER.info("Inside execute method of IzosdwanPricingServiceJobs!!");
			JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
			ApplicationContext appCtx = (ApplicationContext) jobDataMap.get("appCtx");
			MQUtils mqUtils = appCtx.getBean(MQUtils.class);
			IzosdwanPricingServiceRepository izosdwanPricingServiceRepository = appCtx
					.getBean(IzosdwanPricingServiceRepository.class);
			List<IzosdwanPricingService> izosdwanPricingServices = izosdwanPricingServiceRepository.findPricingServicesDetailsByStatus(CommonConstants.NEW_STATUS,CommonConstants.STANDARD);
			if(izosdwanPricingServices!=null && !izosdwanPricingServices.isEmpty()) {
				LOGGER.info("Inside if of IzosdwanPricingServiceJobs!!");
				LOGGER.info("Got new records for processing pricing services !!");
				updateIzosdwanPricingServiceStatus(CommonConstants.INPROGESS_STATUS, izosdwanPricingServices, izosdwanPricingServiceRepository);
				LOGGER.info("Inside IzosdwanPricingServiceJobs before queue call!!");
				mqUtils.send("rabbitmq_oms_izosdwan_quote_price_service", Utils.convertObjectToJson(izosdwanPricingServices));
				LOGGER.info("Inside IzosdwanPricingServiceJobs after queue call!!");
			}
		} catch (Exception e) {
			LOGGER.error("Error occured while triggering the IzosdwanPricingServiceJobs ", e);
		}

	}
	
	public void updateIzosdwanPricingServiceStatus(String status,List<IzosdwanPricingService> izosdwanPricingServices,IzosdwanPricingServiceRepository izosdwanPricingServiceRepository) {
		LOGGER.info("Updating the pricing service status to {}",status);
		izosdwanPricingServices.stream().forEach(services->{
			services.setStatus(status);
			services.setUpdatedBy(Utils.getSource());
			services.setUpdatedTime(new Date());
		});
		izosdwanPricingServiceRepository.saveAll(izosdwanPricingServices);
	}

}
