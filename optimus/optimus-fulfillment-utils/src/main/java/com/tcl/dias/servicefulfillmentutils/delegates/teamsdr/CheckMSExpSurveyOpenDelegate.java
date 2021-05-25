package com.tcl.dias.servicefulfillmentutils.delegates.teamsdr;

import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

/**
 * Delegate to check whether managed service experience survey is already opened or not.
 * 
 * @author arjayapa
 */

@Component("CheckMSExpSurveyOpenDelegate")
public class CheckMSExpSurveyOpenDelegate implements JavaDelegate{

	private static final Logger LOGGER = LoggerFactory.getLogger(CheckMMOpenDelegate.class);

	@Autowired
	WorkFlowService workFlowService;

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
			LOGGER.info("Inside CheckMSExpSurveyOpenDelegate variables {}", executionVariables);
			workFlowService.processServiceTask(execution);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			List<ScComponent> scComponents = scComponentRepository
					.findByScServiceDetailIdAndComponentName(serviceId, AttributeConstants.COMPONENT_LM);
			Optional<ScComponent> scComponent = scComponents.stream().findAny();
			if (scComponent.isPresent()) {
				LOGGER.info("SC Component ID : {}, name {} ", scComponent.get().getId(),
						scComponent.get().getComponentName());
				Map<String, String> scAttributes = commonFulfillmentUtils
						.getComponentAttributesByScComponent(scComponent.get().getId());
				String isMSExperienceSurveyOpen = scAttributes.get(AttributeConstants.IS_MS_EXP_SURVEY_OPEN);
				if (Objects.isNull(isMSExperienceSurveyOpen)) {
					LOGGER.info("isMSExperienceSurveyOpen {}", "no");
					Map<String, String> atMap = new HashMap<>();
					atMap.put(AttributeConstants.IS_MS_EXP_SURVEY_OPEN, AttributeConstants.NO.toLowerCase());
					execution.setVariable(AttributeConstants.IS_MS_EXP_SURVEY_OPEN,
							AttributeConstants.NO.toLowerCase());
					componentAndAttributeService
							.updateAttributes(serviceId, atMap, scComponent.get().getComponentName(),
									scComponent.get().getSiteType());
				} else {
					Map<String, String> atMap = new HashMap<>();
					atMap.put(AttributeConstants.IS_MS_EXP_SURVEY_OPEN, AttributeConstants.YES.toLowerCase());
					execution.setVariable(AttributeConstants.IS_MS_EXP_SURVEY_OPEN,
							AttributeConstants.YES.toLowerCase());
					componentAndAttributeService
							.updateAttributes(serviceId, atMap, scComponent.get().getComponentName(),
									scComponent.get().getSiteType());
				}
			}
			LOGGER.info("Outside CheckMSExpSurveyOpenDelegate variables {}", execution.getVariables());
		} catch (Exception e) {
			LOGGER.error("Exception in CheckMSExpSurveyOpenDelegate {}", e.getMessage());
		}
		workFlowService.processServiceTaskCompletion(execution);
	}

}
