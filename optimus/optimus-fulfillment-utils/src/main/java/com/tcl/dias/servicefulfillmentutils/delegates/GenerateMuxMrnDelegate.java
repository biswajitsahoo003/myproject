package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.sap.response.SapQuantityAvailableRequest;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("generateMuxMrnDelegate")
public class GenerateMuxMrnDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(GenerateMuxMrnDelegate.class);

	@Autowired
	MQUtils mqUtils;

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	TaskService taskService;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	SapService sapService;

	public void execute(DelegateExecution execution) {

		try {
			Task task = workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			String cpeType = (String) processMap.get("cpeType");
			String typeOfExpenses = (String) processMap.get("typeOfExpenses");

			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");

			logger.info("GenerateMuxMrnDelegate invoked for {} serviceCode={}, serviceId={}, cpeType={}",
					execution.getCurrentActivityId(), serviceCode, serviceId, cpeType);
			boolean success = sapService.placeMrnOrder(serviceCode, "MUX", cpeType, typeOfExpenses, scServiceDetail,
					execution.getProcessInstanceId(), execution.getCurrentActivityId(),task.getSiteType());
			execution.setVariable("muxMrnSuccess", success);

			
		} catch (Exception e) {
			execution.setVariable("muxMrnSuccess", false);

			logger.error("generateCpeMrnDelegate Exception {} ", e);
		}
		workFlowService.processServiceTaskCompletion(execution, null);
	}
}
