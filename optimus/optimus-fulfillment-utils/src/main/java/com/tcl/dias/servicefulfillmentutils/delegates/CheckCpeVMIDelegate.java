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
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.MstCostCatalogueRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.ComponentAndAttributeService;
import com.tcl.dias.servicefulfillmentutils.service.v1.SapHanaService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("checkCpeVMIDelegate")
public class CheckCpeVMIDelegate implements JavaDelegate {
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckCpeVMIDelegate.class);

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
	MstCostCatalogueRepository mstCostCatalogueRepository;
	
	@Autowired
	SapHanaService sapHanaService;

	public void execute(DelegateExecution execution) {

		String errorMessage = "";
		String errorCode = "";

		try {
			Task task = workFlowService.processServiceTask(execution);
			Map<String, Object> processMap = execution.getVariables();
			String serviceCode = (String) processMap.get(SERVICE_CODE);
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			String cpeType = (String) processMap.get("cpeType");
			String typeOfExpenses = (String) processMap.get("typeOfExpenses");

			Integer checkCPEInventoryCount = (Integer) processMap.get("checkCPEInventoryCount");
			if (checkCPEInventoryCount == null)
				checkCPEInventoryCount = 1;

			LOGGER.info("checkCpeVMIDelegate  invoked for {} serviceCode={}, serviceId={}, cpeType={}",
					execution.getCurrentActivityId(), serviceCode, serviceId, cpeType);

			boolean isAllMaterialAvailable = sapHanaService.checMaterialAvailableInWbsVmi(serviceCode, serviceId, "CPE",
					cpeType, typeOfExpenses, execution);

			checkCPEInventoryCount = checkCPEInventoryCount + 1;
			execution.setVariable("checkCPEInventoryCount", checkCPEInventoryCount);
			execution.setVariable("isCpeAvailableInInventory", isAllMaterialAvailable);

		} catch (Exception e) {
			execution.setVariable("isCpeAvailableInInventory", false);

			LOGGER.error("checkCpeVMIDelegate  Exception {} ", e);
		}
		workFlowService.processServiceTaskCompletion(execution, errorMessage);
	}
}
