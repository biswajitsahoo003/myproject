package com.tcl.dias.servicefulfillmentutils.delegates.network;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("applyTaxDelegate")
public class ApplyTaxDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(ApplyTaxDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.apply.tax}")
	String applyTaxQueue;
    
    @Autowired
	MQUtils mqUtils;
    
    @Autowired
	TaskRepository taskRepository;

    @Override
	public void execute(DelegateExecution execution) {
		logger.info("ApplyTaxDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			workFlowService.processServiceTask(execution);
   			Map<String, Object> processMap = execution.getVariables();
   			String orderCode = (String) processMap.get(ORDER_CODE);
   			String serviceCode = (String) processMap.get(SERVICE_CODE);
   			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
   			String user = "";
   			Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId,"tax-capture");
			if(task!=null) {
   				Set<TaskAssignment> taskAssignmentList = task.getTaskAssignments();
   				for (TaskAssignment taskAssignment : taskAssignmentList) {
   					user = taskAssignment.getUserName();
   				}
   			}
			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode)
					.concat("#").concat(serviceId.toString()).concat("#").concat(user);
			logger.info("Apply Tax for serviceCode ={} PROCESS ID={}",serviceCode , execution.getProcessInstanceId());
   			String status = (String) mqUtils.sendAndReceive(applyTaxQueue, req);
   			logger.info("Apply Tax for serviceCode ={} status={}",serviceCode , status);
			if (!StringUtils.isBlank(status) && status.equals("Success")) {
				execution.setVariable("applyTaxStatus", "Success");
			} else {
				execution.setVariable("applyTaxStatus", "Failure");
			}

			logger.info("Apply Tax completed");
   		} catch (Exception e) {
			logger.error("Exception in AccountCreationRequiredCheckDelegate{}", e);
			execution.setVariable("applyTaxStatus", "Failure");
		}
		
		workFlowService.processServiceTaskCompletion(execution);

	}
}
