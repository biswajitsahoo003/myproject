package com.tcl.dias.servicefulfillmentutils.delegates.ipc;

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
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("ipcApplyTaxDelegate")
public class IpcApplyTaxDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(IpcApplyTaxDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.ipc.apply.tax}")
	String applyTaxQueue;
    
    @Autowired
	MQUtils mqUtils;
    
    @Autowired
	TaskRepository taskRepository;

    @Override
	public void execute(DelegateExecution execution) {
		logger.info("Ipc ApplyTaxDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			workFlowService.processServiceTask(execution);
   			Map<String, Object> processMap = execution.getVariables();
   			String orderCode = (String) processMap.get(ORDER_CODE);
   			String serviceCode = (String) processMap.get(SERVICE_CODE);
   			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
   			String user = "System";
   			Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId,"ipc-tax-capture");
			if(task!=null) {
   				Set<TaskAssignment> taskAssignmentList = task.getTaskAssignments();
   				for (TaskAssignment taskAssignment : taskAssignmentList) {
   					user = taskAssignment.getUserName();
   				}
   			}
			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode)
					.concat("#").concat(serviceId.toString()).concat("#").concat(user);
			logger.info("Apply Tax for serviceCode ={} PROCESS ID={}",serviceCode , execution.getProcessInstanceId());
   			String status = (String) mqUtils.sendAndReceive(applyTaxQueue, req, 420000);
   			logger.info("Ipc Apply Tax for serviceCode ={} status={}",serviceCode , status);
			if (!StringUtils.isBlank(status) && status.equals("Success")) {
				execution.setVariable("ipcApplyTaxStatus", "Success");
			} else {
				execution.setVariable("ipcApplyTaxStatus", "Failure");
			}

			logger.info("Apply Tax completed");
   		} catch (Exception e) {
			logger.error("Exception in Ipc Apply Tax Delegate{}", e);
			execution.setVariable("ipcApplyTaxStatus", "Failure");
		}
		
		workFlowService.processServiceTaskCompletion(execution);

	}
}
