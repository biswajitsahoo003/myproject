package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CmmnHelperService;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.SERVICE_ACCEPTENCE;

/**
 * This file contains the TriggerServiceAcceptenceWorkflowDelegate.java class.
 * 
 *
 * @author ASyed
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("TriggerServiceAcceptenceWorkflow")
public class TriggerServiceAcceptenceWorkflowDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(TriggerServiceAcceptenceWorkflowDelegate.class);

	@Autowired
	CmmnHelperService cmmnHelperService;

	/**
	 * execute
	 * 
	 * @param execution
	 */
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("###### Inside ###### TriggerServiceAcceptenceWorkflowDelegate");

		Map<String, Object> variables = execution.getVariables();
		logger.info("Variables : {}", variables);
		String caseInstId = (String) variables.get("caseInstId");
		if(variables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID_SERVICE_ACCEPTANCE) != null) {
			variables.put(GscConstants.KEY_GSC_FLOW_GROUP_ID, variables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID_SERVICE_ACCEPTANCE));
		}
		
		String planItemInstancesId = cmmnHelperService.startAndCreateNewPlanItem(caseInstId, SERVICE_ACCEPTENCE,
				variables);
		logger.info("Under caseInstance: {} planItemInstance :{} is triggered", caseInstId, planItemInstancesId);
	}

}
