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
@Component("sdwanOverlayDelegate")
public class IzosdwanOverlayDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanOverlayDelegate.class);
		
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
		logger.info("SdwanOverlayDelegate  invoked");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		Integer solutionId = (Integer) execution.getVariable("solutionId");
		logger.info("SdwanOverlayDelegate.solutionId={},serviceId={},serviceCode={},executionProcessInstId={}", solutionId,serviceId,serviceCode,execution.getProcessInstanceId());
		List<ScSolutionComponent> scSolutionComponentList=scSolutionComponentRepository.findByScServiceDetail3_idAndScServiceDetail2_idAndComponentGroupAndIsActive(solutionId, serviceId,"UNDERLAY", "Y");
		Boolean isUnderlayE2ETasksCompleted=false;
		if(scSolutionComponentList!=null && !scSolutionComponentList.isEmpty()){
			logger.info("SdwanOverlayDelegate.ScSolutionComponentList size::{}",scSolutionComponentList.size());
			for(ScSolutionComponent scSolComp:scSolutionComponentList){
				if("BYON Internet".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())){
					isUnderlayE2ETasksCompleted=true;
				}else if("IAS".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
								|| "GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
								|| "IZO Internet WAN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
								|| "DIA".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())){
					isUnderlayE2ETasksCompleted=checkConfigCompletedforTCLUnderlays(solutionId,scSolComp.getScServiceDetail1().getId(),isUnderlayE2ETasksCompleted);
					logger.info("IAS or GVPN or IWAN or DIA Underlay underlayE2ETasksCompleted::{}",isUnderlayE2ETasksCompleted);
				}
				if(!isUnderlayE2ETasksCompleted){
					logger.info("underlayE2ETasksCompleted breaks for componentGroup::{} with service id::{}",scSolComp.getComponentGroup(),scSolComp.getScServiceDetail1().getId());
					break;
				}
			}
		}
		execution.setVariable("isUnderlayE2ETasksCompleted", isUnderlayE2ETasksCompleted);
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,"");
	}

	private Boolean checkConfigCompletedforTCLUnderlays(Integer solutionId, Integer serviceId, Boolean isUnderlayE2ETasksCompleted) {
		logger.info("SdwanOverlayDelegate.checkConfigCompletedforTCLUnderlays SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if(downTimeDetails!=null){
			logger.info("DownTimeDetails exists with Id::{}", downTimeDetails.getId());
			if("Y".equalsIgnoreCase(downTimeDetails.getIsE2ECompleted())){
				logger.info("E2E Task Completed for serviceId::{}", serviceId);
				isUnderlayE2ETasksCompleted = true;
			}else{
				logger.info("E2E Task Not Completed for serviceId::{}", serviceId);
				isUnderlayE2ETasksCompleted = false;
			}
		}
		return isUnderlayE2ETasksCompleted;
	}

}
