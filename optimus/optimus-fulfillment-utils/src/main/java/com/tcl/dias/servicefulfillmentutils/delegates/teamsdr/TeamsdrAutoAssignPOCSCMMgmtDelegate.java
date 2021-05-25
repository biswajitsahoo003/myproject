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
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;


/**
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("autoAssignTeamsdrPOCSCMMgmtDelegate")
public class TeamsdrAutoAssignPOCSCMMgmtDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(TeamsdrAutoAssignPOCSCMMgmtDelegate.class);
	
	@Autowired
	WorkFlowService workFlowService;
		
	@Autowired
	FieldEngineerRepository fieldEngineerRepository;

	public void execute(DelegateExecution execution) {
		String serviceCode = (String) execution.getVariable(SERVICE_CODE);
		logger.info("TeamsdrAutoAssignPOCSCMMgmtDelegate invoked for serviceCode:{} Id={}", serviceCode,execution.getId());
		String errorMessage = "";
		Task serviceTask=workFlowService.processServiceTask(execution);
		FieldEngineer fieldEngineer = new FieldEngineer();
		fieldEngineer.setTask(serviceTask);
		fieldEngineer.setServiceId(serviceTask.getServiceId());
		fieldEngineer.setAppointmentType(serviceTask.getMstTaskDef().getKey().replace("assign-poc-","poc-"));
		fieldEngineer.setName("Vasudev Grover");
		fieldEngineer.setEmail("Vasudev.Grover@tatacommunications.com");
		fieldEngineerRepository.save(fieldEngineer);
		
        workFlowService.processServiceTaskCompletion(execution ,errorMessage);
	}

}
