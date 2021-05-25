package com.tcl.dias.servicefulfillmentutils.delegates.teamsdr;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.FieldEngineer;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;;


/**
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("autoAssignTeamsdrPOCSCMMLDelegate")
public class TeamsdrAutoAssignPOCSCMMLDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(TeamsdrAutoAssignPOCSCMMLDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;
		
	@Autowired
	FieldEngineerRepository fieldEngineerRepository;
		
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	public void execute(DelegateExecution execution) {
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);
		logger.info("TeamsdrAutoAssignPOCSCMMLDelegate invoked for serviceCode {} with Id={}", serviceCode,execution.getId());
		String errorMessage = "";
		Task serviceTask=workFlowService.processServiceTask(execution);
		FieldEngineer fieldEngineer = new FieldEngineer();
		fieldEngineer.setTask(serviceTask);
		fieldEngineer.setServiceId(serviceTask.getServiceId());
		fieldEngineer.setAppointmentType(serviceTask.getMstTaskDef().getKey().replace("assign-poc-","poc-"));
		logger.info("TeamsdrAutoAssignPOCSCMMLDelegate for serviceCode {}", serviceCode);
		
		fieldEngineer.setName("Hemant Bhalerao");
		fieldEngineer.setEmail("hemant.bhalerao@tatacommunications.com");
		fieldEngineerRepository.save(fieldEngineer);	
		
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
	}

}
