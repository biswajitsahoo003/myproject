package com.tcl.dias.servicefulfillmentutils.ipc.listeners;

import static com.tcl.dias.servicefulfillmentutils.constants.IPCServiceFulfillmentConstant.ORDER_STAGE_SERVICE_ACCEPTANCE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.IpcAcceptanceService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("ipcServiceAcceptanceListener")
public class IpcServiceAcceptanceListener implements ExecutionListener {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(IpcServiceAcceptanceListener.class);

	@Autowired
	private IpcAcceptanceService ipcAcceptanceService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskRepository taskRepository;

	@Override
	public void notify(DelegateExecution execution) {
		logger.info("IpcServiceAcceptanceListener invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",
				execution.getCurrentActivityId(), execution.getId(), execution.getProcessInstanceId(),
				execution.getEventName());
		Map<String, Object> processVariables = runtimeService.getVariables(execution.getProcessInstanceId());
		Integer serviceId = (Integer) processVariables.get(SERVICE_ID);
		Task task  = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId,
				ORDER_STAGE_SERVICE_ACCEPTANCE.toLowerCase());
		if (task !=null) {
			processVariables.put("isDeemedAcceptance", "Y");
			processVariables.put("acceptanceUserName", "admin");
			try {
				ipcAcceptanceService.processServiceAcceptance(task.getId(), Boolean.TRUE, processVariables);
			} catch (TclCommonException e) {
				logger.error("Error in IpcServiceAcceptanceListener delegation ",e);
			}
		}
	}

}
