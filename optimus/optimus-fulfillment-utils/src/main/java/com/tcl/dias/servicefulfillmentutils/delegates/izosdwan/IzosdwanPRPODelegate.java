package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("sdwanPRPODelegate")
public class IzosdwanPRPODelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanPRPODelegate.class);
		
		@Autowired
		WorkFlowService workFlowService;
		
		@Autowired
		ScComponentRepository scComponentRepository;
		
		@Autowired
		ScComponentAttributesRepository scComponentAttributesRepository;
		
	public void execute(DelegateExecution execution) {
			logger.info("SdwanPRPODelegate.execute method invoked");
			String vendorCode = (String) execution.getVariable("vendorCode");
			Integer serviceId = (Integer) execution.getVariable("serviceId");
			Integer cpeOverlayComponentId = (Integer) execution.getVariable("cpeOverlayComponentId");
			Map<String,String> vendorDetails = (Map<String,String>) execution.getVariable("vendorDetails");
			logger.info("serviceId={},vendorCode={},cpeOverlayComponentId={},vendorDetails={},excutionProcessInstId={}", serviceId,vendorCode,cpeOverlayComponentId,vendorDetails,execution.getProcessInstanceId());
			String errorMessage = "";
			execution.setVariable("vendorCode", vendorCode);
			execution.setVariable("vendorName", vendorDetails.get(vendorCode));
			logger.info("Execution Variables:{}", execution.getVariables());
			workFlowService.processServiceTask(execution);
	        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
	}
}
