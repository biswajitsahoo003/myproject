package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.List;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Component("TriggerCountryLevelFlowDelegate")
public class TriggerCountryLevelFlowDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(TriggerCountryLevelFlowDelegate.class);

	@Autowired
	MQUtils mqUtils;
	
	@Value("${rabbitmq.gsc.fulfillment}")
	String o2cGscFulfillmentQueue;
	
	@Autowired
	GscService gscService;
	
	@Override
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside trigger country level process delegate variables {}", executionVariables);
			//String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			List<Integer> serviceIds = gscService.getCountryLevelFlowService(serviceId);
			mqUtils.send(o2cGscFulfillmentQueue, Utils.convertObjectToJson(serviceIds));
			logger.info("Trigger country level process - request {}", serviceIds);
		} catch (TclCommonException e) {
			e.printStackTrace();
		}
	}
}
