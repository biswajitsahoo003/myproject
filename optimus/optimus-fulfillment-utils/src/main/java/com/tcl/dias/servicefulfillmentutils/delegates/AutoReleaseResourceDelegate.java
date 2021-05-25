/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.SalesNegotiationConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.CustomerDepHoldService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 *
 */
@Component("autoReleaseResourceDelegate")
@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
public class AutoReleaseResourceDelegate implements JavaDelegate {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoReleaseResourceDelegate.class);
    
    @Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	TaskService taskService;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Autowired
	CustomerDepHoldService customerDepHoldService;
	
	@Autowired
	TaskRepository taskRepository;

	@Autowired
	TaskCacheService taskCacheService;


	@Override
	public void execute(DelegateExecution execution) {

		LOGGER.info("autoReleaseResourceDelegate invoked for {} Id={}", execution.getCurrentActivityId(),
				execution.getId());
		String errorMessage = "";
		String errorCode = "";

		Map<String, Object> processMap = execution.getVariables();
		String serviceCode = (String) processMap.get(SERVICE_CODE);
		Task task = workFlowService.processServiceTask(execution);
		try {
			boolean realesResourceNeeded=customerDepHoldService.releaseResourceFlowRequire(task.getServiceId());
			if(realesResourceNeeded) {
				Map<String, Object> processVar = customerDepHoldService.addReleaseResourceVariableWorkflow(
						task.getScServiceDetail(), null, null, SalesNegotiationConstants.AUTORESOURCECANCELLATION);
				execution.setVariable("resourceReleaseNeeded", true);
				execution.setVariables(processVar);

			}
			else {
				execution.setVariable("resourceReleaseNeeded", false);
			}
			
			Task customerNegoTiationTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
					task.getServiceId(), "customer-hold-negotaition-CIM");
			if (customerNegoTiationTask != null && !customerNegoTiationTask.getMstStatus().getCode()
					.equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
				customerNegoTiationTask.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
				customerNegoTiationTask.setCompletedTime(new Timestamp(new Date().getTime()));
			}
			workFlowService.processServiceTaskCompletion(execution, errorMessage);

		} catch (TclCommonException e) {
			errorMessage = "AUTOCANCELLATIONFAILED";
			LOGGER.error("AutoReleaseResourceDelegate Exception {} ", e);

		}


	}
}
