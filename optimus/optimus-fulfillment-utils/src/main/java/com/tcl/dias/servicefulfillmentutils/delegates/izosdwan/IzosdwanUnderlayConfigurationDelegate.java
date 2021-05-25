package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.DownTimeDetails;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
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
@Component("sdwanUnderlayConfigurationDelegate")
public class IzosdwanUnderlayConfigurationDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanUnderlayConfigurationDelegate.class);
		
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
		logger.info("SdwanUnderlayConfigurationDelegate  invoked");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		Integer overlayId = (Integer) execution.getVariable("overlayId");
		Integer solutionId = (Integer) execution.getVariable("solutionId");
		logger.info("SdwanUnderlayConfigurationDelegate.solutionId={},serviceId={},serviceCode={},overlayId={},executionProcessInstId={}", solutionId,serviceId,serviceCode,overlayId,execution.getProcessInstanceId());
		Boolean isConfigurationTasksCompleted=false;
		isConfigurationTasksCompleted=checkConfigCompletedforOverlay(solutionId,overlayId,isConfigurationTasksCompleted);
		logger.info("isConfigurationTasksCompleted::{}",isConfigurationTasksCompleted);
		execution.setVariable("isConfigurationTasksCompleted", isConfigurationTasksCompleted);
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,"");
	}

	private Boolean checkConfigCompletedforOverlay(Integer solutionId, Integer serviceId,Boolean isConfigurationTasksCompleted) {
		logger.info("SdwanUnderlayConfigurationDelegate.checkDownTimeforOverlay SolutionId::{},ServiceId::{}",solutionId, serviceId);
		DownTimeDetails downTimeDetails = downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if ("Y".equalsIgnoreCase(downTimeDetails.getIsConfigCompleted())) {
			logger.info("Cpe Config Completed::{}", downTimeDetails.getId());
			isConfigurationTasksCompleted = true;
		} else {
			logger.info("Cpe Config Completed Config Not Completed::{}", downTimeDetails.getId());
			isConfigurationTasksCompleted = false;
		}
		return isConfigurationTasksCompleted;
	}
}
