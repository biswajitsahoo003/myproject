package com.tcl.dias.servicefulfillmentutils.listeners;

import java.util.Map;

import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_CASE_INST_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_PLAN_ITEM_INST_ID;

/**
 * This listener sets case variables and planItem
 * local variables in to process Instance.
 * 
 *
 * @author ASyed
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("updateCaseAttributesListener")
public class UpdateCaseAttributesListener implements ExecutionListener {

	private static final long serialVersionUID = -5338213645454484958L;
	
	private static final Logger logger = LoggerFactory.getLogger(UpdateCaseAttributesListener.class);
	
	@Autowired
	CmmnRuntimeService cmmnRuntimeService;
	
	@Override
	public void notify(DelegateExecution execution) {
		logger.info("########## Triggered ##########  UpdateCaseAttributesListener");

		if (execution.getVariable(KEY_CASE_INST_ID) != null) {
			Map<String, Object> caseVaribleMap = cmmnRuntimeService.getVariables(execution.getVariable(KEY_CASE_INST_ID).toString());
			execution.setVariables(caseVaribleMap);
		}

		if (execution.getVariable(KEY_PLAN_ITEM_INST_ID) != null) {
			Map<String, Object> planVaribleMap = cmmnRuntimeService.getLocalVariables(execution.getVariable(KEY_PLAN_ITEM_INST_ID).toString());
			execution.setVariables(planVaribleMap);
		}
	}

}
