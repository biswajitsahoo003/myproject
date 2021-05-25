package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import java.util.Optional;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.DownTimeDetails;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScSolutionComponent;
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
@Component("sdwanByonCpeDelegate")
public class IzosdwanByonCpeDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanByonCpeDelegate.class);
		
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
		logger.info("SdwanByonCpeDelegate  invoked");
		Integer serviceId = (Integer) execution.getVariable("serviceId");
		String serviceCode = (String) execution.getVariable("serviceCode");
		Integer overlayId = (Integer) execution.getVariable("overlayId");
		Boolean isDomesticSite = (Boolean) execution.getVariable("isByonDomesticSite");
		logger.info("SdwanByonCpeDelegate.serviceId={},serviceCode={},overlayId={},executionProcessInstId={},byonDomesticSite={}", serviceId,serviceCode,overlayId,execution.getProcessInstanceId(),isDomesticSite);
		ScSolutionComponent scSolutionComponent=scSolutionComponentRepository.findByScServiceDetail1_idAndScServiceDetail2_idAndIsActive(serviceId,overlayId, "Y");
		if(scSolutionComponent!=null && scSolutionComponent.getCpeComponentId()!=null){
			Optional<ScComponent> scComponentOptional=scComponentRepository.findById(scSolutionComponent.getCpeComponentId());
			if(scComponentOptional.isPresent() && scComponentOptional.get().getSiteType()!=null){
				Task trackCpeTask =null;
				if(isDomesticSite){
						trackCpeTask = taskRepository
								.findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeAndMstStatus_codeOrderByCreatedTimeDesc(
										overlayId, "sdwan-track-cpe-delivery", scComponentOptional.get().getSiteType(), "CLOSED");
				}else{
						trackCpeTask = taskRepository
								.findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeAndMstStatus_codeOrderByCreatedTimeDesc(
										overlayId, "sdwan-dispatch-track-cpe-international", scComponentOptional.get().getSiteType(), "CLOSED");
				}
				if(trackCpeTask!=null){
					logger.info("BYON cpeTasks closed");
					execution.setVariable("isByonCPEDelivered", true);
				}else{
					logger.info("cpeTasks not yet closed");
					execution.setVariable("isByonCPEDelivered", false);
				}
			}else{
				logger.info("Byon component not exits");
				execution.setVariable("isByonCPEDelivered", false);
			}
		}else{
			logger.info("Byon cpe solution not exits");
			execution.setVariable("isByonCPEDelivered", false);
		}
		workFlowService.processServiceTask(execution);
        workFlowService.processServiceTaskCompletion(execution ,"");
	}

}
