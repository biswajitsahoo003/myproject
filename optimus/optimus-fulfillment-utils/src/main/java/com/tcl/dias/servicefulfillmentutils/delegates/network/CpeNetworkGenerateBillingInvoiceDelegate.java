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
@Component("cpeGenerateBillingInvoiceDelegate")
public class CpeNetworkGenerateBillingInvoiceDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(CpeNetworkGenerateBillingInvoiceDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.invoiceGenSync.network.cpe}")
	String genInvoiceQueue;
    
    @Autowired
	MQUtils mqUtils;
    
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("cpeGenerateBillingInvoiceDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		try {
			Map<String, Object> processMap = execution.getVariables();
			String orderCode = (String) processMap.get(ORDER_CODE);
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode).concat("#").concat(serviceId.toString());
			String status = (String) mqUtils.sendAndReceive(genInvoiceQueue, req);
			if(!StringUtils.isBlank(status) && status.equals("Success"))
			{
				execution.setVariable("cpebillingInvoiceStatus", true);
			}
			else {
				execution.setVariable("cpebillingInvoiceStatus", false);
				execution.setVariable("cpebillingInvoiceErrorMsg", status.split("Success|Fail")[1]);
            }
			workFlowService.processServiceTask(execution);
            logger.info("cpeGenerateBillingInvoiceDelegate completed");
            workFlowService.processServiceTaskCompletion(execution);
			
		} catch (Exception e) {
			logger.error("Exception in cpeGenerateBillingInvoiceDelegate{}", e);
		}

	}





}
