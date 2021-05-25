package com.tcl.dias.servicefulfillmentutils.delegates.network;

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

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("intlAccountCreationDelegate")
public class InternationalAccountCreationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(InternationalAccountCreationDelegate.class);

	@Autowired
	WorkFlowService workFlowService;

	@Value("${queue.intl.accountCreateSync.network}")
	String intlAccountCreateQueue;

	@Autowired
	MQUtils mqUtils;

	@Override
	public void execute(DelegateExecution execution) {
		logger.info("AccountCreationDelegate International invoked for {} id={}", execution.getCurrentActivityId(),
				execution.getId());
		try {
			workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String orderCode = (String) processMap.get(ORDER_CODE);
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode)
					.concat("#").concat(serviceId.toString());
			logger.info("Billing Account Creation for serviceCode={} PROCESS ID={}  Service Id={}", serviceCode,
					execution.getProcessInstanceId(),serviceId);
			String status = (String) mqUtils.sendAndReceive(intlAccountCreateQueue, req);

			logger.info("Billing Account Creation for serviceCode={} status={}", serviceCode, status);
			if (!StringUtils.isBlank(status) && status.equals("Success")) {
				execution.setVariable("intlAccountCreationAck", true);
			} else {
				execution.setVariable("intlAccountCreationAck", false);
				execution.setVariable("intlAccountCreationErrorMsg", status.split("Success|Fail")[1]);
			}
			logger.info("AccountCreationDelegate completed");			

		} catch (Exception e) {
			logger.error("Exception in International AccountCreationDelegate{}", e);
		}
		
		workFlowService.processServiceTaskCompletion(execution);

	}
}
