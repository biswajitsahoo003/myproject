package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;
import java.util.Objects;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.RepcService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

@Component("getSitesDelegate")
public class GetSitesDelegate implements JavaDelegate {
	
private static final Logger logger = LoggerFactory.getLogger(GetSitesDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	RepcService repcService;
	
	@Override
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside get Sites delegate variables {}", executionVariables);
			String customerId = null;
			workFlowService.processServiceTask(execution);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			customerId = (String) executionVariables.get(GscConstants.CUSTOMER_ORG_ID);
			
			boolean success = repcService.requestForGetSites(serviceCode, serviceId, customerId,execution.getProcessInstanceId());
			if (success) {
				execution.setVariable(GscConstants.KEY_GET_SITES_STATUS, GscConstants.VALUE_SUCCESS);
			} else {
				execution.setVariable(GscConstants.KEY_GET_SITES_STATUS, GscConstants.VALUE_FAILED);
			}
		} catch (Exception e) {
			logger.error("Exception in get Sites {}", e);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}

}
