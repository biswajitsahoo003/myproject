package com.tcl.dias.servicefulfillmentutils.delegates.ipc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_TYPE;
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
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 *  Product Commissioning Delgate for Commissioning Products in Geneva for Billing 
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("productCommissioningDelegate")
public class ProductCommissioningDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(ProductCommissioningDelegate.class);

	
    @Autowired
	WorkFlowService workFlowService;    
   
    @Value("${queue.productCommSync}")
	String prodCommQueue;
    
    @Autowired
	MQUtils mqUtils;
    
    @Override
	public void execute(DelegateExecution execution) {
		logger.info("ProductCommissioningDelegate invoked for {} id={} for queue{}", execution.getCurrentActivityId(),execution.getId(),prodCommQueue);
		try {
			workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String orderCode = (String) processMap.get(ORDER_CODE);
			String orderType = (String) processMap.get(ORDER_TYPE);
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			String req = orderCode.concat("#").concat(execution.getProcessInstanceId()).concat("#").concat(serviceCode)
					.concat("#").concat(orderType).concat("#").concat(serviceId.toString());

			String status = (String) mqUtils.sendAndReceive(prodCommQueue, req,420000);
			if(!StringUtils.isBlank(status) && (status.equals("Success") || status.equals(IpcConstants.NO_RECORD_FOUND)))
            {
				logger.info("status is :- {}",status);
                execution.setVariable("productCommissioningAck", true);
                if(status.equals(IpcConstants.NO_RECORD_FOUND)) {
                	execution.setVariable("skipProductCommissioningAsync", true);
                } else {
                	execution.setVariable("skipProductCommissioningAsync", false);
                }
            }
            else {
            	logger.info("status is :- {}",status);
                execution.setVariable("productCommissioningAck", false);
                execution.setVariable("productCommissioningErrorMsg", "Commissioning Failed");
            }
			
			logger.info("ProductCommissioningDelegate completed");
            workFlowService.processServiceTaskCompletion(execution);
			
		} catch (Exception e) {
			logger.error("Exception in ProductCommissioningDelegate{}", e);
		}

	}





}
