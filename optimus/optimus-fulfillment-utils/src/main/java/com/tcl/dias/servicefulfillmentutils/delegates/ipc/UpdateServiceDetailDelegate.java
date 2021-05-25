package com.tcl.dias.servicefulfillmentutils.delegates.ipc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.IPCAttachmentService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author Ram
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("updateServiceDetailDelegate")
public class UpdateServiceDetailDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(UpdateServiceDetailDelegate.class);

    @Autowired
	WorkFlowService workFlowService;    
   
    @Autowired
    IPCAttachmentService ipcAttachmentService;
    
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("updateServiceDetailDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			ipcAttachmentService.updateServiceDetail(serviceId);
            logger.info("updateServiceDetailDelegate completed");
            workFlowService.processServiceTaskCompletion(execution);
		} catch (Exception e) {
			logger.error("Exception in updateServiceDetailDelegate{}", e);
		}

	}





}
