package com.tcl.dias.servicefulfillmentutils.delegates;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillmentutils.beans.IsValidBtsSyncBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("validateBtsDelegate")
public class ValidateBtsDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(ValidateBtsDelegate.class);
    public static final String VALIDATE_BTS_CALL_FAILURE_REASON = "validateBtsCallFailureReason";

    @Autowired
    MQUtils mqUtils;

    @Value("${queue.validatebtssync}")
    String validateBtsSyncQueue;

    @Autowired
    TaskDataService taskDataService;

    @Autowired
    WorkFlowService workFlowService;
    
	@Autowired
	TaskService taskService;

    public void execute(DelegateExecution execution) {
        logger.info("ValidateBtsDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
        String errorMessage="";
        Map<String, Object> taskDataMap = new HashMap<>();
        try {
            Task task = workFlowService.processServiceTask(execution);
            if (Objects.nonNull(task)) {
                taskDataMap = taskDataService.getTaskData(task);
                logger.info("taskDataMap {} ", taskDataMap);
            } else
                logger.info("Task is null in validate bts sync call delegate");

            IsValidBtsSyncBean isValidBtsSyncBean = validateBtsSyncBean(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId(), taskDataMap);
            String req = Utils.convertObjectToJson(isValidBtsSyncBean);
            logger.info("isValidBtsSyncRequest {} ", req);

            String isValidBtsSyncResponse = (String) mqUtils.sendAndReceive(validateBtsSyncQueue, req);

            logger.info("muxInfoSyncResponse {}", isValidBtsSyncResponse);
            if (StringUtils.isBlank(isValidBtsSyncResponse)) {
                execution.setVariable("validateBtsStatus", false);
                execution.setVariable(VALIDATE_BTS_CALL_FAILURE_REASON, CramerConstants.SYSTEM_ERROR);
                errorMessage=CramerConstants.SYSTEM_ERROR;
            }
            else {
                Response response = Utils.convertJsonToObject(isValidBtsSyncResponse, Response.class);
                if(Boolean.valueOf(response.getStatus())){
                    execution.setVariable("validateBtsStatus", true);
                }else {
                    execution.setVariable("validateBtsStatus", false);
                    execution.setVariable(VALIDATE_BTS_CALL_FAILURE_REASON, response.getErrorMessage());
                    errorMessage=response.getErrorMessage();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            execution.setVariable("validateBtsStatus", false);
            execution.setVariable(VALIDATE_BTS_CALL_FAILURE_REASON, CramerConstants.SYSTEM_ERROR);
            errorMessage=CramerConstants.SYSTEM_ERROR;
        }
        workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }

    /**
     * @param processInstanceId
     * @param taskDataMap
     * @return IsValidBtsSyncBean
     */
    private IsValidBtsSyncBean validateBtsSyncBean(String processInstanceId, Map<String, Object> taskDataMap) {
        IsValidBtsSyncBean validBtsSyncBean = new IsValidBtsSyncBean();
        validBtsSyncBean.setBtsIP("10.110.237.2");
        validBtsSyncBean.setCopfId(StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.ORDER_ID))));
        validBtsSyncBean.setProvider("WIMAX");
        validBtsSyncBean.setRequestId(processInstanceId);
        validBtsSyncBean.setScenarioType("Wireless");
        validBtsSyncBean.setSectorId("1");
        validBtsSyncBean.setServiceId(StringUtils.trimToEmpty(String.valueOf(taskDataMap.get(CramerConstants.SERVICE_CODE))));
        return validBtsSyncBean;
    }

}
