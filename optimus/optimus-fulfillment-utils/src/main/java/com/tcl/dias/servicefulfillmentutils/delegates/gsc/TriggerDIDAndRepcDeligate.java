/**
 * @author vivek
 *
 * 
 */
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

/**
 * @author vivek
 *
 */

@Component("TriggerNumberConfiRepc")
public class TriggerDIDAndRepcDeligate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(TriggerDIDAndRepcDeligate.class);

	@Autowired
	CmmnHelperService cmmnHelperService;

	/**
	 * execute
	 * 
	 * @param execution
	 */
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("###### Inside ###### TriggerDIDAndRepcDeligate");

		Map<String, Object> variables = execution.getVariables();
		logger.info("Variables : {}", variables);
		String caseInstId = (String) variables.get("caseInstId");
		if (execution.getCurrentActivityId().equalsIgnoreCase("trigger-porting-number-workflow")) {
			if(variables.get(GscConstants.NEXT_GSC_FLOW_GROUP_ID)!=null) {
				variables.put(GscConstants.KEY_GSC_FLOW_GROUP_ID, variables.get(GscConstants.NEXT_GSC_FLOW_GROUP_ID));

			}
			String planItemInstancesId = cmmnHelperService.startAndCreateNewPlanItem(caseInstId,GscConstants.PROVISION_DID_PORT_NUMBERS ,
					variables);
			logger.info("Under caseInstance DID PORT: {} planItemInstance :{} is triggered", caseInstId,
					planItemInstancesId);
		} else if (execution.getCurrentActivityId().equalsIgnoreCase("trigger-new-number-workflow")) {
			if(variables.get(GscConstants.NEXT_GSC_FLOW_GROUP_ID)!=null) {
				variables.put(GscConstants.KEY_GSC_FLOW_GROUP_ID, variables.get(GscConstants.NEXT_GSC_FLOW_GROUP_ID));


			}
			String planItemInstancesId = cmmnHelperService.startAndCreateNewPlanItem(caseInstId, GscConstants.PROVISION_DID_NEW_NUMBERS,
					variables);
			logger.info("Under caseInstance DID NEW: {} planItemInstance :{} is triggered", caseInstId,
					planItemInstancesId);
		}
	}

}
