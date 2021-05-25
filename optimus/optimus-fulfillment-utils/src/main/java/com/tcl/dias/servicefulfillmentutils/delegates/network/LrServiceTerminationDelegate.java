package com.tcl.dias.servicefulfillmentutils.delegates.network;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("lrServiceTerminationDelegate")
public class LrServiceTerminationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(LrServiceTerminationDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.lr.service.terminate}")
	String serviceTerminationQueue;
    
    @Autowired
	MQUtils mqUtils;

    @Override
	public void execute(DelegateExecution execution) {
		logger.info("lrServiceTerminationDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			workFlowService.processServiceTask(execution);
   			Map<String, Object> processMap = execution.getVariables();
   			String orderCode = (String) processMap.get(ORDER_CODE);
   			String serviceCode = (String) processMap.get(SERVICE_CODE);
   			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
   			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode).concat("#").concat(serviceId.toString());
   			logger.info("Lr Service Termination for serviceCode={} PROCESS ID={}",serviceCode , execution.getProcessInstanceId());
   			String status = (String) mqUtils.sendAndReceive(serviceTerminationQueue, req);
   			
   			logger.info("Lr Service Termination  for serviceCode={} status={}",serviceCode , status);
			if (!StringUtils.isBlank(status) && status.equals("SUCCESS")) {
				execution.setVariable("serviceTerminatedLrAck", true);
			} else {
				execution.setVariable("serviceTerminatedLrAck", false);
				execution.setVariable("serviceTerminatedLrAckErrorMsg", status.split("SUCCESS|FAILURE")[1]);
			}

			logger.info("lrServiceTerminationDelegate completed");
   			
			
		} catch (Exception e) {
			logger.error("Exception in lrServiceTerminationDelegate{}", e);
		}
		
		workFlowService.processServiceTaskCompletion(execution);

	}
}
