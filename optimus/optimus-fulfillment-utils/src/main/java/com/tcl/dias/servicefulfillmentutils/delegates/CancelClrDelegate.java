package com.tcl.dias.servicefulfillmentutils.delegates;
import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

@Component("cancelClrDelegate")
public class CancelClrDelegate implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreateCLRDelegate.class);

    @Autowired
    WorkFlowService workFlowService;

    @Autowired
    TaskService taskService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    MQUtils mqUtils;

    @Autowired
    CommonFulfillmentUtils commonFulfillmentUtils;

    @Value("${oms.set.lrexport.enable}")
    String setLRexportEnbale;
    
	@Autowired
	RuntimeService runtimeService;
	



	public void execute(DelegateExecution execution) {
		LOGGER.info("CancelClrDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());
		String errorMessage = "";
		String serviceCode ="";
		try {
			Map<String, Object> processMap = execution.getVariables();
			Integer serviceId = (Integer) processMap.get(MasterDefConstants.SERVICE_ID);
			 serviceCode = (String) processMap.get(MasterDefConstants.SERVICE_CODE);
			String orderCode = (String) processMap.get(MasterDefConstants.ORDER_CODE);
			String cancellationInitiatedBy = (String) processMap.get("cancellationInitiatedBy");
			String retainExistingNwresource = (String) processMap.get("retainExistingNwresource");
			LOGGER.info("Cance clr service id :{} and cancellationInitiatedBy:{}", serviceId,cancellationInitiatedBy);
			Boolean clrCacelFlag = taskService.cancelClrResource(serviceId, cancellationInitiatedBy,retainExistingNwresource);
			if (clrCacelFlag) {
				errorMessage = "Cancel Clr success";
				if (cancellationInitiatedBy != null && "Move to M6".equalsIgnoreCase(cancellationInitiatedBy)) {
					String reponse = (String) mqUtils.sendAndReceive(setLRexportEnbale, orderCode);
					LOGGER.info("LR export enable queue response : {} ", reponse);

				}

			} else {
				errorMessage = "Cancel Clr faliure";
			}
			workFlowService.processServiceTaskCompletion(execution, errorMessage);

		} catch (Exception ex) {
			LOGGER.error("CancelClrDelegate  Exception:{} with service:{} ", ex,serviceCode);
		}
	}
    
}
