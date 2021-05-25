package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;

import java.util.Map;
import java.util.Objects;

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
 * serviceHandoverDeligate for handling handover deligate
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component("serviceHandoverDeligate")
public class ServiceHandoverDeligate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(ServiceHandoverDeligate.class);

	@Autowired
	WorkFlowService workFlowService;
	

    @Value("${service.handover.deligate.queue}")
    private String serviceHandoverQueue;
    
    @Value("${rabbitmq.o2c.si.terminate.queue}")
	private String siTerminateDetail;
    
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

			logger.info("serviceHandoverDeligate invoked for {} serviceCode={}, serviceId={},",
					execution.getCurrentActivityId(), serviceCode, serviceId);

			ServiceHandoverInputBean handoverInputBean = new ServiceHandoverInputBean();
			handoverInputBean.setServiceCode(serviceCode);
			handoverInputBean.setServiceId(serviceId);
			handoverInputBean.setOrderCode((String) processMap.get(ORDER_CODE));

			mqUtils.send(serviceHandoverQueue, Utils.convertObjectToJson(handoverInputBean));
			
			if(Objects.nonNull(processMap.get("parentServiceCode"))){
				logger.info("ServiceHandoverDeligate: parentServiceCode exists");
				String parentServiceCode = (String) processMap.get("parentServiceCode");
				logger.info("ParentServiceCode",parentServiceCode);
				mqUtils.send(siTerminateDetail, parentServiceCode);
			}
			
		} catch (TclCommonException | IllegalArgumentException e) {
			logger.error("serviceHandoverDeligate execution error:{} and serviceid:{}", e, serviceCode);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}

}
