package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.DownTimeDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("sdwanByonUnderlayAcceptanceDelegate")
public class IzosdwanByonUnderlayAcceptanceDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanByonUnderlayAcceptanceDelegate.class);
		
		@Autowired
		WorkFlowService workFlowService;
		
		@Autowired
		ScSolutionComponentRepository scSolutionComponentRepository;
		
		@Autowired
		ScComponentRepository scComponentRepository;
		
		@Autowired
		TaskRepository taskRepository;
		
		@Autowired
		DownTimeDetailsRepository downTimeDetailsRepository;

	public void execute(DelegateExecution execution) {
		logger.info("SdwanByonUnderlayAcceptanceDelegate  invoked");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		Integer overlayId = (Integer) execution.getVariable("overlayId");
		logger.info("SdwanByonUnderlayAcceptanceDelegate.serviceId={},serviceCode={},overlayId={},executionProcessInstId={}", serviceId,serviceCode,overlayId,execution.getProcessInstanceId());
		Task serviceAcceptance = taskRepository
				.findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeAndMstStatus_codeOrderByCreatedTimeDesc(
						overlayId, "sdwan-service-acceptance","A", "CLOSED");
		if(serviceAcceptance!=null){
			logger.info("OverlayId Service Acceptance task completed={}",overlayId);
			execution.setVariable("isOverlayAcceptanceCompleted", true);
		}else{
			logger.info("OverlayId Service Acceptance task not yet completed={}",overlayId);
			execution.setVariable("isOverlayAcceptanceCompleted", false);
		}
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,"");
	}

}
