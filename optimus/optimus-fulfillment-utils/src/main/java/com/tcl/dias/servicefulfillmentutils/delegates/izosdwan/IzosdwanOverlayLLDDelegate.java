package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("sdwanOverlayLLDDelegate")
public class IzosdwanOverlayLLDDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanOverlayLLDDelegate.class);
		
		@Autowired
		WorkFlowService workFlowService;
		
		@Autowired
		TaskRepository taskRepository;
		

	public void execute(DelegateExecution execution) {
		logger.info("SdwanOverlayLLDDelegate  invoked");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		Integer solutionId = (Integer) execution.getVariable("solutionId");
		logger.info("SdwanOverlayLLDDelegate.solutionId={},serviceId={},serviceCode={},executionProcessInstId={}", solutionId,serviceId,serviceCode,execution.getProcessInstanceId());
		Task uploadLLDTask=taskRepository.findByServiceIdAndMstStatus_codeAndMstTaskDef_key(solutionId, "CLOSED", "upload-lld-migration-document");
		Boolean isUploadLLDTaskCompleted=false;
		if(uploadLLDTask!=null){
			logger.info("Upload LLD Task closed on Solution Id:{} for overlay:{}",solutionId,serviceId);
			isUploadLLDTaskCompleted=true;
		}
		execution.setVariable("isUploadLLDTaskCompleted", isUploadLLDTaskCompleted);
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,"");
	}


}
