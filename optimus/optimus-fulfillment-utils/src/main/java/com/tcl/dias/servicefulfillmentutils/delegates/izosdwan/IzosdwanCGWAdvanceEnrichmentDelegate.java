package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.DownTimeDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("sdwanCGWAdvanceEnrichmentDelegate")
public class IzosdwanCGWAdvanceEnrichmentDelegate implements JavaDelegate {
	

	private static final Logger logger = LoggerFactory.getLogger(IzosdwanCGWDelegate.class);
		
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
		logger.info("SdwanCGWAdvanceEnrichmentDelegate  invoked");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		Integer solutionId = (Integer) execution.getVariable("solutionId");
		logger.info("SdwanCGWAdvanceEnrichmentDelegate.solutionId={},serviceId={},serviceCode={},executionProcessInstId={}", solutionId,serviceId,serviceCode,execution.getProcessInstanceId());
		Integer pyCgwServiceId =scSolutionComponentRepository.findAllByScServiceDetail3_idAndComponentGroupAndServiceIdAndIsActive(solutionId,serviceId,"CGW","Y");
		if(pyCgwServiceId!=null){
			logger.info("PyCgwServiceId exists::{}",pyCgwServiceId);
			Task advanceEnrichmentTask = taskRepository
					.findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeAndMstStatus_codeOrderByCreatedTimeDesc(
							pyCgwServiceId, "sdwan-cgw-advanced-enrichment","A", "CLOSED");
			if(advanceEnrichmentTask!=null){
				logger.info("Py CGW Advance Enricchment task completed");
				execution.setVariable("isPyAdvanceEnrichmentCompleted", true);
			}else{
				logger.info("Py CGW Advance Enricchment task not completed");
				execution.setVariable("isPyAdvanceEnrichmentCompleted", false);
			}
		}else{
			logger.info("Py CGW Service Id not exists");
			execution.setVariable("isPyAdvanceEnrichmentCompleted", false);
		}
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,"");
	}
}
