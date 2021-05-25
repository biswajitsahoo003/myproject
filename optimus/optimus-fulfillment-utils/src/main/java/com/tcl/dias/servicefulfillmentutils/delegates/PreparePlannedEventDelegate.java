package com.tcl.dias.servicefulfillmentutils.delegates;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.beans.CreatePlannedEventBean;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("preparePlannedEventDelegate")
public class PreparePlannedEventDelegate implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(PreparePlannedEventDelegate.class);

    @Autowired
    WorkFlowService workFlowService;

    @Autowired
    TaskDataService taskDataService;

    @Value("${queue.serviceactivation.availabilityslots}")
    private String availabilitySlotQueue;

    @Autowired
    MQUtils mqUtils;
    
	@Autowired
	TaskService taskService;


    public void execute(DelegateExecution execution) {
        logger.info("PreparePlannedEventDelegate invoked for {} ", execution.getCurrentActivityId());
        String errorMessage = "";
        try {
            workFlowService.processServiceTask(execution);
            Map<String, Object> executionVariables = execution.getVariables();
            Integer serviceId = (Integer) executionVariables.get(MasterDefConstants.SERVICE_ID);
            String processInstanceId = execution.getProcessInstanceId();
            String serviceCode = StringUtils.trimToEmpty((String)executionVariables.get(MasterDefConstants.SERVICE_CODE));
            String siteType = StringUtils.trimToEmpty((String)executionVariables.get("site_type"));
    		if(StringUtils.isBlank(siteType))siteType="A";
    		
            String response = (String) mqUtils.sendAndReceive(availabilitySlotQueue, Utils.convertObjectToJson(new CreatePlannedEventBean(serviceId,taskService.getRandomNumberForCrammer()+processInstanceId,siteType,serviceCode)));
            logger.info("PreparePlannedEventDelegate queue response - >>>>>>>" ,response);
            if (!StringUtils.isEmpty(response)) {
                execution.setVariable("preparePlannedEventSuccess", true);
            }
        } catch (Exception e) {
            logger.error("PreparePlannedEventDelegate Exception {} ", e);
            execution.setVariable("preparePlannedEventSuccess", false);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
        workFlowService.processServiceTaskCompletion(execution, errorMessage);
    }
}
