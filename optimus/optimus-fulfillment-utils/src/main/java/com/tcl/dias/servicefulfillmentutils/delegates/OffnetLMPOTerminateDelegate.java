package com.tcl.dias.servicefulfillmentutils.delegates;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

/**
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@Component("OffnetLMPOTerminateDelegate")
public class OffnetLMPOTerminateDelegate implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(OffnetLMPODelegate.class);

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
        String errorCode="500";

        Task task = workFlowService.processServiceTask(execution);
        try {
            LOGGER.error("Entering OffnetLMPOTerminateDelegate ");

            Map<String, Object> processMap = execution.getVariables();
            String serviceCode = (String) processMap.get(SERVICE_CODE);
            Integer serviceId = (Integer) processMap.get(SERVICE_ID);
        	String siteType=null;
    		siteType = StringUtils.trimToEmpty((String) processMap.get("site_type"));
    		if (StringUtils.isBlank(siteType)) {
    			siteType = "A";
    		}
            LOGGER.info("offnetLMPOTerminateDelegate  invoked for {} serviceCode={}, serviceId={}",execution.getCurrentActivityId(), serviceCode, serviceId);
            errorMessage = sapService.processAutoPoTerminate(serviceId,execution,siteType);
            LOGGER.error("Exiting OffnetLMPOTerminateDelegate ");


        } catch (Exception e) {
            LOGGER.error("OffnetLMPOTerminateDelegate  Exception {} ", e);

            execution.setVariable("offnetPOTerminateCompleted", false);
            errorMessage=e.getMessage();
        }
        String message = StringUtils.trimToEmpty(errorMessage);
        if (StringUtils.isNotBlank(message)) {
            try {
                LOGGER.info("Offnet Terminate PO error log started");

                componentAndAttributeService.updateAdditionalAttributes(task.getServiceId(),
                        "offnetPOTerminateFailureReason",
                        componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
                        AttributeConstants.ERROR_MESSAGE, "terminate-offnet-po");

            } catch (Exception e) {
                LOGGER.error("Offnet Terminate PO getting error message details----------->{}", e);
                errorMessage=e.getMessage();
            }

        }


        workFlowService.processServiceTaskCompletionWithAction(execution, errorMessage,"SUCCESS");

    }

}
