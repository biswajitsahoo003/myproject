package com.tcl.dias.servicefulfillmentutils.cancellation.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillmentutils.service.v1.CancellationChargeLineItemService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 *  CancellationLoadLineItemsDelegate for loading and saving lineitems in chargelineitems for Billing 
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("cancellationLoadLineItems")
public class CancellationLoadLineItemsDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(CancellationLoadLineItemsDelegate.class);

    @Autowired
	WorkFlowService workFlowService;
    
    @Autowired
    CancellationChargeLineItemService cancellationChargeLineItemService;
    
    @Override
	public void execute(DelegateExecution execution) {
		logger.info("Cancellation Load LineItems invoked for {} id={} for queue{}", execution.getCurrentActivityId(),execution.getId());
		try {
			workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String orderCode = (String) processMap.get(ORDER_CODE);
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			cancellationChargeLineItemService.deleteExistingLineItems(serviceId);
			String status = cancellationChargeLineItemService.loadLineItems(orderCode, serviceCode, serviceId);
			logger.info("Cancellation Load LineItems completed with status: {}", status);
            workFlowService.processServiceTaskCompletion(execution);
			
		} catch (Exception e) {
			logger.error("Exception in Cancellation Load LineItems Delegate : {}", e);
		}

	}

}
