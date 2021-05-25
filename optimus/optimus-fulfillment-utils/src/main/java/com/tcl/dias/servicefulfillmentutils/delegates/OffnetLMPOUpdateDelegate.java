package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.AutoPoResponse;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("offnetLMPOUpdateDelegate")
public class OffnetLMPOUpdateDelegate implements JavaDelegate {
	private static final Logger LOGGER = LoggerFactory.getLogger(OffnetLMPOUpdateDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	TaskService taskService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	SapService sapService;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;


	public void execute(DelegateExecution execution) {

		String errorMessage = "";

		try {
			Task task = workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
		
			LOGGER.info("offnetLMPOUpdateDelegate  invoked for {} serviceCode={}, serviceId={}",execution.getCurrentActivityId(), serviceCode, serviceId);
		
		
			errorMessage = sapService.processOffnetAutoPoUpdate(serviceId,execution);
	
		} catch (Exception e) {
			LOGGER.error("offnetLMPOUpdateDelegate  Exception {} ", e);

			execution.setVariable("offnetPOUpdateCompleted", false);
		}		
		workFlowService.processServiceTaskCompletionWithAction(execution, errorMessage,"SUCCESS");

	}

}
