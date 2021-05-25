package com.tcl.dias.servicefulfillmentutils.delegates.teamsdr;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Trigger TeamsDR O2C Delegate
 *
 * @author Srinivasa Raghavan
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("triggerTeamsDRO2CDelegate")
public class TriggerTeamsDRO2CDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerTeamsDRO2CDelegate.class);

	@Value("${rabbitmq.o2c.teamsdr.trigger}")
	private String o2cTriggerQueue;

	@Autowired
	private WorkFlowService workFlowService;

	@Autowired
	private MQUtils mqUtils;

	/**
	 * Execute trigger teamsDR O2C delegate
	 *
	 * @param execution
	 */
	public void execute(DelegateExecution execution) {
		LOGGER.info("TriggerTeamsDRO2CDelegate.execute method invoked");
		try {
			workFlowService.processServiceTask(execution);
			String errorMessage = "";
			Integer serviceId = (Integer) execution.getVariable("serviceId");
			String serviceCode = (String) execution.getVariable("serviceCode");
			String orderCode = (String) execution.getVariable("orderCode");
			LOGGER.info("TriggerTeamsDRO2CDelegate.serviceId={},serviceCode={},orderCode={},excutionProcessInstId={}",
					serviceId, serviceCode, orderCode, execution.getProcessInstanceId());
			mqUtils.send(o2cTriggerQueue, orderCode);
			LOGGER.info("Execution Variables:{}", execution.getVariables());
			workFlowService.processServiceTaskCompletion(execution, errorMessage);
		} catch (Exception e) {
			LOGGER.error("TriggerTeamsDRO2CDelegate------------------- getting error details----------->{}", e);
		}
	}
}
