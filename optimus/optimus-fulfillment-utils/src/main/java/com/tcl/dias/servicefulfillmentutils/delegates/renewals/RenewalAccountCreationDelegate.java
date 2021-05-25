package com.tcl.dias.servicefulfillmentutils.delegates.renewals;

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
@Component("renewalAccountCreationDelegate")
public class RenewalAccountCreationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(RenewalAccountCreationDelegate.class);
		
		@Autowired
		WorkFlowService workFlowService;
		
		@Autowired
		ScServiceDetailRepository scServiceDetailRepository;
		
	public void execute(DelegateExecution execution) {
		logger.info("RenewalAccountCreationDelegate.execute method invoked");
		Integer serviceId = (Integer) execution.getVariable("renewalServiceId");
		String errorMessage = "";
		logger.info("RenewalAccountCreation.ServiceId::{}",serviceId);
		Optional<ScServiceDetail> scServiceDetailOptional=scServiceDetailRepository.findById(serviceId);
		if(scServiceDetailOptional.isPresent()) {
			execution.setVariable("serviceCode", scServiceDetailOptional.get().getUuid());
		}
		execution.setVariable("serviceId", serviceId);
		logger.info("RenewalAccountCreationDelegate.Execution Variables:{}", execution.getVariables());
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
	}
}
