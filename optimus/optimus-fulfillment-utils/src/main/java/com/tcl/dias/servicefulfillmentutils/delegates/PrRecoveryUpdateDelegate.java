package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("prRecoveryUpdateDelegate")
public class PrRecoveryUpdateDelegate implements JavaDelegate {
		private static final Logger logger = LoggerFactory.getLogger(PrRecoveryUpdateDelegate.class);
	
		@Autowired
		MQUtils mqUtils;
		
		@Autowired
		ScServiceDetailRepository scServiceDetailRepository;
		
		@Autowired
		ComponentAndAttributeService componentAndAttributeService;
		
		@Autowired
		TaskService taskService;
		
		@Autowired
		WorkFlowService workFlowService;

	public void execute(DelegateExecution execution) {
		logger.info("PrRecoveryUpdateDelegate invoked for {} Id={}", execution.getCurrentActivityId(),
				execution.getId());

		String errorMessage = "";
		String errorCode="";

		try {
			Task task = workFlowService.processServiceTask(execution);

			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			String parentServiceCode = (String) processMap.get("parentServiceCode");
			
			Map<String, String> ipConfigRequestMapper = new HashMap<>();
			ipConfigRequestMapper.put("SERVICE_ID", serviceCode);			
			logger.info("PrRecoveryUpdateDelegate parentServiceCode= {} , serviceCode={}",parentServiceCode,serviceCode);

		} catch (Exception e) {
			logger.error("PrRecoveryUpdateDelegate Exception {} ", e);
			errorMessage = CramerConstants.SYSTEM_ERROR;
			errorCode="500";

		}
	
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);

	}

}
