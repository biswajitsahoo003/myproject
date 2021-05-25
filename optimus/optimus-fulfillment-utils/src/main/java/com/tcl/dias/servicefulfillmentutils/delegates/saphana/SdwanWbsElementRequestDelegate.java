package com.tcl.dias.servicefulfillmentutils.delegates.saphana;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.SdwanSapHanaService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;


@Component("sdwanWbsElementRequestDelegate")
public class SdwanWbsElementRequestDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(SdwanWbsElementRequestDelegate.class);
	
	@Autowired
	SdwanSapHanaService sdwanSapHanaService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	WorkFlowService workFlowService;
	
	private static final String wbsRequestRetryCount = "wbsRequestRetryCount";
	
	@Override
	@Transactional(readOnly = false)
	public void execute(DelegateExecution execution) {
		
		try {
			Task task = workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			String cpeType = (String) processMap.get("cpeType");
			
			LOGGER.info("WbsElementRequestDelegate  invoked for {} serviceCode={}, serviceId={}, cpeType={}",
					execution.getCurrentActivityId(), serviceCode, serviceId, cpeType);
			
			ScServiceDetail serviceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");

			sdwanSapHanaService.processWbsElementSapWaitAsync(serviceDetail, execution);
			
			if(execution.getVariable(wbsRequestRetryCount) == null) {
				execution.setVariable(wbsRequestRetryCount, 1);
			}else {
				Integer retryCount = (Integer) execution.getVariable(wbsRequestRetryCount);
				retryCount = retryCount +1;
				if(retryCount > 6) {
					execution.setVariable("wbsRequestNeeded", false);
				}
				execution.setVariable(wbsRequestRetryCount, retryCount);
			}
			
			workFlowService.processServiceTaskCompletion(execution, null, task);
		}catch (Exception e) {
			LOGGER.error("WbsElementRequestDelegate  Exception {} ", e);
			
		}
		
	}

}
