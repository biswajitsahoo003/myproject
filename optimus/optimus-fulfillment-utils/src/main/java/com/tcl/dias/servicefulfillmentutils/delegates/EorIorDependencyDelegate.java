package com.tcl.dias.servicefulfillmentutils.delegates;

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
import com.tcl.dias.servicefulfillment.entity.repository.ActivityRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstActivityDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.EorIorDependencyBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("eorIorDependencyDelegate")
public class EorIorDependencyDelegate implements JavaDelegate {
    private static final Logger logger = LoggerFactory.getLogger(EorIorDependencyDelegate.class);

    @Autowired
    MQUtils mqUtils;

    @Value("${queue.eorior.dependency}")
    String eorIorDependencyQueue;

    @Autowired
    MstTaskDefRepository mstTaskDefRepository;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskCacheService taskCacheService;

    @Autowired
    MstActivityDefRepository mstActivityDefRepository;

    @Autowired
    TaskDataService taskDataService;

    @Autowired
    TaskService taskService;

    @Autowired
    WorkFlowService workFlowService;

    @Autowired
    ComponentAndAttributeService componentAndAttributeService;


    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;
    
    @Value("${application.env:DEV}")
	String appEnv;

    public void execute(DelegateExecution execution) {
        logger.info("EorIorDependencyDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
        String errorMessage = "";
        String errorCode = "500";

        Task task = workFlowService.processServiceTask(execution);
        try {
            EorIorDependencyBean eorIorDependencyBean = new EorIorDependencyBean();
            eorIorDependencyBean.setTaskId(task.getId());
            eorIorDependencyBean.setServiceCode(task.getServiceCode());
            eorIorDependencyBean.setProcessInstanceId(taskService.getRandomNumberForCrammer() + execution.getProcessInstanceId());
            String req = Utils.convertObjectToJson(eorIorDependencyBean);

            logger.info("EorIorDependencyRequest {}", req);

            String eorIorDependencySyncResponse = (String) mqUtils.sendAndReceive(eorIorDependencyQueue, req, 90000);
            logger.info("eorIorDependencySyncResponse= {} ", eorIorDependencySyncResponse);
            if (StringUtils.isBlank(eorIorDependencySyncResponse)) {
                execution.setVariable("hasIOREORDepandency", false);
            } else {
                Response response = Utils.convertJsonToObject(eorIorDependencySyncResponse, Response.class);
                if (response.getStatus() != null && response.getStatus()) {
                    execution.setVariable("hasIOREORDepandency", true);
                    errorMessage = response.getErrorMessage();
                    errorCode = response.getErrorCode();
                } else {
                    execution.setVariable("hasIOREORDepandency", false);                   
                }
                
            }
        } catch (Exception e) {
            logger.error("EorIorDependencyDelegate Exception", e);
            execution.setVariable("hasIOREORDepandency", true);
        }
        String message = StringUtils.trimToEmpty(errorMessage);
        if (StringUtils.isNotBlank(message)) {
            try {
                logger.info("eorIorDependencyDelegate error log started");

                componentAndAttributeService.updateAdditionalAttributes(task.getServiceId(),
                        "hasIOREORDepandency",
                        componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
                        AttributeConstants.ERROR_MESSAGE, "check-eor-ior-dependency");

            } catch (Exception e) {
                logger.error("eorIorDependencyDelegate getting error message details----------->{}", e);
            }

        }
		if (appEnv!=null && "DEV".equalsIgnoreCase(appEnv)) {
			execution.setVariable("hasIOREORDepandency", false);
		}
        workFlowService.processServiceTaskCompletion(execution, errorMessage);
    }
}

