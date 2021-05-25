package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("checkP2PConfigurationDelegate")
public class CheckP2PConfigurationDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(CheckP2PConfigurationDelegate.class);

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	TaskRepository taskRepository;

	@Override
	public void execute(DelegateExecution execution) {
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);
		Integer serviceId = (Integer) execution.getVariable(MasterDefConstants.SERVICE_ID);
		logger.info("CheckP2PConfigurationDelegate invoked for serviceCode {},serviceId {} with {} Id={}", serviceCode,
				serviceId, execution.getCurrentActivityId(), execution.getId());
		String errorMessage = "";
		workFlowService.processServiceTask(execution);
		List<Task> rfConfigP2PTaskList = taskRepository.findByServiceIdAndMstTaskDef_key(serviceId, "rf-config-p2p");
		if (rfConfigP2PTaskList == null || rfConfigP2PTaskList.isEmpty()) {
			logger.info("rf-config-p2p not opened for serviceId {}", serviceId);
			execution.setVariable("p2pConfigRequired", true);
		}
		workFlowService.processServiceTaskCompletion(execution, errorMessage);
	}

}
