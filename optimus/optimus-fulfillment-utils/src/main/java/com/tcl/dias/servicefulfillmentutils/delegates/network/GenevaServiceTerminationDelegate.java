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
@Component("genevaServiceTerminationDelegate")
public class GenevaServiceTerminationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(GenevaServiceTerminationDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.geneva.service.terminate}")
	String serviceTerminationQueue;
    
    @Autowired
	MQUtils mqUtils;

    @Override
	public void execute(DelegateExecution execution) {
		logger.info("GenevaServiceTerminationDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			workFlowService.processServiceTask(execution);
   			Map<String, Object> processMap = execution.getVariables();
   			String orderCode = (String) processMap.get(ORDER_CODE);
   			String serviceCode = (String) processMap.get(SERVICE_CODE);
   			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
   			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode).concat("#").concat(serviceId.toString());
   			logger.info("Service Termination for serviceCode={} PROCESS ID={}",serviceCode , execution.getProcessInstanceId());
   			String status = (String) mqUtils.sendAndReceive(serviceTerminationQueue, req,240000);
   			
   			logger.info("Service Termination  for serviceCode={} status={}",serviceCode , status);
			if (!StringUtils.isBlank(status) && status.equals("SUCCESS")) {
				execution.setVariable("serviceTerminatedGenevaAck", true);
			} else {
				execution.setVariable("serviceTerminatedGenevaAck", false);
				execution.setVariable("serviceTerminatedGenevaAckErrorMsg", status.split("Success|Fail")[1]);
			}

			logger.info("genevaServiceTerminationDelegate completed");
   			
			
		} catch (Exception e) {
			logger.error("Exception in genevaServiceTerminationDelegate{}", e);
		}
		
		workFlowService.processServiceTaskCompletion(execution);

	}
}
