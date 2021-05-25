package com.tcl.dias.servicefulfillmentutils.delegates.renewals;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
@Component("renewalBillingHandoverDelegate")
public class RenewalBillingHandoverDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(RenewalBillingHandoverDelegate.class);
		
		@Autowired
		WorkFlowService workFlowService;
		
		@Autowired
		ScServiceDetailRepository scServiceDetailRepository;
		
	public void execute(DelegateExecution execution) {
		logger.info("RenewalBillingHandoverDelegate.execute method invoked");
		Integer serviceId = (Integer) execution.getVariable("renewalServiceId");
		String errorMessage = "";
		String serviceCode = "";
		logger.info("RenewalBillingHandover.ServiceId::{}",serviceId);
		Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(serviceId);
		if(scServiceDetailOptional.isPresent()) {
			serviceCode = scServiceDetailOptional.get().getUuid();
			execution.setVariable("serviceCode", scServiceDetailOptional.get().getUuid());
			ScServiceDetail scServiceDetail=scServiceDetailOptional.get();
			scServiceDetail.setBillingStatus("ACTIVE");
			scServiceDetail.setBillingCompletedDate(new Timestamp(new Date().getTime()));
			scServiceDetailRepository.saveAndFlush(scServiceDetail);
		}
		
		ScServiceDetail scServiceDetail = scServiceDetailRepository.findFirstByUuidAndIsMigratedOrder(serviceCode, "Y");
		if (scServiceDetail != null){
			execution.setVariable("lrTerminationRequired", true);
			execution.setVariable("optimusMacd", false);
		} else {
			execution.setVariable("lrTerminationRequired", false);
			execution.setVariable("optimusMacd", true);
		}
		
		execution.setVariable("serviceId", serviceId);
		logger.info("RenewalBillingHandoverDelegate.Execution Variables:{}", execution.getVariables());
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
	}
}
