package com.tcl.dias.servicefulfillmentutils.delegates;

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
import com.tcl.dias.servicefulfillment.entity.repository.ActivityRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstActivityDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.CramerInfoBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
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
@Component("createCLRDelegate")
public class CreateCLRDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(CreateCLRDelegate.class);

	@Autowired
	MQUtils mqUtils;
	@Value("${queue.createclrsync}")
	String createCLRSyncQueue;
	
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
	
	public void execute(DelegateExecution execution) {
		logger.info("CreateCLRDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		String errorMessage="";
		String errorCode="500";

		Task task = workFlowService.processServiceTask(execution);

		try {
		
			CramerInfoBean cramerInfoBean = new CramerInfoBean();
			cramerInfoBean.setTaskId(task.getId());
			cramerInfoBean.setProcessInstanceId(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId());
			String req = Utils.convertObjectToJson(cramerInfoBean);
			
			logger.info("CreateCLRRequest {}", req);

			String createCLRSyncResponse = (String) mqUtils.sendAndReceive(createCLRSyncQueue, req,120000);
			logger.info("createCLRSyncResponse= {} ", createCLRSyncResponse);
			if (StringUtils.isBlank(createCLRSyncResponse)) {
				execution.setVariable("isCLRSyncCallSuccess", false);
                errorMessage = CramerConstants.SYSTEM_ERROR;
            } else {
                Response response = Utils.convertJsonToObject(createCLRSyncResponse, Response.class);
                if(response.getStatus()!=null && response.getStatus()) {   
                	logger.info("response status success");
                	if(Objects.nonNull(response.getData()) && "skip".equalsIgnoreCase(response.getData())){
                		logger.info("skipClr");
                		 execution.setVariable("action", "skipCLR");
                		 execution.setVariable("isCLRSyncCallSuccess", false);
                		 execution.setVariable("serviceDesignCompleted", false);
                		 errorMessage = "Skip Clr Chosen from Select Mux";
                	}/*else if(Objects.nonNull(response.getData()) && "skipExistClr".equalsIgnoreCase(response.getData())){
                		logger.info("skipExistClr");
	               		 execution.setVariable("action", "skipCLR");
	               		 execution.setVariable("isCLRSyncCallSuccess", true);
	               		 execution.setVariable("serviceDesignCompleted", true);
                	}*/else if(Objects.nonNull(response.getData()) && "additionalIpScenario".equalsIgnoreCase(response.getData())){
                		logger.info("CLR Delegate::additionalIpScenario");
	               		execution.setVariable("action", "skipCLR");
	               		execution.setVariable("isCLRSyncCallSuccess", false);
	               		execution.setVariable("serviceDesignCompleted", false);
	               		errorMessage="Please configure Additional Ip in CLR!";
                	}else{
                		 logger.info("isCLRSyncCallSuccess");
                		 execution.setVariable("isCLRSyncCallSuccess", true);
                	}
                }else{
                    execution.setVariable("isCLRSyncCallSuccess", false);
                    errorMessage = response.getErrorMessage();
                    errorCode=response.getErrorCode();
                }
			}
		} catch (Exception e) {
            logger.error("CreateCLRDelegate Exception", e);
            execution.setVariable("isCLRSyncCallSuccess", false);
            execution.setVariable("serviceDesignCallFailureReason", CramerConstants.SYSTEM_ERROR);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
		String message = StringUtils.trimToEmpty(errorMessage);
		if (StringUtils.isNotBlank(message)) {			
				try {
					logger.info("CreateCLRRequest error log started");

					componentAndAttributeService.updateAdditionalAttributes(task.getServiceId(),
							"serviceDesignCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
							AttributeConstants.ERROR_MESSAGE, "enrich-service-design");

				} catch (Exception e) {
					logger.error("createCLRDelegate getting error message details----------->{}", e);
				}		

		}
				workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }

}
