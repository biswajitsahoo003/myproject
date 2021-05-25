package com.tcl.dias.servicefulfillmentutils.delegates.ipc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 *  Account Creation Delegate for IPC products - New order 
 *  
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("accountCreationDelegate")
public class AccountCreationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(AccountCreationDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.accountCreateSync}")
	String accountCreateQueue;
    
    @Autowired
	MQUtils mqUtils;

    @Override
   	public void execute(DelegateExecution execution) {
   		logger.info("AccountCreationDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
   		try {
   			workFlowService.processServiceTask(execution);
   			Map<String, Object> processMap = execution.getVariables();
   			String orderCode = (String) processMap.get(ORDER_CODE);
   			String serviceCode = (String) processMap.get(SERVICE_CODE);
   			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
   			Boolean isInternationalBilling = (Boolean) (processMap.containsKey(CommonConstants.IS_IPC_BILLING_INTL) ? processMap.get(CommonConstants.IS_IPC_BILLING_INTL) : false);
   			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode).concat("#").concat(serviceId.toString()).concat("#").concat(isInternationalBilling.toString());
   			logger.info("Account Creation for ORDER ID {} PROCESS ID {}",orderCode , execution.getProcessInstanceId());
   			String status = (String) mqUtils.sendAndReceive(accountCreateQueue, req);
   			if(!StringUtils.isBlank(status) && status.equals("Success"))
   			{
   				execution.setVariable("accountCreationAck", true);
   			}
   			else {
   				execution.setVariable("accountCreationAck", false);
   				execution.setVariable("accountCreationErrorMsg", (status!= null && status.split("Success|Fail").length >1) ? status.split("Success|Fail")[1] : "Failure");
               }
   			logger.info("AccountCreationDelegate completed");
   			workFlowService.processServiceTaskCompletion(execution);
   			
   		} catch (Exception e) {
   			logger.error("Exception in AccountCreationDelegate{}", e);
   		}

   	}





}
