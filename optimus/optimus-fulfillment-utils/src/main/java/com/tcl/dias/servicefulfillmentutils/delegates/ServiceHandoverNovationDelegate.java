package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.beans.ServiceHandoverInputBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * ServiceHandoverNovationDelegate for handling handover delegate
 *
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */

@Component("serviceHandoverNovationDelegate")
public class ServiceHandoverNovationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(ServiceHandoverNovationDelegate.class);

	@Autowired
	WorkFlowService workFlowService;
	
    @Value("${service.handover.delegate.novation.queue}")
    private String serviceHandoverNovationQueue;
    
    @Autowired
    MQUtils mqUtils;

	@Override
	public void execute(DelegateExecution execution) {
		String serviceCode = null;
		try {

			workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);

			logger.info("task name :{} and service code :{} ", execution.getCurrentActivityId(), serviceCode);

			logger.info("ServiceHandoverNovationDelegate invoked for {} serviceCode={}, serviceId={},",
					execution.getCurrentActivityId(), serviceCode, serviceId);

			ServiceHandoverInputBean handoverInputBean = new ServiceHandoverInputBean();
			handoverInputBean.setServiceCode(serviceCode);
			handoverInputBean.setServiceId(serviceId);
			handoverInputBean.setOrderCode((String) processMap.get(ORDER_CODE));

			mqUtils.send(serviceHandoverNovationQueue, Utils.convertObjectToJson(handoverInputBean));
			
		} catch (TclCommonException | IllegalArgumentException e) {
			logger.error("ServiceHandoverNovationDelegate execution error:{} and serviceid:{}", e, serviceCode);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}

}
