package com.tcl.dias.servicefulfillmentutils.delegates.teamsdr;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TeamsDRService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

/* This class is used to check batch count*/
@Component("CheckTrainingOpenDelegate")
public class CheckTrainingOpenDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckTrainingOpenDelegate.class);

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	TeamsDRService teamsDRService;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	/**
	 * Execute the delegate
	 *
	 * @param execution
	 */
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			LOGGER.info("Inside check training open delegate variables {}", executionVariables);
			workFlowService.processServiceTask(execution);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			List<ScComponent> scComponents = scComponentRepository
					.findByScServiceDetailIdAndComponentName(serviceId, AttributeConstants.COMPONENT_LM);
			Optional<ScComponent> scComponent = scComponents.stream().findAny();
			if (scComponent.isPresent()) {
				LOGGER.info("SC Component ID : {}, name {} ", scComponent.get().getId(),
						scComponent.get().getComponentName());
				Map<String, String> scAttributes = commonFulfillmentUtils
						.getComponentAttributesByScComponent(scComponent.get().getId());
				String isTrainingOpen = scAttributes.get(AttributeConstants.IS_TRAINING_OPEN);
				if (Objects.isNull(isTrainingOpen)) {
					LOGGER.info("Is training open : {}", false);
					Map<String, String> atMap = new HashMap<>();
					atMap.put(AttributeConstants.IS_TRAINING_OPEN, AttributeConstants.NO);
					execution.setVariable(AttributeConstants.IS_TRAINING_OPEN, AttributeConstants.NO);
					componentAndAttributeService
							.updateAttributes(serviceId, atMap, scComponent.get().getComponentName(),
									scComponent.get().getSiteType());
					String isEndUserTraining = scAttributes.get(AttributeConstants.IS_END_USER_TRAINING);
					if (Objects.nonNull(isEndUserTraining)) {
						LOGGER.info("Is isEndUserTraining : {}", isEndUserTraining);
						execution.setVariable(AttributeConstants.IS_END_USER_TRAINING, isEndUserTraining);
					}
					String isAdvancedTraining = scAttributes.get(AttributeConstants.IS_ADVANCED_LEVEL_TRAINING);
					if (Objects.nonNull(isAdvancedTraining)) {
						LOGGER.info("Is isAdvancedTraining : {}", isAdvancedTraining);
						execution.setVariable(AttributeConstants.IS_ADVANCED_LEVEL_TRAINING, isAdvancedTraining);
					}
				} else {
					Map<String, String> atMap = new HashMap<>();
					atMap.put(AttributeConstants.IS_TRAINING_OPEN, AttributeConstants.YES);
					execution.setVariable(AttributeConstants.IS_TRAINING_OPEN, AttributeConstants.YES);
					componentAndAttributeService
							.updateAttributes(serviceId, atMap, scComponent.get().getComponentName(),
									scComponent.get().getSiteType());
				}
			}
			LOGGER.info("Outside check training open delegate variables {}", execution.getVariables());
		} catch (Exception e) {
			LOGGER.error("Exception in CheckTrainingOpenDelegate {}", e.getMessage());
		}
		workFlowService.processServiceTaskCompletion(execution);
	}
}
