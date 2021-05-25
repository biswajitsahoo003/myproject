package com.tcl.dias.servicefulfillmentutils.cancellation.delegates;

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
 *  Invoice Generation Delegate for Creating invoice for Commissioned Products 
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("cancellationBillingInvoiceDelegate")
public class CancellationBillingInvoiceDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(CancellationBillingInvoiceDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.cancellationInvoiceGenSync}")
	String genInvoiceQueue;
    
    @Autowired
	MQUtils mqUtils;
    
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("Cancellation GenerateBillingInvoiceDelegate for IPC invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			workFlowService.processServiceTask(execution);
            Map<String, Object> processMap = execution.getVariables();
            String orderCode = (String) processMap.get(ORDER_CODE);
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode).concat("#").concat(serviceId.toString());
			String status = (String) mqUtils.sendAndReceive(genInvoiceQueue, req);
			if(!StringUtils.isBlank(status) && status.equals("Success"))
			{
				execution.setVariable("billingInvoiceStatus", true);
			}
			else {
				execution.setVariable("billingInvoiceStatus", false);
				execution.setVariable("billingInvoiceErrorMsg", status.split("Success|Fail")[1]);
            }
			logger.info("Cancellation GenerateBillingInvoiceDelegate completed");
            workFlowService.processServiceTaskCompletion(execution);
			
		} catch (Exception e) {
			logger.error("Exception in Cancellation GenerateBillingInvoiceDelegate{}", e);
		}

	}





}
