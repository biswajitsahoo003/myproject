package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.CmmnHelperService;

/**
 * This file contains the TriggerUpdateSupplierResponseDelegate.java class.
 * 
 *
 * @author ASyed
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("TriggerUpdateSupplierResponse")
public class TriggerUpdateSupplierResponseDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(TriggerUpdateSupplierResponseDelegate.class);

	@Autowired
	CmmnHelperService cmmnHelperService;

	/**
	 * execute
	 * 
	 * @param execution
	 */
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("###### Inside ###### TriggerUpdateSupplierResponseDelegate");

		Map<String, Object> variables = execution.getVariables();
		logger.info("Variables : {}", variables);
		String caseInstId = (String) variables.get("caseInstId");

		
		String newPlanItem = cmmnHelperService.createAndStartPlanItem(caseInstId, "planItem_UpdateSupplierResponse", variables);
		logger.info("Created newPlanItem : {}", newPlanItem);

	}

}
