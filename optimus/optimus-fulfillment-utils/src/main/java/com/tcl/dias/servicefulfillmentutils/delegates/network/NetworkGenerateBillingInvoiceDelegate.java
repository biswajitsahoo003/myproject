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
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("nwGenerateBillingInvoiceDelegate")
public class NetworkGenerateBillingInvoiceDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(NetworkGenerateBillingInvoiceDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.invoiceGenSync.network}")
	String genInvoiceQueue;
    
    @Autowired
	MQUtils mqUtils;
    
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("GenerateBillingInvoiceDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			Map<String, Object> processMap = execution.getVariables();
			String orderCode = (String) processMap.get(ORDER_CODE);
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode).concat("#").concat(serviceId.toString());
			String status = (String) mqUtils.sendAndReceive(genInvoiceQueue, req);
			logger.info("Status recevied {}",status);
			if(!StringUtils.isBlank(status) && status.equals("Success"))
			{
				execution.setVariable("billingInvoiceStatus", true);
			}
			else {
				execution.setVariable("billingInvoiceStatus", false);
				if(StringUtils.isBlank(status)) {
					execution.setVariable("billingInvoiceErrorMsg", "ERROR");
				}else {
					execution.setVariable("billingInvoiceErrorMsg", status.split("Success|Fail")[1]);
				}
            }
			workFlowService.processServiceTask(execution);
            logger.info("GenerateBillingInvoiceDelegate completed");
            workFlowService.processServiceTaskCompletion(execution);
			
		} catch (Exception e) {
			logger.error("Exception in GenerateBillingInvoiceDelegate{}", e);
		}

	}

}
