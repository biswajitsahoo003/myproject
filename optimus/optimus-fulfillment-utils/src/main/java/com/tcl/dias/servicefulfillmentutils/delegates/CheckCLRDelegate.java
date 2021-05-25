package com.tcl.dias.servicefulfillmentutils.delegates;

import java.util.Optional;

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
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.CramerInfoBean;
import com.tcl.dias.servicefulfillmentutils.beans.Response;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.CramerConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("checkCLRDelegate")
public class CheckCLRDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(CheckCLRDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Value("${queue.checkclrinfosync}")
	String checkCLRSyncQueue;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	TaskService taskService;

	@Autowired
    ComponentAndAttributeService componentAndAttributeService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Value("${application.env:DEV}")
	String appEnv;

	public void execute(DelegateExecution execution) {
        logger.info("CheckCLRDelegate invoked for {} ", execution.getCurrentActivityId());
        String errorMessage="";
		String errorCode="";

        Task task = null;
        try {
            task = workFlowService.processServiceTask(execution);
            CramerInfoBean cramerInfoBean = new CramerInfoBean();
            cramerInfoBean.setServiceCode(task.getServiceCode());
            cramerInfoBean.setProcessInstanceId(taskService.getRandomNumberForCrammer()+execution.getProcessInstanceId());
            String req = Utils.convertObjectToJson(cramerInfoBean);

            String checkCLRSyncResponse = (String) mqUtils.sendAndReceive(checkCLRSyncQueue, req);
            logger.error("checkCLRSyncResponse => {} ", checkCLRSyncResponse);

            if (StringUtils.isBlank(checkCLRSyncResponse)) {
                execution.setVariable("checkCLRSuccess", false);
                execution.setVariable("serviceDesignCallFailureReason", CramerConstants.SYSTEM_ERROR);
                errorMessage = CramerConstants.SYSTEM_ERROR;
            }else {
                Response response = Utils.convertJsonToObject(checkCLRSyncResponse, Response.class);
                if (response.getStatus()!=null && response.getStatus() ) {
                    execution.setVariable("checkCLRSuccess", true);
                    
                    String serviceContents = StringUtils.trimToEmpty(response.getData()).toLowerCase();
                    logger.error("serviceContents => {} ", serviceContents);
                    execution.setVariable("serviceContents",serviceContents);
                    
                    if((serviceContents.contains("tx") || serviceContents.contains("sdh")) && !serviceContents.contains("mpls")) {
                    	execution.setVariable("isSoftLoopPossibleAtCE",true);
                    }
                    
                } else {
                    execution.setVariable("checkCLRSuccess", false);
                    errorCode=response.getErrorCode();
                    if(response.getErrorMessage()!=null) {
                        execution.setVariable("serviceDesignCallFailureReason",response.getErrorMessage());
                        errorMessage = response.getErrorMessage();
                    }else {
                        execution.setVariable("serviceDesignCallFailureReason", CramerConstants.SYSTEM_ERROR);
                        errorMessage = CramerConstants.SYSTEM_ERROR;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("CheckCLRDelegate Exception {} ", e);
            execution.setVariable("checkCLRSuccess", false);
            execution.setVariable("serviceDesignTxCallFailureReason",CramerConstants.SYSTEM_ERROR);
            errorMessage = CramerConstants.SYSTEM_ERROR;
        }
        errorMessage = StringUtils.trimToEmpty(errorMessage);
        if (StringUtils.isNotBlank(errorMessage)) {
        Optional<ScServiceDetail> scServiceDetail=	scServiceDetailRepository.findById(task.getServiceId());
			if (scServiceDetail.isPresent()&& StringUtils.isNotBlank(errorMessage)) {
				
				try {
					logger.info("CheckCLRDelegate error log started");


					componentAndAttributeService.updateAdditionalAttributes(scServiceDetail.get(),
							"serviceDesignTxCallFailureReason",
							componentAndAttributeService.getErrorMessageDetails(errorMessage, errorCode),
							AttributeConstants.ERROR_MESSAGE, "check-clr");

				}
				catch (Exception e) {
					logger.error("checkCLRDelegate getting error message details----------->{}", e);
				}
				}
        }
        
		if (appEnv!=null && "DEV".equalsIgnoreCase(appEnv)) {
			execution.setVariable("checkCLRSuccess", true);
		}
        workFlowService.processServiceTaskCompletion(execution,errorMessage);
    }
}
