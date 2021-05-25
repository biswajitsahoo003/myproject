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
@Component("sdwanUnderlayDownTimeDelegate")
public class IzosdwanUnderlayDownTimeDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanUnderlayDownTimeDelegate.class);
		
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
		logger.info("SdwanUnderlayDownTimeDelegate  invoked");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		Integer overlayId = (Integer) execution.getVariable("overlayId");
		Integer solutionId = (Integer) execution.getVariable("solutionId");
		logger.info("SdwanUnderlayDownTimeDelegate.solutionId={},serviceId={},serviceCode={},overlayId={},executionProcessInstId={}", solutionId,serviceId,serviceCode,overlayId,execution.getProcessInstanceId());
		List<String> componentGroups = new ArrayList<>();
		componentGroups.add("OVERLAY");
		componentGroups.add("UNDERLAY");
		List<ScSolutionComponent> scSolutionComponentList=scSolutionComponentRepository.findByScServiceDetail3_idAndScServiceDetail2_idOrScServiceDetail1_idAndComponentGroupInAndIsActive(solutionId, overlayId,overlayId,componentGroups, "Y");
		Boolean isDownTimeTasksCompleted=false;
		if(scSolutionComponentList!=null && !scSolutionComponentList.isEmpty()){
			logger.info("SdwanUnderlayDownTimeDelegate.ScSolutionComponentList size::{}",scSolutionComponentList.size());
			for(ScSolutionComponent scSolComp:scSolutionComponentList){
				if("OVERLAY".equalsIgnoreCase(scSolComp.getComponentGroup())){
					isDownTimeTasksCompleted=checkConfigCompletedforOverlay(solutionId,scSolComp.getScServiceDetail1().getId(),isDownTimeTasksCompleted);
					logger.info("Overlay isDownTimeTasksCompleted::{}",isDownTimeTasksCompleted);
				}else if("BYON Internet".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())){
					isDownTimeTasksCompleted=true;
				}else if(("IAS".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
								|| "GVPN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
								|| "DIA".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName())
								|| "IZO Internet WAN".equalsIgnoreCase(scSolComp.getScServiceDetail1().getErfPrdCatalogProductName()))
						&& (scSolComp.getScServiceDetail1().getOrderType().equalsIgnoreCase("MACD") &&
								((!scSolComp.getScServiceDetail1().getOrderCategory().equalsIgnoreCase("ADD_SITE") && 
								scSolComp.getScServiceDetail1().getOrderSubCategory()!=null && !scSolComp.getScServiceDetail1().getOrderSubCategory().toLowerCase().contains("parallel")) 
								|| ("CHANGE_ORDER".equalsIgnoreCase(scSolComp.getScServiceDetail1().getOrderCategory())
										&& scSolComp.getScServiceDetail1().getOrderSubCategory()==null)))){
					isDownTimeTasksCompleted=checkConfigCompletedforTCLUnderlays(solutionId,scSolComp.getScServiceDetail1().getId(),isDownTimeTasksCompleted);
					logger.info("IAS or GVPN or DIA or IWAN Underlay isDownTimeTasksCompleted::{}",isDownTimeTasksCompleted);
				}else{
					logger.info("SdwanUnderlayDownTimeDelegate skip service Id::{}",scSolComp.getScServiceDetail1().getId());
					continue;
				}
				if(!isDownTimeTasksCompleted){
					logger.info("isDownTimeTasksCompleted breaks for componentGroup::{} with service id::{}",scSolComp.getComponentGroup(),scSolComp.getScServiceDetail1().getId());
					break;
				}
			}
		}
		logger.info("isDownTimeTasksCompleted::{}",isDownTimeTasksCompleted);
		execution.setVariable("isDownTimeTasksCompleted", isDownTimeTasksCompleted);
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,"");
	}

	private Boolean checkConfigCompletedforTCLUnderlays(Integer solutionId, Integer serviceId, Boolean isDownTimeTasksCompleted) {
		logger.info("SdwanUnderlayDownTimeDelegate.checkConfigCompletedforTCLUnderlays SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if(downTimeDetails!=null){
			logger.info("DownTimeDetails exists with Id::{}", downTimeDetails.getId());
			if("Y".equalsIgnoreCase(downTimeDetails.getIsCpeAlreadyManaged())){
				logger.info("Cpe Managed exists with Id::{}", downTimeDetails.getId());
				isDownTimeTasksCompleted = true;
				return isDownTimeTasksCompleted;
			}
			if ("Y".equalsIgnoreCase(downTimeDetails.getIsConfigCompleted())) {
				logger.info("Config Task closed::{}",downTimeDetails.getId());
				isDownTimeTasksCompleted = true;
			} else {
				logger.info("Config Task Not Closed for service id::{}",downTimeDetails.getId());
				isDownTimeTasksCompleted = false;
			}
		}
		return isDownTimeTasksCompleted;
	}

	private Boolean checkConfigCompletedforOverlay(Integer solutionId,Integer serviceId, Boolean isDownTimeTasksCompleted) {
		logger.info("SdwanUnderlayDownTimeDelegate.checkDownTimeforOverlay SolutionId::{},ServiceId::{}",solutionId,serviceId);
		DownTimeDetails downTimeDetails=downTimeDetailsRepository.findFirstBySolutionIdAndScServiceDetailIdOrderByIdDesc(solutionId, serviceId);
		if("Y".equalsIgnoreCase(downTimeDetails.getIsConfigCompleted())){
				 logger.info("Cpe Config Completed::{}",downTimeDetails.getId());
				 isDownTimeTasksCompleted=true;
		}else{
				 logger.info("Cpe Config Completed Config Not Completed::{}",downTimeDetails.getId());
				 isDownTimeTasksCompleted=false;
		}
		return isDownTimeTasksCompleted;
	}
}
