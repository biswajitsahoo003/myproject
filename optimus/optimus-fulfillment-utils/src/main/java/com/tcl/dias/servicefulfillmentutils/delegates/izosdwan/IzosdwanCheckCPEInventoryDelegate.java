package com.tcl.dias.servicefulfillmentutils.delegates.izosdwan;

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
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.IzosdwanSapService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
/**
 * 
 * This file contains the IzosdwanCheckCPEInventoryDelegate.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("izosdwanCheckCPEInventoryDelegate")
public class IzosdwanCheckCPEInventoryDelegate implements JavaDelegate{
	private static final Logger logger = LoggerFactory.getLogger(IzosdwanCheckCPEInventoryDelegate.class);

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
	IzosdwanSapService sapService;

	public void execute(DelegateExecution execution) {
		try {
			String taskName = execution.getCurrentActivityId();
			Task task = workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			Integer componentId = (Integer) processMap.get("cpeOverlayComponentId");
			String cpeType = (String) processMap.get("cpeType");
			String vendorCode = (String) processMap.get("vendorCode");
			Integer checkCPEInventoryCount = (Integer) processMap.get("checkCPEInventoryCount");
			if (checkCPEInventoryCount == null)
				checkCPEInventoryCount = 1;

			logger.info("task name :{} and service code :{} and cpeType:{} and component id: {} and vendor code: {}",
					execution.getCurrentActivityId(), serviceCode, cpeType, componentId, vendorCode);
			boolean isQuantityAvailable = sapService.checMaterialQuantityAndPlaceMrn(serviceCode, serviceId, "CPE",
					cpeType, (String) processMap.get("typeOfExpenses"), execution, componentId, vendorCode);
			execution.setVariable("isCpeAvailableInInventory", isQuantityAvailable);

			logger.info("checkCPEInventoryDelegate invoked for {} serviceCode={}, serviceId={}, cpeType={}",
					execution.getCurrentActivityId(), serviceCode, serviceId, cpeType);

			checkCPEInventoryCount = checkCPEInventoryCount + 1;
			execution.setVariable("checkCPEInventoryCount", checkCPEInventoryCount);

		} catch (Exception e) {
			execution.setVariable("isCpeAvailableInInventory", false);
			execution.setVariable("isCpeLcAvailableInInventory", false);
			logger.error("checkCPEInventoryDelegate Exception {} ", e);
		}
		workFlowService.processServiceTaskCompletion(execution, null);
	}
}
