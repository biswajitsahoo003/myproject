package com.tcl.dias.servicefulfillmentutils.delegates.teamsdr;

import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TeamsDRService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_GSC_FLOW_GROUP_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

/* This class is used to set flow group id to next task*/
@Component("SetFlowGroupBatchIdDelegate")
public class SetFlowGroupBatchIdDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(SetFlowGroupBatchIdDelegate.class);

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	TeamsDRService teamsDRService;

	@Autowired
	ScComponentRepository scComponentRepository;

	/**
	 * Execute the delegate
	 *
	 * @param execution
	 */
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			LOGGER.info("Inside SetFlowGroupBatchIdDelegate variables {}", executionVariables);
			workFlowService.processServiceTask(execution);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			Integer componentId = (Integer) executionVariables.get(AttributeConstants.SC_COMPONENT_ID);
			Optional<ScComponent> scComponent = scComponentRepository.findById(componentId);
			if (scComponent.isPresent()) {
				LOGGER.info("SC Component ID : {}, name {} ", scComponent.get().getId(),
						scComponent.get().getComponentName());
				GscFlowGroup flowGroup = teamsDRService.getLatestFlowGroup(scComponent.get());
				if (Objects.nonNull(flowGroup)) {
					LOGGER.info("FlowGroup/Batch ID : {}", flowGroup.getId());
					execution.setVariable(KEY_GSC_FLOW_GROUP_ID, flowGroup.getId());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception in SetFlowGroupBatchIdDelegate {}", e.getMessage());
		}
		workFlowService.processServiceTaskCompletion(execution);
	}
}
