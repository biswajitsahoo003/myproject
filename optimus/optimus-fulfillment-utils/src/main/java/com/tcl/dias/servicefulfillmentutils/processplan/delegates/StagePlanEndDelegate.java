package com.tcl.dias.servicefulfillmentutils.processplan.delegates;

import java.util.Map;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.factory.ProjectPlanHelperService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("stagePlanEndDelegate")
public class StagePlanEndDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(StagePlanEndDelegate.class);
	
	@Autowired
	private ProjectPlanHelperService projectPlanHelperService;
	
	private Expression preceders;


	@Override
	public void execute(DelegateExecution execution) {
		if(execution.getCurrentActivityId()!=null) {
			logger.info("StagePlanEndDelegate invoked for {} ",execution.getCurrentActivityId());
			

			
			Map<String, Object> variableMap = execution.getVariables();

			String processsType = (String) variableMap.get("processType");
			

			
			try {
				projectPlanHelperService.processProjectPLan(processsType, "stagePlanEnd", execution, preceders);
			} catch (Exception e) {
				e.printStackTrace();	
				throw new BpmnError(e.getMessage());
			}
		}		
	}

}
