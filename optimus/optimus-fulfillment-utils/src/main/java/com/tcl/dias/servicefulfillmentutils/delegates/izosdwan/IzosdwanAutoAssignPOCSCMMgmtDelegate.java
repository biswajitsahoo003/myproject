package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.util.ArrayList;
import java.util.List;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.ImmutableList;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.servicefulfillment.entity.entities.FieldEngineer;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;;


/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("autoAssignPOCSCMMgmtDelegate")
public class IzosdwanAutoAssignPOCSCMMgmtDelegate implements JavaDelegate {
	
	    private static final Logger logger = LoggerFactory.getLogger(IzosdwanAutoAssignPOCSCMMgmtDelegate.class);
	
		@Autowired
		WorkFlowService workFlowService;
		
		@Autowired
		FieldEngineerRepository fieldEngineerRepository;

	public void execute(DelegateExecution execution) {
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);
		String ownerRegion = (String) execution.getVariable(IzosdwanCommonConstants.OWNER_REGION);
		logger.info("SdwanAutoAssignPOCSCMMgmtDelegate invoked for serviceCode:{} with owner region::{} and Id={}", serviceCode,ownerRegion,execution.getId());
		String errorMessage = "";
		Task serviceTask=workFlowService.processServiceTask(execution);
		FieldEngineer fieldEngineer = new FieldEngineer();
		fieldEngineer.setTask(serviceTask);
		fieldEngineer.setServiceId(serviceTask.getServiceId());
		fieldEngineer.setAppointmentType(serviceTask.getMstTaskDef().getKey().replace("assign-poc-","poc-"));
		if("Srilanka".equalsIgnoreCase(ownerRegion)){
			logger.info("SdwanAutoAssignPOCSCMMgmtDelegate for serviceCode {} has only Srilanka", serviceCode);
			fieldEngineer.setName("Ishan Balasuriya");
			fieldEngineer.setEmail("Ishan.Balasuriya@tatacommunications.com");
			fieldEngineerRepository.save(fieldEngineer);
		}else if("Europe".equalsIgnoreCase(ownerRegion) || "Americas".equalsIgnoreCase(ownerRegion)){
			logger.info("SdwanAutoAssignPOCSCMMgmtDelegate for serviceCode {} has both UK and USA", serviceCode);
			fieldEngineer.setName("Utkarsh Srivastava");
			fieldEngineer.setEmail("Utkarsh.Srivastava@tatacommunications.com");
			fieldEngineer.setSecondaryname("Asif Chandel");
			fieldEngineer.setSecondaryemail("asif.chandel@tatacommunications.com");
			fieldEngineerRepository.save(fieldEngineer);
		}else{
			logger.info("SdwanAutoAssignPOCSCMMgmtDelegate for serviceCode {} has ROW", serviceCode);
			fieldEngineer.setName("Ashwin Pothukuchi");
			fieldEngineer.setEmail("Ashwin.Pothukuchi@tatacommunications.com");
			fieldEngineer.setSecondaryname("Sumeet Rathi");
			fieldEngineer.setSecondaryemail("sumeet.rathi@tatacommunications.com");
			fieldEngineerRepository.save(fieldEngineer);
		}
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
	}

}
